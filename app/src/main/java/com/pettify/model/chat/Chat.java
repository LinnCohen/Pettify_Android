//package com.pettify.model.chat;
//
//import androidx.annotation.NonNull;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//import com.google.firebase.Timestamp;
//import com.google.firebase.firestore.FieldValue;
//
//import java.io.Serializable;
//import java.util.HashMap;
//import java.util.Map;
//
//    @Entity
//    public class Chat implements Serializable {
//        @PrimaryKey
//        @NonNull
//        private String id = "";
//        private String message = "";
//        private String user_name = "";
//        private long lastUpdated;
//
//        public long getLastUpdated() {
//            return lastUpdated;
//        }
//
//        public void setLastUpdated(long lastUpdated) {
//            this.lastUpdated = lastUpdated;
//        }
//
//        public Chat() {
//        }
//
//        public Chat(@NonNull String id, String message, String user_name) {
//            this.id = id;
//            this.message = message;
//            this.user_name = user_name;
//        }
//
//        @NonNull
//        public String getId() {
//            return id;
//        }
//
//        public void setId(@NonNull String id) {
//            this.id = id;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//
//        public String getUser_name() {
//            return user_name;
//        }
//
//        public void setUser_name(String user_name) {
//            this.user_name = user_name;
//        }
//
//        public Map<String, Object> toMap() {
//            Map<String, Object> result = new HashMap<>();
//            result.put("message", this.message);
//            result.put("user_name", this.user_name);
//            return result;
//        }
//        public void fromMap(Map<String, Object> data) {
//            this.message = (String) data.get("message");
//            this.user_name = (String) data.get("user_name");
//        }
//        @Override
//        public String toString() {
//            return "Report{" +
//                    "id='" + id + '\'' +
//                    ", message='" + message + '\'' +
//                    ", user_name='" + user_name + '\'' + '}';
//        }
//
//
//    }
//
