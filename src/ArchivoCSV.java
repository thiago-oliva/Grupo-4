import java.io.*;
import java.util.*;

import java.io.*;
import java.util.*;

public class ArchivoCSV<T> {
    private Map<String, List<Celda<T>>> columnas;

    // Constructor para cargar datos desde archivo CSV
    public ArchivoCSV(String archivoCSV) {
        columnas = new LinkedHashMap<>();
        cargarDatos(archivoCSV);
    }

    // Constructor para guardar una Tabla en un archivo CSV
    public ArchivoCSV(Tabla<T> tabla, String rutaDestino) {
        guardarTablaEnCSV(tabla, rutaDestino);
    }

    // Devuelve el mapa con los datos por columna
    public Map<String, List<Celda<T>>> getMap() {
        return columnas;
    }

    // Cargar datos desde archivo CSV
    private void cargarDatos(String archivoCSV) {
        String linea;
        String separador = ",";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivoCSV), "UTF-8"))) {
            if ((linea = br.readLine()) != null) {
                String[] nombresColumnas = linea.split(separador);
                for (String nombre : nombresColumnas) {
                    columnas.put(nombre, new ArrayList<>());
                }
            }

            while ((linea = br.readLine()) != null) {
                String[] datosFila = linea.split(separador);
                int i = 0;
                for (String nombreColumna : columnas.keySet()) {
                    Celda<T> celda;
                    if (i < datosFila.length) {
                        celda = new Celda<>((T) convertirDato(datosFila[i]));
                    } else {
                        celda = new Celda<>(null);
                    }
                    columnas.get(nombreColumna).add(celda);
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Convierte String a tipo apropiado (Integer, Double, Boolean o String)
    private Object convertirDato(String dato) {
        if (dato == null || dato.isEmpty() || dato.equalsIgnoreCase("NA")) {
            return null;
        }
        if (dato.equalsIgnoreCase("true") || dato.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(dato);
        }
        try {
            if (dato.contains(".")) return Double.parseDouble(dato);
            return Integer.parseInt(dato);
        } catch (NumberFormatException e) {
            return dato;
        }
    }

    // Guarda una tabla en un archivo CSV
    public void guardarTablaEnCSV(Tabla<?> tabla, String archivoDestino) { //Recibe la tabla cuyos datos se van a guardar y devuelve la ruta donde se guardará el archivo CSV.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoDestino))) {
            // Cabecera
            for (int i = 0; i < tabla.getColumnas().size(); i++) {
                writer.write(tabla.getColumnas().get(i).getNombre());
                if (i < tabla.getColumnas().size() - 1) writer.write(",");
            }
            writer.newLine();

            // Filas
            for (int fila = 0; fila < tabla.getCantidadFilas(); fila++) {
                for (int col = 0; col < tabla.getColumnas().size(); col++) {
                    Columna<?> columna = tabla.getColumnas().get(col);
                    ? valor = columna.getCeldas().get(fila).getValor();
                    writer.write(valor != null ? valor.toString() : "");
                    if (col < tabla.getColumnas().size() - 1) writer.write(",");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




//public class ArchivoCSV<T> {
//    private Map<String, List<Celda<T>>> columnas;
//
//
//    public ArchivoCSV(String archivoCSV) { // Constructor que carga los datos desde un archivo CSV.
//        columnas = new LinkedHashMap<>();
//        cargarDatos(archivoCSV);
//    }
//
//
//    public ArchivoCSV(Tabla tabla, String rutaDestino) { //Constructor que guarda una tabla en un archivo CSV. Recibe la tabla que se va a guardar.
//        guardarTablaEnCSV(tabla, rutaDestino); // Devuelve la ruta donde se guardará el archivo CSV.
//    }
//
//    public Map<String, List<Object>> getMap() { //Devuelve un mapa que contiene los nombres de las columnas y sus datos.
//        return columnas;
//    }
//
//
//    private void cargarDatos(String archivoCSV) { //Carga los datos desde el archivo CSV y los almacena en el mapa de columnas.
//        String linea;
//        String separador = ",";
//
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivoCSV), "UTF-8"))) {
//            // Leer la primera línea para obtener los nombres de las columnas
//            if ((linea = br.readLine()) != null) {
//                String[] nombresColumnas = linea.split(separador);
//
//                // Inicializar una lista vacía para cada columna
//                for (String nombreColumna : nombresColumnas) {
//                    columnas.put(nombreColumna, new ArrayList<>());
//                }
//            }
//
//            // Leer cada línea de datos y almacenarla en la lista correspondiente
//            while ((linea = br.readLine()) != null) {
//                String[] datosFila = linea.split(separador);
//                int i = 0;
//
//                for (String nombreColumna : columnas.keySet()) {
//                    if (i < datosFila.length) {
//                        columnas.get(nombreColumna).add(convertirDato(datosFila[i]));
//                    } else {
//                        columnas.get(nombreColumna).add(null); // Manejo de valores faltantes
//                    }
//                    i++;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private Object convertirDato(String dato) { //Convierte un dato de cadea de texto al tipo adecuado (Integer, Double, Boolean o String) o null si está vacío o es "NA".
//        if (dato.isEmpty() || dato.equalsIgnoreCase("NA")) {
//            return null;
//        }
//
//        // Intentar convertir a boolean
//        if (dato.equalsIgnoreCase("true")) {
//            return true;
//        } else if (dato.equalsIgnoreCase("false")) {
//            return false;
//        }
//
//        // Intentar convertir a Double o Integer
//        try {
//            if (dato.contains(".")) {
//                return Double.parseDouble(dato);
//            }
//            return Integer.parseInt(dato);
//        } catch (NumberFormatException e) {
//            return dato; // Si no es numérico ni booleano, devolver como String
//        }
//    }
//
//
//    public void guardarTablaEnCSV(Tabla tabla, String archivoDestino) { //Recibe la tabla cuyos datos se van a guardar y devuelve la ruta donde se guardará el archivo CSV.
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoDestino))) {
//            // Escribir la cabecera con los nombres de las columnas
//            for (int i = 0; i < tabla.getColumnas().size(); i++) {
//                writer.write(tabla.getColumnas().get(i).getNombre());
//                if (i < tabla.getColumnas().size() - 1) {
//                    writer.write(","); // Agregar coma entre nombres de columnas
//                }
//            }
//            writer.newLine(); // Nueva línea después de la cabecera
//
//            // Escribir cada fila de datos
//            for (int fila = 0; fila < tabla.getCantidadFilas(); fila++) {
//                for (int col = 0; col < tabla.getColumnas().size(); col++) {
//                    Columna columna = tabla.getColumnas().get(col);
//                    Object valor = columna.getCeldas().get(fila).getValor();
//                    writer.write(valor != null ? valor.toString() : ""); // Manejo de valores nulos
//
//                    if (col < tabla.getColumnas().size() - 1) {
//                        writer.write(",");
//                    }
//                }
//                writer.newLine();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
