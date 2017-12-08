var y = 5; // altura inicial y0=10%, debe leerse al iniciar si queremos que tenga alturas diferentes dependiendo del dispositivo
var v = 0;
var g = 1.622;
var a = g; //a= -g es para motor encendido
var dt = 0.016683;
var timer;
var gasolina = 100;
var dificultad = 1;
var gasolinaTotal = 100;
var intentos = 1;
var modeloNave = 1;
var modeloLuna = 1;
var timerFuel = null;
var id_configuracion="sin_seleccionar";
var v_id_usuario;
var v_id_puntuacion;
var v_json;


window.onload = function arrancarJuego() {
    //CAPTURA SI EL DISPOSITIVO RECIBE EVENTOS OUNTOUCH (SMARTPHONE)
    function is_touch_device() {
        if ('ontouchstart' in window) {
            document.getElementById("botonOn").style.display = "inline-block";
        }
    }
    is_touch_device();
    //CAPTURANDO EVENTOS DEL PANEL DERECHA
    document.getElementById("reanudar").onclick = function () {
        reanudar();
    };
    document.getElementById("pausa").onclick = function () {
        pausar();
    };
    document.getElementById("reinicia").onclick = function () {
        reiniciarJuego();
    };
    document.getElementById("instrucciones").onclick = function () {
        mostrarInstrucciones();
    };
    document.getElementById("botonAjustes").onclick = function () {
      $("#ajustes").modal();
      stop();
    };
    document.getElementById("botonConfiguracion").onclick = function () {
        mostrarConfiguracion();
    };
    
    //Mostrar listas de partidas
    document.getElementById("botonlistaPartidas").onclick = function () {
        mostrarListado();
    };
    
    //Mostrar listas de usuarios on-line
    document.getElementById("botonlistaUsuarios").onclick = function () {
        mostrarUsuarios();
    };
    
    //CAPTURANDO EVENTOS PARA EL PANEL DERECHO EN SMARTPHONE
    document.getElementById("reanudaSmartphone").onclick = function () {
        reanudarSmartphone();
    };
    document.getElementById("pausaSmartphone").onclick = function () {
        pausarSmartphone();
    };
    document.getElementById("reiniciaSmartphone").onclick = function () {
        reiniciarJuegoSmartphone();
    };
    document.getElementById("ayudaSmartphone").onclick = function () {
        mostrarInstruccionesSmartphone();
    };
    document.getElementById("botonAjustesSmartphone").onclick = function () {
        mostrarAjustesSmartphone();
    };
    //EVENTOS DE FIN DEL JUEGO
    document.getElementById("jugarOtraVez").onclick = function () {
        reiniciarJuego();
    };
    document.getElementById("jugarOtraVezSmartphone").onclick = function () {
        reiniciarJuegoSmartphone();
    };
    document.getElementById("jugarAgain").onclick = function () {
        reiniciarJuego();
    };
    document.getElementById("jugarAgainSmartphone").onclick = function () {
        reiniciarJuegoSmartphone();
    };
    //CAMBIAR LA DIFICULTAD DEL JUEGO
    document.getElementById("dificultad").onchange= function cambiarDificultad(){
        var valor=$('#dificultad').val();
        switch (valor) { // cambiamos la dificultad
            case "1":  // si la dificultad era facil, pasamos a media
                dificultad = 1;//media
                restart();
                break;
            case "2": // si la dificultad era media pasamos a dificil
                dificultad = 2;//dificil
                restart();
                break;
            case "3": // si la dificultad era dificil pasamos a facil
                dificultad = 3//facil
                restart();
                break;
        }
        recarga_dificultad();

    }
    //CAMBIAR LA IMAGEN DE LA LUNA
    document.getElementById("modeloLuna").onchange = function cambiarModeloLuna() {
        var valor=$('#modeloLuna').val();
        switch (valor) {
            case "1":
                modeloLuna = 1;
                break;
            case "2":
                modeloLuna = 2;
                break;
        }
        //pintamos el modelo de nave
        recarga_luna();
    }

    //CAMBIAR LA IMAGEN DE LA NAVE Y EL MOTOR
    document.getElementById("modeloNave").onchange= function cambiarModeloNave() {
        var valor=$('#modeloNave').val();
        switch (valor) {
            //selecionas el numero de dificultad
            case "1":
                modeloNave = 1;
                break;
            case "2":
                modeloNave = 2;
                break;

        }
        //pintamos el modelo de nave
        recarga_nave();

    }

    //ASIGNAR EVENTOS TOUCH SCREEN PARA LA VERSION SMARTPHONE
    var botonOnSmartphone = document.getElementById("botonOn");
    botonOnSmartphone.addEventListener("touchstart", handlerFunction, false);
    botonOnSmartphone.addEventListener("touchend", endingFunction, false);
    function handlerFunction(event) {
        encenderMotor();
    }
    function endingFunction(event) {
        apagarMotor();
    }

    //CON TECLADO (tecla ESPACIO)
    window.onkeydown = function (e) {
        var claveTecla;
        if (window.event)
            claveTecla = window.event.keyCode;
        else if (e)
            claveTecla = e.which;
        if ((claveTecla == 32))
        {
            encenderMotor();
        }
    }
    window.onkeyup = apagarMotor;

}//TERMINA EL WINDOW.ONLOAD


