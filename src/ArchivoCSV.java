import java.io.*;
import java.util.*;

public class ArchivoCSV {
    private Map<String, List<Object>> columnas;


    public ArchivoCSV(String archivoCSV) { // Constructor que carga los datos desde un archivo CSV.
        columnas = new LinkedHashMap<>();
        cargarDatos(archivoCSV);
    }


    public ArchivoCSV(Tabla tabla, String rutaDestino) { //Constructor que guarda una tabla en un archivo CSV.
        guardarTablaEnCSV(tabla, rutaDestino);
    }

    public Map<String, List<Object>> getMap() { //Devuelve un mapa que contiene los nombres de las columnas y sus datos.
        return columnas;
    }


    private void cargarDatos(String archivoCSV) { //Carga los datos desde el archivo CSV y los almacena en el mapa de columnas.
        String linea;
        String separador = ",";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivoCSV), "UTF-8"))) {
            // Leer la primera línea para obtener los nombres de las columnas
            if ((linea = br.readLine()) != null) {
                String[] nombresColumnas = linea.split(separador);

                // Inicializar una lista vacía para cada columna
                for (String nombreColumna : nombresColumnas) {
                    columnas.put(nombreColumna, new ArrayList<>());
                }
            }

            // Leer cada línea de datos y almacenarla en la lista correspondiente
            while ((linea = br.readLine()) != null) {
                String[] datosFila = linea.split(separador);
                int i = 0;

                for (String nombreColumna : columnas.keySet()) {
                    if (i < datosFila.length) {
                        columnas.get(nombreColumna).add(convertirDato(datosFila[i]));
                    } else {
                        columnas.get(nombreColumna).add(null); // Manejo de valores faltantes
                    }
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convierte un dato de cadena de texto al tipo adecuado (Integer, Double, Boolean o String).
     *
     * @param dato El dato en forma de cadena de texto.
     * @return El dato convertido al tipo adecuado o null si está vacío o es "NA".
     */
    private Object convertirDato(String dato) {
        if (dato.isEmpty() || dato.equalsIgnoreCase("NA")) {
            return null;
        }

        // Intentar convertir a boolean
        if (dato.equalsIgnoreCase("true")) {
            return true;
        } else if (dato.equalsIgnoreCase("false")) {
            return false;
        }

        // Intentar convertir a Double o Integer
        try {
            if (dato.contains(".")) {
                return Double.parseDouble(dato);
            }
            return Integer.parseInt(dato);
        } catch (NumberFormatException e) {
            return dato; // Si no es numérico ni booleano, devolver como String
        }
    }


    public void guardarTablaEnCSV(Tabla tabla, String archivoDestino) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoDestino))) {
            // Escribir la cabecera con los nombres de las columnas
            for (int i = 0; i < tabla.getColumnas().size(); i++) {
                writer.write(tabla.getColumnas().get(i).getNombre());
                if (i < tabla.getColumnas().size() - 1) {
                    writer.write(","); // Agregar coma entre nombres de columnas
                }
            }
            writer.newLine(); // Nueva línea después de la cabecera

            // Escribir cada fila de datos
            for (int fila = 0; fila < tabla.getCantidadFilas(); fila++) {
                for (int col = 0; col < tabla.getColumnas().size(); col++) {
                    Columna columna = tabla.getColumnas().get(col);
                    Object valor = columna.getCeldas().get(fila).getValor();
                    writer.write(valor != null ? valor.toString() : ""); // Manejo de valores nulos

                    if (col < tabla.getColumnas().size() - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
