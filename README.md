# Recicland

Recicland es un videojuego educativo de código abierto que promueve el reciclaje y el manejo adecuado de los desechos. A través de una experiencia interactiva, los jugadores aprenden sobre la clasificación de residuos y los procesos de tratamiento, fomentando la conciencia ambiental y prácticas sostenibles.

## Tabla de Contenidos

- [Características](#características)
- [Requisitos del Sistema](#requisitos-del-sistema)
- [Instalación](#instalación)
- [Cómo Jugar](#cómo-jugar)
- [Detalles de los Niveles](#detalles-de-los-niveles)
- [Añadir Nuevos Desechos y Contenedores](#añadir-nuevos-desechos-y-contenedores)
  - [Agregar Desechos Personalizados](#agregar-desechos-personalizados)
  - [Agregar Contenedores Personalizados](#agregar-contenedores-personalizados)
  - [Sistema de Detección Automática](#sistema-de-detección-automática)
  - [Recomendaciones](#recomendaciones)
- [Contribuyendo](#contribuyendo)
- [Licencia](#licencia)
- [Créditos](#créditos)

## Características

- **Educativo**: Aprende sobre la importancia del reciclaje y cómo clasificar correctamente los desechos.
- **Interactivo**: Participa en niveles desafiantes que ponen a prueba tus conocimientos.
- **Multijugador**: Compite con amigos y familiares para ver quién es el mejor en reciclar.
- **Multinivel**: Progresa a través de niveles que incrementan en dificultad y enseñan conceptos más avanzados.
- **Código Abierto**: Disponible bajo la licencia AGPL-3.0, permitiendo modificaciones y mejoras por parte de la comunidad.

## Requisitos del Sistema

- **Java JDK 8** o superior instalado.
- Un entorno de desarrollo Java como **IntelliJ IDEA**, **Eclipse** o **NetBeans**.

## Instalación

1. **Clona el repositorio**:

    ```bash
    git clone https://github.com/Ranphe/Recicland.git
    ```

2. **Importa el proyecto** en tu IDE preferido.

3. **Compila y ejecuta** el proyecto:
    - Navega a la clase principal `Recicland`.
    - Ejecuta la aplicación para iniciar el juego.

## Cómo Jugar

1. **Inicia el juego** y dirígete a la ventana de **Jugar**.
2. **Añade a los jugadores** ingresando sus nombres. El juego detecta automáticamente el número de jugadores registrados:
    - **1 jugador**: Modo single player.
    - **2 o más jugadores**: Modo multijugador.
3. **Clasifica los desechos** arrastrándolos al contenedor correcto.
4. **Ordena los tratamientos**: Después de clasificar, ordena los pasos de tratamiento de cada desecho.
5. **Acumula puntos** y avanza a través de los niveles mientras aprendes sobre reciclaje.

## Detalles de los Niveles

El juego consta de **3 niveles**, cada uno con objetivos y tiempos específicos:

- **Nivel 1**:
  - **Objetivo**: Clasificar y tratar correctamente **7 desechos**.
  - **Tiempo**: **3 minutos** por jugador.
- **Nivel 2**:
  - **Objetivo**: Clasificar y tratar correctamente **8 desechos**.
  - **Tiempo**: **2 minutos** por jugador.
- **Nivel 3** (Último Nivel):
  - **Objetivo**: Clasificar y tratar correctamente **9 desechos**.
  - **Tiempo**: **1 minuto** por jugador.

**Nota**: En todos los niveles se presentan **10 desechos al azar**, garantizando que siempre sea posible alcanzar el objetivo establecido.

### Nota Adicional

Si se eliminan desechos del repositorio y quedan, por ejemplo, 5 desechos en el nivel 1, el sistema intentará mostrar **10 desechos**, pero solo mostrará el mínimo entre los disponibles y 10. Por lo tanto, si solo hay 5 desechos en el nivel 1, se mostrarán 5, haciendo imposible cumplir el objetivo de clasificar y tratar **7 desechos** para pasar al nivel 2.

- **Recomendación**: Si se eliminan desechos, modifica el **objetivo del nivel** correspondiente para que sea alcanzable con la cantidad de desechos disponibles.
- **Recuperación en Caso de Corrupción**: Si al modificar el archivo se corrompe y el usuario no sabe cómo recuperarlo, debe **borrar el archivo de desechos (`desechos.txt`)**. El programa generará automáticamente un nuevo archivo con los desechos por defecto.

## Añadir Nuevos Desechos y Contenedores

Recicland permite la incorporación de **desechos** y **contenedores** personalizados para ampliar la experiencia de juego. A continuación, se detallan los pasos para agregar nuevos elementos al repositorio:

### Agregar Desechos Personalizados

1. **Añadir un desecho nuevo**:
    - Abre el archivo `desechos.txt`.
    - Añade un nuevo desecho siguiendo la estructura existente. Asegúrate de definir:
        - **Nombre del Desecho**
        - **Contenedor** al que pertenece
        - **Pasos de Tratamiento** (exactamente **3 pasos**)

    **Ejemplo**:

    ```plaintext
    # Desecho 37
    Nombre: Lata de aluminio
    Contenedor: Metales
    PasosTratamiento:
    - Vaciar el contenido
    - Lavar la lata
    - Aplastar para reducir volumen
    ```

2. **Agregar imágenes personalizadas** (opcional):
    - Si deseas que el desecho tenga una imagen específica en el juego, añade la imagen correspondiente en la carpeta de imágenes de desechos.
    - **Importante**: La imagen debe tener el **mismo nombre** que el desecho tal como se define en el archivo de configuración (por ejemplo, `latadealuminio.png`).

### Agregar Contenedores Personalizados

1. **Añadir un contenedor nuevo**:
    - No es necesario crear un archivo separado para los contenedores. Los contenedores se detectan automáticamente a partir de los desechos definidos en `desechos.txt`.
    - Al añadir un desecho con un contenedor que no existe en el juego, el sistema automáticamente añadirá el contenedor con una imagen genérica.

2. **Agregar imágenes personalizadas para contenedores** (opcional):
    - Si deseas que el contenedor tenga una imagen específica en el juego, añade la imagen correspondiente en la carpeta de imágenes de contenedores.
    - **Importante**: La imagen debe tener el **mismo nombre** que el contenedor tal como se define en el archivo de configuración (por ejemplo, `metales.png` y `metalesHighlighted.png` para su imagen resaltada).

### Sistema de Detección Automática

- **Detección Automática**: El sistema de Recicland detecta automáticamente los nuevos desechos y contenedores añadidos al repositorio a través del archivo `desechos.txt`.
- **Imágenes Genéricas**: Si no se proporcionan imágenes personalizadas para los nuevos desechos y contenedores, el juego utilizará una imagen genérica por defecto (`desechoGenerico.png` para desechos y `contenedorGenerico.png` para contenedores).
- **Personalización de Imágenes**: Para una experiencia visual más personalizada, es recomendable añadir imágenes específicas siguiendo las convenciones de nombres mencionadas anteriormente.

### Recomendaciones

- **Consistencia en Nombres**: Asegúrate de que los nombres de los desechos y contenedores sean consistentes y no contengan caracteres especiales que puedan causar errores en el sistema.
- **Validación de Pasos**: Verifica que cada nuevo desecho tenga exactamente 3 pasos de tratamiento para mantener la integridad del juego.
- **Formato de Nombres de Imágenes**: Los nombres de las imágenes de los desechos y contenedores deben estar escritos sin mayúsculas ni espacios. Por ejemplo, `latadealuminio.png` en lugar de `lataDeAluminio.png`.

## Contribuyendo

¡Tu colaboración es bienvenida! Para contribuir al proyecto:

1. Haz un **fork** del repositorio.
2. Crea una **rama nueva** para tu funcionalidad o corrección de errores:

    ```bash
    git checkout -b feature/nueva-funcionalidad
    ```

3. Realiza tus cambios y haz **commits**:

    ```bash
    git commit -m "Agrega nueva funcionalidad"
    ```

4. Envía tus cambios al repositorio remoto:

    ```bash
    git push origin feature/nueva-funcionalidad
    ```

5. Abre un **pull request** describiendo tus cambios en detalle.

## Licencia

Este proyecto está licenciado bajo los términos de la **Licencia Pública General Affero de GNU v3.0 (AGPL-3.0)**. Consulta el archivo [LICENSE](LICENSE.md) para más detalles.

## Créditos

Desarrollado por **[Ranphe]**.

## Agradecimientos

Agradecemos sinceramente a todas las personas que han contribuido al desarrollo de **Recicland**. Su dedicación y esfuerzo han sido fundamentales para llevar este proyecto adelante.

Puedes ver la lista completa de contribuidores en el archivo [CONTRIBUTORS.md](CONTRIBUTORS.md).