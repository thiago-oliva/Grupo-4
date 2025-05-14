import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tabla {

    private String nombreTabla;
    private List<Columna> columnas;
    private List<String> etiquetasFilas;
    private List<String> etiquetasColumnas;
    private Map<String, Integer> mapaFilas;
    private Map<String, Integer> mapaColumnas;

    public Tabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
        this.columnas = new ArrayList<>();
        this.etiquetasFilas = new ArrayList<>();
        this.etiquetasColumnas = new ArrayList<>();
        this.mapaFilas = new HashMap<>();
        this.mapaColumnas = new HashMap<>();
    }


}