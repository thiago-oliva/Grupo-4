import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; //es una estructura que asocia una clave (K) con un valor (V).

public class Tabla {

    private String nombreTabla;
    private List<Columna> columnas;
    private List<String> etiquetasFilas;
    private List<String> etiquetasColumnas;
    private Map<String, Integer> mapaFilas;// Cada nombre de lista (por ejemplo "Nombre" o "Edad") está asociado a su posición (índice) en la lista filas
    private Map<String, Integer> mapaColumnas;//Igual que arriba

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


    // Con este metodo, obtengo los tipos de datos de todas las columnas
    public List<Columna.TipoDato> getTiposColumnas() {
        List<Columna.TipoDato> tipos = new ArrayList<>();
        for (Columna columna : columnas) {
            tipos.add(columna.getTipoDeDato());
        }
        return tipos;
    }


    //Aca se recibe la etiqueta de una columna y se devuele la lista de valores de esa columna
    public List<Celda> obtenerColumna(String etiqueta) {
        Integer indice = mapaColumnas.get(etiqueta); //Obtiene el indice de la columna con mapaColumnas
        if (indice == null) {
            throw new IllegalArgumentException("No existe una columna con etiqueta: " + etiqueta);
        }
        return columnas.get(indice).obtenerCeldas();
    }

    //Misma logica que en obtenerColumna, solo que con las etiquetas de las filas
    public List<Object> obtenerFila(String etiqueta) {
        Integer filaIndex = mapaFilas.get(etiqueta);
        List<Object> fila = new ArrayList<>();
        for (Columna col : columnas) {
            fila.add(col.getValor(filaIndex));
        }
        return fila;
    }




    // Con este metodo, obtengo el tipo de dato de una columna segun el nombre
 //   public Columna.TipoDato getTipoColumna(String etiqueta) {
 //       Integer index = mapaColumnas.get(etiqueta);
 //       if (index == null) throw new RuntimeException("Columna no encontrada: " + etiqueta);
 //       return columnas.get(index).getTipoDeDato();
 //   }
//    public Celda getCelda(String etiquetaFila, String etiquetaColumna) {
//        int fila = mapaFilas.get(etiquetaFila);
//        int columna = mapaColumnas.get(etiquetaColumna);
//        return columnas.get(columna).getCelda(fila);
//    }

//    public void setCelda(String etiquetaFila, String etiquetaColumna, Object valor) throws Columna.excepcionTipoDato {
//        int fila = mapaFilas.get(etiquetaFila);
//        int columna = mapaColumnas.get(etiquetaColumna);
//        columnas.get(columna).setCelda(fila, valor);





}
