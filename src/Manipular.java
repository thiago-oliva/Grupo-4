import java.util.List;

public interface Manipular {
    List<Object> obtenerFila(String etiqueta) throws ExcepcionesTabla.ExcepcionFilaNoEncontrada, ExcepcionesTabla.ExcepcionIndiceInvalido;
    void eliminarFila(String etiqueta) throws ExcepcionesTabla.ExcepcionFilaNoEncontrada;
    void insertarColumna(int indice, String nombreColumna, List<String> nuevaColumna) throws ExcepcionesTabla.ExcepcionLongitudColumna;
    void eliminarColumna(int indice);
    void insertarFila(List<Object> valores) throws ExcepcionesTabla.ExcepcionTipoDato;;
}