package edu.najah.easyproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class SearchController {
  public TableView<Product> table;
  public TableColumn<Product, Integer> idColumn;
  public TableColumn<Product, String> amountColumn;
  public TableColumn<Product, String> nameColumn;
  public TableColumn<Product, String> priceColumn;

  public Label status;

  public TextField ID;
  public TextField Name;
  public TextField Price;
  public TextField Amount;


  public Button editInfo;
  public Button deleteUserButton;

  public Button nextButton;
  public Button prevButton;
  public Button AddP;
  public ComboBox<String> searchBy;
  public TextField searchField;
  public ComboBox TypePrice;
  public Label Date;
  String[] usersInfo;
  String[] res;
  Product[] products;
  String selectedId;
  String TypeByFlag = "";
  String searchByFlag = "";
  String searchFieldStr = "";
  String tempEmail = "";
  ObservableList<Product> productList;
  int index;
  boolean flag = false;

  String changePrice="";
  
  public void initialize() throws IOException {
    Helper.get_form_online_API();
    Date.setText(Helper.NowDateANDTime());
    res = new String[3];
    products = new Product[5];
    setFieldsToDefaultValues();
    prevButton.setDisable(true);
    nextButton.setDisable(true);
    
    searchField.textProperty().addListener((obs, old, niu) -> {
      searchFieldStr = searchField.getText();
      try {
        getUsersFromDB();
        index = 0;
        refreshTable();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
    getUsersFromDB();
    refreshTable();
    searchBy.setItems(FXCollections.observableArrayList("ID", "Name" ,"Amount","Price"));
    TypePrice.setItems(FXCollections.observableArrayList("USD", "EUR"));
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));


    table.setRowFactory(tv -> {
      TableRow<Product> row = new TableRow<>();
      row.setOnMouseClicked(event -> {
        if (row.getItem() != null && !Objects.equals(row.getItem().getId(), "")) {
          selectedId = row.getItem().getId();
          for (Product product : products) {
            if (product != null && product.getId().equals(row.getItem().getId())) {
              this.ID.setText(product.getId());
              this.Name.setText(product.getName());
              this.Price.setText(product.getPrice());
              this.Amount.setText(product.getAmount());
              enableDisable(true);
              editInfo.setText("Edit Info");
              this.editInfo.setDisable(false);
              this.deleteUserButton.setDisable(false);
              this.AddP.setDisable(true);
            }
          }
        } else {
          setFieldsToDefaultValues();
        }
      });
      return row;
    });
  }
  
  private void refreshTable() {
    if(changePrice == "EUR")
    {
      try {
        for (int i = 0; i < 5; i++)
          products[i] = new Product("", "", "", "");
        for (int i = index * 20, j = 0; i < Math.min(index * 20 + 20, usersInfo.length); i += 4, j++) {
          float PriceF = Float.parseFloat(this.usersInfo[i + 3]) * Float.parseFloat(Helper.EURS);
          String newP = PriceF + "";
          products[j] = new Product(this.usersInfo[i], this.usersInfo[i + 1], this.usersInfo[i + 2], newP);
        }
        productList = FXCollections.observableArrayList(products);
        table.setItems(productList);
        nextButton.setDisable(index * 5 + 5 >= usersInfo.length / 4);
        prevButton.setDisable(index == 0);
      }catch (Exception d)
      {
        productList = FXCollections.observableArrayList(products);
        table.setItems(productList);
        nextButton.setDisable(index * 5 + 5 >= usersInfo.length / 4);
        prevButton.setDisable(index == 0);
        return;
      }
    }
    else {
      for (int i = 0; i < 5; i++)
        products[i] = new Product("", "", "", "");
      for (int i = index * 20, j = 0; i < Math.min(index * 20 + 20, usersInfo.length); i += 4, j++)
        products[j] = new Product(this.usersInfo[i], this.usersInfo[i + 1], this.usersInfo[i + 2], this.usersInfo[i + 3]);
      productList = FXCollections.observableArrayList(products);
      table.setItems(productList);
      nextButton.setDisable(index * 5 + 5 >= usersInfo.length / 4);
      prevButton.setDisable(index == 0);

    }

  }

  @FXML
  void enableAdd() {
    this.ID.setText("");
    this.Name.setText("");
    this.Amount.setText("");
    this.Price.setText("");
    enableDisable(false);
    this.editInfo.setDisable(true);
    this.deleteUserButton.setDisable(true);
    this.AddP.setDisable(false);
  }

  @FXML
  public void Refresh(){
    Date.setText(Helper.NowDateANDTime());
  }

  @FXML
  public void AddProduct() throws IOException {
    int IDi;
    int Amount1;
    float Price1;
    String AmountG;

    if (ID.getText().isEmpty() || Name.getText().isEmpty()  || Amount.getText().isEmpty()) {
      status.setText("Please Fill All The Fields");
    }
    else {
      try{
        IDi =Integer.parseInt(ID.getText());
        if(IDi<=0)
        {
          status.setText("ID must be number"+"\n"+" positive(not negative)");
          return ;
        }
      }catch (Exception h)
      {
        AlertControl.label="ID must be number positive(not negative)" +"\n"+"No letters ";
        Helper.showAlert();
        return;
      }
      try {
        if (Price.getText().isEmpty()) {
          AmountG = "100";
        } else {
          Amount1 = Integer.parseInt(Price.getText());
          if (Amount1 <= 0) {
            status.setText("Amount must be number" + "\n" + " positive(not negative)");
            return;
          }
          else {
            AmountG=Price.getText();
          }
        }}catch(Exception h)
        {
          AlertControl.label = "Amount must be number positive(not negative)" + "\n" + "No letters ";
          Helper.showAlert();
          return;
        }

      try{
        Price1 =Float.parseFloat(Name.getText());
        if(Price1<=0.0)
        {
          status.setText("Price must be number"+"\n"+" positive(not negative)");
         return;
        }
      }catch (Exception h)
      {
        AlertControl.label="Price must be number positive(not negative)" +"\n"+"No letters ";
        Helper.showAlert();
        return;
      }
      status.setText("");
      String  priceN = changePrice.equals("EUR") ? Float.parseFloat(Name.getText())/Float.parseFloat(Helper.EURS) +"" :
              Name.getText();
      String[] params = {
              "function", "addNewUser",
              "ID", ID.getText().equals("") ? "default" : ID.getText(),
              "Name",Amount.getText(),
              "Amount", AmountG,
              "Price", priceN,
      };
      String info = Helper.prepareParameters(params);
      String response = Helper.connectToServer("AdminServer", info);
      status.setText(response);
      if(!response.equals("ID is already taken!"))
      {
        ID.setText("");
        Name.setText("");
        Amount.setText("");
        Price.setText("");
      }
    }
    getUsersFromDB();
    index = 0;
    refreshTable();
    }

  void setFieldsToDefaultValues() {
    this.ID.setText("");
    this.Name.setText("");
    this.Amount.setText("");
    this.Price.setText("");
    this.editInfo.setDisable(true);
    this.deleteUserButton.setDisable(true);
    this.AddP.setDisable(true);
  }
  
  private void getUsersFromDB() throws IOException {
    String[] params = {
            "function", "getUsersFromDB",
            "withSearch", searchFieldStr.equals("") ? "0" : "1",
            "searchBy", searchByFlag.equals("ID") ? "ID" :
            searchByFlag.equals("Name") ? "Name":
            searchByFlag.equals("Amount") ? "Amount":
            searchByFlag.equals("Price") ? "Price":"none",
            "searchField", searchFieldStr.length() > 0 ? searchFieldStr : "none"
    };
    String info = Helper.prepareParameters(params);
    String response = Helper.connectToServer("AdminServer", info);
    String[] users = response.split(";");
    usersInfo = new String[4 * users.length];
    
    for (int i = 0; i < users.length; i++)
      if (users[i] != null && !users[i].equals("")) {
        System.arraycopy(users[i].split(","), 0, usersInfo, i * 4, 4);
        flag = false;
      } else {
        for (int j = 0; j < 4; j++)
          usersInfo[i * 4 + j] = "";
        flag = true;
      }
  }
  
  public void logout() throws IOException {
    Helper.changeWindow(Helper.getCurrentStage(), "AdminProfile", "Admin Profile", 489, 400);
  }

  private void enableDisable(boolean b) {
    ID.setDisable(b);
    Price.setDisable(b);
    Amount.setDisable(b);
    Name.setDisable(b);
  }
  
  public void deleteUser() throws IOException {
    String[] params = {"function", "deleteUser", "ID", selectedId};
    String info = Helper.prepareParameters(params);
    String response = Helper.connectToServer("AdminServer", info);
    status.setText(response);
    refreshTable();
    index = 0;
    setFieldsToDefaultValues();
    getUsersFromDB();
    refreshTable();
  }
  
  public void updateInfo() throws IOException {
    int IDi;
    int Amount1;
    float Price1;
    String AmountG;
    if (editInfo.getText().equals("Edit Info")) {
      tempEmail = ID.getText();
      editInfo.setText("Save Edits");
      enableDisable(false);
      ID.setDisable(true);
    } else {
      try{
        IDi =Integer.parseInt(ID.getText());
        if(IDi<=0)
        {
          status.setText("ID must be number"+"\n"+" positive(not negative)");
          return ;
        }
      }catch (Exception h)
      {
        AlertControl.label="ID must be number positive(not negative)" +"\n"+"No letters ";
        Helper.showAlert();
        return;
      }
      try{
        if (Price.getText().isEmpty()) {
          AmountG = "100";
        } else {
          Amount1 = Integer.parseInt(Price.getText());
          if (Amount1 <= 0) {
            status.setText("Amount must be number" + "\n" + " positive(not negative)");
            return;
          } else {
            AmountG = Price.getText();
          }
        }
      }catch (Exception h)
      {
        AlertControl.label="Amount must be number positive(not negative)" +"\n"+"No letters ";
        Helper.showAlert();
        return;
      }
      try{
        Price1 =Float.parseFloat(Name.getText());
        if(Price1<=0)
        {
          status.setText("Price must be number"+"\n"+" positive(not negative)");
          return;
        }
      }catch (Exception h)
      {
        AlertControl.label="Price must be number positive(not negative)" +"\n"+"No letters ";
        Helper.showAlert();
        return;
      }
      editInfo.setText("Edit Info");
      enableDisable(true);
      if (ID.getText().isEmpty() || Name.getText().isEmpty() || Amount.getText().isEmpty()) {
        status.setText("Please fill all the fields");
      } else {
        status.setText("");
        String  priceN = changePrice.equals("EUR") ? Float.parseFloat(Name.getText())/Float.parseFloat(Helper.EURS) +"" :
                Name.getText();
        String[] params = {"function", "updateInfo", "ID", ID.getText(), "Name", Amount.getText(), "Amount", AmountG, "Price", priceN, "ID", selectedId};
        String info = Helper.prepareParameters(params);
        String response = Helper.connectToServer("AdminServer", info);
        status.setText(response);
        getUsersFromDB();
        refreshTable();
      }
    }
  }
  
  public void prev() {
    index--;
    refreshTable();
  }
  
  public void next() {
    index++;
    refreshTable();
  }

  public void changeSearchBy() throws IOException {
    searchByFlag = searchBy.getSelectionModel().getSelectedItem();
    getUsersFromDB();
    index = 0;
    refreshTable();
  }
  public void changeTypeBy() throws IOException {
    TypeByFlag = (String) TypePrice.getSelectionModel().getSelectedItem();
    changePrice = TypeByFlag.equals("USD")?"USD":"EUR";
    getUsersFromDB();
    index = 0;
    refreshTable();
  }


}