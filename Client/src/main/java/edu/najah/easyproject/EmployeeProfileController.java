package edu.najah.easyproject;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


import java.io.IOException;

public class EmployeeProfileController {
  public TextField ID;
  public TextField password;

  public Label nameLabel;

  public Button editInfo;
  public Label status;
  public Label Last_Time1;




  public void initialize()
  {
    this.ID.setText(EmployeeLoginController.employeeInfo[0]);
    this.password.setText(EmployeeLoginController.employeeInfo[1]);
    this.Last_Time1.setText(EmployeeLoginController.employeeInfo[2]);
  }
  
  public void logout() throws IOException {
    Helper.changeWindow(Helper.getCurrentStage(), "MainPage", "Main Page", 569, 400);
  }
  
  public void updateInfo() throws IOException {
    if (editInfo.getText().equals("Edit Info")) {
      editInfo.setText("Save Edits");
      enableDisable(false);
    } else {
      editInfo.setText("Edit Info");
      enableDisable(true);
      if (ID.getText().isEmpty() || password.getText().isEmpty()) {
        status.setText("Please fill all the fields");
      } else {
        status.setText("");
        String[] params = {"function", "updateP", "ID", ID.getText(), "password", password.getText()};
        String info = Helper.prepareParameters(params);
        String response = Helper.connectToServer("AdminServer", info);
        status.setText(response);
        if (response.equals("Updated Successfully")) {
          status.setText("Updated Successfully");
        }
      }
    }
  }

  private void enableDisable(boolean b)
  {
    //ID.setDisable(b);
    password.setDisable(b);
  }
  
  public void goToEmployeesPage() throws IOException {
    Helper.changeWindow(Helper.getCurrentStage(), "Search", "Employees Information", 476, 435);
  }
}