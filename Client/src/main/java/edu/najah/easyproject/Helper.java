package edu.najah.easyproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Stack;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;




public class Helper {
  
  public static Stage Window1;
  public static String EURS;
  public static String DateAPI;

  
  public static void changeWindow(Stage currentWindow, String windowName, String windowTitle, int width, int height) throws IOException {
    Stage primaryStage = new Stage();
    Window1 = primaryStage;
    Parent root = FXMLLoader.load(Objects.requireNonNull(Helper.class.getResource(windowName + ".fxml")));
    primaryStage.setTitle(windowTitle);
    primaryStage.setScene(new Scene(root, width, height));
    primaryStage.show();
    currentWindow.close();
  }

  public static void showAlert() throws IOException {
    Stage primaryStage = new Stage();
    Parent root = FXMLLoader.load(Objects.requireNonNull(Helper.class.getResource("Alert.fxml")));
    primaryStage.setTitle("Alert");
    primaryStage.setScene(new Scene(root, 330, 182));
    primaryStage.show();
  }

  
  public static String addParameter(String info, String name, String value) {
    if (name == null || value == null || name.length() == 0 || value.length() == 0) return info;
    info += name + "=" + URLEncoder.encode(value, StandardCharsets.US_ASCII) + "&";
    return info;
  }
  
  public static String prepareParameters(String[] arr) {
    String info = "";
    for (int i = 0; i < arr.length; i++) info = addParameter(info, arr[i], arr[++i]);
    return info;
  }
  
  public static String connectToServer(String serverName, String info) throws IOException {
    URL url;
    String Response = "";
    if(MainPageController.flagS && !MainPageController.flagT)
    {
      url= new URL("http://localhost/Networks2-main/Server/" + serverName + ".php");
      System.out.println("php Post");
      Response = PostServer(url,info);
    }else if(!MainPageController.flagS && !MainPageController.flagT)
    {
      url = new URL("http://localhost:8080/Networks2-main/" + serverName);
      System.out.println("servlest Post");
      Response = PostServer(url,info);

    } else if(MainPageController.flagS && MainPageController.flagT)
    {
      url = new URL("http://localhost/Networks2-main/Server/" + serverName + "1.php" +"?"+info);
      System.out.println("php Get");
      Response = GetServer(url);
    }else if(!MainPageController.flagS && MainPageController.flagT)
    {
      url = new URL("http://localhost/Networks2-main/server/responseServlet");
      //Networks2-main/Client/src/main/java/edu/najah/
      System.out.println("Servlest Get");
      Response = GetServer(url);
    }else {
      System.out.println("Select Server & Type Request");
    }

    return Response;
  }

  
  public static Stage getCurrentStage() {
    return (Stage) Stage.getWindows().stream().filter(Window::isShowing).iterator().next();
  }
  
  public static String[] convertResponseToArray(String response) {
    String[] temp = response.split(";");
    Stack<String> stack = new Stack<>();
    for (String s : temp) stack.push(s.startsWith("Last Access time") ? s.split("Last Access time:")[1] : s.split(":")[1]);
    String[] arr = new String[stack.size()];
    stack.toArray(arr);
    return arr;
  }

  public static void get_form_online_API() {
    String EUR ="";
    String data_update="";
    try {
    StringBuffer response = new StringBuffer();
    String access_key = "hdT8xu7b1KTGQk8pKsiK7a8pDQIS6XUW";
    String exchange_rates_api_uri = "https://api.apilayer.com/exchangerates_data/latest?symbols=EUR&base=USD";
    URL obj = new URL(exchange_rates_api_uri);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestProperty("apikey", access_key);
    con.setRequestMethod("GET");
    int responseCode = con.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) { // success
      BufferedReader in = new BufferedReader(new InputStreamReader(
              con.getInputStream()));
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
     // System.out.println(response);
      in.close();
      JsonObject jsonarray_data = new JsonParser().parse(response.toString()).getAsJsonObject();
      EUR = jsonarray_data.getAsJsonObject("rates").getAsJsonPrimitive("EUR").getAsString();
      String base = jsonarray_data.getAsJsonPrimitive("base").getAsString();
       data_update = jsonarray_data.getAsJsonPrimitive("date").getAsString();
      System.out.println("EUR: " + EUR +" Base: " + base + " Date: " + data_update);
    } else {
      System.out.println("GET request not worked");
    }

  }
    catch (Exception e) {
      e.printStackTrace();
    }
    EURS =EUR;
    DateAPI =data_update;
  }

  private static  String PostServer(URL url ,String info) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    try {
      connection.setDoOutput(true);
      connection.setDoInput(true);
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      connection.setUseCaches(false);
      BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
      outputStream.write(info.getBytes());
      outputStream.flush();
      outputStream.close();
    }catch (Exception h)
    {
      System.out.println("Connection error");
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    StringBuilder response = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      response.append(line);
    }
    reader.close();
    return response.toString();
  }

  private static  String GetServer(URL url) throws IOException{
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    int responseCode = con.getResponseCode();
    String response1="";
    System.out.println("GET Response Code :: " + responseCode);
    if (responseCode == HttpURLConnection.HTTP_OK) { // success
      BufferedReader in = new BufferedReader(new InputStreamReader(
              con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      response1 = response.toString();
      System.out.println(response.toString());
    } else {
      System.out.println("GET request not worked");
    }
    return response1.toString();
 }

 public static String NowDateANDTime() {
   LocalDateTime myDateObj = LocalDateTime.now();
   DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
   String formattedDate = myDateObj.format(myFormatObj);
   return formattedDate;
 }
}