//FUNCION EMPEZAR EL JUEGO
function start() {
    timer = setInterval(function () {
        moverNave();
    }, dt * 1000);
}

//FUNCION PARAR NAVE Y CONTROLADORES
function stop() {
    clearInterval(timer);
}

//FUNCION PARA QUE CAIGA LA NAVE A TRAVES DE LA PANTALLA
function moverNave() {
    v += a * dt;
    document.getElementById("velocidad").innerHTML = v.toFixed(2);
    y += v * dt;
    document.getElementById("altura").innerHTML = y.toFixed(2);
    //mover hasta que top sea un 70% de la pantalla
    if (y < 70) {
        document.getElementById("nave").style.top = y + "%";
    } else {
        stop();
        finalizarJuego();
    }
}

//HACER QUE LOS DIVS IZQUIERDA Y DERECHA NO RECIBAN EVENTOS ONCLICK
function eventosOff() {
    document.getElementById("izquierda").style.pointerEvents = 'none';
    document.getElementById("derecha").style.pointerEvents = 'none';
    document.getElementById("reanudar").style.pointerEvents = 'none';
    document.getElementById("pausa").style.pointerEvents = 'none';
}
//HACER QUE LOS DIVS IZQUIERDA Y DERECHA SI RECIBAN EVENTOS ONCLICK
function eventosOn() {
    //auto es lo que tiene por defecto.
    document.getElementById("izquierda").style.pointerEvents = 'auto';
    document.getElementById("derecha").style.pointerEvents = 'auto';
    document.getElementById("reanudar").style.pointerEvents = 'auto';
    document.getElementById("pausa").style.pointerEvents = 'auto';
}

//FUNCION PARA ACABAR EL JUEGO
function finalizarJuego() {
    if (v > 5) {
        switch (modeloNave) {
            case 1:
                eventosOff();
                document.getElementById("imgNave").src = "img/nave_rota.gif";
                document.getElementById("gameOver").style.display = "block";
                document.getElementById("intentos").innerHTML = intentos;
                break;
            case 2:
                eventosOff();
                document.getElementById("imgNave").src = "img/mod2rota.gif";
                document.getElementById("gameOver").style.display = "block";
                document.getElementById("intentos").innerHTML = intentos;
                break;
        }
    } else {
        document.getElementById("userWin").style.display = "block";
        eventosOff();
    }
    finPuntuacion();//SE PONE ESTA FUNCION AL TERMINAR EL JUEGO
}

//FUNCION QUE ACTUA EN CUANTO SE ENCIENDE EL MOTOR
function encenderMotor() {
    a = -g;
    document.getElementById("fuel").innerHTML = porcentajeGasolina();
    document.getElementById("fuel").style.color = "rgb(" + 0 + (100 - porcentajeGasolina()) + "%, 0%, 0%)";
    document.getElementById("imgMotor").style.display = "block";
    if (timerFuel == null) {
        timerFuel = setInterval(function () {
            actualizarGasolina();
        }, 100);
    }
    if (gasolina <= 0) {
        apagarMotor();
        document.getElementById("fuel").innerHTML = 0;
    }
}

//CALCULO EL PORCENTAJE DE GASOLINA QUE QUEDA
function porcentajeGasolina() {
    var result = gasolina * 100 / gasolinaTotal;
    return result.toFixed(0);
}


