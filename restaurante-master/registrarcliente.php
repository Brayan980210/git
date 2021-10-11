<?php
	require("config.php");
	$conex=mysql_connect($servername,$username,$password,$dbname);
	if ($conex) {
		echo "todo correcto";
	}else{
		echo "error de conexion";
	}

if (isset($_POST['regis'])) {

		if (!empty($_POST['usuario']) && !empty($_POST['pass'])) {
			$Username = trim($_POST['usuario']);
			$Pass=trim($_POST['pass']);
			$Correo=trim($_POST['correo']);
			$Telefono=trim($_POST['telefono']);
			$Ciudad=trim($_POST['ciudad']);
			$Direccion=trim($_POST['direccion']);

			$consulta = "INSERT INTO tbl_staff (username, password, status, role, correo, telefono, ciudad, direcion) VALUES ('$Username','$Pass','a','b','$Correo','$Telefono','$Ciudad','$Direccion')";

			$resultado = mysql_query($conex,$consulta);
			// $addStaffQuery = "INSERT INTO tbl_staff (username ,password ,status ,role,correo,telefono,ciudad,direccion) VALUES ('{$staffUsername}' ,'{$staffPass}' ,'xd' ,'xd','{$staffPass}','{$staffCorreo}','{$staffTelefono}','{$staffCiudad}','{$staffDireccion}') ";

			if ($resultado) {
				header("Location: index.php");
				?>
				<h3>Registrado orrectamente</h3>
				<?php
			}else{
				?>
				<h3>No se pudo registrar</h3>
				<?php
			}
		}
	}
?>

	
