package com.hypernymbiz.logistics.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Metis on 23-Mar-18.
 */

public class IsViewed {


    @SerializedName("viewed")
    @Expose
    private Boolean viewed;
    @SerializedName("email")
    @Expose
    private String email;
    public Boolean getViewed() {
        return viewed;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}