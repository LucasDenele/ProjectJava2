package fr.isen.java2.db.daos;

import fr.isen.java2.Services.AddPersonToList;
import fr.isen.java2.db.entities.Cluster;
import fr.isen.java2.db.entities.Person;
import fr.isen.java2.fxml.AddPersonMenuController;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class PersonDao {

    public List<Person> getPersons(){
        List<Person> listOfPersons = new LinkedList<>();

        try(Connection connection = DataSourceFactory.getDataSource().getConnection()) {
            try(Statement statement = connection.createStatement()){
                String sqlQuery = "SELECT * FROM person";
                try(ResultSet resultSet = statement.executeQuery(sqlQuery)){
                    listOfPersons = AddPersonToList.addPersonToList(resultSet);
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfPersons;
    }

    public List<Person> getPersonBySearch(String search){
        List<Person> listOfPersons = new LinkedList<>();

        try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM person where (firstname=? OR lastname = ? OR nickname = ? OR phone_number = ?)")){
                statement.setString(1,search);
                statement.setString(2,search);
                statement.setString(3,search);
                statement.setString(4,search);
                try(ResultSet resultSet = statement.executeQuery()){
                    listOfPersons = AddPersonToList.addPersonToList(resultSet);
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listOfPersons;
    }

    public Person getPersonById(int id){
        Person person = new Person();

        try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM person where idperson=?")){
                statement.setInt(1,id);
                try(ResultSet resultSet = statement.executeQuery()){
                    person.setId(resultSet.getInt("idperson"));
                    person.setFirstName(resultSet.getString("firstname"));
                    person.setLastName(resultSet.getString("lastname"));
                    person.setNickName(resultSet.getString("nickname"));
                    person.setPhoneNumber(resultSet.getString("phone_number"));
                    person.setAddress(resultSet.getString("address"));
                    person.setEmailAddress(resultSet.getString("email_address"));
                    person.setBirthDate(resultSet.getDate("birth_date"));
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return person;

    }

    public void addPerson(Person person, Cluster cluster){
        try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
            String sqlQuery = "insert into person(lastname, firstname, nickname, phone_number, address, email_address, birth_date)"+"VALUES(?,?,?,?,?,?,?)";
            try(PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)){
                statement.setString(1, person.getLastName());
                statement.setString(2, person.getFirstName());
                statement.setString(3, person.getNickName());
                statement.setString(4, person.getPhoneNumber());
                statement.setString(5, person.getAddress());
                statement.setString(6, person.getEmailAddress());
                statement.setDate(7, person.getBirthDate());
                statement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(cluster != null){
            try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
                String sqlQuery = "insert into linkage(personid, clusterid)"+"VALUES(?,?)";
                try(PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)){
                    statement.setInt(1, person.getId());
                    statement.setInt(2, cluster.getId());
                    statement.executeUpdate();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void modifyPerson(Person person){
        try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
            String sqlQuery = "update person set lastname=?, firstname=?, nickname=?, phone_number=?, address=?, email_address=?, birth_date=? where idperson=?";
            try(PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)){
                statement.setString(1, person.getLastName());
                statement.setString(2, person.getFirstName());
                statement.setString(3, person.getNickName());
                statement.setString(4, person.getPhoneNumber());
                statement.setString(5, person.getAddress());
                statement.setString(6, person.getEmailAddress());
                statement.setDate(7, person.getBirthDate());

                statement.setInt(8, person.getId());
                statement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePerson(int id){
        try(Connection connection = DataSourceFactory.getDataSource().getConnection()) {
            String sqlQuery = "delete from person where idperson=?";
            try(PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)){
                statement.setInt(1, id);
                statement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
