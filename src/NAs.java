public interface NAs {
    void leerNAs(Tabla tabla) throws ExcepcionesTabla.ExcepcionIndiceInvalido; // Recorre y detecta valores nulos o faltantes
    void mostrarNAs(Tabla tabla) throws ExcepcionesTabla.ExcepcionIndiceInvalido; // Imprime en consola o devuelve info sobre los NA
    void reemplazarNAs(Tabla tabla, Object valor) throws ExcepcionesTabla.ExcepcionTipoDato, ExcepcionesTabla.ExcepcionIndiceInvalido; // Imputa todos los NAs con valor dado
}