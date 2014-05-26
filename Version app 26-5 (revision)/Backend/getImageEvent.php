<?php

$idEvent= $_POST['idEvent'];


require_once 'funciones_bd.php';
$db = new funciones_BD();
//$resultado[] = $db->getImage(1);

$resultado = $db->getImage(1);

header ('Content-type: image/jpeg');

echo json_encode($resultado);

?>