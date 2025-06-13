public class ExcepcionesTabla {

    // Excepción base para errores generales en la tabla.
    public static class ExcepcionTabla extends Exception {
        public ExcepcionTabla(String mensaje) {
            super(mensaje);
        }

        public ExcepcionTabla(String mensaje, Throwable causa) {
            super(mensaje, causa);
        }
    }

    // Excepción para índices fuera del rango válido.
    public static class ExcepcionIndiceInvalido extends ExcepcionTabla {
        public ExcepcionIndiceInvalido(int indice) {
            super(String.format("Índice fuera de rango: %d", indice));
        }
    }

    // Excepción para cuando no se encuentra una fila por su etiqueta.
    public static class ExcepcionFilaNoEncontrada extends ExcepcionTabla {
        public ExcepcionFilaNoEncontrada(String etiqueta) {
            super(String.format("Fila no encontrada: '%s'", etiqueta));
        }
    }

    // Excepción para cuando un valor no coincide con el tipo de dato esperado.
    public static class ExcepcionTipoDato extends ExcepcionTabla {
        public ExcepcionTipoDato(TipoDato tipoEsperado, Object valorRecibido) {
            super(String.format("Tipo inválido. Esperado: %s, Recibido: %s",
                    tipoEsperado,
                    valorRecibido != null ? valorRecibido.getClass().getSimpleName() : "null"));
        }
    }

    // Excepción para cuando no se encuentra una columna por su nombre.
    public static class ExcepcionColumnaNoEncontrada extends ExcepcionTabla {
        public ExcepcionColumnaNoEncontrada(String columna) {
            super(String.format("Columna no encontrada: '%s'", columna));
        }
    }

    // Excepción para cuando una columna tiene una cantidad incorrecta de filas.
    public static class ExcepcionLongitudColumna extends ExcepcionTabla {
        public ExcepcionLongitudColumna(String columna) {
            super(String.format("Longitud incorrecta en columna '%s'", columna));
        }
    }

    // Excepción para columnas incompatibles al intentar concatenar tablas.
    public static class ExcepcionColumnasIncompatibles extends ExcepcionTabla {
        public ExcepcionColumnasIncompatibles(String detalle) {
            super("Columnas incompatibles: " + detalle);
        }
    }

    // Excepción para expresiones de filtro mal formadas o inválidas.
    public static class ExcepcionFiltroInvalido extends ExcepcionTabla {
        public ExcepcionFiltroInvalido(String mensaje) {
            super("Filtro inválido: " + mensaje);
        }
    }

    // Excepción para errores de formato o corrupción en archivos.
    public static class ExcepcionFormatoArchivo extends ExcepcionTabla {
        public ExcepcionFormatoArchivo(String nombreArchivo, String razon) {
            super(String.format("Archivo '%s' corrupto o mal formateado: %s",
                    nombreArchivo, razon));
        }
    }

    // Excepción para cuando se solicita una columna inexistente por nombre.
    public static class ExcepcionColumnaInexistente extends ExcepcionTabla {
        public ExcepcionColumnaInexistente(String nombre) {
            super("No existe la columna con nombre: " + nombre);
        }
    }
}