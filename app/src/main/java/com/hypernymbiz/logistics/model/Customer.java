package com.hypernymbiz.logistics.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Metis on 22-Mar-18.
 */

public class Customer {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("subscription_is_valid")
    @Expose
    private Boolean subscriptionIsValid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSubscriptionIsValid() {
        return subscriptionIsValid;
    }

    public void setSubscriptionIsValid(Boolean subscriptionIsValid) {
        this.subscriptionIsValid = subscriptionIsValid;
    }

}
