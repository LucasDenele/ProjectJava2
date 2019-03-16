package fr.isen.java2.fxml;

import fr.isen.java2.Services.ActionButtonTableCell;
import fr.isen.java2.db.daos.ClusterDao;
import fr.isen.java2.db.daos.PersonDao;
import fr.isen.java2.db.entities.Cluster;
import fr.isen.java2.db.entities.Person;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.LinkedList;


public class ClusterAddMenuController {

    private LinkedList<Person> listOfPersons = new LinkedList<>();
    private PersonDao personDao = new PersonDao();
    private ClusterDao clusterDao = new ClusterDao();
    public boolean isSaved = false;

    @FXML
    private TextField clusterName;
    @FXML
    private TableView contactTable;
    @FXML
    private Button saveButton;
    @FXML
    private Label logMessage;

    @FXML
    public void initialize(){

        ObservableList<Person> personsInTable = FXCollections.observableArrayList(personDao.getPersons());

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

        TableColumn checkCol = new TableColumn("Add" );
        checkCol.setCellFactory(ActionButtonTableCell.<Person>forTableColumn(">", (Person p) -> {
            this.listOfPersons.add(p);
            return p;
        }));

        contactTable.setItems(personsInTable);
        contactTable.getColumns().addAll(firstNameCol, lastNameCol, nickNameCol, checkCol);
    }

    @FXML
    public void saveCluster(){
        clusterDao.addCluster(clusterName.getText(), listOfPersons);
        isSaved = true;
        logMessage.setText("Cluster save, you can close");
    }

    public Cluster getNewCluster(){
        return clusterDao.getCluster(this.clusterName.getText(),false);
    }
}
