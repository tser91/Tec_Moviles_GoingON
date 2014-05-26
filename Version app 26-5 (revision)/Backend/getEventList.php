<?php

$email= $_POST['email'];


require_once 'funciones_bd.php';
$db = new funciones_BD();
$resultado[] = $db->getEventList($email);

//$resultado[] = $db->getEventList("r@r");


echo json_encode($resultado);

?>