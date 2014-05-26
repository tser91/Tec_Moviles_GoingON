<?php

if(isset($_FILES['uploaded'])){
	$target = "images/". basename($_FILES['uploaded']['name']) ;
	//print_r($_FILES);

	if(move_uploaded_file($_FILES['uploaded']['tmp_name'],$target)) 
		echo "OK!";//$chmod o+rw galleries

}else {
	echo "ERROR";	
}

?>