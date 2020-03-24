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
//$param2 = "testing123";

$param1 = $_POST['email'];
$param2 = $_POST['binID'];
$param1 = shell_exec($Decoder." ".$param1);
$param2 = shell_exec($Decoder." ".$param2);

$sql = "INSERT INTO bin_session ". "(user_email,bin_ID) "."VALUES "."('$param1','$param2')";
//echo $sql;
$retval = mysqli_query($conn, $sql);
if($retval){
echo "Session Active";
}
mysqli_close($conn);


?>