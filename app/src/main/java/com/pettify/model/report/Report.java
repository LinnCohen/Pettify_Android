package com.pettify.model.report;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firestore.v1.DocumentTransform;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Report implements Serializable {
    @PrimaryKey
    @NonNull
    private String id = "";
    private String description = "";
    private long lastUpdated;

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
        result.put("lastUpdated", FieldValue.serverTimestamp());
        return result;
    }

    public void fromMap(Map<String, Object> data) {
        this.id = (String)data.get("id");
        this.description = (String)data.get("description");
        Timestamp lastUpdatedTS = (Timestamp) data.get("lastUpdated");
        this.lastUpdated = lastUpdatedTS.getSeconds();
    }
}
