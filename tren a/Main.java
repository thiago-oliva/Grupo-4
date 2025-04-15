public class Main {
    public static void main(String[] args) {
        Viaje viaje1 = new ViajeDiesel(100, 200, 5);
        Viaje viaje2 = new ViajeElectrico(100, 200, 5);
        Viaje viaje3 = new ViajeAltaVelocidad(100, 200, 5);

        System.out.println(viaje1);
        System.out.println(viaje2);
        System.out.println(viaje3);
    }
}