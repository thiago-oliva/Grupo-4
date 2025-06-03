import java.io.*;
import java.util.*;

public class GestorArchivosCSV {

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
                for (int fila = 0; fila < tabla.getFilas(); fila++) {
                    for (int col = 0; col < tabla.getColumnas().size(); col++) {
                        Columna<?> columna = tabla.getColumnas().get(col);
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


//    public static Tabla leerCSV(String ruta, char separador, boolean tieneEncabezado)
//            throws IOException, ExcepcionesTabla.ExcepcionTipoDato {
//
//        BufferedReader lector = new BufferedReader(new FileReader(ruta));
//        List<String[]> filasLeidas = new ArrayList<>();
//        String linea;
//
//        while ((linea = lector.readLine()) != null) {
//            filasLeidas.add(linea.split(Character.toString(separador), -1));
//        }
//        lector.close();
//
//        if (filasLeidas.isEmpty()) throw new IOException("Archivo vacío.");
//
//        int columnas = filasLeidas.get(0).length;
//        List<String> nombres = new ArrayList<>();
//
//        int desde = 0;
//        if (tieneEncabezado) {
//            nombres = Arrays.asList(filasLeidas.get(0));
//            desde = 1;
//        } else {
//            for (int i = 0; i < columnas; i++) nombres.add("col" + i);
//        }
//
//        List<Columna> listaColumnas = new ArrayList<>();
//        for (String nombre : nombres)
//            listaColumnas.add(new Columna(nombre, TipoDato.CADENA));
//
//        for (int i = desde; i < filasLeidas.size(); i++) {
//            String[] fila = filasLeidas.get(i);
//            for (int j = 0; j < columnas; j++) {
//                String val = j < fila.length ? fila[j] : "";
//                listaColumnas.get(j).agregarCelda(new Celda(val.equalsIgnoreCase("NA") ? null : val));
//            }
//        }
//
//        Tabla tabla = new Tabla("tabla"); // Usá el constructor que tengas disponible
//
//        for (Columna c : listaColumnas) {
//            tabla.insertarColumna(c); // Este método debe existir en tu clase Tabla
//        }
//
//        return tabla;
//    }
//
//
//    public static void guardarEnCSV(String ruta, char separador, boolean incluirEncabezado, Tabla tabla) throws IOException {
//        BufferedWriter escritor = new BufferedWriter(new FileWriter(ruta));
//
//        int filas = tabla.getCantidadFilas();
//        int columnas = tabla.getCantidadColumnas();
//
//        if (incluirEncabezado) {
//            List<String> nombres = tabla.getEtiquetasColumnas();
//            escritor.write(String.join(Character.toString(separador), nombres));
//            escritor.newLine();
//        }
//
//        for (int i = 0; i < filas; i++) {
//            List<String> fila = new ArrayList<>();
//            for (int j = 0; j < columnas; j++) {
//                Object valor = tabla.getCelda(i, j).getValor();
//                fila.add(valor == null ? "NA" : valor.toString());
//            }
//            escritor.write(String.join(Character.toString(separador), fila));
//            escritor.newLine();
//        }
//
//        escritor.close();
//    }
}
