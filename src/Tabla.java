import java.util.function.Predicate;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.List;
import java.util.Map;

public class Tabla implements Manipular, NAs {
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

    // Devuelve la cantidad de columnas
    public int getCantidadColumnas() {
        return columnas.size();
    }

    // Devuelve copia(para no modificar el original) de las etiquetas de las filas
    public List<String> getEtiquetasFilas() {
        return new ArrayList<>(etiquetasFilas);
    }

    // Devuelve copia de las etiquetas de las columnas
    public List<String> getEtiquetasColumnas() {
        return new ArrayList<>(etiquetasColumnas);
    }

    // Devuelve copia de la lista de columnas
    public List<Columna> getColumnas()
    {
        return new ArrayList<>(columnas);
    }

    public int getCantidadFilas() {
        if (columnas == null || columnas.isEmpty()) return 0;
        return columnas.get(0).getCeldas().size();
    }

    // Devuelve tipos de datos de columnas dadas (o todas si no se pasa lista/null)
    public List<TipoDato> getTiposColumnas(List<String> etiquetas) throws ExcepcionesTabla.ExcepcionColumnaNoEncontrada{
        List<TipoDato> tipos = new ArrayList<>();
        if (etiquetas == null || etiquetas.isEmpty()) {
            // Si no se pasan etiquetas, devuelve los tipos de todas las columnas
            for (Columna columna : columnas) {
                tipos.add(columna.getTipoDeDato());
            }
        } else {
            // Si se pasan etiquetas, busca el tipo por cada una
            for (String etiqueta : etiquetas) {
                Integer index = mapaColumnas.get(etiqueta);
                if (index == null)
                    throw new ExcepcionesTabla.ExcepcionColumnaNoEncontrada(etiqueta);
                tipos.add(columnas.get(index).getTipoDeDato());
            }
        }
        return tipos;
    }

    // Devuelve una columna completa (lista de valores) a partir de su etiqueta
    public List<Celda> obtenerColumna(String etiqueta) throws ExcepcionesTabla.ExcepcionColumnaNoEncontrada {
        Integer indice = mapaColumnas.get(etiqueta); //Obtiene el indice de la columna con mapaColumnas
        if (indice == null) {
            throw new ExcepcionesTabla.ExcepcionColumnaNoEncontrada(etiqueta);
        }
        return columnas.get(indice).obtenerCeldas();
    }

    @Override
    public List<Object> obtenerFila(String etiqueta) throws ExcepcionesTabla.ExcepcionFilaNoEncontrada {
        if (!mapaFilas.containsKey(etiqueta)) {
            throw new ExcepcionesTabla.ExcepcionFilaNoEncontrada(etiqueta);
        }
        int indexFila = mapaFilas.get(etiqueta);
        List<Object> fila = new ArrayList<>();
        for (Columna col : columnas) {
            fila.add(col.getValor(indexFila));
        }
        return fila;
    }

    @Override
    public void eliminarFila(String etiqueta) throws ExcepcionesTabla.ExcepcionFilaNoEncontrada {
        if (!mapaFilas.containsKey(etiqueta)) {
            throw new ExcepcionesTabla.ExcepcionFilaNoEncontrada(etiqueta);
        }
        int indexFila = mapaFilas.get(etiqueta);

        for (Columna col : columnas) {
            try {
                col.eliminarFila(indexFila);
            } catch (ExcepcionesTabla.ExcepcionIndiceInvalido e) {
                throw new RuntimeException("Error al eliminar fila en columna: " + col.getNombre(), e);
            }
        }
            etiquetasFilas.remove(indexFila);
        mapaFilas.clear();
        for (int i = 0; i < etiquetasFilas.size(); i++) {
            mapaFilas.put(etiquetasFilas.get(i), i);
        }
    }

