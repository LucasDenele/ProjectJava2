package fr.isen.java2.fxml;

import fr.isen.java2.Services.ActionButtonTableCell;
import fr.isen.java2.Services.FileChooser;
import fr.isen.java2.Services.SceneSwitcher;
import fr.isen.java2.db.daos.ClusterDao;
import fr.isen.java2.db.entities.Cluster;
import fr.isen.java2.db.entities.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;

public class ClusterMenuController {
    private SceneSwitcher sceneSwitcher = new SceneSwitcher();
    private ClusterDao clusterDao = new ClusterDao();
    private ObservableList<Cluster> listOfClusters;

    @FXML
    private MenuBar menuBar;
    @FXML
    private TableView contactTable;
    @FXML
    private Label title;

    @FXML
    public void initialize(){

        title.setText("Groups :");

        ObservableList<Cluster> listOfClusters = FXCollections.observableArrayList(clusterDao.getClusters(false));


        TableColumn nameCol = new TableColumn("Group Name");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Cluster, String>("name")
        );
        nameCol.setMinWidth(200);

        TableColumn seeCol = new TableColumn("Informations");
        seeCol.setCellFactory(ActionButtonTableCell.<Cluster>forTableColumn(">", (Cluster c) -> {
            try {
                FXMLLoader loader =  new FXMLLoader(getClass().getResource("/fr/isen/java2/fxml/PersonMenuFXML.fxml"));
                Parent root = loader.load();
                PersonMenuController personMenuController = loader.getController();
                personMenuController.init(clusterDao.getCluster(c.getName(), true));
                Stage stage = (Stage) contactTable.getScene().getWindow();
                stage.setScene(new Scene(root, 600, 1000));
                stage.setResizable(false);
                stage.show();

            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return c;
        }));
        seeCol.setMinWidth(200);

        contactTable.setItems(listOfClusters);
        contactTable.getColumns().addAll(nameCol, seeCol);
    }

    @FXML
    public void addGroupLaunch(ActionEvent actionEvent){
        try{
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("/fr/isen/java2/fxml/ClusterAddMenuFXML.fxml"));
            Parent root = loader.load();
            ClusterAddMenuController clusterAddMenuController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("New Group");
            stage.setScene(new Scene(root, 600, 400));
            stage.setResizable(false);
            stage.setOnHidden(e -> {
                if(clusterAddMenuController.isSaved){
                    this.listOfClusters.add(clusterAddMenuController.getNewCluster());
                }
            });
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void personListLaunch(ActionEvent actionEvent){
        try{
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("/fr/isen/java2/fxml/PersonMenuFXML.fxml"));
            Parent root = loader.load();
            PersonMenuController personMenuController = loader.getController();
            personMenuController.init(null);
            Stage stage = (Stage) contactTable.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 1000));
            stage.setResizable(false);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
