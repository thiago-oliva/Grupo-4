import java.util.function.Predicate;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.List;
import java.util.Map;

public class Tabla implements Manipular, NAs {
    private String nombreTabla;
    private List<Columna<?>> columnas;
    private List<String> etiquetasFilas;
    private List<String> etiquetasColumnas;
    private Map<String, Integer> mapaFilas; // Cada nombre de lista (por ejemplo "Nombre" o "Edad") está asociado a su
                                            // posición (índice) en la lista filas
    private Map<String, Integer> mapaColumnas; //Igual que arriba
    private String nombreColumna;

    public Tabla(String nombreTabla, List<Columna<?>> columnas, List<String> etiquetasFilas, List<String>
            etiquetasColumnas, Map<String, Integer> mapaFilas, Map<String, Integer> mapaColumnas) {
        this.nombreTabla = nombreTabla;
        this.columnas = new ArrayList<>(columnas);
        this.etiquetasFilas = new ArrayList<>(etiquetasFilas);
        this.etiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        this.mapaFilas = new HashMap<>(mapaFilas);
        this.mapaColumnas = new HashMap<>(mapaColumnas);
    }

    public Map<String, Integer> getMapaColumnas() {
        return mapaColumnas;
    }

    public int getCantidadColumnas() {
        return columnas.size();
    }

    public List<String> getEtiquetasFilas() {
        return new ArrayList<>(etiquetasFilas); // Devuelve copia, para no modificar el original, de las etiquetas de las filas.
    }

    public List<Columna<?>> getColumnas() {
        return new ArrayList<>(columnas); // Devuelve copia de la lista de columnas
    }

    public int getCantidadFilas() {
        if (columnas == null || columnas.isEmpty()) return 0;
        return columnas.get(0).getCeldas().size();
    }

    public Tabla(Map<String, List<Celda<?>>> datos) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        columnas = new ArrayList<>();
        etiquetasColumnas = new ArrayList<>();
        etiquetasFilas = new ArrayList<>();
        mapaColumnas = new HashMap<>();
        mapaFilas = new HashMap<>();

        int index = 0;
        int cantidadFilas = -1; // Se determina una sola vez.

        for (Map.Entry<String, List<Celda<?>>> entry : datos.entrySet()) {
            String nombreColumna = entry.getKey();
            List<Celda<?>> celdasGenericas = entry.getValue();

            if (cantidadFilas == -1) {
                cantidadFilas = celdasGenericas.size(); // Solo la primera vez.
            }

            // Inferir el tipo de dato de la columna.
            boolean esNumerico = true;
            boolean esBooleano = true;

            for (Celda<?> celda : celdasGenericas) {
                Object valor = celda.getValor();
                if (valor == null) continue;
                String str = valor.toString().trim();

                if (!str.equalsIgnoreCase("true") && !str.equalsIgnoreCase("false")) {
                    esBooleano = false;
                }

                try {
                    Double.parseDouble(str);
                } catch (NumberFormatException e) {
                    esNumerico = false;
                }
            }

            // Estimar tipo según la lógica.
            TipoDato tipo;
            if (esBooleano) tipo = TipoDato.BOOLEANO;
            else if (esNumerico) tipo = TipoDato.NUMERICO;
            else tipo = TipoDato.CADENA;

            // Convertir List<Celda<?>> a List<Celda<Object>>.
            List<Celda<Object>> celdas = new ArrayList<>();
            for (Celda<?> celda : celdasGenericas) {
                celdas.add(new Celda<>((Object) celda.getValor()));
            }

            // Crear columna.
            Columna<Object> columna = new Columna<>(nombreColumna, tipo, celdas);
            columnas.add(columna);
            etiquetasColumnas.add(nombreColumna);
            mapaColumnas.put(nombreColumna, index++);
        }

