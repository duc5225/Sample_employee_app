package com.example.employeeapp.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class Employee {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String phone;
    private Bitmap image;;
    private LocalDate birthday;
    private String role;
    private Boolean isMale;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getMale() {
        return isMale;
    }

    public void setMale(Boolean male) {
        isMale = male;
    }

    public String getGender(){
        if (isMale == null) return "Không xác định";
        return isMale ?  "Nam" : "Nữ" ;
    }

    public String birthdayToString(){
        return this.birthday.getDayOfMonth() + "/" + (this.birthday.getMonthValue() + 1) + "/" + this.birthday.getYear();
    }

    public String getAge(){
        LocalDate now = LocalDate.now();
        return ChronoUnit.YEARS.between(this.birthday, now) + "";
    }
}
