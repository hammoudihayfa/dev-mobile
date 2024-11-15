package com.example.bookstore.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Book {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String author;
    private double price;
    private boolean isFavorite; // Field for favorite status
    private String imageUri; // Field for image URI

    // Constructor for Room (include all fields)
    public Book(int id, String title, String author, double price, boolean isFavorite, String imageUri) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.isFavorite = isFavorite;
        this.imageUri = imageUri;
    }

    // Constructor without ID (ignored by Room)
    @Ignore
    public Book(String title, String author, double price, String imageUri) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.isFavorite = false; // Default to not favorite
        this.imageUri = imageUri;
    }

    // Constructor without image URI (for backward compatibility)
    @Ignore
    public Book(String title, String author, double price) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.isFavorite = false; // Default to not favorite
        this.imageUri = null;
    }


    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
}
