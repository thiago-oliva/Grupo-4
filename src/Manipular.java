import java.util.List;

public interface Manipular {
    void obtenerFila(String str);
    void eliminarFila(String str);
    void insertarColumna(int indice, List<String> nuevaColumna);
    void eliminarColumna(int indice);
}
