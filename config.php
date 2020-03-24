<?php
/*$ini = parse_ini_file('php.ini');
$server = $ini['serverName'];
$uname = $ini['UID'];
$pass = $ini['PWD'];
$db = $ini['Database'];
$conn = mysqli_connect($server,$uname,$pass,$db); */


$conn = mysqli_connect("127.0.0.1","root","","BEProject");
$key = "NoOneCanFind";
$Decoder = "java SecureManager NoOneCanFind";
// Check connection
 ?>