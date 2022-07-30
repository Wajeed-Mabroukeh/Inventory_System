module edu.najah.easyproject {
  //requires javafx.servlet.api;
  requires javafx.controls;
  requires javafx.base;
  requires javafx.fxml;
  requires java.sql;
  requires mysql.connector.java;
  //requires mysql.connector.java;
  requires java.net.http;
  requires com.google.gson;

    requires servlet.api;


  
  
  opens edu.najah.easyproject to javafx.fxml, javafx.base;
  exports edu.najah.easyproject;
}