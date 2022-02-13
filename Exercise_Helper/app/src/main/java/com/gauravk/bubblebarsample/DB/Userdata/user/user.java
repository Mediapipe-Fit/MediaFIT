package com.gauravk.bubblebarsample.DB.Userdata.user;

import com.google.gson.annotations.SerializedName;

public class user {
    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("sex")
    private int sex;

    @SerializedName("age")
    private String age;

    @SerializedName("score")
    private int score;

    //@SerializedName("data")
    //private List<User> data;

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public int getSex(){
        return sex;
    }
    public void setSex(int sex){
        this.sex = sex;
    }
    public String getAge(){
        return age;
    }
    public void setAge(String age){
        this.age = age;
    }
    public int getScore(){
        return score;
    }
    public void setScore(int score){
        this.score = score;
    }
    /*
    public List<User> getData(){
        return this.data;
    }
    public void setData(List<User> data){
        this.data = data;
    }
    */
    @Override
    public String toString()
    {
        return "[Score = "+score+", Email = "+email+", sex = "+sex+", name = "+name+", age = "+age+"]";
    }

}
