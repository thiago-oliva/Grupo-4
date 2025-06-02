import java.util.List;
import java.util.ArrayList;

public class Columna {
    private String nombre;
    private TipoDato tipo;
    private List<Celda> celdas;

    public Columna(String nombre, TipoDato tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.celdas = new ArrayList<>();
    }

    public Columna(String nombre, TipoDato tipo, List<Celda> celdas) {
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

    public Object getValor(int filaIndex) { //Me busca el indice de la
        return celdas.get(filaIndex).getValor();
    }

    public Celda getCelda(int fila) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        if (fila < 0 || fila >= celdas.size()) {
            throw new ExcepcionesTabla.ExcepcionIndiceInvalido(fila);
        }
        return celdas.get(fila);
    }

    public void setCelda(int fila, Object valor) throws ExcepcionesTabla.ExcepcionTipoDato,
            ExcepcionesTabla.ExcepcionIndiceInvalido {
        if (fila < 0 || fila >= celdas.size()) {
            throw new ExcepcionesTabla.ExcepcionIndiceInvalido(fila);
        }
        if (!esValorValido(valor)) {
            throw new ExcepcionesTabla.ExcepcionTipoDato(tipo, valor);
        }
        celdas.set(fila, new Celda(valor));
    }

    public void agregarCelda(Object valor) throws ExcepcionesTabla.ExcepcionTipoDato {
        if (!esValorValido(valor)) {
            throw new ExcepcionesTabla.ExcepcionTipoDato(tipo, valor);
        }
        celdas.add(new Celda(valor));
    }

    private boolean esValorValido(Object valor) {
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

    public void eliminarFila(int fila) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        if (fila < 0 || fila >= celdas.size()) {
            throw new ExcepcionesTabla.ExcepcionIndiceInvalido(fila);
        }
        celdas.remove(fila);
    }

    public List<Celda> obtenerCeldas() {
        return celdas;
    }

    //crea una nueva columna que contiene solo las primeras limite filas (celdas) de la columna original.
    public Columna copiarPrimerasFilas(int limite) {
        List<Celda> nuevasCeldas = new ArrayList<>();
        for (int i = 0; i < limite && i < celdas.size(); i++) {
            nuevasCeldas.add(new Celda(celdas.get(i).getValor()));
        }
        return new Columna(this.nombre, this.tipo, nuevasCeldas);
    }

    public Columna copiarUltimasFilas(int cantidad) {
        int total = celdas.size();
        int desde = Math.max(0, total - cantidad);
        List<Celda> nuevasCeldas = new ArrayList<>();
        for (int i = desde; i < total; i++) {
            nuevasCeldas.add(new Celda(celdas.get(i).getValor()));
        }
        return new Columna(this.nombre, this.tipo, nuevasCeldas);
    }

    public Columna copiarFilasPorIndices(List<Integer> indices) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        List<Celda> nuevasCeldas = new ArrayList<>();
        for (int i : indices) {
            if (i >= 0 && i < celdas.size()) {
                nuevasCeldas.add(new Celda(celdas.get(i).getValor()));
            } else {
                throw new ExcepcionesTabla.ExcepcionIndiceInvalido(i);
            }
        }
        return new Columna(this.nombre, this.tipo, nuevasCeldas);
    }
}