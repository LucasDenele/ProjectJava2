package fr.isen.java2.fxml;

import fr.isen.java2.db.daos.PersonDao;
import fr.isen.java2.db.entities.Person;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PersonInformationMenuController {

    private PersonDao personDao = new PersonDao();
    public Person person;
    public boolean isSaved = false;
    public boolean isCreation = false;
    public boolean isDelete = false;

    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ListView infoTitleListView;
    @FXML
    private ListView infoPersonListView;
    @FXML
    private ImageView personIcon;
    @FXML
    private Label logMessage;

    public void init(Person person, boolean isCreation){
        this.person = person;
        this.isCreation = isCreation;

        if(isCreation){
            deleteButton.relocate(1000,1000);
        }
        Image personImage = new Image("/fr/isen/java2/fxml/assets/defaultPersonIcon.png");
        personIcon.setImage(personImage);
        personIcon.setX(infoPersonListView.getLayoutX()-personImage.getWidth()/2);
        ObservableList<String> infoTitleList = FXCollections.observableArrayList(
                "First Name :", "Last Name :", "Nick Name :",
                "Address :",
                "Phone Number :","Email :", "Birth Date :"
        );
        infoTitleListView.setItems(infoTitleList);
        if(person.getBirthDate() == null){
            person.setBirthDate(null);
        }
        ObservableList<String> infoPersonList = FXCollections.observableArrayList(
                person.getFirstName(), person.getLastName(), person.getNickName(),
                person.getAddress(),
                person.getPhoneNumber(), person.getEmailAddress(),
                (person.getBirthDate().toString().equals("1970-01-01")) ? "" : person.getBirthDate().toString()
        );
        infoPersonListView.setItems(infoPersonList);
        infoPersonListView.setEditable(true);
        infoPersonListView.setCellFactory(TextFieldListCell.forListView());
    }

    @FXML
    public void saveChanges(ActionEvent actionEvent) {
        ObservableList personInformations = infoPersonListView.getItems();

        this.person.setFirstName((String) personInformations.get(0));
        this.person.setLastName((String) personInformations.get(1));
        this.person.setNickName((String) personInformations.get(2));
        this.person.setAddress((String) personInformations.get(3));
        this.person.setPhoneNumber((String) personInformations.get(4));
        this.person.setEmailAddress((String) personInformations.get(5));
        if(personInformations.get(6) != ""){
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date utilDate = null;
            try {
                utilDate = sdf.parse((String)personInformations.get(6));
                this.person.setBirthDate(new java.sql.Date(utilDate.getTime()));
            } catch (ParseException e) {
                logMessage.setText("The format for date is : dd-mm-yyyy");
            }
        }else{
            this.person.setBirthDate(null);
        }

        if(isCreation){
            personDao.addPerson(person, null);
            isCreation = false;
        }else{
            personDao.modifyPerson(person);
        }
        this.isSaved = true;
        this.logMessage.setText("Contact Saved");
    }

    @FXML
    public void deletePerson(ActionEvent actionEvent) {
        personDao.deletePerson(person.getId());
        isDelete = true;
        ((Stage)deleteButton.getScene().getWindow()).close();
    }


}
