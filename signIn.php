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
//$param1 = "hatim1@gmail.com";
//$param2 = "testing1123";
//$param4 = "testing1123";

$param1 = $_POST['email'];
$param2 = $_POST['password'];
$pass = "password";
$sql = "SELECT * FROM user_register where email ='$param1'";
$retval = mysqli_query($conn, $sql);
if(mysqli_num_rows($retval) == 0) {
			echo "Email Incorrect";
	}
else { 
			$row = mysqli_fetch_assoc($retval);
			if($param2 == $row[$pass]){
				echo "Logged In Successful";
			}
			else{
            echo "Password Incorrect";        	
			}
	}		

mysqli_close($conn);
?>