    @Override
    public void insertarColumna(int indice, List<String> nuevaColumna) throws ExcepcionesTabla.ExcepcionLongitudColumna {
        if (!columnas.isEmpty() && nuevaColumna.size() != columnas.get(0).getCantidadFilas()) {
            throw new ExcepcionesTabla.ExcepcionLongitudColumna("ColumnaNueva");
        }
        List<Celda> celdas = new ArrayList<>();
        for (String val : nuevaColumna) {
            celdas.add(new Celda(val));
        }

        Columna nueva = new Columna("ColumnaNueva", TipoDato.CADENA, celdas); // o definir TipoDato dinámicamente
        columnas.add(indice, nueva);
        etiquetasColumnas.add(indice, "ColumnaNueva");

        mapaColumnas.clear();
        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            mapaColumnas.put(etiquetasColumnas.get(i), i);
        }
    }

    @Override
    public void eliminarColumna(int indice) {
        columnas.remove(indice);
        etiquetasColumnas.remove(indice);

        mapaColumnas.clear();
        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            mapaColumnas.put(etiquetasColumnas.get(i), i);
        }
    }


    // Devuelve una celda específica dada la etiqueta de fila y columna
    public Celda getCelda(String etiquetaFila, String etiquetaColumna) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        int fila = mapaFilas.get(etiquetaFila);
        int columna = mapaColumnas.get(etiquetaColumna);
        return columnas.get(columna).getCelda(fila);
    }

    // Modifica el valor de una celda específica (accedida por etiquetas)
    public void setCelda(String etiquetaFila, String etiquetaColumna, Object valor) throws ExcepcionesTabla.ExcepcionTipoDato, ExcepcionesTabla.ExcepcionIndiceInvalido {
        int fila = mapaFilas.get(etiquetaFila);
        int columna = mapaColumnas.get(etiquetaColumna);
        columnas.get(columna).setCelda(fila, valor);
    }

    //SELECCIONAR PREGUNTAR

    // Devuelve una nueva tabla con solo las primeras x filas
    public Tabla head(int x) {
        int totalFilas = etiquetasFilas.size();
        int limite = Math.min(x, totalFilas);
        // Se copian las etiquetas e índices de las primeras filas
        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();
        for (int i = 0; i < limite; i++) {
            String etiqueta = etiquetasFilas.get(i);
            nuevasEtiquetasFilas.add(etiqueta);
            nuevoMapaFilas.put(etiqueta, i);
        }
        // Se copian solo las filas necesarias de cada columna
        List<Columna> nuevasColumnas = new ArrayList<>();
        for (Columna columna : columnas) {
            Columna nuevaColumna = columna.copiarPrimerasFilas(limite);
            nuevasColumnas.add(nuevaColumna);
        }
        // Se reutilizan etiquetas y mapa de columnas
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);
        return new Tabla(this.nombreTabla + "_head", nuevasColumnas, nuevasEtiquetasFilas, nuevasEtiquetasColumnas, nuevoMapaFilas, nuevoMapaColumnas);
    }

    public Tabla tail(int x) {
        int totalFilas = etiquetasFilas.size(); // Total de filas actuales en la tabla

        // Calcula desde qué índice empezar a copiar las filas (las últimas 'x')
        int desde = Math.max(0, totalFilas - x);

        // Limita la cantidad de filas a copiar (por si 'x' es mayor que el total)
        int limite = Math.min(x, totalFilas);

        // Crear nuevas etiquetas para las filas seleccionadas
        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();

        // Copiamos las etiquetas y actualizamos el nuevo índice (ajustado desde 0)
        for (int i = desde; i < totalFilas; i++) {
            String etiqueta = etiquetasFilas.get(i); // Obtener etiqueta original
            nuevasEtiquetasFilas.add(etiqueta);      // Agregarla a la nueva lista
            nuevoMapaFilas.put(etiqueta, i - desde); // Reindexar para la nueva tabla
        }

        // Crear nuevas columnas copiando solo las últimas 'limite' filas
        List<Columna> nuevasColumnas = new ArrayList<>();
        for (Columna columna : columnas) {
            // Usamos el método de Columna que copia las últimas filas
            nuevasColumnas.add(columna.copiarUltimasFilas(limite));
        }

        // Reutilizamos las etiquetas y mapa de columnas (no cambian)
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);

        // Crear y devolver una nueva tabla con las últimas 'x' filas
        return new Tabla(this.nombreTabla + "_tail", nuevasColumnas, nuevasEtiquetasFilas, nuevasEtiquetasColumnas, nuevoMapaFilas, nuevoMapaColumnas);
    }

    // Crea una nueva tabla uniendo verticalmente las filas de esta tabla con las de otra tabla, siempre que ambas
    // tengan las mismas columnas
    public Tabla concatenar(Tabla otra) throws ExcepcionesTabla.ExcepcionColumnasIncompatibles {
        // Validar que tengan la misma cantidad de columnas
        if (this.columnas.size() != otra.getCantidadColumnas()) {
            throw new ExcepcionesTabla.ExcepcionColumnasIncompatibles(this.columnas.size() + " vs " +
                    otra.getCantidadColumnas());
        }

        // Validar que nombres y tipos de columnas coincidan
        for (int i = 0; i < columnas.size(); i++) {
            Columna col1 = this.columnas.get(i);
            Columna col2 = otra.getColumnas().get(i);

            if (!col1.getNombre().equals(col2.getNombre()) ||
                    col1.getTipoDeDato() != col2.getTipoDeDato()) {
                throw new ExcepcionesTabla.ExcepcionColumnasIncompatibles(
                        String.format("Columna %d: '%s' (%s) vs '%s' (%s)",
                                i, col1.getNombre(), col1.getTipoDeDato(),
                                col2.getNombre(), col2.getTipoDeDato()));
            }
        }

        // Crear nuevas listas
        List<Columna> nuevasColumnas = new ArrayList<>();
        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();

        // Combinar etiquetas de filas
        nuevasEtiquetasFilas.addAll(this.etiquetasFilas);
        nuevasEtiquetasFilas.addAll(otra.getEtiquetasFilas());

        for (int i = 0; i < nuevasEtiquetasFilas.size(); i++) {
            nuevoMapaFilas.put(nuevasEtiquetasFilas.get(i), i);
        }

        // Concatenar columnas
        for (int i = 0; i < columnas.size(); i++) {
            Columna col1 = this.columnas.get(i);
            Columna col2 = otra.getColumnas().get(i);

            List<Celda> celdasConcatenadas = new ArrayList<>();
            celdasConcatenadas.addAll(col1.obtenerCeldas());
            celdasConcatenadas.addAll(col2.obtenerCeldas());

            // Crear nueva columna con las celdas
            Columna nuevaColumna = new Columna(col1.getNombre(), col1.getTipoDeDato(), celdasConcatenadas);
            nuevasColumnas.add(nuevaColumna);
        }

        // Las columnas y su mapa se mantienen
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);

        // Crear nueva tabla
        return new Tabla(
                this.nombreTabla + "_concatenada",
                nuevasColumnas,
                nuevasEtiquetasFilas,
                nuevasEtiquetasColumnas,
                nuevoMapaFilas,
                nuevoMapaColumnas
        );
    }

    // Recibe el nombre de la columna a filtrar y un predicado (condición) para filtrar los valores
    // Devuelve una nueva tabla con solo las filas donde la condición se cumple en esa columna.
    public Tabla filtrarColumnas(String nombreColumna, Predicate<Object> condicion)
            throws ExcepcionesTabla.ExcepcionColumnaNoEncontrada, ExcepcionesTabla.ExcepcionTipoDato {

        // Buscar índice de la columna que se va a filtrar
        Integer indiceColumna = mapaColumnas.get(nombreColumna);
        if (indiceColumna == null) {
            throw new ExcepcionesTabla.ExcepcionColumnaNoEncontrada(nombreColumna);
        }

        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();
        List<Columna> nuevasColumnas = new ArrayList<>();

        // Crear columnas vacías correspondientes a cada columna original
        for (Columna columna : columnas) {
            nuevasColumnas.add(new Columna(columna.getNombre(), columna.getTipoDeDato(), new ArrayList<>()));
        }

        int nuevoIndiceFila = 0;
        for (int i = 0; i < etiquetasFilas.size(); i++) {
            Object valor = columnas.get(indiceColumna).getValor(i);

            if (condicion.test(valor)) {
                String etiquetaFila = etiquetasFilas.get(i);
                nuevasEtiquetasFilas.add(etiquetaFila);
                nuevoMapaFilas.put(etiquetaFila, nuevoIndiceFila);

                for (int j = 0; j < columnas.size(); j++) {
                    Columna colOriginal = columnas.get(j);
                    Columna colNueva = nuevasColumnas.get(j);
                    colNueva.agregarCelda(colOriginal.getValor(i)); // Puede lanzar ExcepcionTipoDato
                }

                nuevoIndiceFila++;
            }
        }

        // Las etiquetas de columnas y mapa de columnas se mantienen
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);

        return new Tabla(
                this.nombreTabla + "_filtrado",
                nuevasColumnas,
                nuevasEtiquetasFilas,
                nuevasEtiquetasColumnas,
                nuevoMapaFilas,
                nuevoMapaColumnas
        );
    }

    // Recibe un Predicate<List<Object>> que representa la condición aplicada a toda la fila (lista de valores de esa fila).
    // Devuelve una nueva Tabla con solo las filas que cumplen esa condición.
    public Tabla filtrarFilas(Predicate<List<Object>> condicion) {
        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();
        List<Columna> nuevasColumnas = new ArrayList<>();

        // Inicializar nuevas columnas vacías (con mismo nombre y tipo)
        for (Columna columna : columnas) {
            // Cambié columna.getTipo() por columna.getTipoDeDato() para coincidir con tu diseño
            nuevasColumnas.add(new Columna(columna.getNombre(), columna.getTipoDeDato(), new ArrayList<>()));
        }

        int nuevoIndiceFila = 0;
        int totalFilas = etiquetasFilas.size();

        for (int i = 0; i < totalFilas; i++) {
            // Construir la fila i como lista de valores
            List<Object> filaValores = new ArrayList<>();
            for (Columna columna : columnas) {
                filaValores.add(columna.getValor(i));
            }

            // Evaluar la condición para esa fila completa
            if (condicion.test(filaValores)) {
                String etiquetaFila = etiquetasFilas.get(i);
                nuevasEtiquetasFilas.add(etiquetaFila);
                nuevoMapaFilas.put(etiquetaFila, nuevoIndiceFila);

                // Copiar las celdas correspondientes de cada columna
                for (int j = 0; j < columnas.size(); j++) {
                    Columna colOriginal = columnas.get(j);
                    Columna colNueva = nuevasColumnas.get(j);
                    try {
                        colNueva.agregarCelda(colOriginal.getValor(i));
                    } catch (ExcepcionesTabla.ExcepcionTipoDato e) {
                        System.out.println("Error al agregar celda en filtrarFilas: " + e.getMessage());
                    }
                }
                nuevoIndiceFila++;
            }
        }

        // Las etiquetas y mapa de columnas se mantienen igual
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);

        return new Tabla(this.nombreTabla + "_filtradoFilas", nuevasColumnas, nuevasEtiquetasFilas, nuevasEtiquetasColumnas, nuevoMapaFilas, nuevoMapaColumnas);
    }

    // Crea una nueva tabla con los mismos datos (columnas, filas, etiquetas, mapas), pero nuevas instancias
    // (no referencias a las listas originales), para no modificar el objeto original cuando haces operaciones que generan nuevas tablas.
    public Tabla copiar() {
        // Copiar etiquetas filas y mapa filas
        List<String> nuevasEtiquetasFilas = new ArrayList<>(this.etiquetasFilas);
        Map<String, Integer> nuevoMapaFilas = new HashMap<>(this.mapaFilas);

        // Copiar etiquetas columnas y mapa columnas
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(this.etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(this.mapaColumnas);

        // Copiar columnas (copiar todas las filas de cada columna)
        List<Columna> nuevasColumnas = new ArrayList<>();
        for (Columna columna : this.columnas) {
            // Podrías usar copiarPrimerasFilas con el tamaño completo de filas
            nuevasColumnas.add(columna.copiarPrimerasFilas(columna.getCantidadFilas()));
        }
        return new Tabla(this.nombreTabla + "_copia", nuevasColumnas, nuevasEtiquetasFilas, nuevasEtiquetasColumnas, nuevoMapaFilas, nuevoMapaColumnas);
    }

    // Devuelve una nueva tabla con n filas elegidas al azar (sin reemplazo) de la tabla original.
    // Usa la clase Columna para copiar solo las filas correspondientes a las filas seleccionadas.
    public Tabla muestreoAleatorio(int n) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        int totalFilas = etiquetasFilas.size();
        if (n <= 0) {
            throw new IllegalArgumentException("El número de filas a muestrear debe ser positivo.");
        }
        if (n > totalFilas) {
            throw new IllegalArgumentException("El número de filas a muestrear no puede ser mayor que el total de filas.");
        }

        // Crear lista con todos los índices de filas (0 a totalFilas-1)
        List<Integer> indicesFilas = new ArrayList<>();
        for (int i = 0; i < totalFilas; i++) {
            indicesFilas.add(i);
        }

        // Mezclar aleatoriamente la lista de índices
        Collections.shuffle(indicesFilas, new Random());

        // Tomar los primeros n índices para el muestreo
        List<Integer> indicesSeleccionados = indicesFilas.subList(0, n);

        // Ordenar para mantener el orden original de las filas seleccionadas
        Collections.sort(indicesSeleccionados);

        // Construir nuevas etiquetas de filas y mapa de filas con las seleccionadas
        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();
        for (int i = 0; i < indicesSeleccionados.size(); i++) {
            int indiceOriginal = indicesSeleccionados.get(i);
            String etiqueta = etiquetasFilas.get(indiceOriginal);
            nuevasEtiquetasFilas.add(etiqueta);
            nuevoMapaFilas.put(etiqueta, i);
        }

        // Crear nuevas columnas copiando solo las filas seleccionadas
        List<Columna> nuevasColumnas = new ArrayList<>();
        for (Columna columna : columnas) {
            nuevasColumnas.add(columna.copiarFilasPorIndices(indicesSeleccionados));
        }

        // Las etiquetas y mapa de columnas permanecen igual
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);

        // Devolver nueva tabla con muestreo aleatorio
        return new Tabla(
                this.nombreTabla + "_muestreoAleatorio",
                nuevasColumnas,
                nuevasEtiquetasFilas,
                nuevasEtiquetasColumnas,
                nuevoMapaFilas,
                nuevoMapaColumnas
        );
    }

    // Imprime por consola una representación tabular simple, mostrando las etiquetas de columnas arriba y
    // las filas con sus etiquetas a la izquierda
    public void mostrar() {
        // Imprimir encabezado con etiquetas de columnas
        System.out.print("Fila\\Col\t"); // espacio para etiqueta filas
        for (String etiquetaCol : etiquetasColumnas) {
            System.out.print(etiquetaCol + "\t");
        }
        System.out.println();

        int totalFilas = etiquetasFilas.size();
        int totalColumnas = columnas.size();

        // Imprimir filas con sus etiquetas y valores
        for (int i = 0; i < totalFilas; i++) {
            System.out.print(etiquetasFilas.get(i) + "\t"); // etiqueta fila
            for (int j = 0; j < totalColumnas; j++) {
                Object valor = columnas.get(j).getValor(i);
                System.out.print(valor + "\t");
            }
            System.out.println();

        }
    }

    @Override
    public void leerNAs(Tabla tabla) {
        for (Columna col : tabla.columnas) {
            int count = 0;
            for (int i = 0; i < col.getCantidadFilas(); i++) {
                Object val = col.getValor(i);
                if (val == null || val.equals("NA")) {
                    count++;
                }
            }
            System.out.println("Columna " + col.getNombre() + " tiene " + count + " valores NA");
        }
    }

    @Override
    public void mostrarNAs(Tabla tabla) {
        for (int i = 0; i < etiquetasFilas.size(); i++) {
            for (Columna col : tabla.columnas) {
                Object val = col.getValor(i);
                if (val == null || val.equals("NA")) {
                    System.out.println("NA en fila " + etiquetasFilas.get(i) + ", columna " + col.getNombre());
                }
            }
        }
    }

    @Override
    public void reemplazarNAs(Tabla tabla, Object valor) throws ExcepcionesTabla.ExcepcionTipoDato {
        for (Columna col : tabla.columnas) {
            if (!col.esValorValido(valor)) {
                throw new ExcepcionesTabla.ExcepcionTipoDato(col.getTipoDeDato(), valor);
            }
            for (int i = 0; i < col.getCantidadFilas(); i++) {
                Object val = col.getValor(i);
                if (val == null || val.equals("NA")) {
                    try {
                        col.setCelda(i, valor);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Tabla seleccionarColumnas(List<String> nombresColumnas) throws ExcepcionesTabla.ExcepcionColumnaInexistente, ExcepcionesTabla.ExcepcionTipoDato {
        List<Columna> nuevasColumnas = new ArrayList<>();
        List<String> nuevasEtiquetasColumnas = new ArrayList<>();

        for (String nombre : nombresColumnas) {
            Columna colEncontrada = null;
            for (Columna c : columnas) {
                if (c.getNombre().equals(nombre)) {
                    colEncontrada = c;
                    break;
                }
            }
            if (colEncontrada == null) {
                throw new ExcepcionesTabla.ExcepcionColumnaInexistente(nombre);
            }
            // Copiar columna completa (nombre, tipo y celdas)
            Columna copiaCol = new Columna(colEncontrada.getNombre(), colEncontrada.getTipoDeDato(), colEncontrada.obtenerCeldas());
            nuevasColumnas.add(copiaCol);
            nuevasEtiquetasColumnas.add(nombre);
        }

        // Mapa filas queda igual
        Map<String, Integer> nuevoMapaFilas = new HashMap<>(this.mapaFilas);

        // Crear mapaColumnas nuevo para las columnas seleccionadas
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>();
        for (int i = 0; i < nuevasEtiquetasColumnas.size(); i++) {
            nuevoMapaColumnas.put(nuevasEtiquetasColumnas.get(i), i);
        }

        // Crear y devolver nueva tabla
        return new Tabla(this.nombreTabla, nuevasColumnas, new ArrayList<>(this.etiquetasFilas), nuevasEtiquetasColumnas, nuevoMapaFilas, nuevoMapaColumnas);
    }

    private void actualizarMapaFilas() {
        mapaFilas.clear();
        for (int i = 0; i < etiquetasFilas.size(); i++) {
            mapaFilas.put(etiquetasFilas.get(i), i);
        }
    }

    private void actualizarMapaColumnas() {
        mapaColumnas.clear();
        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            mapaColumnas.put(etiquetasColumnas.get(i), i);
        }
    }


}