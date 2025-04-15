public class ViajeElectrico implements TipoDeViaje {
    @Override
    public double tiempoDeDemora(Viaje viaje) {
        return (viaje.distancia * viaje.cantidadEstaciones)/2;
    }
}