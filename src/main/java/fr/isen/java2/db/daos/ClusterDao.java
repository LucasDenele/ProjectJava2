package fr.isen.java2.db.daos;

import fr.isen.java2.Services.AddPersonToList;
import fr.isen.java2.db.entities.Cluster;
import fr.isen.java2.db.entities.Person;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ClusterDao {

    public List<Cluster> getClusters(boolean getPersons){
        List<Cluster> listOfCluster = new LinkedList<>();
            try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
                try (Statement statement = connection.createStatement()) {
                    String sqlQuery = "SELECT * FROM cluster";
                    try (ResultSet resultSet = statement.executeQuery(sqlQuery)) {
                        while (resultSet.next()) {
                            listOfCluster.add(new Cluster());
                            ((LinkedList<Cluster>) listOfCluster).getLast().setId(resultSet.getInt("idcluster"));
                            ((LinkedList<Cluster>) listOfCluster).getLast().setName(resultSet.getString("name"));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if(getPersons) {
                    for (Cluster cluster : listOfCluster) {
                        cluster.setPersons((LinkedList<Person>)this.getPersonsInCluster(connection, cluster));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listOfCluster;
    }

    public Cluster getCluster(String name, boolean getPersons){
        Cluster clusterSearch = new Cluster();

        try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM cluster where  name=?")){
                statement.setString(1,name);
                try(ResultSet resultSet = statement.executeQuery()){
                    while(resultSet.next()){
                        clusterSearch.setId(resultSet.getInt("idcluster"));
                        clusterSearch.setName(resultSet.getString("name"));
                        if(getPersons){
                            clusterSearch.setPersons((LinkedList<Person>)this.getPersonsInCluster(connection, clusterSearch));
                        }
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(clusterSearch.getName() == null){
            return null;
        }else{
            return clusterSearch;
        }
    }

    public void addCluster(String name, LinkedList<Person> listOfPersons){
        try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
            String sqlQuery = "insert into cluster(name)"+"VALUES(?)";
            try(PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)){
                statement.setString(1, name);
                statement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(listOfPersons != null){
            Cluster cluster = getCluster(name, false);
            try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
                String sqlQuery = "insert into linkage(personid, clusterid)"+"VALUES(?,?)";
                try(PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)){
                    for(Person person : listOfPersons){
                        statement.setInt(1, person.getId());
                        statement.setInt(2, cluster.getId());
                        statement.executeUpdate();
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Person> getPersonsInCluster(Connection connection, Cluster cluster){
        List<Person> listOfPerson = new LinkedList<>();

        try (Statement statement = connection.createStatement()) {
            String sqlQuery = "SELECT * FROM linkage JOIN person ON linkage.personid=person.idperson where clusterid=" + cluster.getId();
            try (ResultSet resultSet = statement.executeQuery(sqlQuery)) {
                listOfPerson = AddPersonToList.addPersonToList(resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listOfPerson;
    }
}
