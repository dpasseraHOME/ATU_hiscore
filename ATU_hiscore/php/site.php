<?php

	session_start();

	$salt = "!tiny**baby17MOUNTAIN";

	if($_POST['isLocal'] == "yes") {
		$host = "localhost";
		$user = "dpassera";
		$pass = "password";
		$database = "insteadof";
	} else {
		$host = "dpassera.startlogicmysql.com";
		$user = "insteadof00user";
		$pass = "flashothy1717";
		$database = "insteadof";
	}

	$action = $_POST['action'];

	switch($action) {

		case 'register':
			include('register.php');
			break;

	}

?>