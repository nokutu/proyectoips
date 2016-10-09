[![Build Status](https://travis-ci.org/nokutu/proyectoips.svg?branch=master)](https://travis-ci.org/nokutu/proyectoips)
# Proyecto IPS. Grupo 1 Lab2
Este es el repositorio del proyecto de IPS de 3º de Ingeniería Informática del Software. Grupo de laboratorio 2, grupo de trabajo 1.

## Ciclo de desarrollo
Los pasos de un ciclo normal de desarrollo con Git son:

1. Hacer pull del repositorio para descargar los cambio.
2. Trabajar en las historias.
3. Cada vez que hagas algo significativo y **el código compile** hacer commit.
4. Si ya has terminado, hacer push al servidor. Sino, volver al paso 2.

## Importar el proyecto
* Clonar el repositorio para tenerlo en local. Se puede usar Sourcetree o otra herramienta a tu elección.
* Dependiendo del IDE tienes que:
    * Eclipse: (probado con Eclipse Neon, no puedo asegurar que funcione en versiones más antiguas). File -> Import -> Gradle -> Gradle project. Seleccionas el path al proyecto y le das a finish.
    * Si lo anterior de eclipse te da un error, puedes hacer lo siguiente:
         * Vas a la carpeta del proyecto, y abres allí una terminal.
         * Escribes "gradlew eclipse".
         * Importas el proyecto como se hace normalmente, ya que el anterior comando te genera el .project y .classpath.

## Ejecutar el proyecto
Se está utilizando un sistema de buildeo llamado Gradle, similar a Maven. Este se encarga de la compilación, ejecución de tests y descarga de dependencias (jdbc y el driver de la bbdd por ahora). Hay dos formas principales de utilizarlo:

* Desde terminal. Tendrías que ir a la carpeta raiz y ejecutar:
    * Windows 
  
    ```
    gradelw run
    ```

    * Linux 

    ```
    ./gradelw run
    ```
* Desde el IDE. Eclipse, IntelliJ o Netbeans tienen soporte para gradle. Deberíais poder encontrar una lista de tasks, estan run entre ellas.
    * Eclipse: Las distintas opciones de gradle están en el menú inferior, donde pone *Gradle Tasks*. Las principales son *build* y *run*.

## Arrancar la base de datos
La base de datos más simple que pude encontrar se llama H2. Para arrancarla hay que hacer lo siguiente:

* Ir a la carpeta BBDD del proyecto.
* Descomprimir el archivo *.zip*
* Ir a h2 -> bin.
* Ejectutar el archivo *h2.bat* (Windows) o *h2.sh* (Linux).

## Travis
Travis es un sistema de integración continua. Su función es ejecutar builds cada vez que alguien hace un push al repositorio. Cada vez que haya un fallo, se mandará un email al dueño del repositorio (yo) y al autor del commit.
Travis está disponible [aquí](https://travis-ci.org/nokutu/proyectoips)

## ¿Qué hacer en caso de conflicto al hacer push?
Esto ha sucedido porque la versión que tienes en local no se corresponde con la que hay en el servidor. Hay dos posibles casos:

* *Los archivos que se han modificado en ambos sitios no coinciden.* En este caso la solución es más simple. Basta con hacer pull (ten seleccionado la casilla de merge) y luego volver a hacer push.
* *Los archivos coinciden.* En este caso lo anterior fallará. Es bastante más complejo de arreglar, en caso de duda, avísame por whatsapp. Los pasos son:
  * Descargarse [meld](http://meldmerge.org/) e instalarlo.
  * Ir a configuración en Sourcetree. Luego Tools -> Options. En el panel superior, selecciona Diff.
  * En los dropdowns, tienes que seleccionar en ambos custom. Y poner lo siguiente en los campos de abajo. "C:\Program Files (x86)\Meld\meld.exe" en el primero y "$LOCAL $BASE $REMOTE" en el segundo. Lo mismo en ambos sitios. Pulsa OK.
  * En el log/history, selecciona el último commit, el de unindexed changes.
  * Si vas a file status, verás una serie de archivos en el bloque inferior. Esos son los que tienen problemas. Vete uno a uno, haciendo click derecho -> Resolve Conflicts -> Launch External Merge Tool.
  * Ahora se abrirá meld. Hay tres columnas. La de la izquierda y la derecha son los cambios, tuyos o los del servidor. En el medio tienes que dejar el archivo tal y como quieres que quede.
  * Se irán añadiendo archivos al commit a medida que lo vayas haciendo. Cuando termines, haz un nuevo commit y pushea.
  * No añadas los archivos .orig que genera meld, borralos más tarde. Si vuelve a suceder, empiezas por la parte del click derecho.
