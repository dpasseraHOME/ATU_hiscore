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

		$table = "_atu_users";

		$name = clean($_POST['name']);
		$return['name'] = $name;

		$email = clean($_POST['email']);
		$return['email'] = $email;

		$PIN = clean($_POST['PIN']);
		$return['PIN'] = $PIN;

		//check if email address exists in table
		$query = "SELECT * FROM ".$table." WHERE _email='".$email."' OR _name='".$name."'";
		$result = mysql_query($query) or die(mysql_error());

		if(mysql_num_rows($result)) {	
			$return['isSuccess'] = 'no';

			while($row = mysql_fetch_assoc($result)) {
				if($row['_email'] == $email) {
					$return['msg'] = 'duplicate email';
				}
				if($row['_name'] == $name) {
					$return['msg'] = $return['msg'].', duplicate name';
				}
			}
			
		} else {
			//create new row in user table
			$query = "INSERT INTO ".$table." (_name, _email, _pin)
					VALUES ('".$name."', '".$email."', '".$PIN."')";
			$qResult = mysql_query($query, $linkID) or die("INSERT INTO _atu_users failed.");

			$return['isSuccess'] = 'yes';
			$return['msg'] = 'added';
		}

		echo json_encode($return);
	} else {
		die('Unauthorized attempt');
	}

?>