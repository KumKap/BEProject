<?php 
require_once('config.php'); 
include_once 'config.php';                         
require_once 'VerifyEmail.class.php'; 
                                   
if( $conn ) {
    // echo "Connection established.<br />";
}
else{
     echo "Connection could not be established.<br />";
    // die( print_r( sqlsrv_errors(), true));
}
$param1 = "hatim4@gmail.com";
$param2 = "testing@1123";
$param4 = "testing@1123";

// $param1 = $_POST['email'];
// $param2 = $_POST['fullname'];
// $param4 = $_POST['password'];

$mail = new VerifyEmail();

// Set the timeout value on stream
$mail->setStreamTimeoutWait(20);

// Set debug output mode
$mail->Debug= TRUE; 
$mail->Debugoutput= 'html'; 

// Set email address for SMTP request
$mail->setEmailFrom('kumel001@gmail.com');

// Email to check
$email = $param1; 

// Check if email is valid and exist
if($mail->check($email)){ 
    //echo 'Email &lt;'.$email.'&gt; is exist!'; 
	$sql = "INSERT INTO user_register"."(email, name, password)"." VALUES ('$param1', '$param2','$param4')";
	$retval = mysqli_query($conn, $sql);
	if($retval){
		echo "Successfully Registered";
	}	
}elseif(verifyEmail::validate($email)){ 
    echo 'Email &lt;'.$email.'&gt; is valid, but does not exist!'; 
}else{ 
    echo 'Email &lt;'.$email.'&gt; is not valid.'; 
} 

   

mysqli_close($conn);
?>