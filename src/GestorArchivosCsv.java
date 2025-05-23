import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;
import java.util.Arrays;
import java.io.IOException;

public class GestorArchivosCSV {

    public GestorArchivosCSV() {

    }

    public void leerCsv(String ruta) {
        Path archivo = Paths.get(ruta);
        try {
            Files.lines(archivo).forEach(linea -> {
                List<String> fila = Arrays.asList(linea.split(","));
                tabla.agregarFila(fila);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarEnCsv(Tabla tabla, String ruta) {
        Path direccion = Paths.get(ruta);
        StringBuilder contenido = new StringBuilder();

        for (List<String> fila : tabla.getFilas()) {
            contenido.append(String.join(",", fila));
            contenido.append(System.lineSeparator());
        }

        try { //ver clase excepciones
            Files.write(direccion, contenido.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo: " + e.getMessage());
        }
    }


    public void getMapa(Tabla tabla){
//despues
    }
}
