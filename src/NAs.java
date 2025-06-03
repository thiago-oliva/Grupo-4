public interface NAs {
    void leerNAs(Tabla tabla);                // Recorre y detecta valores nulos o faltantes
    void mostrarNAs(Tabla tabla);             // Imprime en consola o devuelve info sobre los NA
    void reemplazarNAs(Tabla tabla, Object valor) throws ExcepcionesTabla.ExcepcionTipoDato; // Imputa todos los NAs con valor dado
}