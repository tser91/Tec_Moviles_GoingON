<?php
 
class funciones_BD {
 
    private $db;
    public $InfoUser;
    public $InfoEvent;
 
    // constructor

    function __construct() {
        require_once 'connectbd.php';
        // connecting to database

        $this->db = new DB_Connect();
        $this->db->connect();
        $InfoUser = array();

     }
 
    // destructor
    function __destruct() {
 
  
    }
 
    /**
     * agregar nuevo usuario
     */
    public function adduser($username, $password, $email, $description, $latitude, $longitude, $usertype) {

    $encriptada = md5($password);
    if (!$this->userExist($email)){ 
    
    $mysqli = new mysqli("mysql5.000webhost.com",  "a2502724_appOn", "ssga1234", "a2502724_appOn");

            /* check connection */
            if (mysqli_connect_errno()) {
                 printf("Connect failed: %s\n", mysqli_connect_error());
                 exit();
            }
    $type = 0;
    if ($usertype == 'local'){
         $type =2;
    }
    else{
         $type = 3;
    }

    $result= $mysqli->prepare("INSERT INTO usuario(Nombre, Password, Correo, Descripcion, idtipousuario) VALUES (?, ?, ?, ?, ?)");
    $result->bind_param('ssssi', $username,$encriptada , $email, $description, $type);
    $result->execute();
    
    // check for successful store
        if ($result) {
          //Se verifica si consiste en un usuario movil o un local
           if ($usertype == 'local'){
                $idUsuario = $mysqli->insert_id;
                $resultUbicacion = $mysqli->prepare("INSERT INTO ubicacion(Latitud, Longitud) VALUES (?, ?)"); 
                $resultUbicacion->bind_param('dd', $latitude,$longitude);
                $resultUbicacion->execute();
                $idUbicacion = $mysqli->insert_id;
                $idCalificacion = 1;
                $resultLocal = $mysqli->prepare("INSERT INTO local(idUbicacion, idUsuario) VALUES (?, ?)"); 
                $resultLocal->bind_param('ii', $idUbicacion,$idUsuario);
                $resultLocal->execute();
                $resultUbicacion->close();
                $resultLocal->close();
          }
           $result->close();
           $mysqli->close(); 
           return true;
        }
        else{
           return false;    
        }        
        
        } 
        else{
             return false;
        }
   }

   /**
   * Verifica si el usuario existe.
   */
   public function userExist($username){
          
      $mysqli = new mysqli("mysql5.000webhost.com",  "a2502724_appOn", "ssga1234", "a2502724_appOn");

            /* check connection */
            if (mysqli_connect_errno()) {
                 printf("Connect failed: %s\n", mysqli_connect_error());
                 exit();
            }
       
       $result= $mysqli->prepare("SELECT Correo FROM usuario WHERE Correo=?");
       $result->bind_param('s', $username);
       $result->execute();

       if ($result->fetch()){
            return true;
       }
       else{
            return false;
       }
       
       $result->close();
       $mysqli->close();
   }


   public function login($username, $password, $typelogin){
       
       $encriptada = md5($password);
        $mysqli = new mysqli("mysql5.000webhost.com",  "a2502724_appOn", "ssga1234", "a2502724_appOn");

            /* check connection */
            if (mysqli_connect_errno()) {
                 printf("Connect failed: %s\n", mysqli_connect_error());
                 exit();
            }
       
       if ($typelogin == 'fblogin'){
       
           if (!$this->userExist($username)){  
                $pass = "null";
                $descripcion = "Facebook User";
                $typeUser =3;
                $result= $mysqli->prepare("INSERT INTO usuario(Nombre, Password, Correo, Descripcion,idtipousuario) VALUES (?, ?, ?, ?, ?)");
                $result->bind_param('ssssi', $password, $pass, $username, $descripcion,$typeUser);
                $result->execute();
                if (!$result->fetch()){
                     $result->close();
                     $mysqli->close();
                     return true;
                 }
                 else{
                     $result->close();
                     $mysqli->close();
                     return false;
                 }
           }
           $mysqli->close();
           return false;          
       }
       else{
           $result= $mysqli->prepare("SELECT Correo FROM usuario WHERE Correo=? AND Password=?");
           $result->bind_param('ss', $username, $encriptada);
           $result->execute();

           if (!$result->fetch()){
               $result->close();
               $mysqli->close();
               return true;
           }
           else{
               return false;
           }
       }
       
       $mysqli->close(); 
   }

