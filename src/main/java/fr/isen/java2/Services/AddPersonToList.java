package fr.isen.java2.Services;

import fr.isen.java2.db.entities.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class AddPersonToList {
    public static List<Person> addPersonToList(ResultSet resultSet) throws SQLException {
        List<Person> listOfPersons = new LinkedList<>();

        while(resultSet.next()){
            listOfPersons.add(new Person());
            ((LinkedList<Person>) listOfPersons).getLast().setId(resultSet.getInt("idperson"));
            ((LinkedList<Person>) listOfPersons).getLast().setFirstName(resultSet.getString("firstname"));
            ((LinkedList<Person>) listOfPersons).getLast().setLastName(resultSet.getString("lastname"));
            ((LinkedList<Person>) listOfPersons).getLast().setNickName(resultSet.getString("nickname"));
            ((LinkedList<Person>) listOfPersons).getLast().setPhoneNumber(resultSet.getString("phone_number"));
            ((LinkedList<Person>) listOfPersons).getLast().setAddress(resultSet.getString("address"));
            ((LinkedList<Person>) listOfPersons).getLast().setEmailAddress(resultSet.getString("email_address"));
            ((LinkedList<Person>) listOfPersons).getLast().setBirthDate(resultSet.getDate("birth_date"));
        }

        return listOfPersons;
    }
}
