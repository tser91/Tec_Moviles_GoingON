<?php

$email= $_POST['email'];

require_once 'funciones_bd.php';
$db = new funciones_BD();

$resultado = $db->getInfoUser($email, "local");

//$resultado = $db->getInfoUser("r@r");

$response[] = array($resultado[0]=>$resultado[1], $resultado[2]=>$resultado[3], $resultado[4]=>$resultado[5], $resultado[6]=>$resultado[7],
                    $resultado[8]=>$resultado[9], $resultado[10]=>$resultado[11]);
echo json_encode($response);

?>