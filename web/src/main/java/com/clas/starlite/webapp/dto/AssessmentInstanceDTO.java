package com.clas.starlite.webapp.dto;

/**
 * Created by Admin on 1/20/2015.
 */
public class AssessmentInstanceDTO {
    private String Username;
    private String CustomerName;
    private String CustomerEmail;
    private long TimeStamp;



    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        CustomerEmail = customerEmail;
    }

    public long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        TimeStamp = timeStamp;
    }
}