        // Generar etiquetas de fila una sola vez.
        for (int i = 0; i < cantidadFilas; i++) {
            String etiqueta = "F" + i;
            etiquetasFilas.add(etiqueta);
            mapaFilas.put(etiqueta, i);
        }
    }

    // Devuelve tipos de datos de columnas dadas (o todas si no se pasa lista/null).
    public List<TipoDato> getTiposColumnas(List<String> etiquetas) throws ExcepcionesTabla.ExcepcionColumnaNoEncontrada{
        List<TipoDato> tipos = new ArrayList<>();
        if (etiquetas == null || etiquetas.isEmpty()) {
            // Si no se pasan etiquetas, devuelve los tipos de todas las columnas.
            for (Columna<?> columna : columnas) {
                tipos.add(columna.getTipoDeDato());
            }
        } else {
            // Si se pasan etiquetas, busca el tipo por cada una.
            for (String etiqueta : etiquetas) {
                Integer index = mapaColumnas.get(etiqueta);
                if (index == null)
                    throw new ExcepcionesTabla.ExcepcionColumnaNoEncontrada(etiqueta);
                tipos.add(columnas.get(index).getTipoDeDato());
            }
        }
        return tipos;
    }

    // Devuelve una columna completa (lista de valores) a partir de su etiqueta.
    public List<? extends Celda<?>> obtenerColumna(String etiqueta) throws ExcepcionesTabla.ExcepcionColumnaNoEncontrada {
        Integer indice = mapaColumnas.get(etiqueta);
        if (indice == null) throw new ExcepcionesTabla.ExcepcionColumnaNoEncontrada(etiqueta);
        return columnas.get(indice).obtenerCeldas();
    }

    // Devuelve la fila completa (lista de valores) a partir de su etiqueta.
    @Override
    public List<Object> obtenerFila(String etiqueta) throws ExcepcionesTabla.ExcepcionFilaNoEncontrada,
            ExcepcionesTabla.ExcepcionIndiceInvalido {
        if (!mapaFilas.containsKey(etiqueta)) {
            throw new ExcepcionesTabla.ExcepcionFilaNoEncontrada(etiqueta);
        }
        int indexFila = mapaFilas.get(etiqueta);
        List<Object> fila = new ArrayList<>();
        for (Columna<?> col : columnas) {  // Cambiado a <?>
            fila.add(col.getValor(indexFila));
        }
        return fila;
    }

    // Inserta una nueva fila en la tabla con los valores dados, validando tipos y actualizando etiquetas e índices.
    @Override
    public void insertarFila(List<Object> valores) throws ExcepcionesTabla.ExcepcionTipoDato {
        if (valores.size() != columnas.size()) {
            throw new IllegalArgumentException("La cantidad de valores no coincide con la cantidad de columnas.");
        }

        for (int i = 0; i < columnas.size(); i++) {
            Columna<?> columna = columnas.get(i);
            Object valor = valores.get(i);

            // Cast genérico para evitar problemas con el tipo.
            ((Columna<Object>) columna).agregarCelda(valor);
        }

        // Generar etiqueta única.
        int contador = 0;
        String nuevaEtiqueta;
        do {
            nuevaEtiqueta = "F" + contador++;
        } while (mapaFilas.containsKey(nuevaEtiqueta));

        etiquetasFilas.add(nuevaEtiqueta);
        mapaFilas.put(nuevaEtiqueta, etiquetasFilas.size() - 1);
    }

    // Elimina una fila dada su etiqueta, actualizando los índices de las filas restantes.
    @Override
    public void eliminarFila(String etiqueta) throws ExcepcionesTabla.ExcepcionFilaNoEncontrada {
        if (!mapaFilas.containsKey(etiqueta)) {
            throw new ExcepcionesTabla.ExcepcionFilaNoEncontrada(etiqueta);
        }
        int indexFila = mapaFilas.get(etiqueta);

        for (Columna<?> col : columnas) {
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

    // Inserta una nueva columna en la posición indicada, con los valores de la lista 'nuevaColumna'.
    @Override
    public void insertarColumna(int indice, String nombreColumna,List<String> nuevaColumna) throws
            ExcepcionesTabla.ExcepcionLongitudColumna {
        if (!columnas.isEmpty() && nuevaColumna.size() != columnas.get(0).getCantidadFilas()) {
            throw new ExcepcionesTabla.ExcepcionLongitudColumna(nombreColumna);
        }
        List<Celda<String>> celdas = new ArrayList<>();
        for (String val : nuevaColumna) {
            celdas.add(new Celda<>(val)); // Celda<String>
        }

        Columna<String> nueva = new Columna<>(nombreColumna, TipoDato.CADENA, celdas);
        columnas.add(indice, nueva);
        etiquetasColumnas.add(indice, nombreColumna);

        mapaColumnas.clear();
        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            mapaColumnas.put(etiquetasColumnas.get(i), i);
        }
    }

    // Elimina una columna en la posición indicada, actualizando los índices de las columnas restantes.
    @Override
    public void eliminarColumna(int indice) {
        columnas.remove(indice);
        etiquetasColumnas.remove(indice);

        mapaColumnas.clear();
        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            mapaColumnas.put(etiquetasColumnas.get(i), i);
        }
    }

    public List<List<Celda<?>>> seleccionar(List<String> nombresColumnas, List<Integer> indicesFilas)
    throws ExcepcionesTabla.ExcepcionColumnaNoEncontrada, ExcepcionesTabla.ExcepcionIndiceInvalido {

        List<List<Celda<?>>> vista = new ArrayList<>();
        List<Columna<?>> columnasSeleccionadas = new ArrayList<>();

        for (String nombre : nombresColumnas) {
            boolean encontrada = false;
            for (Columna<?> col : columnas) {
                if (col.getNombre().equals(nombre)) {
                    columnasSeleccionadas.add(col);
                    encontrada = true;
                    break;
                }
            }
            if (!encontrada) {
                throw new ExcepcionesTabla.ExcepcionColumnaNoEncontrada(nombre);
            }
        }

        int cantidadFilas = columnas.get(0).getCeldas().size();

        for (Integer filaIndex : indicesFilas) {
            if (filaIndex < 0 || filaIndex >= cantidadFilas) {
                throw new ExcepcionesTabla.ExcepcionIndiceInvalido(filaIndex);
            }

            List<Celda<?>> filaVista = new ArrayList<>();
            for (Columna<?> col : columnasSeleccionadas) {
                filaVista.add(col.getCeldas().get(filaIndex));
            }
            vista.add(filaVista);
        }

        return vista;
    }

    // Devuelve una nueva tabla con solo las primeras x filas.
    public Tabla head(int x) {
        int totalFilas = etiquetasFilas.size();
        int limite = Math.min(x, totalFilas);
        // Se copian las etiquetas e índices de las primeras filas.
        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();
        for (int i = 0; i < limite; i++) {
            String etiqueta = etiquetasFilas.get(i);
            nuevasEtiquetasFilas.add(etiqueta);
            nuevoMapaFilas.put(etiqueta, i);
        }
        // Se copian solo las filas necesarias de cada columna.
        List<Columna<?>> nuevasColumnas = new ArrayList<>();
        for (Columna<?> columna : columnas) {
            Columna<?> nuevaColumna = columna.copiarPrimerasFilas(limite);
            nuevasColumnas.add(nuevaColumna);
        }
        // Se reutilizan etiquetas y mapa de columnas.
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);
        return new Tabla(this.nombreTabla + "_head", nuevasColumnas, nuevasEtiquetasFilas,
                nuevasEtiquetasColumnas, nuevoMapaFilas, nuevoMapaColumnas);
    }

    // Devuelve una nueva tabla con las últimas x filas.
    public Tabla tail(int x) {
        int totalFilas = etiquetasFilas.size(); // Total de filas actuales en la tabla.

        // Calcula desde qué índice empezar a copiar las filas (las últimas 'x').
        int desde = Math.max(0, totalFilas - x);

        // Limita la cantidad de filas a copiar (por si 'x' es mayor que el total).
        int limite = Math.min(x, totalFilas);

        // Crear nuevas etiquetas para las filas seleccionadas.
        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();

        // Copiamos las etiquetas y actualizamos el nuevo índice (ajustado desde 0).
        for (int i = desde; i < totalFilas; i++) {
            String etiqueta = etiquetasFilas.get(i); // Obtener etiqueta original.
            nuevasEtiquetasFilas.add(etiqueta);      // Agregarla a la nueva lista.
            nuevoMapaFilas.put(etiqueta, i - desde); // Reindexar para la nueva tabla.
        }

        // Crea nuevas columnas copiando solo las últimas 'limite' filas.
        List<Columna<?>> nuevasColumnas = new ArrayList<>();
        for (Columna<?> columna : columnas) {
            // Usamos el método de Columna que copia las últimas filas.
            nuevasColumnas.add(columna.copiarUltimasFilas(limite));
        }

        // Reutilizamos las etiquetas y mapa de columnas (no cambian).
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);

        // Crear y devolver una nueva tabla con las últimas 'x' filas.
        return new Tabla(this.nombreTabla + "_tail", nuevasColumnas, nuevasEtiquetasFilas,
                nuevasEtiquetasColumnas, nuevoMapaFilas, nuevoMapaColumnas);
    }

    // Crea una nueva tabla uniendo verticalmente las filas de esta tabla con las de otra tabla, siempre que ambas
    // tengan las mismas columnas.
    public Tabla concatenar(Tabla otra) throws ExcepcionesTabla.ExcepcionColumnasIncompatibles {
        // Validar que tengan la misma cantidad de columnas.
        if (this.columnas.size() != otra.getCantidadColumnas()) {
            throw new ExcepcionesTabla.ExcepcionColumnasIncompatibles(this.columnas.size() + " vs " +
                    otra.getCantidadColumnas());
        }
        // Validar que nombres y tipos de columnas coincidan.
        for (int i = 0; i < columnas.size(); i++) {
            Columna<?> col1 = this.columnas.get(i);
            Columna<?> col2 = otra.getColumnas().get(i);

            if (!col1.getNombre().equals(col2.getNombre()) ||
                    col1.getTipoDeDato() != col2.getTipoDeDato()) {
                throw new ExcepcionesTabla.ExcepcionColumnasIncompatibles(
                        String.format("Columna %d: '%s' (%s) vs '%s' (%s)",
                                i, col1.getNombre(), col1.getTipoDeDato(),
                                col2.getNombre(), col2.getTipoDeDato()));
            }
        }

        // Crear nuevas listas.
        List<Columna<?>> nuevasColumnas = new ArrayList<>();
        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();

        // Combinar etiquetas de filas.
        nuevasEtiquetasFilas.addAll(this.etiquetasFilas);
        nuevasEtiquetasFilas.addAll(otra.getEtiquetasFilas());

        for (int i = 0; i < nuevasEtiquetasFilas.size(); i++) {
            nuevoMapaFilas.put(nuevasEtiquetasFilas.get(i), i);
        }
        // Concatenar columnas.
        for (int i = 0; i < columnas.size(); i++) {
            Columna<?> col1 = this.columnas.get(i);
            Columna<?> col2 = otra.getColumnas().get(i);
            // Hacemos un cast seguro (suponiendo que los tipos coinciden).
            Columna<?> nuevaColumna = ((Columna) col1).concatenar((Columna) col2);
            nuevasColumnas.add(nuevaColumna);
        }
        // Las columnas y su mapa se mantienen.
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);
        // Crear nueva tabla.
        return new Tabla(
                this.nombreTabla + "_concatenada", nuevasColumnas, nuevasEtiquetasFilas, nuevasEtiquetasColumnas,
                nuevoMapaFilas, nuevoMapaColumnas);
    }

    // Recibe el nombre de la columna a filtrar y un predicado (condición) para filtrar los valores
    // Devuelve una nueva tabla con solo las filas donde la condición se cumple en esa columna.
    public Tabla filtrarColumnas(String nombreColumna, Predicate<Object> condicion)
            throws ExcepcionesTabla.ExcepcionColumnaNoEncontrada, ExcepcionesTabla.ExcepcionTipoDato,
            ExcepcionesTabla.ExcepcionIndiceInvalido {
        // Buscar índice de la columna que se va a filtrar.
        Integer indiceColumna = mapaColumnas.get(nombreColumna);
        if (indiceColumna == null) {
            throw new ExcepcionesTabla.ExcepcionColumnaNoEncontrada(nombreColumna);
        }
        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();
        List<Columna<?>> nuevasColumnas = new ArrayList<>();
        // Crear columnas vacías correspondientes a cada columna original.
        for (Columna<?> columna : columnas) {
            nuevasColumnas.add(new Columna<>(columna.getNombre(), columna.getTipoDeDato(), new ArrayList<>()));
        }
        int nuevoIndiceFila = 0;
        for (int i = 0; i < etiquetasFilas.size(); i++) {
            Object valor = columnas.get(indiceColumna).getValor(i);

            if (condicion.test(valor)) {
                String etiquetaFila = etiquetasFilas.get(i);
                nuevasEtiquetasFilas.add(etiquetaFila);
                nuevoMapaFilas.put(etiquetaFila, nuevoIndiceFila);

                for (int j = 0; j < columnas.size(); j++) {
                    Columna<?> colOriginal = columnas.get(j);
                    Columna<?> colNueva = nuevasColumnas.get(j);
                    ((Columna) colNueva).agregarCelda(colOriginal.getValor(i));
                }
                nuevoIndiceFila++;
            }
        }
        // Las etiquetas de columnas y mapa de columnas se mantienen.
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);

        return new Tabla(this.nombreTabla + "_filtrado", nuevasColumnas, nuevasEtiquetasFilas, nuevasEtiquetasColumnas,
                nuevoMapaFilas, nuevoMapaColumnas);
    }

    // Recibe un Predicate<List<Object>> que representa la condición aplicada a toda la fila (lista de valores de esa fila).
    // Devuelve una nueva Tabla con solo las filas que cumplen esa condición.
    public Tabla filtrarFilas(Predicate<List<Object>> condicion) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();
        List<Columna<?>> nuevasColumnas = new ArrayList<>();

        // Inicializar nuevas columnas vacías (con mismo nombre y tipo).
        for (Columna<?> columna : columnas) {
            nuevasColumnas.add(new Columna<>(columna.getNombre(), columna.getTipoDeDato(), new ArrayList<>()));
        }
        int nuevoIndiceFila = 0;
        int totalFilas = etiquetasFilas.size();

        for (int i = 0; i < totalFilas; i++) {
            // Construir la fila i como lista de valores
            List<Object> filaValores = new ArrayList<>();
            for (Columna<?> columna : columnas) {
                filaValores.add(columna.getValor(i));
            }
            // Evaluar la condición para esa fila completa.
            if (condicion.test(filaValores)) {
                String etiquetaFila = etiquetasFilas.get(i);
                nuevasEtiquetasFilas.add(etiquetaFila);
                nuevoMapaFilas.put(etiquetaFila, nuevoIndiceFila);

                // Copiar las celdas correspondientes de cada columna.
                for (int j = 0; j < columnas.size(); j++) {
                    Columna<?> colOriginal = columnas.get(j);
                    Columna<?> colNueva = nuevasColumnas.get(j);
                    Object valor = colOriginal.getValor(i);
                    try {
                        Columna<Object> colNuevaCasteada = (Columna<Object>) colNueva;
                        colNuevaCasteada.agregarCelda(valor);
                    } catch (ExcepcionesTabla.ExcepcionTipoDato e) {
                        System.out.println("Error al agregar celda: " + e.getMessage());
                    }
                }
                nuevoIndiceFila++;
            }
        }
        // Las etiquetas y mapa de columnas se mantienen igual.
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);
        return new Tabla(this.nombreTabla + "_filtradoFilas", nuevasColumnas, nuevasEtiquetasFilas,
                nuevasEtiquetasColumnas, nuevoMapaFilas, nuevoMapaColumnas);
    }

    // Crea una nueva tabla con los mismos datos, pero nuevas instancias para no modificar el objeto original cuando
    // haces operaciones que generan nuevas tablas.
    public Tabla copiar() {
        // Copiar etiquetas filas y mapa filas.
        List<String> nuevasEtiquetasFilas = new ArrayList<>(this.etiquetasFilas);
        Map<String, Integer> nuevoMapaFilas = new HashMap<>(this.mapaFilas);

        // Copiar etiquetas columnas y mapa columnas.
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(this.etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(this.mapaColumnas);

        // Copiar columnas (copiar todas las filas de cada columna).
        List<Columna<?>> nuevasColumnas = new ArrayList<>();
        for (Columna<?> columna : this.columnas) {
            nuevasColumnas.add(columna.copiarPrimerasFilas(columna.getCantidadFilas()));
        }
        return new Tabla(this.nombreTabla + "_copia", nuevasColumnas, nuevasEtiquetasFilas, nuevasEtiquetasColumnas,
                nuevoMapaFilas, nuevoMapaColumnas);
    }

    // Devuelve una nueva tabla con n filas elegidas al azar (sin reemplazo) de la tabla original.
    public Tabla muestreoAleatorio(int n) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        int totalFilas = etiquetasFilas.size();
        if (n <= 0) {
            throw new IllegalArgumentException("El número de filas a muestrear debe ser positivo.");
        }
        if (n > totalFilas) {
            throw new IllegalArgumentException("El número de filas a muestrear no puede ser mayor que el total de filas.");
        }

        // Crear lista con todos los índices de filas (0 a totalFilas-1).
        List<Integer> indicesFilas = new ArrayList<>();
        for (int i = 0; i < totalFilas; i++) {
            indicesFilas.add(i);
        }

        // Mezclar aleatoriamente la lista de índices.
        Collections.shuffle(indicesFilas, new Random());

        // Tomar los primeros n índices para el muestreo.
        List<Integer> indicesSeleccionados = indicesFilas.subList(0, n);

        // Ordenar para mantener el orden original de las filas seleccionadas.
        Collections.sort(indicesSeleccionados);

        // Construir nuevas etiquetas de filas y mapa de filas con las seleccionadas.
        List<String> nuevasEtiquetasFilas = new ArrayList<>();
        Map<String, Integer> nuevoMapaFilas = new HashMap<>();
        for (int i = 0; i < indicesSeleccionados.size(); i++) {
            int indiceOriginal = indicesSeleccionados.get(i);
            String etiqueta = etiquetasFilas.get(indiceOriginal);
            nuevasEtiquetasFilas.add(etiqueta);
            nuevoMapaFilas.put(etiqueta, i);
        }

        // Crear nuevas columnas copiando solo las filas seleccionadas.
        List<Columna<?>> nuevasColumnas = new ArrayList<>();
        for (Columna<?> columna : columnas) {
            nuevasColumnas.add(columna.copiarFilasPorIndices(indicesSeleccionados));
        }

        // Las etiquetas y mapa de columnas permanecen igual.
        List<String> nuevasEtiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        Map<String, Integer> nuevoMapaColumnas = new HashMap<>(mapaColumnas);

        // Devolver nueva tabla con muestreo aleatorio.
        return new Tabla(this.nombreTabla + "_muestreoAleatorio", nuevasColumnas, nuevasEtiquetasFilas,
                nuevasEtiquetasColumnas, nuevoMapaFilas, nuevoMapaColumnas);
    }

    // Imprime por consola una representación tabular simple, mostrando las etiquetas de columnas arriba y
    // las filas con sus etiquetas a la izquierda.
    public void mostrar() throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        // Definir ancho fijo para columnas.
        final int ancho = 15;
        // Imprimir encabezado.
        System.out.printf("%-" + ancho + "s", "Índice");
        for (String etiquetaCol : etiquetasColumnas) {
            System.out.printf("%-" + ancho + "s", etiquetaCol);
        }
        System.out.println();

        int totalFilas = etiquetasFilas.size();
        int totalColumnas = columnas.size();

        // Imprimir filas.
        for (int i = 0; i < totalFilas; i++) {
            System.out.printf("%-" + ancho + "s", etiquetasFilas.get(i));
            for (int j = 0; j < totalColumnas; j++) {
                Object valor = columnas.get(j).getValor(i);
                System.out.printf("%-" + ancho + "s", String.valueOf(valor));
            }
            System.out.println();
        }
    }

    // Implementación de la interfaz NAs. Recorre y detecta valores nulos o faltantes por columna.
    @Override
    public void leerNAs(Tabla tabla) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        for (Columna<?> col : tabla.columnas) {
            int count = col.contarNAs();
            System.out.println("Columna " + col.getNombre() + " tiene " + count + " valores NA");
        }
    }

    // Muestra los NAs encontrados en la tabla.
    @Override
    public void mostrarNAs(Tabla tabla) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        for (int i = 0; i < etiquetasFilas.size(); i++) {
            for (Columna<?> col : tabla.columnas) {
                Object val = col.getValor(i);
                if (Columna.esNA(val)) {
                    System.out.println("NA en fila " + etiquetasFilas.get(i) + ", columna " + col.getNombre());
                }
            }
        }
    }

    // Reemplaza los valores NA en todas las columnas de la tabla con un valor dado, validando tipos.
    @Override
    public void reemplazarNAs(Tabla tabla, Object valor) throws ExcepcionesTabla.ExcepcionTipoDato,
            ExcepcionesTabla.ExcepcionIndiceInvalido {
        for (Columna<?> col : tabla.columnas) {
            for (int i = 0; i < col.getCantidadFilas(); i++) {
                Object val = col.getValor(i);
                if (Columna.esNA(val)) {
                    if (!col.esValorValido(valor)) {
                        throw new ExcepcionesTabla.ExcepcionTipoDato(col.getTipoDeDato(), valor);
                    }
                    try {
                        ((Columna) col).setCelda(i, valor);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // Devuelve una nueva tabla con solo las columnas cuyos nombres se pasan en la lista, validando que existan.
    public Tabla seleccionarColumnas(List<String> nombresColumnas) throws ExcepcionesTabla.ExcepcionColumnaNoEncontrada,
            ExcepcionesTabla.ExcepcionTipoDato {
        List<Columna<?>> nuevasColumnas = new ArrayList<>();
        List<String> nuevasEtiquetasColumnas = new ArrayList<>();

        for (String nombre : nombresColumnas) {
            Columna<?> colEncontrada = null;
            for (Columna<?> c : columnas) {
                if (c.getNombre().equals(nombre)) {
                    colEncontrada = c;
                    break;
                }
            }
            if (colEncontrada == null) {
                throw new ExcepcionesTabla.ExcepcionColumnaNoEncontrada(nombre);
            }
            // Copiar columna completa (nombre, tipo y celdas)
            Columna<?> copiaCol = new Columna<>(colEncontrada.getNombre(), colEncontrada.getTipoDeDato(), colEncontrada.obtenerCeldas());
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

        // Crea y devuelve una nueva tabla
        return new Tabla(this.nombreTabla, nuevasColumnas, new ArrayList<>(this.etiquetasFilas), nuevasEtiquetasColumnas,
                nuevoMapaFilas, nuevoMapaColumnas);
    }

    // Actualiza el mapa que asocia etiquetas de filas con sus índices actuales.
    private void actualizarMapaFilas() {
        mapaFilas.clear();
        for (int i = 0; i < etiquetasFilas.size(); i++) {
            mapaFilas.put(etiquetasFilas.get(i), i);
        }
    }

    // Actualiza el mapa que asocia etiquetas de columnas con sus índices actuales.
    private void actualizarMapaColumnas() {
        mapaColumnas.clear();
        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            mapaColumnas.put(etiquetasColumnas.get(i), i);
        }
    }
}