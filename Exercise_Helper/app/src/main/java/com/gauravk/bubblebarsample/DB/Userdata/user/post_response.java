package com.gauravk.bubblebarsample.DB.Userdata.user;

import com.google.gson.annotations.SerializedName;

public class post_response {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+"]";
    }
}
