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
            super(String.format("Índice fuera de rango: %d", indice));
        }
    }

    // Para cuando no se encuentra una fila
    public static class ExcepcionFilaNoEncontrada extends ExcepcionTabla {
        public ExcepcionFilaNoEncontrada(String etiqueta) {
            super(String.format("Fila no encontrada: '%s'", etiqueta));
        }
    }

    // Para tipos de datos incorrectos
    public static class ExcepcionTipoDato extends ExcepcionTabla {
        public ExcepcionTipoDato(TipoDato tipoEsperado, Object valorRecibido) {
            super(String.format("Tipo inválido. Esperado: %s, Recibido: %s",
                tipoEsperado,
                valorRecibido != null ? valorRecibido.getClass().getSimpleName() : "null"));
        }
    }

    // Para cuando no se encuentra una columna
    public static class ExcepcionColumnaNoEncontrada extends ExcepcionTabla {
        public ExcepcionColumnaNoEncontrada(String columna) {
            super(String.format("Columna no encontrada: '%s'", columna));
        }
    }

    // Para el error de la cantidad de filas en una columna
    public static class ExcepcionLongitudColumna extends ExcepcionTabla {
        public ExcepcionLongitudColumna(String columna) {
            super(String.format("Longitud incorrecta en columna '%s'", columna));
        }
    }

    // Para señalar un error entre dos tablas que queres concatenar y sus columnas no son compatibles
    public static class ExcepcionColumnasIncompatibles extends ExcepcionTabla {
        public ExcepcionColumnasIncompatibles(String detalle) {
            super("Columnas incompatibles: " + detalle);
        }
    }

    // Para expresiones de filtrado mal formadas(si intentan ordenar por una columna que no existe o incompatible)
    public static class ExcepcionFiltroInvalido extends ExcepcionTabla {
        public ExcepcionFiltroInvalido(String mensaje) {
            super("Filtro inválido: " + mensaje);
        }
    }

    // Para errores de formato en archivos
    public static class ExcepcionFormatoArchivo extends ExcepcionTabla {
        public ExcepcionFormatoArchivo(String nombreArchivo, String razon) {
            super(String.format("Archivo '%s' corrupto o mal formateado: %s",
                nombreArchivo, razon));
        }
    }

    public static class ExcepcionColumnaInexistente extends ExcepcionTabla {
        public ExcepcionColumnaInexistente(String nombre) {
            super("No existe la columna con nombre: " + nombre);
        }
    }
}