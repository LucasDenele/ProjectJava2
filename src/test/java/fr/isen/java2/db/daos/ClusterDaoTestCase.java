package fr.isen.java2.db.daos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import fr.isen.java2.db.entities.Cluster;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ClusterDaoTestCase {
    private ClusterDao clusterDao = new ClusterDao();

    @Before
    public void initDB()throws Exception{
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

    @Test
    public void shouldListClustersWhithoutPersons(){
        List<Cluster> listOfClusters = clusterDao.getClusters(false);

        assertThat(listOfClusters.size()).isEqualTo(2);
    }

    @Test
    public  void shouldListClusterWhithPersons(){
        List<Cluster> listOfClusters = clusterDao.getClusters(true);

        assertThat(listOfClusters.get(1).getPersons().size()).isEqualTo(2);
    }
    @After
    public void deleteDB() throws Exception{
        Connection connection = DataSourceFactory.getDataSource().getConnection();
        Statement stmt = connection.createStatement();

        stmt.executeUpdate("DELETE FROM cluster");
        stmt.executeUpdate("ALTER TABLE cluster AUTO_INCREMENT = 1;");
        stmt.executeUpdate("DELETE FROM person");
        stmt.executeUpdate("ALTER TABLE person AUTO_INCREMENT = 1;");
        stmt.executeUpdate("DELETE FROM linkage");
        stmt.executeUpdate("ALTER TABLE linkage AUTO_INCREMENT = 1;");

    }
}