//FUNCION QUE ACTUALIZA EL MARCADOR DE FUEL
function actualizarGasolina() {
    gasolina -= 1;
    document.getElementById("fuel").innerHTML = porcentajeGasolina();
    document.getElementById("fuel").style.color = "rgb(" + 0 + (100 - porcentajeGasolina()) + "%, 0%, 0%)";
    if (gasolina <= 0) {
        apagarMotor();
        document.getElementById("fuel").innerHTML = 0;
    }
}
//FUNCION QUE RESPONDE AL MOMENTO DE APAGAR EL MOTOR DE LA NAVE
function apagarMotor() {
    a = g;
    document.getElementById("imgMotor").style.display = "none";
    clearInterval(timerFuel);
    timerFuel = null;
}
function mostrarUsuarios(){
    pausar();
    eventosOff();
    document.getElementById("settingsUsuarios").style.display = "block";
}
function mostrarListado(){
    pausar();
    eventosOff();
    document.getElementById("settingsLista").style.display = "block";
}

function mostrarConfiguracion() {
    pausar();
    eventosOff();
    document.getElementById("settings").style.display = "block";
    //mostramos el usuario al iniciar ventana de ajustes.
    $("#v_id_usuario").val(v_id_usuario);
}

function ocultarConfiguracion() {
    document.getElementById("settings").style.display = "none";
    eventosOn();
}

function ocultarListado() {
    document.getElementById("settingsLista").style.display = "none";
    eventosOn();
}
function ocultarListadoUsuarios(){
    document.getElementById("settingsUsuarios").style.display = "none";
    eventosOn();
}

function mostrarInstrucciones() {
    pausar();
    eventosOff();
    document.getElementById("menuInstrucciones").style.display = "block";
}

function ocultarInstrucciones() {
    document.getElementById("menuInstrucciones").style.display = "none";
    eventosOn();
}

function restart() {
    stop();
    y = 5; // altura inicial y0=10%, debe leerse al iniciar si queremos que tenga alturas diferentes dependiendo del dispositivo
    v = 0;
    g = 1.622;
    a = g;
    dt = 0.016683;
    gasolina = gasolinaTotal;
    document.getElementById("fuel").innerHTML = porcentajeGasolina();
    document.getElementById("fuel").style.color = "black";
}
//OJO COMPORTAMIENTO ESCRITORIO
function reiniciarJuego() {
    stop();
    document.getElementById("reanudar").style.display = "none";
    document.getElementById("pausa").style.display = "inline-block";
    intentos++;
    y = 5; // altura inicial y0=10%, debe leerse al iniciar si queremos que tenga alturas diferentes dependiendo del dispositivo
    v = 0;
    g = 1.622;
    a = g;
    dt = 0.016683;
    gasolina = gasolinaTotal;
    document.getElementById("fuel").innerHTML = porcentajeGasolina();
    document.getElementById("fuel").style.color = "black";
    reanudar();
    clearInterval(timer);
    start();
    eventosOn();
    document.getElementById("intentos").innerHTML = intentos;
    document.getElementById("gameOver").style.display = "none";
    document.getElementById("userWin").style.display = "none";
    if (modeloNave == 1) {
        document.getElementById("imgNave").src = "img/nave.png";
    } else {
        document.getElementById("imgNave").src = "img/mod2nave.gif";
    }
}

function reanudar() {
    stop();
    start();
    document.getElementById("reanudar").style.display = "none";
    document.getElementById("pausa").style.display = "inline-block";
}
function pausar() {
    stop();
    document.getElementById("pausa").style.display = "none";
    document.getElementById("reanudar").style.display = "inline-block";
}

//OJO COMPORTAMIENTO SMARTPHONE
function reanudarSmartphone() {
    start();
    document.getElementById("reanudaSmartphone").style.display = "none";
    document.getElementById("pausaSmartphone").style.display = "inline-block";
    document.getElementById("reiniciaSmartphone").style.display = "none";
    document.getElementById("ayudaSmartphone").style.display = "none";
    document.getElementById("botonAjustesSmartphone").style.display = "none";
    document.getElementById('izquierda').style.display = "inline-block";
    document.getElementById('nave').style.display = "inline-block";
    document.getElementById('zonaAterrizaje').style.display = "inline-block";
    document.getElementById('derechaSmartphone').style.backgroundImage = 'url(img/sol.png)';
    document.getElementById('derechaSmartphone').style.backgroundSize = '60%';
    document.getElementById('derechaSmartphone').style.backgroundRepeat = 'no-repeat';
    document.getElementById('derechaSmartphone').style.width = '35%';
}

