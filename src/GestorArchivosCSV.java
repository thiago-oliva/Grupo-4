import java.io.*;
import java.util.*;

public class GestorArchivosCSV {

    public static Tabla leerCSV(String ruta, char separador, boolean tieneEncabezado)
            throws IOException, ExcepcionesTabla.ExcepcionTipoDato {

        BufferedReader lector = new BufferedReader(new FileReader(ruta));
        List<String[]> filasLeidas = new ArrayList<>();
        String linea;

        while ((linea = lector.readLine()) != null) {
            filasLeidas.add(linea.split(Character.toString(separador), -1));
        }
        lector.close();

        if (filasLeidas.isEmpty()) throw new IOException("Archivo vacío.");

        int columnas = filasLeidas.get(0).length;
        List<String> nombres = new ArrayList<>();

        int desde = 0;
        if (tieneEncabezado) {
            nombres = Arrays.asList(filasLeidas.get(0));
            desde = 1;
        } else {
            for (int i = 0; i < columnas; i++) nombres.add("col" + i);
        }

        List<Columna> listaColumnas = new ArrayList<>();
        for (String nombre : nombres)
            listaColumnas.add(new Columna(nombre, TipoDato.CADENA));

        for (int i = desde; i < filasLeidas.size(); i++) {
            String[] fila = filasLeidas.get(i);
            for (int j = 0; j < columnas; j++) {
                String val = j < fila.length ? fila[j] : "";
                listaColumnas.get(j).agregarCelda(new Celda(val.equalsIgnoreCase("NA") ? null : val));
            }
        }

        Tabla tabla = new Tabla("tabla"); // Usá el constructor que tengas disponible

        for (Columna c : listaColumnas) {
            tabla.insertarColumna(c); // Este método debe existir en tu clase Tabla
        }

        return tabla;
    }


    public static void guardarEnCSV(String ruta, char separador, boolean incluirEncabezado, Tabla tabla) throws IOException {
        BufferedWriter escritor = new BufferedWriter(new FileWriter(ruta));

        int filas = tabla.getCantidadFilas();
        int columnas = tabla.getCantidadColumnas();

        if (incluirEncabezado) {
            List<String> nombres = tabla.getEtiquetasColumnas();
            escritor.write(String.join(Character.toString(separador), nombres));
            escritor.newLine();
        }

        for (int i = 0; i < filas; i++) {
            List<String> fila = new ArrayList<>();
            for (int j = 0; j < columnas; j++) {
                Object valor = tabla.getCelda(i, j).getValor();
                fila.add(valor == null ? "NA" : valor.toString());
            }
            escritor.write(String.join(Character.toString(separador), fila));
            escritor.newLine();
        }

        escritor.close();
    }
}
