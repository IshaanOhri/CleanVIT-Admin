package com.ishaanohri.cleanvit_admin;

public class Complaint {

    private String DateTime;
    private String Landmark;
    private String Remarks;
    private String Status;
    private String ImageURL;
    private String Latitude;
    private String Longitude;
    private String Key;

    public Complaint() {
    }

    public Complaint(String dateTime, String landmark, String remarks, String status, String imageURL, String latitude, String longitude, String key) {
        DateTime = dateTime;
        Landmark = landmark;
        Remarks = remarks;
        Status = status;
        ImageURL = imageURL;
        Latitude = latitude;
        Longitude = longitude;
        Key = key;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String landmark) {
        Landmark = landmark;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
