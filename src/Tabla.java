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

    public Tabla(String nombreTabla, List<Columna> columnas, List<String> etiquetasFilas, List<String> etiquetasColumnas, Map<String, Integer> mapaFilas, Map<String, Integer> mapaColumnas) {
        this.nombreTabla = nombreTabla;
        this.columnas = new ArrayList<>(columnas);
        this.etiquetasFilas = new ArrayList<>(etiquetasFilas);
        this.etiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        this.mapaFilas = new HashMap<>(mapaFilas);
        this.mapaColumnas = new HashMap<>(mapaColumnas);
    }

    public int getCantidadColumnas() {
        return columnas.size();
    }

    public List<String> getEtiquetasFilas() {
        return new ArrayList<>(etiquetasFilas); 
    }

    public List<String> getEtiquetasColumnas() {
        return new ArrayList<>(etiquetasColumnas);
    }

    public List<Columna> getColumnas() {
        return new ArrayList<>(columnas);
    }

    public Celda getCelda(String etiquetaFila, String etiquetaColumna) {
        int fila = mapaFilas.get(etiquetaFila);
        int columna = mapaColumnas.get(etiquetaColumna);
        return columnas.get(columna).getCelda(fila);
    }

    public void setCelda(String etiquetaFila, String etiquetaColumna, Object valor) throws Columna.excepcionTipoDato {
        int fila = mapaFilas.get(etiquetaFila);
        int columna = mapaColumnas.get(etiquetaColumna);
        columnas.get(columna).setCelda(fila, valor);
    }


}
