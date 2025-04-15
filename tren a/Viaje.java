public abstract class Viaje {
    public int capMaximaPasajeros;
    public double distancia;
    public int cantidadEstaciones;

    public Viaje(int capMaximaPasajeros, double distancia, int cantidadEstaciones) {
        this.capMaximaPasajeros = capMaximaPasajeros;
        this.distancia = distancia;
        this.cantidadEstaciones = cantidadEstaciones;
    }
    public abstract double tiempoDeDemora();

    @Override

    public String toString(){
        return String.format("Tiempo de demora: %.2f", tiempoDeDemora());
    }
}