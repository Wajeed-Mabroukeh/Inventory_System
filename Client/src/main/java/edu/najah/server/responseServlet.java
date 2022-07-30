package edu.najah.server;

import edu.najah.easyproject.Helper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.*;
import javax.servlet.http.*;


public class responseServlet extends HttpServlet {

    private Connection server() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/inventorysystem", "root", "");
        return connection;
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(" fff");
            switch ("function") {
                case "login":
                    String Result = login(request.getParameter("ID"), request.getParameter("password"));
                    if(!(Result.equals("ID or Password is incorrect") && Result.equals("Login Failed"))){
                        String Datee = Helper.NowDateANDTime();
                        String query1="UPDATE employees SET Last Access time='"+Datee.toString()+
                                "' WHERE ID = '"+request.getParameter("ID")+"'"+
                                " and password = '"+request.getParameter("password")+"'";
                        Statement stmt1 = server().createStatement();
                        ResultSet rs1=stmt1.executeQuery(query1);
                        out.print(Result);
                    }
                    else {
                        out.print("not exist");
                    }
                    break;
                case "updateInfo":
                    String Result1 = updateInfo(request.getParameter("ID"),
                                     request.getParameter("Name"),
                                     request.getParameter("Amount"),
                                     request.getParameter("Price"));
                    if(!(Result1.equals("ID is already taken!") && Result1.equals("Updated Failed")))
                    {
                        out.print(Result1);
                    }
                    out.print(Result1);
                    break;
                case "updateP":
                    String Result2 = updateP(request.getParameter("ID"),
                            request.getParameter("password"));
                    if(!(Result2.equals("ID is already taken!") && Result2.equals("Updated Failed")))
                    {
                        out.print(Result2);
                    }
                    out.print(Result2);
                    break;
                case "addNewUser":
                    String Result3 = updateInfo(request.getParameter("ID"),
                            request.getParameter("Name"),
                            request.getParameter("Amount"),
                            request.getParameter("Price"));
                    if(!(Result3.equals("ID is already taken!") && Result3.equals("Name is already taken!")))
                    {
                        out.print(Result3);
                    }
                    out.print(Result3);
                    break;
                case "getUsersFromDB":
                    String Result4 = updateInfo(request.getParameter("ID"),
                            request.getParameter("Name"),
                            request.getParameter("Amount"),
                            request.getParameter("Price"));
                    if(!(Result4.equals("ID is already taken!") && Result4.equals("Updated Failed")))
                    {
                        out.print(Result4);
                    }
                    out.print(Result4);
                    break;
                case "deleteUser":
                    String Result5 = updateInfo(request.getParameter("ID"),
                            request.getParameter("Name"),
                            request.getParameter("Amount"),
                            request.getParameter("Price"));
                    if(!(Result5.equals("ID is already taken!") && Result5.equals("Updated Failed")))
                    {
                        out.print(Result5);
                    }
                    out.print(Result5);
                    break;
                default:
                    System.out.println("hh");
            }
        } catch (SQLException ex) {
            Logger.getLogger(edu.najah.server.responseServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    public  String login(String ID, String password) {
        try {

            String result = "";
            Class.forName("com.mysql.cj.jdbc.Driver");
            PreparedStatement preparedStatement = server().prepareStatement("SELECT * FROM employees WHERE ID='" + ID + "' AND Password='" + password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result += "ID:" + resultSet.getString("ID") + ";";
                result += "Password:" + resultSet.getString("Password") + ";";
                result += "Last Access time:" + resultSet.getString("Last Access time") + ";";
                return result;
            }
            return "ID or Password is incorrect";
        } catch (Exception e) {
            e.printStackTrace();
            return "Login Failed";
        }
    }

    public  String updateInfo(String ID, String Name, String Amount, String Price) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/inventorysystem", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement("Select ID from products where id='" + ID + "'");
            ResultSet resultSet = preparedStatement.executeQuery();

            // check if ID is not changed
            if (resultSet.next() && resultSet.getString("ID").equals(ID)) {
                preparedStatement = connection.prepareStatement("UPDATE products SET Name='" + Name + "', Amount='" + Amount + "', Price='" + Price + "' WHERE ID='" + ID + "'");
                return preparedStatement.executeUpdate() > 0 ? "Updated Successfully" : "Updated Failed";
            }

            // check if ID is changed
            preparedStatement = connection.prepareStatement("Select ID from products where ID='" + ID + "'");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return "ID is already taken!";
            }
            else{
                preparedStatement = connection.prepareStatement("UPDATE products SET ID='" + ID +"' ,Name='" + Name + "', Amount='" + Amount + "', Price='" + Price + "'");
                return preparedStatement.executeUpdate() > 0 ? "Updated Successfully" : "Updated Failed";

            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Updated Failed";
        }
    }

    public  String updateP(String ID, String password) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/inventorysystem", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement("Select ID from products where id='" + ID + "'");
            ResultSet resultSet = preparedStatement.executeQuery();

                preparedStatement = connection.prepareStatement("UPDATE products SET Password='" + password + "' WHERE ID='" + ID + "'");
                return preparedStatement.executeUpdate() > 0 ? "Updated Successfully" : "Updated Failed";

        } catch (Exception e) {
            e.printStackTrace();
            return "Updated Failed";
        }
    }


    public  String addNewUser(String ID, String Name, String Amount, String Price) {
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/inventorysystem", "root", "");

            // if id is given, check if the id is already exist
            if (!ID.equals("default")) {
                preparedStatement = connection.prepareStatement("Select id from products where ID='" + ID + "'");
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) return "ID is already taken!";
            }

            // check if email is already exist
            preparedStatement = connection.prepareStatement("Select Name from products where Name='" + Name + "'");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return "Name is already taken!";


            // insert new user
            if (!ID.equals("default"))
                preparedStatement = connection.prepareStatement("INSERT INTO products (ID, Name, Amount, Price) VALUES (?, ?, ?, ?)");
            else
                preparedStatement = connection.prepareStatement("INSERT INTO products (ID, Name, Amount, Price) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, ID);
            preparedStatement.setString(2, Name);
            preparedStatement.setString(3, Amount);
            preparedStatement.setString(4, Price);
            if (!ID.equals("default")) preparedStatement.setString(7, ID);
            return preparedStatement.executeUpdate() > 0 ? "Added Successfully" : "Added Failed";
        } catch (Exception e) {
            e.printStackTrace();
            return "Added Failed";
        }
    }

    public  String getUsersFromDB( String withSearch, String searchBy, String searchField) {
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/inventorysystem", "root", "");

            if(withSearch.equals("1") && !searchBy.equals("none")) {
                if (searchBy.equals("ID")) {
                    preparedStatement = connection.prepareStatement("Select * from products where ID like '%" + searchField + "%'");
                } else if (searchBy.equals("Name")) {
                    preparedStatement = connection.prepareStatement("Select * from products where Name like '%" + searchField + "%'");
                } else if (searchBy.equals("Amount")) {
                    preparedStatement = connection.prepareStatement("Select * from products where Amount like '%" + searchField + "%'");
                } else if (searchBy.equals("Price")) {
                    preparedStatement = connection.prepareStatement("Select * from products where Price like '%" + searchField + "%'");
                }
            }
            else{
                preparedStatement = connection.prepareStatement("Select * from products"+"'");
            }
            resultSet = preparedStatement.executeQuery();
            StringBuilder users = new StringBuilder();
            while (resultSet.next())
                users.append(resultSet.getString("ID")).append(",").append(resultSet.getString("Name")).append(",").append(resultSet.getString("Amount")).append(",").append(resultSet.getString("Price")).append(";");
            return users.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Query Failed";
        }
    }

    public  String deleteUser(String id) {
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/inventorysystem", "root", "");
            preparedStatement = connection.prepareStatement("Delete from products where ID='" + id + "'");
            return preparedStatement.executeUpdate() > 0 ? "Deleted Successfully" : "Deleted Failed";
        } catch (Exception e) {
            e.printStackTrace();
            return "Query Failed";
        }
    }

}