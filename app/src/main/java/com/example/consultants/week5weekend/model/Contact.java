package com.example.consultants.week5weekend.model;

//simple contact with name, number, email, location
public class Contact {
    private String contactName;
    private String contactNumber;
    private String contactEmail;
    private String contactLocation;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactLocation() {
        return contactLocation;
    }

    public void setContactLocation(String contactLocation) {
        this.contactLocation = contactLocation;
    }
}

