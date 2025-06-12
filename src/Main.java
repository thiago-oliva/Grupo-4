import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Leer archivo CSV original
            ArchivoCSV lector = new ArchivoCSV("C:\\Users\\anton\\Downloads\\EJEMPLO.csv");
            Tabla tabla = new Tabla(lector.getMap());

            System.out.println("Etiquetas de filas:");
            System.out.println(tabla.getEtiquetasFilas());

            // Mostrar una fila existente
            System.out.println("\nContenido de la fila F1:");
            System.out.println(tabla.obtenerFila("F1"));

            // Insertar nueva fila
            List<Object> nuevaFila = Arrays.asList("Fran", 32, true);
            tabla.insertarFila(nuevaFila);
            System.out.println("\nFila 'Fran' insertada correctamente.");

            // Eliminar una fila por etiqueta
            String etiquetaAEliminar = "F3";
            tabla.eliminarFila(etiquetaAEliminar);
            System.out.println("Fila con etiqueta " + etiquetaAEliminar + " eliminada correctamente.");

            // Insertar una nueva columna
            List<String> nuevaCol = Arrays.asList("Buenos Aires", "Córdoba", "Mendoza", "Santa Fe");
            tabla.insertarColumna(1, nuevaCol);
            System.out.println("Nueva columna insertada.");

            // Eliminar una columna por índice
            tabla.eliminarColumna(0); // por ejemplo, elimina la primera
            System.out.println("Primera columna eliminada.");

            // Mostrar valores faltantes
            System.out.println("\n Valores faltantes:");
            tabla.mostrarNAs(tabla);

            // Reemplazar valores faltantes por "N/A"
            tabla.reemplazarNAs(tabla, "N/A");
            System.out.println("NAs reemplazados por 'N/A'.");

            // Guardar la tabla modificada
            String rutaDestino = "C:\\Users\\anton\\Downloads\\EJEMPLO_MODIFICADO.csv";
            ArchivoCSV escritor = new ArchivoCSV(rutaDestino);
            escritor.guardarTablaEnCSV(tabla, rutaDestino);
            System.out.println("Archivo CSV guardado en: " + rutaDestino);

        } catch (ExcepcionesTabla.ExcepcionFilaNoEncontrada e) {
            System.err.println("Fila no encontrada: " + e.getMessage());
        } catch (ExcepcionesTabla.ExcepcionTipoDato e) {
            System.err.println("Error de tipo de dato: " + e.getMessage());
        } catch (ExcepcionesTabla.ExcepcionLongitudColumna e) {
            System.err.println("Error al insertar columna: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


