import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Leer archivo CSV
            ArchivoCSV lector = new ArchivoCSV("//Users/franciscoestevezsala.EJEMPLO.csv");
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
            ArchivoCSV escritor = new ArchivoCSV("C:\\Users\\Usuario\\Downloads\\EJEMPLO_MODIFICADO.csv");
            escritor.guardarTablaEnCSV(tabla, "C:\\Users\\Usuario\\Downloads\\EJEMPLO_MODIFICADO.csv");
            System.out.println("Archivo modificado guardado con éxito.");

            // Elimina una columna
            tabla.eliminarColumna(2);

            // Busco primeras 2 filas
            System.out.println(tabla.head(2));

            // Selecciono primer y segunda fila y columna edad
            List<String> columnasElegidas = new ArrayList<>();
            columnasElegidas.add("Edad");
            List<Integer> filasElegidas = new ArrayList<>();
            filasElegidas.add(1);
            filasElegidas.add(2);
            tabla.seleccionar(columnasElegidas, filasElegidas);


            // Busco ultimas 2 filas
            System.out.println(tabla.tail(2));

            // Filtra por mayores de edad
            tabla.filtrarColumnas("Edad", valor ->
            {return valor instanceof Integer && (Integer) valor >= 18;});


            // Copia la tabla
            Tabla copia = tabla.copiar();





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