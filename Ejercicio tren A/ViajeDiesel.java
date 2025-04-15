public class ViajeDiesel extends Viaje {

    public ViajeDiesel(int capMaximaPasajeros, double distancia, int cantidadEstaciones) {
        super(capMaximaPasajeros,distancia, cantidadEstaciones);
    }
    @Override
    public double tiempoDeDemora() {
        return (distancia * cantidadEstaciones)/2 + ((cantidadEstaciones + capMaximaPasajeros)/10);
    }
}