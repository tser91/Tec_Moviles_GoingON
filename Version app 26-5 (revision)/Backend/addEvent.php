<?php
 
$eventName= $_POST['eventName'];
$eventDescr= $_POST['eventDescr'];
$eventPrice = $_POST['eventPrice'];
$eventCategory= $_POST['eventCategory'];
$initDateDay= $_POST['initDateDay'];
$initDateMonth= $_POST['initDateMonth'];
$initDateYear= $_POST['initDateYear'];
$initTimeHour= $_POST['initTimeHour'];
$initTimeMinute= $_POST['initTimeMinute'];
$finalDateDay= $_POST['finalDateDay'];
$finalDateMonth= $_POST['finalDateMonth'];
$finalDateYear= $_POST['finalDateYear'];
$finalTimeHour= $_POST['finalTimeHour'];
$finalTimeMinute= $_POST['finalTimeMinute'];
$latitude= $_POST['latitude'];
$longitude= $_POST['longitude'];
$usermail = $_POST['usermail'];
 
require_once 'funciones_bd.php';
$db = new funciones_BD();

if($db->addEvent($eventName, $eventDescr, $eventPrice, $eventCategory,
     $initDateDay, $initDateMonth, $initDateYear, $initTimeHour, $initTimeMinute,
     $finalDateDay, $finalDateMonth,$finalDateYear,  $finalTimeHour,$finalTimeMinute,
     $latitude, $longitude, $usermail))
     {	

//if($db->addEvent("Feria de Negocios", "Te ofrece la mejor oportunidad para sobresalir con tus proyectos.", "Gratis", "Business",
//     "11", "05", "2014", "14", "30",
//     "11", "05","2014",  "18","30",
//     "-16.3294544", "-71.5673941", "kathya0812@gmail.com"))
//     {	
         //successfully inserted into database
        $resultado[]=array("logstatus"=>"0");
 
       // echoing JSON response
       echo json_encode($resultado);			
		 }else{
       // failed to insert row
        $resultado[]=array("logstatus"=>"1");
 
        // echoing JSON response
        echo json_encode($resultado);
	}	
        	
 
?>	