package main.models;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RepositorioDesechos {
    private static List<Desecho> desechosNivel1 = new ArrayList<>();
    private static List<Desecho> desechosNivel2 = new ArrayList<>();
    private static List<Desecho> desechosNivel3 = new ArrayList<>();

    private static List<Contenedor> contenedores = new ArrayList<>();

    static {
        cargarDesechosDesdeArchivo("desechos.txt");
    }

    private static void agregarDesecho(String nivel, String nombreDesecho, String nombreContenedor, List<String> pasosTratamiento) {
        if (nombreDesecho.isEmpty() || nombreContenedor.isEmpty() || pasosTratamiento.isEmpty()) {
            System.out.println("Desecho incompleto: " + nombreDesecho);
            return;
        }
        Contenedor contenedor = obtenerContenedor(nombreContenedor);
        Desecho desecho = new Desecho(nombreDesecho, contenedor, new ArrayList<>(pasosTratamiento));
        switch (nivel) {
            case "1":
                desechosNivel1.add(desecho);
                break;
            case "2":
                desechosNivel2.add(desecho);
                break;
            case "3":
                desechosNivel3.add(desecho);
                break;
            default:
                System.out.println("Nivel desconocido para el desecho: " + nombreDesecho);
                break;
        }
    }

    private static void cargarDesechosDesdeArchivo(String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            System.out.println("Archivo no encontrado. Creando archivo por defecto...");
            crearArchivoPorDefecto(nombreArchivo);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            String nivelActual = "";
            String nombreDesecho = "";
            String nombreContenedor = "";
            List<String> pasosTratamiento = new ArrayList<>();

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue; // Ignorar líneas vacías o comentarios
                }

                if (linea.startsWith("[Nivel]")) {
                    if (!nombreDesecho.isEmpty()) {
                        agregarDesecho(nivelActual, nombreDesecho, nombreContenedor, pasosTratamiento);
                        nombreDesecho = "";
                        nombreContenedor = "";
                        pasosTratamiento = new ArrayList<>();
                    }
                } else if (linea.startsWith("NombreNivel:")) {
                    nivelActual = linea.substring(linea.indexOf(':') + 1).trim();
                } else if (linea.startsWith("Nombre:")) {
                    if (!nombreDesecho.isEmpty()) {
                        agregarDesecho(nivelActual, nombreDesecho, nombreContenedor, pasosTratamiento);
                    }
                    nombreDesecho = linea.substring(linea.indexOf(':') + 1).trim();
                    nombreContenedor = "";
                    pasosTratamiento = new ArrayList<>();
                } else if (linea.startsWith("Contenedor:")) {
                    nombreContenedor = linea.substring(linea.indexOf(':') + 1).trim();
                } else if (linea.startsWith("PasosTratamiento:")) {
                    pasosTratamiento.clear();
                } else if (linea.startsWith("-")) {
                    pasosTratamiento.add(linea.substring(1).trim());
                }
            }
            if (!nombreDesecho.isEmpty()) {
                agregarDesecho(nivelActual, nombreDesecho, nombreContenedor, pasosTratamiento);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de desechos: " + e.getMessage());
        }
    }

    private static void crearArchivoPorDefecto(String nombreArchivo) {
        String contenidoPorDefecto = """
                [Nivel]
                NombreNivel: 1
                
                # Desecho 1
                Nombre: Botella de plástico
                Contenedor: Plasticos Y Envases
                PasosTratamiento:
                - Vaciar el contenido
                - Enjuagar la botella
                - Aplastar y tapar
                
                # Desecho 2
                Nombre: Envase de shampoo
                Contenedor: Plasticos Y Envases
                PasosTratamiento:
                - Enjuagar para eliminar residuos
                - Secar el envase
                - Cerrar bien la tapa
                
                # Desecho 3
                Nombre: Bolsa de plástico
                Contenedor: Plasticos Y Envases
                PasosTratamiento:
                - Limpiar residuos
                - Doblar o enrollar
                - Agrupar con otras bolsas
                
                # Desecho 4
                Nombre: Envase de detergente
                Contenedor: Plasticos Y Envases
                PasosTratamiento:
                - Enjuagar el envase
                - Secar completamente
                - Cerrar y depositar
                
                # Desecho 5
                Nombre: Caja de cereal
                Contenedor: Papel Y Carton
                PasosTratamiento:
                - Vaciar migajas
                - Desarmar la caja
                - Aplastar para reducir volumen
                
                # Desecho 6
                Nombre: Periódico viejo
                Contenedor: Papel Y Carton
                PasosTratamiento:
                - Quitar plásticos
                - Apilar ordenadamente
                - Atar con cuerda
                
                # Desecho 7
                Nombre: Hoja de papel usada
                Contenedor: Papel Y Carton
                PasosTratamiento:
                - Quitar grapas y clips
                - Apilar hojas
                - Atar
                
                # Desecho 8
                Nombre: Caja de zapatos
                Contenedor: Papel Y Carton
                PasosTratamiento:
                - Retirar etiquetas
                - Desarmar la caja
                - Aplastar para reducir volumen
                
                # Desecho 9
                Nombre: Pañal desechable
                Contenedor: No Reciclables
                PasosTratamiento:
                - Cerrar el pañal
                - Colocar en bolsa
                - Sellar y desechar
                
                # Desecho 10
                Nombre: Esponja de cocina usada
                Contenedor: No Reciclables
                PasosTratamiento:
                - Enjuagar la esponja
                - Secar al aire
                - Depositar en bolsa de basura
                
                # Desecho 11
                Nombre: Cerámica rota
                Contenedor: No Reciclables
                PasosTratamiento:
                - Recoger fragmentos con cuidado
                - Envolver en papel periódico
                - Etiquetar como frágil
                
                # Desecho 12
                Nombre: Papel higiénico usado
                Contenedor: No Reciclables
                PasosTratamiento:
                - Colocar en bolsa
                - Sellar la bolsa
                - Depositar en contenedor no reciclable
                
                [Nivel]
                NombreNivel: 2
                
                # Desecho 13
                Nombre: Cáscara de plátano
                Contenedor: Organicos
                PasosTratamiento:
                - Separar de otros residuos
                - Depositar en compostera
                - Tapar para evitar plagas
                
                # Desecho 14
                Nombre: Bolsa de té usada
                Contenedor: Organicos
                PasosTratamiento:
                - Retirar grapa o etiqueta
                - Colocar en compostera
                - Mezclar con otros orgánicos
                
                # Desecho 15
                Nombre: Restos de vegetales
                Contenedor: Organicos
                PasosTratamiento:
                - Cortar en trozos pequeños
                - Añadir a compostera
                - Mantener humedad adecuada
                
                # Desecho 16
                Nombre: Cáscara de huevo
                Contenedor: Organicos
                PasosTratamiento:
                - Triturar las cáscaras
                - Añadir al compost
                - Mezclar bien
                
                # Desecho 17
                Nombre: Botella de vidrio
                Contenedor: Vidrio
                PasosTratamiento:
                - Enjuagar la botella
                - Retirar tapas y etiquetas
                - Depositar en contenedor
                
                # Desecho 18
                Nombre: Frasco de mermelada
                Contenedor: Vidrio
                PasosTratamiento:
                - Vaciar restos
                - Lavar el frasco
                - Secar y reciclar
                
                # Desecho 19
                Nombre: Envase de perfume
                Contenedor: Vidrio
                PasosTratamiento:
                - Vaciar contenido
                - Retirar partes plásticas
                - Depositar en contenedor
                
                # Desecho 20
                Nombre: Tarro de vidrio
                Contenedor: Vidrio
                PasosTratamiento:
                - Quitar etiquetas
                - Lavar para eliminar residuos
                - Reciclar
                
                # Desecho 21
                Nombre: Pila AA usada
                Contenedor: Pilas Y Baterias
                PasosTratamiento:
                - Colocar en recipiente seguro
                - No mezclar con otros residuos
                - Llevar a punto de recolección
                
                # Desecho 22
                Nombre: Batería de celular
                Contenedor: Pilas Y Baterias
                PasosTratamiento:
                - Aislar terminales con cinta
                - Guardar en bolsa hermética
                - Entregar en centro autorizado
                
                # Desecho 23
                Nombre: Pila de reloj
                Contenedor: Pilas Y Baterias
                PasosTratamiento:
                - Colocar en recipiente sellado
                - No perforar ni aplastar
                - Llevar a reciclaje especializado
                
                # Desecho 24
                Nombre: Batería de laptop
                Contenedor: Pilas Y Baterias
                PasosTratamiento:
                - Proteger con material aislante
                - No exponer al calor
                - Entregar para reciclaje
                
                [Nivel]
                NombreNivel: 3
                
                # Desecho 25
                Nombre: Teléfono móvil viejo
                Contenedor: Electronicos
                PasosTratamiento:
                - Quitar tarjeta SIM
                - Retirar batería si es posible
                - Entregar en punto limpio
                
                # Desecho 26
                Nombre: Cargador de portátil dañado
                Contenedor: Electronicos
                PasosTratamiento:
                - Enrollar el cable
                - Asegurar partes expuestas
                - Llevar a centro de reciclaje
                
                # Desecho 27
                Nombre: Monitor roto
                Contenedor: Electronicos
                PasosTratamiento:
                - No desarmar el equipo
                - Proteger pantalla con cartón
                - Transportar con cuidado al reciclaje
                
                # Desecho 28
                Nombre: Teclado viejo
                Contenedor: Electronicos
                PasosTratamiento:
                - Limpiar el teclado
                - Enrollar el cable
                - Entregar en punto de recolección
                
                # Desecho 29
                Nombre: Camiseta vieja
                Contenedor: Textiles
                PasosTratamiento:
                - Lavar la prenda
                - Doblar ordenadamente
                - Donar o reciclar
                
                # Desecho 30
                Nombre: Sábanas usadas
                Contenedor: Textiles
                PasosTratamiento:
                - Lavar y secar
                - Doblar y empaquetar
                - Llevar a centro textil
                
                # Desecho 31
                Nombre: Zapatos desgastados
                Contenedor: Textiles
                PasosTratamiento:
                - Limpiar el calzado
                - Atar los cordones juntos
                - Depositar en contenedor textil
                
                # Desecho 32
                Nombre: Bolso de tela roto
                Contenedor: Textiles
                PasosTratamiento:
                - Vaciar contenido
                - Reparar si es posible
                - Reciclar o reutilizar
                
                # Desecho 33
                Nombre: Pintura sobrante
                Contenedor: Residuos Peligrosos
                PasosTratamiento:
                - Cerrar bien el envase
                - Etiquetar claramente
                - Llevar a centro especializado
                
                # Desecho 34
                Nombre: Aceite de motor usado
                Contenedor: Residuos Peligrosos
                PasosTratamiento:
                - Colocar en recipiente sellado
                - No mezclar con otros líquidos
                - Entregar en taller mecánico
                
                # Desecho 35
                Nombre: Termómetro de mercurio roto
                Contenedor: Residuos Peligrosos
                PasosTratamiento:
                - Evitar contacto directo
                - Colocar en bolsa hermética
                - Contactar servicios especializados
                
                # Desecho 36
                Nombre: Aerosol vacío
                Contenedor: Residuos Peligrosos
                PasosTratamiento:
                - No perforar el envase
                - Etiquetar como peligroso
                - Depositar en punto limpio
                """;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo, StandardCharsets.UTF_8))) {
            writer.write(contenidoPorDefecto);
            System.out.println("Archivo de desechos creado con contenido por defecto.");
        } catch (IOException e) {
            System.out.println("Error al crear el archivo de desechos por defecto: " + e.getMessage());
        }
    }

    public static List<Desecho> getDesechosNivel1() {
        return new ArrayList<>(desechosNivel1);
    }

    public static List<Desecho> getDesechosNivel2() {
        return new ArrayList<>(desechosNivel2);
    }

    public static List<Desecho> getDesechosNivel3() {
        return new ArrayList<>(desechosNivel3);
    }

    public static List<Contenedor> obtenerContenedores() {
        return new ArrayList<>(contenedores);
    }

    public static Contenedor obtenerContenedor(String nombreContenedor) {
        for (Contenedor contenedor : contenedores) {
            if (contenedor.getNombre().equalsIgnoreCase(nombreContenedor)) {
                return contenedor;
            }
        }
        Contenedor nuevoContenedor = new Contenedor(nombreContenedor);
        contenedores.add(nuevoContenedor);
        return nuevoContenedor;
    }
}