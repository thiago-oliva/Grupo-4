public interface NAs {
    void leerNAs(Tabla tabla) throws ExcepcionesTabla.ExcepcionIndiceInvalido;
    void mostrarNAs(Tabla tabla) throws ExcepcionesTabla.ExcepcionIndiceInvalido;
    void reemplazarNAs(Tabla tabla, Object valor) throws ExcepcionesTabla.ExcepcionTipoDato, ExcepcionesTabla.ExcepcionIndiceInvalido;
}