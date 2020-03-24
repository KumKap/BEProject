<?php 
require_once('config.php'); 
include_once 'config.php';                                                            
if( $conn ) {
    // echo "Connection established.<br />";
}
else{
     echo "Connection could not be established.<br />";
    // die( print_r( sqlsrv_errors(), true));
}

$param1 = $_POST['email'];
//$param1 = "hatim1@gmail.com2";

$sql = "SELECT * FROM user_register where email ='$param1'";
$retval = mysqli_query($conn, $sql);
$row = mysqli_fetch_assoc($retval);
if($row) {
            echo "Email already registered!";        
		}
else{
	echo "success";
}		
mysqli_close($conn);


?>