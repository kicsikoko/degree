package com.kicsiroot.passwordstodo;

public class UserProfile {
    public String email, image;

    public UserProfile() {
    }

    public UserProfile(String email, String image) {
        this.email = email;
        this.image= image;
    }

    public String getsEmail() {
        return email;
    }
    public void setsEmail(String email) {
        this.email = email;
    }
    public String getsImage() {
        return image;
    }
    public void setsImage(String image){
        this.image = image;
    }
}
