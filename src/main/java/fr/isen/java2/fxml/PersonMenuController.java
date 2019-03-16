package fr.isen.java2.fxml;

import com.sun.jndi.toolkit.url.Uri;
import fr.isen.java2.Services.ActionButtonTableCell;
import fr.isen.java2.Services.FileChooser;
import fr.isen.java2.Services.SceneSwitcher;
import fr.isen.java2.db.daos.ClusterDao;
import fr.isen.java2.db.daos.PersonDao;
import fr.isen.java2.db.entities.Cluster;
import fr.isen.java2.db.entities.Person;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class PersonMenuController {

    private SceneSwitcher sceneSwitcher = new SceneSwitcher();
    private PersonDao personDao = new PersonDao();
    private ClusterDao clusterDao = new ClusterDao();
    private ObservableList<Person> listOfPersons;

    @FXML
    private MenuBar menuBar;

    @FXML
    private TableView contactTable;
    @FXML
    private Label title;

    public void init(Cluster cluster){

        title.setText("Contacts :");

        this.listOfPersons = (cluster == null) ?
                FXCollections.observableArrayList(personDao.getPersons()) :
                FXCollections.observableArrayList(cluster.getPersons());

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setCellValueFactory(
            new Callback<TableColumn.CellDataFeatures<Person, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Person, String> p) {
                    // p.getValue() returns the Person instance for a particular TableView row
                    return p.getValue().getFirstNameProperty();
                }
            });
        firstNameCol.setMinWidth(400/3);
        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Person, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Person, String> p) {
                        // p.getValue() returns the Person instance for a particular TableView row
                        return p.getValue().getLastNameProperty();
                    }
                });
        lastNameCol.setMinWidth(400/3);
        TableColumn nickNameCol = new TableColumn("Nick Name");
        nickNameCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Person, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Person, String> p) {
                        // p.getValue() returns the Person instance for a particular TableView row
                        return p.getValue().getNickNameProperty();
                    }
                });
        nickNameCol.setMinWidth(400/3);

        TableColumn seeCol = new TableColumn("Informations");
        seeCol.setCellFactory(ActionButtonTableCell.<Person>forTableColumn(">", (Person p) -> {
            try {
                FXMLLoader loader =  new FXMLLoader(getClass().getResource("/fr/isen/java2/fxml/PersonInformationMenuFXML.fxml"));
                Parent root = loader.load();
                PersonInformationMenuController personInformationMenuController = loader.getController();
                personInformationMenuController.init(p, false);
                Stage stage = new Stage();
                stage.setTitle("Contact");
                stage.setScene(new Scene(root, 600, 400));
                stage.setResizable(false);
                stage.setOnHidden(e ->{
                    if(personInformationMenuController.isDelete){
                        listOfPersons.remove(p);
                    }
                });
                stage.show();

            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return p;
        }));
        seeCol.setMinWidth(100);

        contactTable.setItems(listOfPersons);
        contactTable.getColumns().addAll(firstNameCol, lastNameCol, nickNameCol, seeCol);
    }

    @FXML
    public void addPersonLaunch(ActionEvent actionEvent){
        try{
            Person person = new Person();
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("/fr/isen/java2/fxml/PersonInformationMenuFXML.fxml"));
            Parent root = loader.load();
            PersonInformationMenuController personInformationMenuController = loader.getController();
            personInformationMenuController.init(person, true);
            personInformationMenuController.isCreation = true;
            Stage stage = new Stage();
            stage.setTitle("Contact");
            stage.setScene(new Scene(root, 600, 400));
            stage.setResizable(false);
            stage.setOnHidden(e -> {
                if(personInformationMenuController.isSaved){
                    listOfPersons.add(personInformationMenuController.person);
                }
            });
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void clusterListLaunch(ActionEvent actionEvent){
        try{
            sceneSwitcher.uploadNewScene((Stage)menuBar.getScene().getWindow(),
                                            "/fr/isen/java2/fxml/ClusterMenuFXML.fxml",
                                            600, 1000);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void importPerson(ActionEvent actionEvent){
        FileChooser.makeImport();
    }

    @FXML
    public void exportPersons(ActionEvent actionEvent){
        FileChooser.makeExport((LinkedList<Person>) listOfPersons);
    }

}
