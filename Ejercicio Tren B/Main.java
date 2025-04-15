public class Main {
    public static void main(String[] args) {
        Viaje viaje1 = new Viaje(100, 200, 5, new ViajeDiesel());
        Viaje viaje2 = new Viaje(100, 200, 5, new ViajeElectrico());
        Viaje viaje3 = new Viaje(100, 200, 5, new ViajeAltaVelocidad());

        System.out.println("\nRelacionando con tipo de viaje:");
        System.out.println(viaje1);
        System.out.println(viaje2);
        System.out.println(viaje3);
    }
}