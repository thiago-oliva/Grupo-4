public class ExcepcionesTabla {

    // Excepción base
    public static class ExcepcionTabla extends Exception {
        public ExcepcionTabla(String mensaje) {
            super(mensaje);
        }
        
        public ExcepcionTabla(String mensaje, Throwable causa) {
            super(mensaje, causa);
        }
    }

    // Para índices inválidos
    public static class ExcepcionIndiceInvalido extends ExcepcionTabla {
        public ExcepcionIndiceInvalido(int indice) {
            super(String.format("[%s] Índice %d fuera de rango (máximo: %d)", indice));
        }
    }

    // Para tipos de datos incorrectos
    public static class ExcepcionTipoDato extends ExcepcionTabla {
        public ExcepcionTipoDato(Columna.TipoDato tipoEsperado, Object valorRecibido) {
            super(String.format("Tipo inválido. Esperado: %s, Recibido: %s",
                tipoEsperado,
                valorRecibido != null ? valorRecibido.getClass().getSimpleName() : "null"));
        }
    }

    // Para longitud incorrecta en columnas
    public static class ExcepcionLongitudColumna extends ExcepcionTabla {
        public ExcepcionLongitudColumna(String columna, int longitudEsperada, int longitudRecibida) {
            super(String.format("[COLUMNA '%s'] Longitud incorrecta. Esperada: %d, Recibida: %d",
                columna, longitudEsperada, longitudRecibida));
        }
    }

    // Para errores de formato en archivos
    public static class ExcepcionFormatoArchivo extends ExcepcionTabla {
        public ExcepcionFormatoArchivo(String nombreArchivo, String razon) {
            super(String.format("Archivo '%s' corrupto o mal formateado: %s",
                nombreArchivo, razon));
        }
    }
}