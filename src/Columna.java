import java.util.List;
import java.util.ArrayList;

public class Columna {

    private String nombre;
    private TipoDato tipo;
    private List<Celda> celdas;

    public enum TipoDato {
        NUMERICO,
        BOOLEANO,
        CADENA
    }

    public TipoDato getTipoDeDato() {
        return tipo;
    }

    public Columna(String nombre, TipoDato tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.celdas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public TipoDato getTipo() {
        return tipo;
    }

    public int getCantidadFilas() {
        return celdas.size();
    }

    public Celda getCelda(int fila) {
        if (fila < 0 || fila >= celdas.size()) { 
            throw new IndexOutOfBoundsException("Índice de fila inválido: " + fila);
        }
        return celdas.get(fila);
    }
    
    public void setCelda(int fila, Object valor) throws excepcionTipoDato {
        if (fila < 0 || fila >= celdas.size()) {
            throw new IndexOutOfBoundsException("Índice de fila fuera de rango.");
        }
        if (!esValorValido(valor)) {
            throw new excepcionTipoDato("Valor incompatible con tipo de columna: " + tipo); 
        }
        celdas.set(fila, new Celda(valor));
    }

    public void agregarCelda(Object valor) throws excepcionTipoDato {
        if(!esValorValido(valor)) {
            throw new excepcionTipoDato("Valor incompatible con tipo de columna: " + tipo);
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

    public void eliminarFila(int fila) {
        if (fila < 0 || fila >= celdas.size()) {
            throw new IndexOutOfBoundsException("Índice de fila inválido: " + fila);
        }
        celdas.remove(fila);
    }

    public List<Celda> obtenerCeldas() {
        return celdas;
    }

    public static class excepcionTipoDato extends Exception {
        public excepcionTipoDato(String mensaje) {
            super(mensaje);
        }
    }
}
