import java.util.*;

public class Main {
    public static void main(String[] args) throws ExcepcionesTabla.ExcepcionTipoDato, ExcepcionesTabla.ExcepcionColumnaNoEncontrada, ExcepcionesTabla.ExcepcionIndiceInvalido, ExcepcionesTabla.ExcepcionColumnasIncompatibles {

        List<Celda> Nombre = Arrays.asList(new Celda("fran"), new Celda("dante"), new Celda("thiago"));
        List<Celda> Edad = Arrays.asList(new Celda(20), new Celda(1), new Celda(20));

        Columna colNombre = new Columna("Nombre", TipoDato.CADENA, Nombre);
        Columna colEdad = new Columna("Edad", TipoDato.NUMERICO, Edad);

        List<Columna> columnas = Arrays.asList(colNombre, colEdad);
        List<String> etiquetasFilas = Arrays.asList("fila1", "fila2", "fila3");
        List<String> etiquetasColumnas = Arrays.asList("Nombre", "Edad");

        Map<String, Integer> mapaFilas = new HashMap<>();
        mapaFilas.put("fila1", 0);
        mapaFilas.put("fila2", 1);
        mapaFilas.put("fila3", 2);

        Map<String, Integer> mapaColumnas = new HashMap<>();
        mapaColumnas.put("Nombre", 0);
        mapaColumnas.put("Edad", 1);

        Tabla tabla = new Tabla("Personas", columnas, etiquetasFilas, etiquetasColumnas, mapaFilas, mapaColumnas);

        tabla.mostrar();

        tabla.head(2).mostrar();

        Tabla filtrada = tabla.filtrarColumna("Edad", valor -> (Integer) valor > 23);
        filtrada.mostrar();
    }
}
