package edu.najah.easyproject;

public class Product {
  public String ID;
  public String Amount;
  public String Price;
  public String name;
  
  public Product(String id, String Amount, String Price, String name) {
    this.ID=id;
    this.Amount=Amount;
    this.Price=Price;
    this.name=name;
  }
  
  // setters and getters
  public String getId() {
    return ID;
  }
  
  public void setId(String id) {
    this.ID=id;
  }
  
  public String getAmount() {
    return Amount;
  }
  
  public void setAmount(String Amount) {
    this.Amount=Amount;
  }
  
  public String getPrice() {
    return Price;
  }
  
  public void setPrice(String Price) {
    this.Price=Price;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name=name;
  }

}