   public function addEvent($eventName, $eventDescr, $eventPrice, $eventCategory,
     $initDateDay, $initDateMonth, $initDateYear, $initTimeHour, $initTimeMinute,
     $finalDateDay, $finalDateMonth,$finalDateYear,  $finalTimeHour,$finalTimeMinute,
     $latitude, $longitude, $usermail){
 
     $mysqli = new mysqli("mysql5.000webhost.com",  "a2502724_appOn", "ssga1234", "a2502724_appOn");

            /* check connection */
            if (mysqli_connect_errno()) {
                 printf("Connect failed: %s\n", mysqli_connect_error());
                 exit();
            }
       
      $FechaInicio= "{$initDateDay}-{$initDateMonth}-{$initDateYear}";
      $FechaFinal= "{$finalDateDay}-{$finalDateMonth}-{$finalDateYear}";
      $HoraInicio = "{$initTimeHour}:{$initTimeMinute}";
      $HoraFinal = "{$finalTimeHour}:{$finalTimeMinute}";
      
      $statUbicacion = $mysqli->prepare("INSERT INTO ubicacion(Latitud, Longitud) VALUES (?,?)");
      $statUbicacion->bind_param('dd', $latitude, $longitude);
      $statUbicacion->execute();
      $idUbicacion = $mysqli->insert_id;

      $statCategoria = $mysqli->prepare("SELECT idCategoria FROM categoria WHERE Nombre=?");
      $statCategoria->bind_param('s', $eventCategory);
      $statCategoria->execute();
      $statCategoria->bind_result($idCategoria);
      while ($statCategoria->fetch())
      {
            $idCateg= $idCategoria;
      }

      $statUsuario = $mysqli->prepare("SELECT idUsuario FROM usuario WHERE Correo=? ");
      $statUsuario->bind_param('s', $usermail);
      $statUsuario->execute();
      $statUsuario->bind_result($idUsuario);
      while ($statUsuario->fetch())
      {
            $idUsuarioObtenido = $idUsuario;
      }
  

      $idImagen = 1;
      $now = new DateTime();
      $now->format('d-m-Y');
      $currentTime = date_format($now, 'd-m-Y'); 
          
      $statEvent= $mysqli->prepare("INSERT INTO evento (Nombre, Descripcion, Precio, idUbicacion, idCategoria, idImagen, idUsuario, FechaInicio, FechaFinal, HoraInicio, HoraFinal, FechaHoraCreacion) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);");
      $statEvent->bind_param('sssiiiisssss', $eventName, $eventDescr, $eventPrice, $idUbicacion, $idCateg, $idImagen, $idUsuarioObtenido , $FechaInicio, $FechaFinal, $HoraInicio, $HoraFinal, $currentTime);
      $statEvent->execute();   

      $statEvent->close();
      $statUbicacion->close();
      $statCategoria->close();
      $statUsuario->close();
      $mysqli->close();
   }

