import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            ArchivoCSV lector = new ArchivoCSV("C:\\Users\\Usuario\\Downloads\\EJEMPLO.csv");
            Tabla tabla = new Tabla(lector.getMap());

            // Obtener tipos de todas las columnas (pasando null)
            List<TipoDato> tipos = tabla.getTiposColumnas(null);

            System.out.println("Tipos de todas las columnas:");
            for (int i = 0; i < tipos.size(); i++) {
                System.out.println("Columna " + i + ": " + tipos.get(i));
            }

            // Ejemplo con columnas específicas
            List<String> columnasDeseadas = Arrays.asList("Nombre", "Edad");
            List<TipoDato> tiposFiltrados = tabla.getTiposColumnas(columnasDeseadas);

            System.out.println("\nTipos de columnas seleccionadas:");
            for (int i = 0; i < columnasDeseadas.size(); i++) {
                System.out.println(columnasDeseadas.get(i) + ": " + tiposFiltrados.get(i));
            }

        } catch (ExcepcionesTabla.ExcepcionColumnaNoEncontrada e) {
            System.err.println("Error: Columna no encontrada - " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}