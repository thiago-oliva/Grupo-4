import java.util.List;
import java.util.ArrayList;

public class Columna<T> {
    private String nombre;
    private TipoDato tipo;
    private List<Celda<T>> celdas;

    // Constructor para crear una columna con un nombre, tipo de dato y una lista de celdas
    public Columna(String nombre, TipoDato tipo, List<Celda<T>> celdas) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.celdas = new ArrayList<>(celdas);
    }

    public TipoDato getTipoDeDato() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidadFilas() {
        return celdas.size();
    }

    public List<Celda<T>> getCeldas() {
        return this.celdas;
    }

    // Obtiene el valor de una celda en una fila específica
    // Si la fila es inválida, lanza una excepción
    public T getValor(int filaIndex) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        if (filaIndex < 0 || filaIndex >= celdas.size()) {
            throw new ExcepcionesTabla.ExcepcionIndiceInvalido(filaIndex);
        }
        return celdas.get(filaIndex).getValor();
    }

    // Obtiene la celda completa en una fila específica
    // Si la fila es inválida, lanza una excepción
    public Celda getCelda(int fila) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        if (fila < 0 || fila >= celdas.size()) {
            throw new ExcepcionesTabla.ExcepcionIndiceInvalido(fila);
        }
        return celdas.get(fila);
    }

    // Establece el valor de una celda en una fila específica
    // Si la fila es inválida o el valor no es del tipo correcto, lanza una excepción
    public void setCelda(int fila, T valor) throws ExcepcionesTabla.ExcepcionTipoDato,
            ExcepcionesTabla.ExcepcionIndiceInvalido {
        if (fila < 0 || fila >= celdas.size()) {
            throw new ExcepcionesTabla.ExcepcionIndiceInvalido(fila);
        }
        if (!esValorValido(valor)) {
            throw new ExcepcionesTabla.ExcepcionTipoDato(tipo, valor);
        }
        celdas.set(fila, new Celda<>(valor));
    }

    // Agrega una nueva celda al final de la columna
    // Si el valor no es del tipo correcto, lanza una excepción
    public void agregarCelda(T valor) throws ExcepcionesTabla.ExcepcionTipoDato {
        if (!esValorValido(valor)) {
            throw new ExcepcionesTabla.ExcepcionTipoDato(tipo, valor);
        }
        celdas.add(new Celda<>(valor));
    }

    // chequea si el valor es válido según el tipo de dato de la columna
    public boolean esValorValido(Object valor) {
        if (valor == null) return true;
        switch (tipo) {
            case NUMERICO:
                return valor instanceof Number;
            case BOOLEANO:
                return valor instanceof Boolean;
            case CADENA:
                return valor instanceof String;
            default:
                return false;
        }
    }

    // Elimina una fila específica
    public void eliminarFila(int fila) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        if (fila < 0 || fila >= celdas.size()) {
            throw new ExcepcionesTabla.ExcepcionIndiceInvalido(fila);
        }
        celdas.remove(fila);
    }

    // Obtiene una lista de todas las celdas de la columna
    public List<Celda<T>> obtenerCeldas() {
        return new ArrayList<>(celdas);
    }

    //crea una nueva columna que contiene solo las primeras limite filas (celdas) de la columna original.
    public Columna<T> copiarPrimerasFilas(int limite) {
        List<Celda<T>> nuevasCeldas = new ArrayList<>();
        for (int i = 0; i < limite && i < celdas.size(); i++) {
            nuevasCeldas.add(new Celda<>(celdas.get(i).getValor()));
        }
        return new Columna(this.nombre, this.tipo, nuevasCeldas);
    }

    // crea una nueva columna que contiene las últimas filas (celdas) de la columna original.
    public Columna<T> copiarUltimasFilas(int cantidad) {
        int total = celdas.size();
        int desde = Math.max(0, total - cantidad);
        List<Celda<T>> nuevasCeldas = new ArrayList<>();
        for (int i = desde; i < total; i++) {
            nuevasCeldas.add(new Celda<>(celdas.get(i).getValor()));
        }
        return new Columna<>(this.nombre, this.tipo, nuevasCeldas);
    }

    // copia filas específicas de la columna original según los índices proporcionados
    public Columna<T> copiarFilasPorIndices(List<Integer> indices) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        List<Celda<T>> nuevasCeldas = new ArrayList<>();
        for (int i : indices) {
            if (i >= 0 && i < celdas.size()) {
                nuevasCeldas.add(new Celda<>(celdas.get(i).getValor()));
            } else {
                throw new ExcepcionesTabla.ExcepcionIndiceInvalido(i);
            }
        }
        return new Columna<>(this.nombre, this.tipo, nuevasCeldas);
    }

    // Método para verificar si un valor es NA
    public static boolean esNA(Object valor) {
        return valor == null || valor.equals("NA");
    }

    // Método para contar NAs en esta columna específica
    public int contarNAs() throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        int count = 0;
        for (int i = 0; i < this.getCantidadFilas(); i++) {
            if (esNA(this.getValor(i))) {
                count++;
            }
        }
        return count;
    }
}

