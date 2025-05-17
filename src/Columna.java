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
        return celdas.getFila();
    }


    public void setCelda(int fila, Object valor) throws excepcionTipoDato {  //modifica el valor de una celda que ya existe
        if (!esValorValido(valor)) {
            throw new excepcionTipoDato("Valor incompatible con tipo de columna: " + tipo); //verifica que el tipo del valor sea válido para esta columna
        }
        celdas.set(fila, new Celda(valor));
    }


    public void agregarCelda(Object valor) throws excepcionTipoDato {
        if(!esValorValido(valor)) {
            throw new excepcionTipoDato("Valor incompatible con tipo de columna: " + tipo);
        }
        celdas.add(new Celda(valor)); //crea una nueva celda con ese valor
    }


    private boolean esValorValido(Object valor) { //comprueba si el valor coincide con el tipo de la columna
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
        celdas.remove(fila);
    }


    public List<Celda> obtenerCeldas() {
        return celdas;
    }


    public static class excepcionTipoDato extends Exception {  //excepcion para error de tipo
        public excepcionTipoDato(String mensaje) {
            super(mensaje);
        }
    }
}

