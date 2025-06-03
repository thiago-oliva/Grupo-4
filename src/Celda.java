public class Celda {
    private Object valor;

    public Celda(Object valor) {
        this.valor = valor;
    }
    // getter: obtiene el valor almacenado en una celda
    public Object getValor() {
        return valor;
    }
    // setter: modifica el valor almacenado en una celda
    public void setValor(Object valor) {
        this.valor = valor;
    }
}
