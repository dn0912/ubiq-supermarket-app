package com.example.dnguyen.ubiq_supermarket;

/**
 * Created by dnguyen on 10.01.18.
 */

public class Product {
    private String name;
    private boolean selected;

    public Product(String name, Boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
