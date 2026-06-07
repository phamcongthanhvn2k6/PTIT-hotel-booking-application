package com.phuc.datvekhachsan.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.model.Hotel;
import com.phuc.datvekhachsan.network.AdminApiService;
import com.phuc.datvekhachsan.network.RetrofitClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHotelEditorActivity extends AppCompatActivity {

    private ImageView imgHotelPreview;
    private EditText edtHotelName, edtHotelLocation, edtHotelPrice, edtHotelDescription;
    private ProgressBar progressBar;
    
    private Long hotelId = null;
    private String currentImageUrl = "";
    private Uri selectedImageUri = null;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    Glide.with(this).load(selectedImageUri).into(imgHotelPreview);
                    imgHotelPreview.setPadding(0, 0, 0, 0); // remove padding so image fills
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_hotel_editor);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        imgHotelPreview = findViewById(R.id.imgHotelPreview);
        edtHotelName = findViewById(R.id.edtHotelName);
        edtHotelLocation = findViewById(R.id.edtHotelLocation);
        edtHotelPrice = findViewById(R.id.edtHotelPrice);
        edtHotelDescription = findViewById(R.id.edtHotelDescription);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.btnUploadImage).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        findViewById(R.id.btnSaveHotel).setOnClickListener(v -> saveHotel());

        if (getIntent().hasExtra("HOTEL_ID")) {
            hotelId = getIntent().getLongExtra("HOTEL_ID", -1);
            if (hotelId != -1) {
                ((TextView) findViewById(R.id.txtTitle)).setText("Sửa Khách sạn");
                // TODO: Load hotel details if we had an endpoint for GET /api/admin/hotels/{id}
                // Temporarily we rely on name/location passed or assume creating new if empty
            }
        }
    }

    private void saveHotel() {
        String name = edtHotelName.getText().toString().trim();
        String location = edtHotelLocation.getText().toString().trim();
        String priceStr = edtHotelPrice.getText().toString().trim();
        String description = edtHotelDescription.getText().toString().trim();

        if (name.isEmpty() || location.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin bắt buộc (*)", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        progressBar.setVisibility(View.VISIBLE);

        if (selectedImageUri != null) {
            uploadImageAndSave(name, location, price, description);
        } else {
            submitHotel(name, location, price, description, currentImageUrl);
        }
    }

    private void uploadImageAndSave(String name, String location, double price, String description) {
        String filePath = getRealPathFromURI(selectedImageUri);
        if (filePath == null) {
            Toast.makeText(this, "Không thể lấy đường dẫn ảnh", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        AdminApiService apiService = RetrofitClient.getClient(this).create(AdminApiService.class);
        apiService.uploadImage(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String uploadedUrl = response.body().get("url").getAsString();
                    submitHotel(name, location, price, description, uploadedUrl);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AdminHotelEditorActivity.this, "Lỗi upload ảnh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminHotelEditorActivity.this, "Lỗi kết nối upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitHotel(String name, String location, double price, String description, String imageUrl) {
        AdminApiService apiService = RetrofitClient.getClient(this).create(AdminApiService.class);
        
        // Hotel model requires id, name, location, imageUrl, rating, price, description
        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setLocation(location);
        hotel.setImageUrl(imageUrl);
        hotel.setRating(5.0);
        hotel.setPricePerNight(price);
        hotel.setDescription(description);

        Call<Hotel> call;
        if (hotelId != null && hotelId != -1) {
            call = apiService.updateHotel(hotelId, hotel);
        } else {
            call = apiService.addHotel(hotel);
        }

        call.enqueue(new Callback<Hotel>() {
            @Override
            public void onResponse(Call<Hotel> call, Response<Hotel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(AdminHotelEditorActivity.this, "Lưu thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdminHotelEditorActivity.this, "Lỗi lưu khách sạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Hotel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminHotelEditorActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
}
