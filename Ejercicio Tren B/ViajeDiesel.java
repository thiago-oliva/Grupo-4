public class ViajeDiesel implements TipoDeViaje {
    @Override
    public double tiempoDeDemora(Viaje viaje) {
        return (viaje.distancia * viaje.cantidadEstaciones)/2 + ((viaje.cantidadEstaciones + viaje.capMaximaPasajeros)/10.0);
    }
}