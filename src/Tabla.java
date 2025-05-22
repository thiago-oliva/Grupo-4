import java.util.function.Predicate;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
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
    public List<Columna> getColumnas() {
        return new ArrayList<>(columnas);
    }

    // Devuelve tipos de datos de columnas dadas (o todas si no se pasa lista/null)
    public List<Columna.TipoDato> getTiposColumnas(List<String> etiquetas) {
        List<Columna.TipoDato> tipos = new ArrayList<>();
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
                    throw new IllegalArgumentException("Columna no encontrada: " + etiqueta);
                tipos.add(columnas.get(index).getTipoDeDato());
            }
        }
        return tipos;
    }

    // Devuelve una columna completa (lista de valores) a partir de su etiqueta
    public List<Celda> obtenerColumna(String etiqueta) {
        Integer indice = mapaColumnas.get(etiqueta); //Obtiene el indice de la columna con mapaColumnas
        if (indice == null) {
            throw new IllegalArgumentException("No existe una columna con etiqueta: " + etiqueta);
        }
        return columnas.get(indice).obtenerCeldas();
    }

    // Devuelve una fila completa (lista de valores) a partir de su etiqueta
    public List<Object> obtenerFila(String etiqueta) {
        Integer filaIndex = mapaFilas.get(etiqueta);
        List<Object> fila = new ArrayList<>();
        for (Columna col : columnas) {
            fila.add(col.getValor(filaIndex));
        }
        return fila;
    }

    // Devuelve una celda específica dada la etiqueta de fila y columna
    public Celda getCelda(String etiquetaFila, String etiquetaColumna) {
        int fila = mapaFilas.get(etiquetaFila);
        int columna = mapaColumnas.get(etiquetaColumna);
        return columnas.get(columna).getCelda(fila);
    }

    // Modifica el valor de una celda específica (accedida por etiquetas)
    public void setCelda(String etiquetaFila, String etiquetaColumna, Object valor) throws Columna.excepcionTipoDato {
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

    public Tabla concatenar(Tabla otra) {
        // Validar que tengan las mismas columnas y tipos (simplificado)
        if (this.columnas.size() != otra.getCantidadColumnas()) {
            throw new IllegalArgumentException("Las tablas tienen diferente cantidad de columnas");
        }
        for (int i = 0; i < columnas.size(); i++) {
            if (!this.columnas.get(i).getNombre().equals(otra.getColumnas().get(i).getNombre()) ||
                    this.columnas.get(i).getTipo() != otra.getColumnas().get(i).getTipo()) {
                throw new IllegalArgumentException("Las columnas no coinciden en nombre o tipo");
            }
        }

        // Nuevas listas para construir la tabla resultante
        List<Columna> nuevasColumnas = new ArrayList<>();
        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();

        // Construir nuevas etiquetas filas y mapa filas (concatenadas)
        nuevasEtiquetasFilas.addAll(this.etiquetasFilas);
        nuevasEtiquetasFilas.addAll(otra.getEtiquetasFilas());
        for (int i = 0; i < nuevasEtiquetasFilas.size(); i++) {
            nuevoMapaFilas.put(nuevasEtiquetasFilas.get(i), i);
        }

        // Para cada columna, concatenar las celdas de ambas tablas
        for (int i = 0; i < columnas.size(); i++) {
            Columna colThis = this.columnas.get(i);
            Columna colOtra = otra.getColumnas().get(i);

            List<Celda> celdasConcatenadas = new ArrayList<>();
            celdasConcatenadas.addAll(colThis.obtenerCeldas());
            celdasConcatenadas.addAll(colOtra.obtenerCeldas());

            // Crear nueva columna con las celdas concatenadas
            Columna nuevaColumna = new Columna(colThis.getNombre(), colThis.getTipo(), new ArrayList<>());

            // Agregar celdas usando try-catch para manejar excepciones
            for (Celda celda : celdasConcatenadas) {
                try {
                    nuevaColumna.agregarCelda(celda.getValor());
                } catch (Columna.excepcionTipoDato e) {
                    System.out.println("Error al agregar celda en concatenar: " + e.getMessage());
                }
            }
            nuevasColumnas.add(nuevaColumna);
        }

        // Las columnas y su mapa se mantienen igual (mismo orden y nombres)
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);

        return new Tabla(this.nombreTabla + "_concatenada", nuevasColumnas, nuevasEtiquetasFilas, nuevasEtiquetasColumnas, nuevoMapaFilas, nuevoMapaColumnas);
    }

    // Recibe el nombre de la columna a filtrar y un predicado (condición) para filtrar los valores
    // Devuelve una nueva tabla con solo las filas donde la condición se cumple en esa columna.
    public Tabla filtrarColumna(String nombreColumna, Predicate<Object> condicion) {
        // Buscar índice de la columna que se va a filtrar
        Integer indiceColumna = mapaColumnas.get(nombreColumna);
        if (indiceColumna == null) {
            throw new IllegalArgumentException("La columna " + nombreColumna + " no existe.");
        }

        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();
        List<Columna> nuevasColumnas = new ArrayList<>();

        // Construye nuevas listas de celdas para cada columna, pero solo con filas que cumplen la condicion

        // Primero obtenemos las columnas originales para recorrerlas por índice
        for (Columna columna : columnas) {
            // Creamos listas vacías para las celdas filtradas
            nuevasColumnas.add(new Columna(columna.getNombre(), columna.getTipo(), new ArrayList<>()));
        }

        // Iterar sobre cada fila
        int nuevoIndiceFila = 0;
        for (int i = 0; i < etiquetasFilas.size(); i++) {
            // Obtenemos el valor de la columna a filtrar en la fila i
            Object valor = columnas.get(indiceColumna).getValor(i);
            if (condicion.test(valor)) {  // Si cumple la condición
                // Añadir etiqueta y mapa
                String etiquetaFila = etiquetasFilas.get(i);
                nuevasEtiquetasFilas.add(etiquetaFila);
                nuevoMapaFilas.put(etiquetaFila, nuevoIndiceFila);

                // Agregar la celda correspondiente de cada columna para esta fila
                for (int j = 0; j < columnas.size(); j++) {
                    Columna colOriginal = columnas.get(j);
                    Columna colNueva = nuevasColumnas.get(j);
                    try {
                        colNueva.agregarCelda(colOriginal.getValor(i));
                    } catch (Columna.excepcionTipoDato e) {
                        // Esto no debería pasar si el tipo es correcto, pero lo capturamos por si acaso
                        System.out.println("Error al agregar celda en filtrarColumna: " + e.getMessage());
                    }
                }
                nuevoIndiceFila++;
            }
        }
        // Las etiquetas y mapa de columnas no cambian
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);

        return new Tabla(this.nombreTabla + "_filtrado", nuevasColumnas, nuevasEtiquetasFilas, nuevasEtiquetasColumnas, nuevoMapaFilas, nuevoMapaColumnas);
    }

    // Recibe un Predicate<List<Object>> que representa la condición aplicada a toda la fila (lista de valores de esa fila).
    // Devuelve una nueva Tabla con solo las filas que cumplen esa condición.
    public Tabla filtrarFilas(Predicate<List<Object>> condicion) {
        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();
        List<Columna> nuevasColumnas = new ArrayList<>();

        // Inicializar nuevas columnas vacías (con mismo nombre y tipo)
        for (Columna columna : columnas) {
            nuevasColumnas.add(new Columna(columna.getNombre(), columna.getTipo(), new ArrayList<>()));
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
                    } catch (Columna.excepcionTipoDato e) {
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
    public Tabla muestreoAleatorio(int n) {
        int totalFilas = etiquetasFilas.size();

        if (n <= 0) {
            throw new IllegalArgumentException("El número de filas a muestrear debe ser positivo.");
        }
        if (n > totalFilas) {
            throw new IllegalArgumentException("El número de filas a muestrear no puede ser mayor que el total de filas.");
        }

        // Crear una lista con todos los índices de filas
        List<Integer> indicesFilas = new ArrayList<>();
        for (int i = 0; i < totalFilas; i++) {
            indicesFilas.add(i);
        }

        // Mezclar aleatoriamente
        Collections.shuffle(indicesFilas, new Random());

        // Tomar solo las primeras n posiciones
        List<Integer> indicesSeleccionados = indicesFilas.subList(0, n);

        // Ordenar para mantener el orden original de filas
        Collections.sort(indicesSeleccionados);

        // Crear nuevas etiquetas de filas y mapa filas
        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();
        for (int i = 0; i < indicesSeleccionados.size(); i++) {
            int indiceOriginal = indicesSeleccionados.get(i);
            String etiqueta = etiquetasFilas.get(indiceOriginal);
            nuevasEtiquetasFilas.add(etiqueta);
            nuevoMapaFilas.put(etiqueta, i);
        }

        // Copiar columnas con las filas seleccionadas
        List<Columna> nuevasColumnas = new ArrayList<>();
        for (Columna columna : columnas) {
            nuevasColumnas.add(columna.copiarFilasPorIndices(indicesSeleccionados));
        }

        // Reutilizar etiquetas y mapa columnas
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);

        return new Tabla(this.nombreTabla + "_muestreoAleatorio", nuevasColumnas, nuevasEtiquetasFilas, nuevasEtiquetasColumnas, nuevoMapaFilas, nuevoMapaColumnas);
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
}