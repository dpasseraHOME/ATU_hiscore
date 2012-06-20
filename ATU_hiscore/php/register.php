<?php

	if($salt != '') {

		$linkID = mysql_connect($host,$user,$pass) or die("Could not connect to host.");
		mysql_select_db($database, $linkID) or die("Could not find database.");	

		function clean($str) {
			$str = @trim($str);
			if(get_magic_quotes_gpc()) {
				$str = stripslashes($str);
			}
			return mysql_real_escape_string($str);
		}

		$name = clean($_POST['name']);
		$return['name'] = $name;

		$email = clean($_POST['email']);
		$return['email'] = $email;

		$PIN = clean($_POST['PIN']);
		$return['PIN'] = $PIN;

		//create new row in user table
		$query = "INSERT INTO _atu_users (_name, _email, _pin)
				VALUES ('".$name."', '".$email."', '".$PIN."')";
		$qResult = mysql_query($query, $linkID) or die("INSERT INTO _atu_users failed.");

		$return['isSuccess'] = 'yes';
		$return['msg'] = 'added';

		echo json_encode($return);
	} else {
		die('Unauthorized attempt');
	}

?>