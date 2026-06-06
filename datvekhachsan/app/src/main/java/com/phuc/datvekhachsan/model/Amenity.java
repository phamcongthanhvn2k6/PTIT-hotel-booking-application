package com.phuc.datvekhachsan.model;

import java.io.Serializable;

public class Amenity implements Serializable {
    private String name;
    private int iconResId;

    public Amenity(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getName() { return name; }
    public int getIconResId() { return iconResId; }
}
