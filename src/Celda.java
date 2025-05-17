import java.util.List;
import java.util.ArrayList;

public class Celda {
    public List<Object> valor;


    public Celda(List<Object> valor){
        this.valor = new ArrayList<>();
    }


    public List<Object>  getValor(){
        return valor
    }

    public void setValor(List<Object> valor) {
        this.valor = valor;
    }


    public void agregarValor(Object nuevoValor) {
        valor.add(nuevoValor);
    }
    public void modificarValor(int indice, Object nuevoValor) {
        if (indice >= 0 && indice < valor.size()) {
            valor.set(indice, nuevoValor);
    else{
                System.out.println("Índice fuera de rango.");
    }




    }



}



}
