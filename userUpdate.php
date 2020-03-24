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
//$param1 = "hatim@gmail.com";
//$param2 = "testing1123";
//$param4 = "testing1123";

$param1 = $_POST['email'];
$param2 = $_POST['fullname'];

$sql = "SELECT * FROM user_register where email ='$param1'";
$retval = mysqli_query($conn, $sql);
$row = mysqli_fetch_assoc($retval);
if($row) {
            echo "Signed In";        
		}
else { 
	
	$sql = "INSERT INTO user_register"."(email, name)"." VALUES ('$param1','$param2')";
	$retval = mysqli_query($conn, $sql);
	if($retval){
		echo "Successfully Registered";
		}	

	}		

mysqli_close($conn);
?>