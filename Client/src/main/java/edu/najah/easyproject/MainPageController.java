package edu.najah.easyproject;


import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import java.io.IOException;

public class MainPageController {

  public   RadioButton Get;
  public   RadioButton Post;
  public   RadioButton php;
  public   RadioButton servlest;

  public static boolean flagS =false;
  public static boolean flagT =false;
  private ToggleGroup groupT = new ToggleGroup();
  private ToggleGroup groupS = new ToggleGroup();


  public void initialize()
  {
    this.Get.setSelected(true);
    this.php.setSelected(true);
    flagT = true;
    flagS = true;
  }
  public void LoginEmployee() throws IOException {
    Helper.changeWindow(Helper.getCurrentStage(), "EmployeeLogin", "Employee Login", 310, 400);
  }

  public void RadioGroupT()
  {
   Get.setToggleGroup(groupT);
   Post.setToggleGroup(groupT);
   if(Get.isSelected())
   {
     flagT =true;
   }
   else{
     flagT=false;
   }
  }
  public void RadioGroupS()
  {
    php.setToggleGroup(groupS);
    servlest.setToggleGroup(groupS);
    if(php.isSelected())
    {
      flagS =true;
    }
    else{
      flagS=false;
    }
  }
}