public class ViajeAltaVelocidad extends Viaje {

    public ViajeAltaVelocidad(int capMaximaPasajeros, double distancia, int cantidadEstaciones) {
        super(capMaximaPasajeros, distancia, cantidadEstaciones);
    }
    @Override
    public double tiempoDeDemora() {
        return (distancia)/10;
    }
}