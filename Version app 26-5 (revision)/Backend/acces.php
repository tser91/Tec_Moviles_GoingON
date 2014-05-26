<?php

/*LOGIN*/

$mail= $_POST['email'];
$passw = $_POST['password'];
$typelogin = $_POST['typeLogin'];


require_once 'funciones_bd.php';
$db = new funciones_BD();

  	if($db->login($mail,$passw, $typelogin)){
           if (!$db->userExist($mail)){
	       $resultado[]=array("logstatus"=>"2");
           }
           else{
               $resultado[]=array("logstatus"=>"0");  
           }
	}
        else{
               $resultado[]=array("logstatus"=>"1");          
	}

        echo json_encode($resultado);

?>
				