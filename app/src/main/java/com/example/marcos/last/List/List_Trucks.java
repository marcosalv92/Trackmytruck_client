package com.example.marcos.last.List;

/**
 * Created by windows7 on 8/13/2019.
 */
public class List_Trucks {
    private int id;
    private String name;
    private String description;
    private String brand ;
    private String model;
    private String realProfileImage;

    public List_Trucks(int _id, String _name, String _description, String _brand, String _model,String _realProfileImage) {
        id = _id;
        name = _name;
        description = _description;
        brand = _brand;
        model = _model;
        realProfileImage = _realProfileImage;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getRealProfileImage() {
        return realProfileImage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setRealProfileImage(String realProfileImage) {
        this.realProfileImage = realProfileImage;
    }
}