   public function getInfoUser($email, $typeUser){
      $mysqli = new mysqli("mysql5.000webhost.com",  "a2502724_appOn", "ssga1234", "a2502724_appOn");

      /* check connection */
      if (mysqli_connect_errno()) {
           printf("Connect failed: %s\n", mysqli_connect_error());
           exit();
      }
      $statUsuario = $mysqli->prepare("SELECT Nombre, Descripcion, idUsuario FROM usuario WHERE Correo=? ");
      $statUsuario->bind_param('s', $email);
      $statUsuario->execute();
      $statUsuario->bind_result($Nombre, $Descripcion, $idUsuarioObtenido);
      while ($statUsuario->fetch())
      {
            $InfoUser[0] = "getstatus";
            $InfoUser[1] = "1";
            $InfoUser[2] = "name";
            $InfoUser[3] = $Nombre;
            $InfoUser[4] = "description";
            $InfoUser[5] = $Descripcion;
      }
      $statFriends = $mysqli->prepare("SELECT * FROM  listausuarios WHERE Usuario=? ");
      $statFriends ->bind_param('i', $idUsuarioObtenido);
      $statFriends ->execute();
      $InfoUser[6] = "countfriends";
      $InfoUser[7] = 0;
      while ($statFriends->fetch())
      {
            $InfoUser[7] = $InfoUser[7] +1;
      }
      if ($typeUser == "local"){
           $statUbication = $mysqli->prepare("SELECT idUbicacion FROM  local WHERE idUsuario=? ");
           $statUbication ->bind_param('i', $idUsuarioObtenido);
           $statUbication ->execute();
           $statUbication ->bind_result($idUbicacion);
           while ($statUbication ->fetch()){
               $Ubicacion = $idUbicacion;
           }
           $statLocation= $mysqli->prepare("SELECT Latitud, Longitud FROM  ubicacion WHERE idUbicacion=? ");
           $statLocation->bind_param('i', $Ubicacion);
           $statLocation->execute();
           $statLocation->bind_result($idLatitude, $idLongitude);
           while ($statLocation->fetch()){
               $InfoUser[8] = "latitude";
               $InfoUser[9] = $idLatitude;
               $InfoUser[10] = "longitude";
               $InfoUser[11] = $idLongitude;           
           }
           $statLocation->close();
           $statUbication->close();           
      }
      $statFriends->close();
      $statUsuario->close();
      $mysqli->close();
      return $InfoUser; 
   } 

   public function getEventList($mail){
      
      $mysqli = new mysqli("mysql5.000webhost.com",  "a2502724_appOn", "ssga1234", "a2502724_appOn");

      /* check connection */
      if (mysqli_connect_errno()) {
           printf("Connect failed: %s\n", mysqli_connect_error());
           exit();
      }


      $statUser = $mysqli->prepare("SELECT idUsuario, idtipousuario FROM usuario WHERE Correo=? ");
      $statUser->bind_param('s', $mail);
      $statUser->execute();
      $statUser->bind_result($idUsuario, $idtipo);
      while ($statUser->fetch())
      {
            $idUsuarioObtenido= $idUsuario;
            $typeUser = $idtipo;
      }
      $statEvent =  $mysqli->prepare("SELECT idEvento, Nombre, Descripcion, idUbicacion FROM evento WHERE idUsuario=? ");
      $statEvent->bind_param('i', $idUsuarioObtenido);
      $statEvent->execute();
      $statEvent->bind_result($idEvent, $Nombre, $Descripcion, $idUbica);
      
      $InfoEvent["getEventStatus"] = 1;
      $InfoEvent["events"]= array();
      while($statEvent->fetch()){
           
           $ListaUtil = $this->getUbicationList($idUbica);
           $Evento = array();           
           $Evento["name"] = $Nombre;
           $Evento["description"] = $Descripcion;
           $Evento["idEvento"] = $idEvent;
           $Evento["latitude"] = $ListaUtil[0];
           $Evento["longitude"] = $ListaUtil[1];
           array_push($InfoEvent["events"], $Evento);
           
      }
      $InfoEvent["typeUser"]= $typeUser;
      $statUser->close();
      $statEvent->close();
      $mysqli->close();
      return $InfoEvent;
      
   }

