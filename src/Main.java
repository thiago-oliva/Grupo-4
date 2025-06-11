import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Leer archivo CSV
            ArchivoCSV lector = new ArchivoCSV("C:\\Users\\Usuario\\Downloads\\EJEMPLO.csv");
            Tabla tabla = new Tabla(lector.getMap());

            // Obtener una columna específica
            Columna<?> columna = tabla.getColumna("EDA"); // Asegurate que "EDA" exista en tu CSV
            System.out.println("Columna seleccionada: " + columna.getNombre());
            System.out.println("Tipo de dato: " + columna.getTipoDeDato());

            // Mostrar valor original en la fila 0
            Celda<?> celdaOriginal = columna.getCelda(0);
            System.out.println("Valor original en fila 0: " + celdaOriginal.getValor());

            // Cambiar el valor de la celda en la fila 0
            if (columna.getTipoDeDato() == TipoDato.NUMERICO) {
                ((Columna)columna).setCelda(0, 999);
            } else if (columna.getTipoDeDato() == TipoDato.CADENA) {
                ((Columna)columna).setCelda(0, "Modificado");
            } else if (columna.getTipoDeDato() == TipoDato.BOOLEANO) {
                ((Columna)columna).setCelda(0, true);
            }

            // Mostrar valor modificado
            Celda<?> celdaModificada = columna.getCelda(0);
            System.out.println("Valor modificado en fila 0: " + celdaModificada.getValor());

            // Guardar la tabla modificada en un nuevo archivo CSV
            ArchivoCSV escritor = new ArchivoCSV();
            escritor.guardarTablaEnCSV(tabla, "C:\\Users\\Usuario\\Downloads\\EJEMPLO_MODIFICADO.csv");
            System.out.println("Archivo modificado guardado con éxito.");

        } catch (ExcepcionesTabla.ExcepcionIndiceInvalido e) {
            System.err.println("Error de índice: " + e.getMessage());
        } catch (ExcepcionesTabla.ExcepcionTipoDato e) {
            System.err.println("Error de tipo de dato: " + e.getMessage());
        } catch (ExcepcionesTabla.ExcepcionColumnaNoEncontrada e) {
            System.err.println("Columna no encontrada: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}