package fr.isen.java2.db.entities;

import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;
import java.util.List;

public class Cluster {

//Attributs :
    private int id;
    private SimpleStringProperty name = new SimpleStringProperty();
    private List<Person> persons;

    //Setters :
    public void setId(int id){ this.id = id; }

    public void setName(String name) { this.name.set((name != null) ? name : "" ); }

    public void setPersons(LinkedList<Person> persons) {
        this.persons = persons;
    }

//Getters :
    public int getId(){ return id; }

    public String getName() {
        return name.get();
    }

    public LinkedList<Person> getPersons() {
        return (LinkedList<Person>) persons;
    }
}
