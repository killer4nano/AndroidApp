package com.example.kille.ostdeploy;

/**
 * Created by kille on 2017-08-04.
 */

public class SosTasks {


    private String name;
    private String description;
    private String notes;

    public SosTasks(String name, String desc, String notes) {
        this.name = name;
        this.description = desc;
        this.notes = notes;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getNotes() {
        return notes;
    }


}
