import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        return new ArrayList<>(etiquetasFilas); //devuelve una copia para que no modifiquen la original
    }


    public List<String> getEtiquetasColumnas() {
        return new ArrayList<>(etiquetasColumnas);
    }


    public List<Columna> getColumnas() {
        return new ArrayList<>(columnas);
    }






    // Este metodo sirve para obtener el tipo de dato de una o mas columnas.
    // Si se pasa null o lista vacía, devuelve los tipos de todas las columnas
    public List<Columna.TipoDato> getTiposDeColumnas(List<String> etiquetas) {
        List<Columna.TipoDato> tipos = new ArrayList<>();


        if (etiquetas == null || etiquetas.isEmpty()) {
            for (Columna columna : columnas) {
                tipos.add(columna.getTipoDeDato());
            }
        } else {
            for (String etiqueta : etiquetas) {
                Integer index = mapaColumnas.get(etiqueta);
                if (index == null)
                    throw new IllegalArgumentException("Columna no encontrada: " + etiqueta);
                tipos.add(columnas.get(index).getTipoDeDato());
            }
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




    //Devuelve una celda específica usando las etiquetas como referencia
    public Celda getCelda(String etiquetaFila, String etiquetaColumna) {
        int fila = mapaFilas.get(etiquetaFila);
        int columna = mapaColumnas.get(etiquetaColumna);
        return columnas.get(columna).getCelda(fila);
    }


    //Modifica el valor de una celda específica, accediendo por etiquetas
    public void setCelda(String etiquetaFila, String etiquetaColumna, Object valor) throws Columna.excepcionTipoDato {
        int fila = mapaFilas.get(etiquetaFila);
        int columna = mapaColumnas.get(etiquetaColumna);
        columnas.get(columna).setCelda(fila, valor);
    }


    //SELECCIONAR PREGUNTAR


    //devuelve una nueva tabla que contiene solo las primeras x filas de la tabla original.
    public Tabla head(int x) {
        int totalFilas = etiquetasFilas.size();
        int limite = Math.min(x, totalFilas);
        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();
        for (int i = 0; i < limite; i++) {
            String etiqueta = etiquetasFilas.get(i);
            nuevasEtiquetasFilas.add(etiqueta);
            nuevoMapaFilas.put(etiqueta, i);
        }
        List<Columna> nuevasColumnas = new ArrayList<>();
        for (Columna columna : columnas) {
            Columna nuevaColumna = columna.copiarFilas(limite);
            nuevasColumnas.add(nuevaColumna);
        }
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);
        return new Tabla(this.nombreTabla + "_head", nuevasColumnas, nuevasEtiquetasFilas, nuevasEtiquetasColumnas, nuevoMapaFilas, nuevoMapaColumnas);
    }

}
