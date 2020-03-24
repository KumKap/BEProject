<?php
if(isset($_POST['image'] )){
	 
	$name = $_POST['email'];
	$location = $_POST['location'];	
	$upload_folder = "Upload";
	$path = "$upload_folder/$name.jpeg";
	$image= $_POST['image'];
	if(file_put_contents($path, base64_decode($image)) != false){
		echo "Uploaded_Success";
		
		exit;
	}
	else{
		echo "Uploaded_Failed";
		exit;
	}
}
else{
	echo "Image_not_in";
		exit;
	
}

?>