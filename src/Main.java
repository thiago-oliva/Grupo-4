import java.util.*;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {

        try {
            // Leer tabla desde archivo CSV
            //Tabla tabla = ArchivoCSV.cargarDatos("archivo.csv");
            ArchivoCSV<?> lector = new ArchivoCSV<>("ruta/del/archivo.csv");
            Map<String, List<Celda<?>>> datos = lector.getMap();
            Tabla tabla = new Tabla(datos);

            System.out.println("Tabla original:");
            tabla.mostrar();

            // 1. Filtrar por valores mayores a 50 en la columna "edad"
            Tabla filtradaPorEdad = tabla.filtrarColumnas("edad", valor -> {
                if (valor instanceof Integer) return (Integer) valor > 50;
                return false;
            });
            System.out.println("\nFiltrada por edad > 50:");
            filtradaPorEdad.mostrar();

            // 2. Filtrar por fila: suma de primera y segunda columna > 100
            Tabla filtradaPorFila = tabla.filtrarFilas(fila -> {
                try {
                    Object a = fila.get(0);
                    Object b = fila.get(1);
                    if (a instanceof Number && b instanceof Number) {
                        return ((Number) a).doubleValue() + ((Number) b).doubleValue() > 100;
                    }
                } catch (Exception ignored) {}
                return false;
            });
            System.out.println("\nFiltrada por condición fila:");
            filtradaPorFila.mostrar();

            // 3. Copiar tabla
            Tabla copia = tabla.copiar();
            System.out.println("\nCopia de la tabla:");
            copia.mostrar();

            // 4. Muestreo aleatorio de 3 filas
            Tabla muestra = tabla.muestreoAleatorio(3);
            System.out.println("\nMuestreo aleatorio (3 filas):");
            muestra.mostrar();

            // 5. Leer NAs
            System.out.println("\nLectura de NAs:");
            tabla.leerNAs(tabla);

            // 6. Mostrar NAs
            System.out.println("\nUbicación de NAs:");
            tabla.mostrarNAs(tabla);

            // 7. Reemplazar NAs por "Desconocido"
            tabla.reemplazarNAs(tabla, "Desconocido");
            System.out.println("\nTabla con NAs reemplazados:");
            tabla.mostrar();

            // 8. Seleccionar columnas específicas
            List<String> columnasDeseadas = Arrays.asList("nombre", "edad");
            Tabla tablaReducida = tabla.seleccionarColumnas(columnasDeseadas);
            System.out.println("\nTabla con columnas seleccionadas:");
            tablaReducida.mostrar();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

