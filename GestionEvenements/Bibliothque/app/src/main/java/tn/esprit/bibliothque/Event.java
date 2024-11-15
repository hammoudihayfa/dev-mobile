package tn.esprit.bibliothque;

import java.io.Serializable;

public class Event implements Serializable {
    private String id;
    private String name;
    private String date;
    private String location;

    public Event(String id, String name, String date, String location) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.location = location;
    }
    public Event(){};

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Nom: " + name + ", Date: " + date + ", Lieu: " + location;
    }
    public static Event fromString(String eventString) {
        String[] parts = eventString.split(",");
        return new Event(parts[0], parts[1], parts[2], parts[3]);
    }
}
