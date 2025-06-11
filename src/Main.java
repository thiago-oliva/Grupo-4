import java.util.*;
import java.util.function.Predicate;

import java.util.*;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {
        try {
            // Ruta del archivo CSV de entrada
            String rutaEntrada = "C:\\Users\\Usuario\\Downloads\\EJEMPLO.csv";

            // Crear instancia de ArchivoCSV para leer datos
            ArchivoCSV lector = new ArchivoCSV(rutaEntrada);

            // Obtener el mapa con los datos leídos
            Map<String, List<Celda<?>>> datos = lector.getMap();

            // Mostrar datos leídos por columna
            System.out.println("Datos leídos del archivo CSV:");
            for (String columna : datos.keySet()) {
                System.out.print(columna + ": ");
                List<Celda<?>> celdas = datos.get(columna);
                for (Celda<?> celda : celdas) {
                    System.out.print(celda.getValor() + " ");
                }
                System.out.println();
            }

            // Guardar los datos en otro archivo CSV
            String rutaSalida = "C:\\Users\\Usuario\\Downloads\\EJEMPLO_SALIDA.csv";

            // Crear un objeto Tabla temporal a partir de los datos para usar el método guardaTabla
            Tabla tablaTemporal = new Tabla(datos);

            ArchivoCSV archivoEscritor = new ArchivoCSV(tablaTemporal, rutaSalida);

            System.out.println("\nDatos guardados en: " + rutaSalida);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}