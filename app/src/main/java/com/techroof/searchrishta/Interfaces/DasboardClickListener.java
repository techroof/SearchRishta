package com.techroof.searchrishta.Interfaces;

public interface DasboardClickListener {

    void onItemclickk(String userId,String name,String dob,String height,String relegion,String education,String maritalstatus,
                     String city,String province,String country);


    void onRemoveClick(String userId,String name,String dob,String height,String relegion,String education,String maritalstatus,
                       String city,String province,String country);

}
