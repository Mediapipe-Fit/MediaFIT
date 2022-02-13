package com.gauravk.bubblebarsample.DB.Userdata.user;

import com.google.gson.annotations.SerializedName;

public class dataall {
    @SerializedName("status")
    private String status;
    @SerializedName("length")
    private int length;
    @SerializedName("data")
    private user[] data;


    public String getStatus(){
        return this.status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public int getLength(){
        return this.length;
    }
    public void setLength(int length){
        this.length = length;
    }
    public user[] getData(){
        return this.data;
    }
    public void setData(user[] data){
        this.data = data;
    }
    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", length = "+length+", status = "+status+"]";
    }
}
