<?php
    
    $conn = mysql_connect("mysql5.000webhost.com", "a2502724_appOn", "ssga1234");
    mysql_select_db("a2502724_appOn") or die(mysql_error());
    $sql = "SELECT imageType,imageData FROM output_images WHERE imageId=2";
    $result = mysql_query("$sql") or die("<b>Error:</b> Problem on Retrieving Image BLOB<br/>" . mysql_error());
    $row = mysql_fetch_array($result);
    header("Content-type: " . $row["imageType"]);
    echo $row["imageData"];
    mysql_close($conn);
?>			