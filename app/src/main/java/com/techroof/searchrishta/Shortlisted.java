package com.techroof.searchrishta;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shortlisted_table")
public class Shortlisted {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userid;
    private String name;
    private String DOB;
    private String height;
    private String relegion;
    private String education;
    private String maritalstatus;
    private String city;
    private String province;
    private String country;

    public int getId() {
        return id;
    }

    public String getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getDOB() {
        return DOB;
    }

    public String getHeight() {
        return height;
    }

    public String getRelegion() {
        return relegion;
    }

    public String getEducation() {
        return education;
    }

    public String getMaritalstatus() {
        return maritalstatus;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Shortlisted(String userid, String name, String DOB, String height,
                       String relegion, String education, String maritalstatus,
                       String city, String province, String country) {
        this.userid = userid;
        this.name = name;
        this.DOB = DOB;
        this.height = height;
        this.relegion = relegion;
        this.education = education;
        this.maritalstatus = maritalstatus;
        this.city = city;
        this.province = province;
        this.country = country;
    }



    /*public Shortlisted() {
    }*/


}