   public function getUbicationList($idUbicac){

      $mysqli = new mysqli("mysql5.000webhost.com",  "a2502724_appOn", "ssga1234", "a2502724_appOn");

      /* check connection */
      if (mysqli_connect_errno()) {
           printf("Connect failed: %s\n", mysqli_connect_error());
           exit();
      }


      $statUser = $mysqli->prepare("SELECT Latitud, Longitud FROM ubicacion WHERE idUbicacion=? ");
      $statUser->bind_param('i', $idUbicac);
      $statUser->execute();
      $statUser->bind_result($Lat, $Long);
      while($statUser->fetch()){
          $Lista[0] = $Lat;
          $Lista[1] = $Long;
      }
      $mysqli->close();
      $statUser->close();
      return $Lista;
   }

    public function getImage($idEvent){
      $mysqli = new mysqli("mysql5.000webhost.com",  "a2502724_appOn", "ssga1234", "a2502724_appOn");

      /* check connection */
      if (mysqli_connect_errno()) {
           printf("Connect failed: %s\n", mysqli_connect_error());
           exit();
      }
      
      $statEvent =  $mysqli->prepare("SELECT idCategoria FROM evento WHERE idEvento=? ");
      $statEvent->bind_param('i', $idEvent);
      $statEvent->execute();
      $statEvent->bind_result($idCategoria);
      while ($statEvent->fetch())
      {
            $idCateg = $idCategoria;
      }
      $statImagen = $mysqli->prepare("SELECT Icono FROM categoria WHERE idCategoria =? ");
      $statImagen->bind_param('i', $idCateg);
      $statImagen->execute();
      $statImagen->bind_result($Imagen);
      while ($statImagen->fetch())
      {
            $Image = $Imagen;
      }
      $statImagen->close();
      $statEvent->close();
      $mysqli->close();

      return $Image;
    }

    public function getEventInfo($EventName){
      $mysqli = new mysqli("mysql5.000webhost.com",  "a2502724_appOn", "ssga1234", "a2502724_appOn");

      /* check connection */
      if (mysqli_connect_errno()) {
           printf("Connect failed: %s\n", mysqli_connect_error());
           exit();
      }
      $statEvent =  $mysqli->prepare("SELECT Descripcion,Precio, idUbicacion, idCategoria, FechaInicio, 
                                     HoraInicio FROM evento WHERE Nombre=? ");
      $statEvent->bind_param('s', $EventName);
      $statEvent->execute();
      $statEvent->bind_result($Descripcion, $Precio, $idUbica, $idCategoria, $FechaInicio, $HoraInicio);
      while ($statEvent->fetch()){
         $InfoEvent["name"] = $EventName;
         $InfoEvent["description"] = $Descripcion;
         $InfoEvent["price"] = $Precio;
         $InfoEvent["initialtime"] = $FechaInicio;
         $InfoEvent["initialdate"] = $HoraInicio;
         $idUbicacionObtenido = $idUbica;
         $idCategoriaObtenida = $idCategoria;
      }

      $statUbica = $mysqli->prepare("SELECT Latitud, Longitud FROM ubicacion WHERE idUbicacion=? ");
      $statUbica ->bind_param('i', $idUbicacionObtenido);
      $statUbica ->execute();
      $statUbica ->bind_result($Lat, $Long);
      while($statUbica ->fetch()){
          $InfoEvent["latitude"] = $Lat;
          $InfoEvent["longitude"] = $Long;
      }

      $statCateg = $mysqli->prepare("SELECT Nombre FROM categoria WHERE idCategoria=? ");
      $statCateg ->bind_param('i', $idCategoriaObtenida );
      $statCateg ->execute();
      $statCateg ->bind_result($Nombre);
      while($statCateg ->fetch()){
          $InfoEvent["category"] = $Nombre;
      }
      $statCateg->close();
      $statUbica->close();
      $statEvent->close();
      $mysqli->close();
      return $InfoEvent;
    }
}	