function pausarSmartphone() {
    stop();
    document.getElementById("pausaSmartphone").style.display = "none";
    document.getElementById("reanudaSmartphone").style.display = "inline-block";
    document.getElementById("reiniciaSmartphone").style.display = "inline-block";
    document.getElementById("ayudaSmartphone").style.display = "inline-block";
    document.getElementById("botonAjustesSmartphone").style.display = "inline-block";
    document.getElementById('derechaSmartphone').style.backgroundImage = 'url(img/fondo_menu.jpg)';
    document.getElementById('derechaSmartphone').style.backgroundSize = 'auto';
    document.getElementById('derechaSmartphone').style.backgroundRepeat = 'repeat';
    document.getElementById('derechaSmartphone').style.width = '100%';
}

function reiniciarJuegoSmartphone() {
    stop();
    intentos++;
    y = 5; // altura inicial y0=10%, debe leerse al iniciar si queremos que tenga alturas diferentes dependiendo del dispositivo
    v = 0;
    g = 1.622;
    a = g;
    dt = 0.016683;
    gasolina = gasolinaTotal;
    document.getElementById("fuel").innerHTML = porcentajeGasolina();
    document.getElementById("fuel").style.color = "black";
    reanudarSmartphone();
    clearInterval(timer);
    start();
    eventosOn();
    document.getElementById("intentos").innerHTML = intentos;
    document.getElementById("gameOver").style.display = "none";
    document.getElementById("userWin").style.display = "none";
    if (modeloNave == 1) {
        document.getElementById("imgNave").src = "img/nave.png";
    } else {
        document.getElementById("imgNave").src = "img/mod2nave.gif";
    }
}

function mostrarAjustesSmartphone() {
    pausarSmartphone();
    document.getElementById("settings").style.display = "block";
}

function mostrarInstruccionesSmartphone() {
    pausarSmartphone();
    document.getElementById("menuInstrucciones").style.display = "block";
}


//funciones para cambiar los datos de la nave
function recarga_dificultad() {
    switch (dificultad) {
        case 1:
            gasolina = 100;
            gasolinaTotal = 100;
            $('#dificultad').val("1");
            //document.getElementById("dificultad").innerHTML = "Fácil";
            restart();
            break;
        case 2:
            gasolina = 50;
            gasolinaTotal = 50;
            $('#dificultad').val("2");
            //document.getElementById("dificultad").innerHTML = "Media";
            restart();
            break;
        case 3:
            gasolina = 25;
            gasolinaTotal = 25;
            $('#dificultad').val("3");
            //document.getElementById("dificultad").innerHTML = "Difícil";
            restart();
            break;
    }
    ;
}
//recargamos nave
function recarga_nave() {
    switch (modeloNave) {
        case 1:
            document.getElementById("imgNave").src = "img/nave.png";
            document.getElementById("imgMotor").src = "img/motor.gif";
            $('#modeloNave').val("1");
            //document.getElementById("modeloNave").innerHTML = "Modelo Estándar";
            restart();
            break;
        case 2:
            document.getElementById("imgNave").src = "img/mod2nave.gif";
            document.getElementById("imgMotor").src = "img/mod2motor.gif";
            $('#modeloNave').val("2");
            //document.getElementById("modeloNave").innerHTML = "Modelo PodRacer";
            restart();
            break;
    }
    ;

}

//recargamos luna
function recarga_luna() {
    switch (modeloLuna) {
        case 1:
            document.getElementById("luna").src = "img/luna.png";
            $('#modeloLuna').val("1");
            //document.getElementById("modeloLuna").innerHTML = "Amarilla";
            restart();

            break;
        case 2:
            document.getElementById("luna").src = "img/mod2luna.png";
            $('#modeloLuna').val("2");
            //document.getElementById("modeloLuna").innerHTML = "Gris";
            restart();
            break;
    }
    ;
}



