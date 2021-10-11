<?php

include("registrarcliente.php")

?>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
            <title>
            </title>
            <link href="stylereg.css" rel="stylesheet" type="text/css">
                <link href="responsive.css" rel="stylesheet">
                    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
                    </link>
                </link>
            </link>
        </meta>
    </head>
    <body>
        <form method="POST">
            <div class="footer">
                <div class="container-fluid">
                    <div class="row">
                        <div class=" col-md-12">
                            <h2>
                                Registrate
                                <strong class="white">
                                    Ahora
                                </strong>
                            </h2>
                        </div>
                        <div class="col-xl-6 col-lg-6 col-md-6 col-sm-12">
                            <form class="main_form">
                                <div class="row">
                                    <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12">
                                        <input class="form-control" name="usuario" placeholder="Usuario" type="text">
                                        </input>
                                    </div>
                                    <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12">
                                        <input class="form-control" name="pass" placeholder="Contraseña" type="password">
                                        </input>
                                    </div>
                                    <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12">
                                        <input class="form-control" name="correo" placeholder="Correo" type="text">
                                        </input>
                                    </div>
                                    <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12">
                                        <input class="form-control" name="telefono" placeholder="Telefono" type="text">
                                        </input>
                                    </div>
                                    <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12">
                                        <input class="form-control" name="ciudad" placeholder="Ciudad" type="text">
                                        </input>
                                    </div>
                                    <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12">
                                        <input class="form-control" name="direccion" placeholder="Direccion" type="text">
                                        </input>
                                    </div>
                                    <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12">
                                        <button class="send" name="regis" type="submit">
                                            Registar
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
