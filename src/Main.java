import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Leer archivo CSV
            ArchivoCSV lector = new ArchivoCSV("C:\\Users\\Usuario\\Downloads\\EJEMPLO.csv");
            Tabla tabla = new Tabla(lector.getMap());

            System.out.println(tabla.getEtiquetasFilas());

            // Crear una nueva fila con valores compatibles con las columnas
            List<Object> nuevaFila = new ArrayList<>();
            nuevaFila.add("Fran");
            nuevaFila.add(32);
            nuevaFila.add(true);

            // Insertar nueva fila
            tabla.insertarFila(nuevaFila);
            System.out.println("Fila insertada correctamente.");

            // Eliminar una fila por etiqueta
            String etiquetaAEliminar = "F3"; // Asegurate que esta etiqueta exista
            tabla.eliminarFila(etiquetaAEliminar);
            System.out.println("Fila con etiqueta " + etiquetaAEliminar + " eliminada correctamente.");

            // Guardar la tabla modificada en un nuevo archivo CSV
            String rutaDestino = "C:\\Users\\Usuario\\Downloads\\EJEMPLO_MODIFICADO.csv";
            ArchivoCSV escritor = new ArchivoCSV(rutaDestino);
            escritor.guardarTablaEnCSV(tabla, rutaDestino);
            System.out.println("Archivo CSV guardado con éxito en: " + rutaDestino);

        } catch (ExcepcionesTabla.ExcepcionFilaNoEncontrada e) {
            System.err.println("Fila no encontrada: " + e.getMessage());
        } catch (ExcepcionesTabla.ExcepcionTipoDato e) {
            System.err.println("Error de tipo de dato: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

