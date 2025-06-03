// Con generics
public class Celda<T> {
    private T valor;

    public Celda(T valor) {
        this.valor = valor;
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }
}

// Con object
// public class Celda {
//    private Object valor;
//
//    public Celda(Object valor) {
//        this.valor = valor;
//    }
//    // getter: obtiene el valor almacenado en una celda
//    public Object getValor() {
//        return valor;
//    }
//    // setter: modifica el valor almacenado en una celda
//    public void setValor(Object valor) {
//        this.valor = valor;
//    }
//}