public class ViajeAltaVelocidad implements TipoDeViaje {
    @Override
    public double tiempoDeDemora(Viaje viaje) {
        return viaje.distancia / 10;
    }
}