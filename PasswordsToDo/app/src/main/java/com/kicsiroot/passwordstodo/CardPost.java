package com.kicsiroot.passwordstodo;

import com.google.firebase.database.ServerValue;

public class CardPost {

    private String postKey;
    private String cNumber;
    private String cName;
    private String cDate;
    private String cCVC;
    private Object timeStamp;

    public CardPost() {
    }

    public CardPost(String cNumber, String cName, String cDate, String cCVC, Object timeStamp) {
        this.cNumber = cNumber;
        this.cName = cName;
        this.cDate = cDate;
        this.cCVC = cCVC;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getcNumber() {
        return cNumber;
    }

    public String getcName() {
        return cName;
    }

    public String getcDate() {
        return cDate;
    }

    public String getcCVC() {
        return cCVC;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setcNumber(String cNumber) {
        this.cNumber = cNumber;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }

    public void setcCVC(String cCVC) {
        this.cCVC = cCVC;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
