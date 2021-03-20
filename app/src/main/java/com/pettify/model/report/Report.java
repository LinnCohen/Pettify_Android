package com.pettify.model.report;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Report implements Serializable {
    @PrimaryKey
    @NonNull
    private String id = "";
    private String description = "";
    private String title = "";
    private String address = "";
    private String animal_type = "";
    private String report_type = "";
    private String image_url = "";

    public String getImage_url() { return image_url; }

    public void setImage_url(String image_url) { this.image_url = image_url; }

    private long lastUpdated;

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getAnimal_type() { return animal_type; }

    public void setAnimal_type(String animal_type) { this.animal_type = animal_type; }

    public String getReport_type() { return report_type; }

    public void setReport_type(String report_type) { this.report_type = report_type; }

    public Report() {
    }

    public Report(@NonNull String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", this.id);
        result.put("description", this.description);
        result.put("title", this.title);
        result.put("address", this.address);
        result.put("animal type", this.animal_type);
        result.put("report type", this.report_type);
        //TODO - add image
        result.put("lastUpdated", FieldValue.serverTimestamp());
        return result;
    }

    public void fromMap(Map<String, Object> data) {
        this.id = (String)data.get("id");
        this.description = (String)data.get("description");
        this.title = (String)data.get("title");
        this.address = (String)data.get("address");
        this.report_type = (String)data.get("report_type");
        this.animal_type = (String)data.get("animal_type");
        //TODO - add image
        Timestamp lastUpdatedTS = (Timestamp) data.get("lastUpdated");
        this.lastUpdated = lastUpdatedTS.getSeconds();
    }
}
