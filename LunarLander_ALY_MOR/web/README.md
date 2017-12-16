# lunar-landing-javascript
Versión esqueleto del juego Lunar Landing que incluye:

* Html con los elementos básicos del juego
* Css: d.css y m.css dos versiones que cargan mediante media query dependiendo del tamaño de pantalla (pruébalo!)
* Js: con el javascript necesario para dejar caer la nave y parar cuando llega a un límite. Actualiza la velocidad y la altura en %/s y % (1% de pantalla = 1 metro).
* No dispone de imágenes.

Previsualización: https://rawgit.com/urbinapro/lunar-landing-javascript/master/index.html

Tareas a desarrollar:
* Poner fondo, imagen de la nave y luna. Poner una tierra fijada a la pantalla para que se vea en cualquier tipo de dispositivo. Optimizar las imágenes. Recuerda que se pueden cargar diferentes tamaños y formas de fondos en función del dispositivo usando css.
* Crear el menú: O bien creamos un menú 100% del espacio disponible (que para el móvil será el 100% de la pantalla) o ponemos un menú horizontal para la versión de escritorio y un menú 100% para el móvil.
* Al pulsar una tecla o bien hacer click en la pantalla la nave debe cambiar de aspecto a *nave con motor encendido* y debe cambiar la aceleración de g a -g
* Al pulsar una tecla o bien hacer click en la pantalla debe vaciarse el tanque de combustible de forma proporcional al tiempo que mantenemos pulsado el propulsor. Opcionalmente se pueden disponer de menores o mayores tanques de combustible para aumentar o disminuir la dificultad del juego.
* Al tocar fondo debe mirarse si la velocidad de impacto es inferior a un valor umbral, en caso afirmativo mostrar mensaje de felicitación, en caso negativo explotar la nave. En ambos casos el juego finaliza y puede reiniciarse con la opción del menú *reiniciar*
* Valores umbrales: 1m/s en modo difícil, 5m/s en modo muy fácil (los modos de dificultad son opcionalmente implementables)
* Debe haber una página de *How to play* y una página de *About* accesibles desde el menú

Cualquier otra funcionalidad o cambio debe quedar debidamente documentada.

**Este documento, y el proyecto, es susceptible de sufrir modificaciones sin previo aviso**
