package fr.isen.java2.db.entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.sql.Date;

public class Person {
//Attributs :

    private int id;
    private SimpleStringProperty firstName = new SimpleStringProperty();
    private SimpleStringProperty lastName = new SimpleStringProperty();
    private SimpleStringProperty nickName = new SimpleStringProperty();
    private String phoneNumber;
    private String address;
    private String emailAddress;
    private Date birthDate;

//Setters :
    public void setId(int id) { this.id = id; }

    public void setFirstName(String firstName) { this.firstName.set((firstName != null) ? firstName : "" ); }

    public void setLastName(String lastName) {
        this.lastName.set((lastName != null) ? lastName : "");
    }

    public void setNickName(String nickName) { this.nickName.set((nickName != null) ? nickName : ""); }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = (phoneNumber != null) ? phoneNumber : "";
    }

    public void setAddress(String address) { this.address = (address != null) ? address : ""; }

    public void setEmailAddress(String emailAddress) { this.emailAddress = (emailAddress != null) ? emailAddress : ""; }

    public void setBirthDate(Date birthDate) { this.birthDate = (birthDate != null) ? birthDate : new Date(0);}

//Getters :
    public int getId() { return id; }

    public String getFirstName() {
        return firstName.get();
    }
    public SimpleStringProperty getFirstNameProperty(){return firstName;}

    public String getLastName() {
        return lastName.get();
    }
    public SimpleStringProperty getLastNameProperty(){return lastName;}

    public String getNickName() { return nickName.get(); }
    public SimpleStringProperty getNickNameProperty(){return nickName;}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Date getBirthDate() {
        return birthDate;
    }
}
