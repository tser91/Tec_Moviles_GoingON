<?php

$description= $_POST['descripcion'];
$username= $_POST['usuario'];
$password= $_POST['password'];
$email= $_POST['email'];
$latitude= $_POST['latitude'];
$longitude= $_POST['longitude'];
$usertype= $_POST['usertype'];

require_once 'funciones_bd.php';
$db = new funciones_BD();

$response = array();

        if($db->adduser($username, $password, $email, $description, $latitude, $longitude, $usertype)){ 
        //  if($db->adduser("SGON", "hola", "vv@x", "es una prueba", "12.08", "14.5", "local")){          
                   
                  //$response["signinstatus"] = "1"; 
                         
                  $resultado[]=array("signinstatus"=>"1");
           }else{
         // failed to insert row
            
            if ($db->userExist($email)){
                    //$response["signinstatus"] = "2";  
                    $resultado[]=array("signinstatus"=>"2");
               }
               else{
                   //$response["signinstatus"] = "0";  
                   $resultado[]=array("signinstatus"=>"0");
               } 
        // echoing JSON response
        
        }		
        echo json_encode($resultado);  
?>
			