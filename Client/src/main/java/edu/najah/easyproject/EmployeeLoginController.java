package edu.najah.easyproject;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmployeeLoginController {
  public static String[] employeeInfo = new String[4];
  public PasswordField password;
  public TextField ID;
  public Label status;

  public void login() throws IOException {
    if (ID.getText().isEmpty() || password.getText().isEmpty()) {
      status.setText("Please fill all the fields");
    } else {
      String[] params={"function", "login", "ID", ID.getText(), "password", password.getText(),
              "Last_Access_time",Helper.NowDateANDTime()};
      String info=Helper.prepareParameters(params);
      String response=Helper.connectToServer("AdminServer", info);
      if (response.equals("ID or Password is incorrect")) {
        status.setText(response);
      } else {
        employeeInfo=Helper.convertResponseToArray(response);
        Helper.changeWindow(Helper.getCurrentStage(), "AdminProfile", "Employee Profile", 489, 400);
      }
    }
  }
  
  public void logout() throws IOException {
    Helper.changeWindow(Helper.getCurrentStage(), "MainPage", "Main Page", 569, 400);
  }
}