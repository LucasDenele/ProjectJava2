package fr.isen.java2.db.daos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import fr.isen.java2.db.entities.Cluster;
import fr.isen.java2.db.entities.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PersonDaoTestCase {
    private PersonDao personDao = new PersonDao();

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

        stmt.close();
        connection.close();
    }

    @Test
    public void shouldListPersons(){
        List<Person> listOfPersons = personDao.getPersons();

        assertThat(listOfPersons).hasSize(3);
    }

    @Test
    public void shouldGetPerson(){
        List<Person> listOfPersons = personDao.getPersonBySearch("denele");

        assertThat(listOfPersons).hasSize(2);
    }

    @Test
    public void shouldAddPersonNoCluster(){
        Person person = new Person();
        person.setLastName("ME");
        person.setPhoneNumber("0645029843");

        personDao.addPerson(person, null);
        List<Person> listOfPersons = personDao.getPersons();
        assertThat(listOfPersons).hasSize(4);
    }

    @Test
    public void shouldAddPersonWithCluster(){
        Person person = new Person();
        person.setLastName("denele");
        person.setFirstName("robin");

        Cluster cluster = new Cluster();
        cluster.setId(2);

        personDao.addPerson(person, cluster);
        List<Person> listOfPersons = personDao.getPersonBySearch("robin");
        assertThat(listOfPersons).hasSize(1);

        try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
            String sqlQuery = "select * from linkage where (personid="+
                    ((LinkedList<Person>)listOfPersons).getLast().getId()+" and clusterid=2)";
            try(Statement statement = connection.createStatement()){
                try(ResultSet resultSet=statement.executeQuery(sqlQuery)){
                    while(resultSet.next()) {
                        assertThat(resultSet.getInt("personid")).isEqualTo(
                                ((LinkedList<Person>) listOfPersons).getLast().getId());
                        assertThat(resultSet.getInt("clusterid")).isEqualTo(2);
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
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
