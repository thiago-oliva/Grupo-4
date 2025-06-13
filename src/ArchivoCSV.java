import java.io.*;
import java.util.*;

public class ArchivoCSV {
    private Map<String, List<Celda<?>>> columnas;

    // Constructor que carga datos desde un archivo CSV.
    public ArchivoCSV(String archivoCSV) {
        columnas = new LinkedHashMap<>();
        cargarDatos(archivoCSV);
    }

    // Constructor que guarda una tabla en un archivo CSV.
    public ArchivoCSV(Tabla tabla, String rutaDestino) {
        guardarTablaEnCSV(tabla, rutaDestino);
    }

    // Devuelve el mapa con las columnas y sus celdas cargadas.
    public Map<String, List<Celda<?>>> getMap() {
        return columnas;
    }

    // Carga datos desde un archivo CSV a la estructura interna de columnas.
    public void cargarDatos(String archivoCSV) {
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
                    Celda<?> celda;
                    if (i < datosFila.length) {
                        celda = new Celda<>(convertirDato(datosFila[i]));
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

    // Convierte un String a un tipo de dato apropiado (Integer, Double, Boolean o String).
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

    // Guarda los datos de una Tabla en un archivo CSV en la ruta indicada.
    public void guardarTablaEnCSV(Tabla tabla, String archivoDestino) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoDestino))) {
            // Cabecera.
            for (int i = 0; i < tabla.getColumnas().size(); i++) {
                writer.write(tabla.getColumnas().get(i).getNombre());
                if (i < tabla.getColumnas().size() - 1) writer.write(",");
            }
            writer.newLine();
            // Filas.
            for (int fila = 0; fila < tabla.getCantidadFilas(); fila++) {
                for (int col = 0; col < tabla.getColumnas().size(); col++) {
                    Columna<?> columna = tabla.getColumnas().get(col);
                    Object valor = columna.getCeldas().get(fila).getValor();
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