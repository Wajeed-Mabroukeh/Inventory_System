<?php
function login($ID, $password,$Last_Access_time) {
  $conn = new mysqli("localhost", "root", "", "inventorysystem");
  $loginQuery = "SELECT * FROM employees WHERE ID='" . $ID . "' AND Password='" . $password."'";
  $result = mysqli_query($conn, $loginQuery);
  if (mysqli_num_rows($result) > 0) {
   $updateQuery = "UPDATE employees SET `Last Access time`='" . $Last_Access_time ."' WHERE `ID`='" . $ID . "'";
   $re = mysqli_query($conn, $updateQuery);
    $str = "";
    foreach (mysqli_fetch_assoc($result) as $key => $value) {
        $str .= $key . ":" . $value . ";";
    }
    return $str;
  } else {
    return "ID or Password is incorrect";
  }
}

function updateInfo($id, $Name, $Amount, $Price) {
  $conn = new mysqli("localhost", "root", "", "inventorysystem");
  $updateQuery = "Select ID from products where ID='" . $id . "'";
  $result = mysqli_query($conn, $updateQuery);

    $updateQuery = "UPDATE products SET Name='" . $Name . "', Amount='" . $Amount . "', Price='" . $Price . "' WHERE ID='" . $id . "'";
    return (mysqli_query($conn, $updateQuery) ? "Updated Successfully" : "Updated Failed");
}

function updateP($id, $password) {
  $conn = new mysqli("localhost", "root", "", "inventorysystem");
  $updateQuery = "Select ID from employees where ID='" . $id . "'";
  $result = mysqli_query($conn, $updateQuery);

    $updateQuery = "UPDATE employees SET password='" . $password . "' WHERE ID='" . $id . "'";
    return (mysqli_query($conn, $updateQuery) ? "Updated Successfully" : "Updated Failed");
}



function addNewUser($ID, $Name, $Amount, $Price) {
  $conn = new mysqli("localhost", "root", "", "inventorysystem");

  if ($ID != "default") { // if id is given, check if it is already exist
    $ids = array();
    $getIdsQuery = "SELECT ID FROM products";
    $result = mysqli_query($conn, $getIdsQuery);
    while ($row1 = mysqli_fetch_assoc($result)) {
      $ids[] = $row1['ID'];
    }
    if (in_array($ID, $ids)) { // if id is already exist
      return "ID is already taken!";
    }
  }
  
  // check if Name is already exist
  $Name1 = array();
  $getEmailsQuery = "SELECT Name FROM products";
  $result = mysqli_query($conn, $getEmailsQuery);
  while ($row1 = mysqli_fetch_assoc($result)) {
    $Name1[] = $row1['Name'];
  }
  if (in_array($Name, $Name1)) { // if Name is already exist
    return "This Name is already taken!";
  }

  if ($ID != "default")
  {
    $insertQuery = "INSERT INTO products (ID, Name, Amount, Price) VALUES ('$ID', '$Name', '$Amount', '$Price')";
    }
  else{
  $ID=rand();
  $insertQuery = "INSERT INTO products (ID, Name, Amount, Price) VALUES ('$ID', '$Name', '$Amount', '$Price')";
  }
  return (mysqli_query($conn, $insertQuery) ? "Added Successfully" : "Added Failed");
}

function getUsersFromDB($withSearch, $searchBy, $searchField) {
  $conn = new mysqli("localhost", "root", "", "inventorysystem");
  if ($withSearch == "1" && $searchBy != "none") {
    if ($searchBy == "ID")
    {
        $getUsersQuery = "SELECT * FROM products WHERE ID like '%$searchField%'";
    }
    else if($searchBy == "Name")
    {
       $getUsersQuery = "SELECT * FROM products WHERE Name like '%$searchField%'";
    }
    else if($searchBy == "Amount")
    {
       $getUsersQuery = "SELECT * FROM products WHERE Amount like '%$searchField%'";
    }
    else if($searchBy == "Price")
    {
        $getUsersQuery = "SELECT * FROM products WHERE Price like '%$searchField%'";
    }


    $result = mysqli_query($conn, $getUsersQuery);
    $users = array();
    while ($row = mysqli_fetch_assoc($result)) $users[] = $row;
    $result = '';
    foreach ($users as $product) {
        $result .= implode(',', $product) . ";";
    }
    return $result;
  }
   else {
        $getUsersQuery = "SELECT * FROM products ";
         $result = mysqli_query($conn, $getUsersQuery);
            $users = array();
            while ($row = mysqli_fetch_assoc($result)) $users[] = $row;
            $result = '';
            foreach ($users as $product) {
                $result .= implode(',', $product) . ";";
            }
            return $result;
      }
}

function deleteUser($id) {
  $conn = new mysqli("localhost", "root", "", "inventorysystem");
  $deleteQuery = "DELETE FROM products WHERE ID='$id'";
  return (mysqli_query($conn, $deleteQuery) ? "Deleted Successfully" : "Deleted Failed");
}


switch ($_GET['function']) {
  case 'login':
    echo login($_GET['ID'], $_GET['password'],$_GET['Last_Access_time']);
    break;
  case 'updateInfo':
    echo updateInfo($_GET['ID'], $_GET['Name'], $_GET['Amount'], $_GET['Price']);
    break;
    case 'updateP':
        echo updateP($_GET['ID'], $_GET['password']);
        break;
  case 'addNewUser':
    echo addNewUser($_GET['ID'], $_GET['Name'], $_GET['Amount'], $_GET['Price']);
    break;
  case 'getUsersFromDB':
    echo getUsersFromDB($_GET['withSearch'], $_GET['searchBy'], $_GET['searchField']);
    break;
  case 'deleteUser':
    echo deleteUser($_GET['ID']);
    break;
  default:
    echo "Invalid Request";
}