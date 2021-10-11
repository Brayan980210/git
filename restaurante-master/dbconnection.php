<?php
	
	require("config.php");

	// Create connection
	$sqlconnection = new mysqli($servername, $username, $password,$dbname);

	// Check connection
	if ($sqlconnection->connect_error) {
    	die("Connection failed: " . $sqlconnection->connect_error);
	}
	

	$conex=mysql_connect($servername,$username,$password,$dbname);
	if ($conex) {
		echo "todo correcto";
	}else{
		echo "error de conexion";
	}
?>