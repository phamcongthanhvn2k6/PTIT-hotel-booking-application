package com.phuc.datvekhachsan.data;

import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.model.Amenity;
import com.phuc.datvekhachsan.model.Hotel;
import com.phuc.datvekhachsan.model.SliderItem;
import com.phuc.datvekhachsan.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MockData {

    // Helper methods for each hotel to customize their description images easily
    public static List<Integer> getMuongThanhImages() {
        return Arrays.asList(
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel
        );
    }

    public static List<Integer> getVinpearlImages() {
        return Arrays.asList(
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel
        );
    }

    public static List<Integer> getPullmanImages() {
        return Arrays.asList(
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel
        );
    }

    public static List<Integer> getInterContinentalImages() {
        return Arrays.asList(
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel
        );
    }

    public static List<Integer> getNovotelImages() {
        return Arrays.asList(
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel
        );
    }

    public static List<Integer> getSheratonImages() {
        return Arrays.asList(
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel
        );
    }

    public static List<Integer> getFusionMaiaImages() {
        return Arrays.asList(
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel
        );
    }

    public static List<Integer> getMarriottImages() {
        return Arrays.asList(
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel
        );
    }

    public static List<Integer> getLaSiestaImages() {
        return Arrays.asList(
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel,
                R.drawable.hotel
        );
    }

    public static List<SliderItem> getBanners() {
        List<SliderItem> banners = new ArrayList<>();
        banners.add(new SliderItem(R.drawable.hotel));
        banners.add(new SliderItem(R.drawable.hotel));
        banners.add(new SliderItem(R.drawable.hotel));
        banners.add(new SliderItem(R.drawable.hotel));
        banners.add(new SliderItem(R.drawable.hotel));
        return banners;
    }

    public static List<Hotel> getPopularHotels() {
        List<Hotel> hotels = new ArrayList<>();

        hotels.add(new Hotel(
                "Khách sạn Mường Thanh",
                "Khách sạn 5 sao với dịch vụ cao cấp, nằm ngay trung tâm thành phố. " +
                        "Phòng nghỉ rộng rãi, thoáng mát với tầm nhìn đẹp ra thành phố. " +
                        "Đội ngũ nhân viên chuyên nghiệp, phục vụ tận tình 24/7.",
                "Hà Nội", R.drawable.hotel, getMuongThanhImages(), 4.8, 1500000,
                Arrays.asList("WiFi", "Hồ bơi", "Spa", "Nhà hàng", "Phòng Gym"),
                getDefaultFacilities()
        ));

        hotels.add(new Hotel(
                "Vinpearl Resort",
                "Resort nghỉ dưỡng cao cấp bên bờ biển với bãi cát trắng trải dài. " +
                        "Khu vui chơi giải trí đẳng cấp quốc tế, ẩm thực đa dạng. " +
                        "Trải nghiệm thiên đường nghỉ dưỡng tuyệt vời nhất Việt Nam.",
                "Nha Trang", R.drawable.hotel, getVinpearlImages(), 4.9, 2500000,
                Arrays.asList("WiFi", "Bãi biển", "Hồ bơi", "Spa", "Bar"),
                getDefaultFacilities()
        ));

        hotels.add(new Hotel(
                "Pullman Saigon",
                "Khách sạn thương mại hạng sang tọa lạc tại vị trí đắc địa quận 1. " +
                        "Phong cách kiến trúc hiện đại kết hợp văn hóa Việt Nam. " +
                        "Lý tưởng cho các chuyến công tác và nghỉ dưỡng ngắn ngày.",
                "TP.HCM", R.drawable.hotel, getPullmanImages(), 4.7, 2000000,
                Arrays.asList("WiFi", "Nhà hàng", "Phòng họp", "Spa", "Gym"),
                getDefaultFacilities()
        ));

        hotels.add(new Hotel(
                "InterContinental",
                "Khách sạn quốc tế 5 sao với tiêu chuẩn dịch vụ toàn cầu. " +
                        "Phòng nghỉ sang trọng với nội thất cao cấp, minibar đầy đủ. " +
                        "Nhà hàng buffet quốc tế phục vụ hơn 100 món ăn mỗi ngày.",
                "Đà Nẵng", R.drawable.hotel, getInterContinentalImages(), 4.6, 1800000,
                Arrays.asList("WiFi", "Hồ bơi", "Nhà hàng", "Bar", "Gym"),
                getDefaultFacilities()
        ));

        hotels.add(new Hotel(
                "Novotel Phú Quốc",
                "Resort hiện đại nằm bên bãi biển Phú Quốc hoang sơ tuyệt đẹp. " +
                        "Không gian xanh mát, yên tĩnh và thư giãn. " +
                        "Lý tưởng cho kỳ nghỉ gia đình và tuần trăng mật.",
                "Phú Quốc", R.drawable.hotel, getNovotelImages(), 4.5, 1600000,
                Arrays.asList("WiFi", "Bãi biển", "Hồ bơi", "Nhà hàng", "Spa"),
                getDefaultFacilities()
        ));

        return hotels;
    }

    public static List<Hotel> getRecommendedHotels() {
        List<Hotel> hotels = new ArrayList<>();

        hotels.add(new Hotel(
                "Sheraton Hà Nội",
                "Khách sạn quốc tế cao cấp bên Hồ Tây thơ mộng. " +
                        "View hồ tuyệt đẹp, không gian yên bình giữa lòng thủ đô. " +
                        "Dịch vụ chuyên nghiệp đạt chuẩn Marriott International.",
                "Hà Nội", R.drawable.hotel, getSheratonImages(), 4.7, 2200000,
                Arrays.asList("WiFi", "Hồ bơi", "Spa", "Nhà hàng", "Phòng họp"),
                getDefaultFacilities()
        ));

        hotels.add(new Hotel(
                "Fusion Maia Resort",
                "Resort nghỉ dưỡng phong cách tối giản bên bờ biển Đà Nẵng. " +
                        "Gói spa trọn gói miễn phí - điểm độc đáo nhất. " +
                        "Kiến trúc hài hòa với thiên nhiên, vườn nhiệt đới xanh mát.",
                "Đà Nẵng", R.drawable.hotel, getFusionMaiaImages(), 4.8, 3000000,
                Arrays.asList("WiFi", "Spa miễn phí", "Hồ bơi", "Yoga", "Bãi biển"),
                getDefaultFacilities()
        ));

        hotels.add(new Hotel(
                "JW Marriott Phú Quốc",
                "Resort 5 sao đẳng cấp với thiết kế lấy cảm hứng từ Đại học tưởng tượng. " +
                        "Bãi biển riêng, nhiều nhà hàng quốc tế. " +
                        "Khu vui chơi trẻ em rộng lớn, lý tưởng cho gia đình.",
                "Phú Quốc", R.drawable.hotel, getMarriottImages(), 4.9, 3500000,
                Arrays.asList("WiFi", "Bãi biển riêng", "Waterpark", "Golf", "Kids Club"),
                getDefaultFacilities()
        ));

        hotels.add(new Hotel(
                "La Siesta Hội An",
                "Boutique hotel phong cách Đông Dương giữa phố cổ Hội An. " +
                        "Gần Chùa Cầu và chợ đêm, thuận tiện tham quan. " +
                        "Lớp học nấu ăn và tour xe đạp miễn phí cho khách lưu trú.",
                "Hội An", R.drawable.hotel, getLaSiestaImages(), 4.6, 1200000,
                Arrays.asList("WiFi", "Xe đạp", "Nấu ăn", "Hồ bơi", "Bar"),
                getDefaultFacilities()
        ));

        return hotels;
    }

    public static List<Hotel> searchHotels(String query) {
        List<Hotel> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase(Locale.ROOT);

        List<Hotel> allHotels = new ArrayList<>(getPopularHotels());
        allHotels.addAll(getRecommendedHotels());

        List<String> seenNames = new ArrayList<>();

        for (Hotel hotel : allHotels) {
            boolean matches = hotel.getName().toLowerCase(Locale.ROOT).contains(lowerQuery) ||
                    hotel.getLocation().toLowerCase(Locale.ROOT).contains(lowerQuery);
            if (matches && !seenNames.contains(hotel.getName())) {
                results.add(hotel);
                seenNames.add(hotel.getName());
            }
        }
        return results;
    }

    private static List<Amenity> getDefaultFacilities() {
        List<Amenity> facilities = new ArrayList<>();
        facilities.add(new Amenity("WiFi miễn phí", R.drawable.ic_star));
        facilities.add(new Amenity("Hồ bơi", R.drawable.ic_star));
        facilities.add(new Amenity("Nhà hàng", R.drawable.ic_star));
        facilities.add(new Amenity("Phòng Gym", R.drawable.ic_star));
        facilities.add(new Amenity("Spa & Massage", R.drawable.ic_star));
        return facilities;
    }

    private static List<User> mockUsers = new ArrayList<>(Arrays.asList(
            new User("admin", "123456", "Quản trị viên", "admin@datphong.com"),
            new User("phuc", "123456", "Phúc Nguyễn", "phuc@gmail.com")
    ));

    public static User checkLogin(String username, String password) {
        for (User user : mockUsers) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static boolean registerUser(String username, String password, String fullName, String email) {
        for (User user : mockUsers) {
            if (user.getUsername().equals(username)) {
                return false; // Username already exists
            }
        }
        mockUsers.add(new User(username, password, fullName, email));
        return true;
    }
}
