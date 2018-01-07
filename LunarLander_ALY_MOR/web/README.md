# LunarLander_ALY_MOR
Proyecto de videojuego **LunarLander_ALY_MOR**.   


Aplicación web JPA en grupo de trabajo
Proyecto LunarLander_ALY_MOR

Hemos diseñado un juego con acceso a datos a una base de datos de PostgreSQL que guarda los datos de cada jugador como la configuración y la puntuación de ellos.
Para la realización del proyecto nos hemos dividido en tres partes (simulando el trabajo  a realizar en una empresa). Estos grupos son:
A) Frontend (Jquery, Html, Css …)
B) Backend (JPA, modelo, controladores, Servlets, PostgreSql ...)
C) Control de versiones: Changelog, Readme, Documentación,...Exportar el juego a un PAAS.

Se empieza a trabajar con el proyecto base: https://github.com/urbinapro/lunar-landing-javascript.
Como repositorio tenemos el Github, creamos dos ramas Frontend, Backend y la rama máster por defecto.
Desde el proyecto base se crea el diseño web responsive o adaptativo que es una técnica de diseño web que busca la correcta visualización de una misma página en distintos dispositivos. Desde ordenadores de escritorio a tablets y móviles. 
Según la necesidad del proyecto se van rellenando los diferentes div que componen el proyecto: izquierda, derecha, nave y zonaAterrizaje. 

Al ser un juego que necesita registro de usuario , hemos puesto una ventana modal para registrarse la cual lleva a un enlace sino estuvieras registrado, que nos lleva a otra ventana modal registro usuario. Una vez registrado y logueado ya estas en el juego.  Para que el usuario no tenga que loguearse cada vez que entra en el juego, se utilizan Cookies.

En la parte de la derecha  se encuentran los botones del control del juego, con las instrucciones del juego, play, stop, reiniciar y ajustes. Pulsando ajustes, te lleva a una ventana modal con un menú de configuración ( usuario, dificultad del juego, modelo luna y el modelo nave que deseé para su diseño del juego. En Configuración puede agregar una configuración nueva, guardarla, cargarla y jugar  o si lo desea también borrar la configuración). Cambien dispone de una seria de listados para que el jugador pueda ver tanto los usuarios que en ese momento estén Online o también un listado de los jugadores con más partidas jugadas.
Cada partida que haga el usuario es guardada en la base de datos.

En la parte de la izquierda se encuentran los controles de la nave . Velocidad, Fuel y Altura. 

Parte del   Backend:

En esta parte se ha creado la  conexión a la base de datos de PostgreSQL desde netbeans, para ellos hemos agregado a  la librería de nuestro proyecto el conector postgreSQL. A continuación creamos una clase cuyo package es listeners en el cual botón derecho y creamos una clase web Aplication Listener la cual Implementa los métodos contextInitialized y contextDestroyed .
Una vez hecho, podemos llamar desde el Servlet al emf con el siguiente código:
EntityManagerFactory emf = (EntityManagerFactory) getServletContext (). GetAttribute ( "LunarLander_ALY_MORPU")-→Aqui tienes que poner tu persistence-unit , se encuentra en el fichero persistence.
Esta clase lo que hace es abrir la base de datos cuando la aplicación Web se inicia, creando una instancia de un EntityManagerFactory (usar Table Generation Strategy al persistence.xml para crear las tablas si no existen). La base de datos se cerrará cuando se para la aplicación web (o cuando el sitio web se apaga), llamando a EntityManagerFactory.close ().

Es decir, el ServletContextListener realiza operaciones de inicialización y limpieza del proyecto. 

Se crea  también el pooling connection en el cual hay que modificar el documento  META-INF / context.xml y el documento persistence.xml. 

Una vez que la conexión a la base de datos funciona , creamos las Entity y los controllers a través del script de la bd.
Package model -→Entity.
Package controllers-→ controllers

Las Entity son las clases que se crean automáticamente a través del script de la base de datos de postgreSQL. Para ellos en el proyecto botón derecho → new → Entity classes fron database y ya tendríamos las clases con las tablas de la base de datos. Y de  manera parecida se crea los controllers. Proyecto  botón derecho-→ new-→ JPA Controller Classes fron Entity Classes.

Ya tenemos las clases con las cuales trabajar.

Creamos los servlet, que es con lo que vamos a gestionar el acceso a la base de datos.

Para el proyecto hemos utilidado 8 servlet, 6 post y 2 get, a continuación explico su funcionamiento.

PostLoguin → Servlet que nos permite validar si el usuario es correcto. Puede hacerlo mediante dos formas: O bien revisando las Cookies usuario/password o si estas no están informadas, mediante los parámetros del request. Devuelve en el response las configuraciones del usuario validado a través de un String Json.

PostUsuario-→ Servlet que recibe los datos del usuario por request y los guarda en la base de datos.

PostConfiguraciones-→ Servlet que recibe los datos de una configuración que e usuario quiere guardar, la guarda en la base de datos y devuelve todas las configuraciones a través del response en String Json,  para mantener actualizado el Frontend.

PostBorrar-→ Servlet que recibe el id de una configuración que el usuario desea borrar y una vez eliminado de la base de datos, devuelve el conjunto de configuraciones restante, en formato Json para mantener actualizado el Frontend.

PostInicioPartida-→ Sevlet que crea una nueva partida en la base de datos. Devuelve el id de dicha partida en formato Json al Frontend.  Se guarda el inicia de partida cada vez que comienza una partida, ya sea al loguearse, como al reiniciar.

PostFinPartida-→ Con el id proporcionado al inicio de partida, marcamos cuando la partid se finaliza así, como los tiempos, la velocidad, el fuel y la configuración seleccionada. 

GetUsuarosOnLine-→ Función que devuelve un listado con los jugadores Online, a través de un String Json al response de la petición Ajax del Frontend. Se hace mediante una consulta sobre las partidas activas de los últimos 5 minutos.

GetTopJugadores-→Función que devuelve un listado con los 10 primeros registros de la select que obtiene el número de partidas del jugador. Esto se devuelve en formato Json al Frontend, que trata los datos y se los muestra al usuario.

Y explicar el merge( la unificación de las ramas)
Falta la parte del servidor