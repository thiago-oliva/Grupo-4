public class Viaje {
    public int capMaximaPasajeros;
    public double distancia;
    public int cantidadEstaciones;
    private TipoDeViaje tipoDeViaje;

    public Viaje(int capMaximaPasajeros, double distancia, int cantidadEstaciones, TipoDeViaje tipoDeViaje) {
        this.capMaximaPasajeros = capMaximaPasajeros;
        this.distancia = distancia;
        this.cantidadEstaciones = cantidadEstaciones;
        this.tipoDeViaje = tipoDeViaje;
    }

    public double tiempoDeDemora() {
        return tipoDeViaje.tiempoDeDemora(this);
    }

    @Override
    public String toString(){
        return String.format("Tiempo de demora: %.2f", tiempoDeDemora());
    }
}