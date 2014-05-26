<?php

$EventName= $_POST['EventName'];


require_once 'funciones_bd.php';
$db = new funciones_BD();

$resultado[] = $db->getEventInfo($EventName);

//$resultado[] = $db->getEventInfo(1);

echo json_encode($resultado);

?>