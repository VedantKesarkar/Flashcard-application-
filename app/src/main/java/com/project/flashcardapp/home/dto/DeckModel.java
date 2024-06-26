package com.project.flashcardapp.home.dto;

import com.google.firebase.firestore.Exclude;

public class DeckModel {

    private String name,date;

    @Exclude
    private String id;
    private int count;

    public DeckModel() {
    }

    public DeckModel(String name, String date, int count) {
        this.name = name;
        this.date = date;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
