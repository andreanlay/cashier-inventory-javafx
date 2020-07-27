package cashier.inventory.project.database;

import java.sql.*;

public class dbHandler{
    private String loc = "jdbc:mysql://localhost:3306";
    private Connection connection = null;
    private Statement statement;
    private PreparedStatement preStatement;

    public dbHandler(){
        connect();
        createDB();
    }
    private void connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(loc, "root", "root");
        }catch(ClassNotFoundException | SQLException e){
            System.out.println("ERROR!");
        }
    }
    private void createDB(){
        String createDatabase = "CREATE DATABASE IF NOT EXISTS data CHARACTER SET utf8 COLLATE utf8_general_ci";
        String itemTable = "CREATE TABLE IF NOT EXISTS data.items (item_id varchar(20) PRIMARY KEY, item_name varchar(20), category varchar(20), quantity int, price int)";
        String categoryTable = "CREATE TABLE IF NOT EXISTS data.category (category varchar(20) PRIMARY KEY)";
        try{
            statement = connection.createStatement();
            statement.execute(createDatabase);
            statement.execute(itemTable);
            statement.execute(categoryTable);
        }catch(SQLException e){
            System.out.println("ERROR!");
        }
    }
    public boolean addItem(String id, String name, String category, int quantity, int price){
        String insert = "INSERT INTO data.items (item_id, item_name, category, quantity, price) VALUES (?, ?, ?, ?, ?)";
        try{
            preStatement = connection.prepareStatement(insert);
            preStatement.setString(1, id);
            preStatement.setString(2, name);
            preStatement.setString(3, category);
            preStatement.setInt(4, quantity);
            preStatement.setInt(5, price);
            preStatement.execute();
            return true;
        }catch(SQLException e){
            //System.out.println("ERROR Inserting!");
        }
        return false;
    }
    public void deleteItem(String id) throws SQLException{
        try{
            String delete = "DELETE FROM data.items WHERE item_id = ?";
            preStatement = connection.prepareStatement(delete);
            preStatement.setString(1, id);
            preStatement.execute();
        }catch(SQLException e){
            System.out.println("ERROR deleting!");
        }
    }
    public void deleteItemByName(String name){
        try{
            String delete = "DELETE FROM data.items WHERE item_name = ?";
            preStatement = connection.prepareStatement(delete);
            preStatement.setString(1, name);
            preStatement.execute();
            //Display.Message("Success", "Successfully deleted item! Reopen to update table!");
        }catch(SQLException e){
            System.out.println("ERROR deleting!");
        }
    }
    public ResultSet getItem(){
        String select = "SELECT*FROM data.items";
        try{
            statement = connection.createStatement();
            return statement.executeQuery(select);
        }catch(SQLException e){
            System.out.println("ERROR Selecting!");
        }
        return null;
    }
    public boolean addCategory(String categoryName){
        String insert = "INSERT INTO data.category (category) VALUES (?)";
        try{
            preStatement = connection.prepareStatement(insert);
            preStatement.setString(1, categoryName);
            preStatement.execute();
            return true;
        }catch(SQLException e){
            System.out.println("ERROR Inserting Category!");
        }
        return false;
    }
    public ResultSet getCategory(){
        String select = "SELECT*FROM data.category;";
        try{
            connection.createStatement();
            return statement.executeQuery(select);
        }catch(SQLException e){
            System.out.println("ERROR getting category!");
        }
        return null;
    }
    public ResultSet getItemByCategory(String category) {
        String select = "SELECT*FROM data.items WHERE category = ?";
        try{
            preStatement = connection.prepareStatement(select);
            preStatement.setString(1, category);
            return preStatement.executeQuery();
        }catch(SQLException ex){
            System.out.println("ERROR selecting item by category");
        }
        return null;
    }
    public ResultSet getItemsByName(String itemName){
        String select = "SELECT*FROM data.items WHERE item_name = ?";
        try{
            preStatement = connection.prepareStatement(select);
            preStatement.setString(1, itemName);
            return preStatement.executeQuery();
        }catch(SQLException ex){
            System.out.println("ERROR selecting item by name");
        }
        return null;
    }
    public void updateStock(String name, int amount){
        String update = "UPDATE data.items SET quantity = ?";
        try{
            preStatement = connection.prepareStatement(update);
            preStatement.setInt(1, amount);
            preStatement.execute();
        }catch(SQLException e){
            System.out.println("ERROR updating quantity");
        }
    }
}
