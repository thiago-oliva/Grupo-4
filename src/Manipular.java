import java.util.List;

public interface Manipular {
    List<Object> obtenerFila(String etiqueta) throws ExcepcionesTabla.ExcepcionFilaNoEncontrada;
    void eliminarFila(String etiqueta) throws ExcepcionesTabla.ExcepcionFilaNoEncontrada;
    void insertarColumna(int indice, List<String> nuevaColumna) throws ExcepcionesTabla.ExcepcionLongitudColumna;
    void eliminarColumna(int indice);

}