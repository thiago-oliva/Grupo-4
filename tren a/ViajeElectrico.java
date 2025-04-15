public class ViajeElectrico extends Viaje {

    public ViajeElectrico(int capMaximaPasajeros, double distancia, int cantidadEstaciones) {
        super(capMaximaPasajeros,distancia, cantidadEstaciones);
    }
    @Override
    public double tiempoDeDemora() {
        return (distancia * cantidadEstaciones)/2;
    }
}