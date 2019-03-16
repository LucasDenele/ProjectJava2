package fr.isen.java2;

import fr.isen.java2.db.daos.DataSourceFactory;
import fr.isen.java2.fxml.PersonMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Statement;


public class Start extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //generateDB();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isen/java2/fxml/PersonMenuFXML.fxml"));
        Parent root = loader.load();
        PersonMenuController personMenuController = loader.getController();
        personMenuController.init(null);
        Scene scene = new Scene(root, 600, 1000);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Projet Java M1");
        primaryStage.setResizable(false);

        primaryStage.show();
    }


    public static void main(String[] args) {

        Application.launch(Start.class,args);
    }

    public void generateDB() throws Exception{
        Connection connection = DataSourceFactory.getDataSource().getConnection();
        Statement stmt = connection.createStatement();

        stmt.executeUpdate("insert into person(lastname, firstname, nickname, phone_number, address, email_address"+
                ",birth_date) values('denele', 'lucas', 'gaardur', '0645029843', '260 rue du Tournier, Samer'"+
                ",'lucas.denele@isen.yncrea.fr','1996-05-19')");
        stmt.executeUpdate("insert into person(lastname, firstname, nickname)"+
                " values('tej', 'junior', 'M1')");
        stmt.executeUpdate("insert into person(lastname, firstname) values('denele', 'jean-luc')");

        stmt.executeUpdate("insert into cluster(name) values('amis')");
        stmt.executeUpdate("insert into cluster(name) values('famille')");

        stmt.executeUpdate("insert into linkage(personid, clusterid) values(1, 2)");
        stmt.executeUpdate("insert into linkage(personid, clusterid) values(3, 2)");
        stmt.executeUpdate("insert into linkage(personid, clusterid) values(2, 1)");

        stmt.close();
        connection.close();
    }
}
