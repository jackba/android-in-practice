package com.manning.aip.auto;

public class Contact {
    public String phone = "";
    public String firstName = "";
    public String lastName = "";
    public String email = "";
    public String id = "";
    
    @Override
    public String toString(){
        return firstName + " " + lastName + " " + phone + " " + email;
    }
}
