import java.util.List;
import java.util.ArrayList;

public class Columna implements NAs {
    private String nombre;
    private TipoDato tipo;
    private List<Celda> celdas;

    // Constructor para crear una columna con un nombre y tipo de dato
    public Columna(String nombre, TipoDato tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.celdas = new ArrayList<>();
    }

    // Constructor para crear una columna con un nombre, tipo de dato y una lista de celdas
    public Columna(String nombre, TipoDato tipo, List<Celda> celdas) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.celdas = new ArrayList<>(celdas);
    }

    public TipoDato getTipoDeDato() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidadFilas() {
        return celdas.size();
    }

    public List<Celda> getCeldas() {
        return this.celdas;
    }

    // Obtiene el valor de una celda en una fila específica
    // Si la fila es inválida, lanza una excepción
    public Object getValor(int filaIndex) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        if (filaIndex < 0 || filaIndex >= celdas.size()) {
            throw new ExcepcionesTabla.ExcepcionIndiceInvalido(filaIndex);
        }
        return celdas.get(filaIndex).getValor();
    }

    // Obtiene la celda completa en una fila específica
    // Si la fila es inválida, lanza una excepción
    public Celda getCelda(int fila) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        if (fila < 0 || fila >= celdas.size()) {
            throw new ExcepcionesTabla.ExcepcionIndiceInvalido(fila);
        }
        return celdas.get(fila);
    }

    // Establece el valor de una celda en una fila específica
    // Si la fila es inválida o el valor no es del tipo correcto, lanza una excepción
    public void setCelda(int fila, Object valor) throws ExcepcionesTabla.ExcepcionTipoDato,
            ExcepcionesTabla.ExcepcionIndiceInvalido {
        if (fila < 0 || fila >= celdas.size()) {
            throw new ExcepcionesTabla.ExcepcionIndiceInvalido(fila);
        }
        if (!esValorValido(valor)) {
            throw new ExcepcionesTabla.ExcepcionTipoDato(tipo, valor);
        }
        celdas.set(fila, new Celda(valor));
    }

    // Agrega una nueva celda al final de la columna
    // Si el valor no es del tipo correcto, lanza una excepción
    public void agregarCelda(Object valor) throws ExcepcionesTabla.ExcepcionTipoDato {
        if (!esValorValido(valor)) {
            throw new ExcepcionesTabla.ExcepcionTipoDato(tipo, valor);
        }
        celdas.add(new Celda(valor));
    }

    // chequea si el valor es válido según el tipo de dato de la columna
    public boolean esValorValido(Object valor) {
        if (valor == null) return true;
        switch (tipo) {
            case NUMERICO:
                return valor instanceof Number;
            case BOOLEANO:
                return valor instanceof Boolean;
            case CADENA:
                return valor instanceof String;
            default:
                return false;
        }
    }

    // Elimina una fila específica
    public void eliminarFila(int fila) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        if (fila < 0 || fila >= celdas.size()) {
            throw new ExcepcionesTabla.ExcepcionIndiceInvalido(fila);
        }
        celdas.remove(fila);
    }

    // Obtiene una lista de todas las celdas de la columna
    public List<Celda> obtenerCeldas() {
        return new ArrayList<>(celdas);
    }

    //crea una nueva columna que contiene solo las primeras limite filas (celdas) de la columna original.
    public Columna copiarPrimerasFilas(int limite) {
        List<Celda> nuevasCeldas = new ArrayList<>();
        for (int i = 0; i < limite && i < celdas.size(); i++) {
            nuevasCeldas.add(new Celda(celdas.get(i).getValor()));
        }
        return new Columna(this.nombre, this.tipo, nuevasCeldas);
    }

    // crea una nueva columna que contiene las últimas filas (celdas) de la columna original.
    public Columna copiarUltimasFilas(int cantidad) {
        int total = celdas.size();
        int desde = Math.max(0, total - cantidad);
        List<Celda> nuevasCeldas = new ArrayList<>();
        for (int i = desde; i < total; i++) {
            nuevasCeldas.add(new Celda(celdas.get(i).getValor()));
        }
        return new Columna(this.nombre, this.tipo, nuevasCeldas);
    }

    // copia filas específicas de la columna original según los índices proporcionados
    public Columna copiarFilasPorIndices(List<Integer> indices) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        List<Celda> nuevasCeldas = new ArrayList<>();
        for (int i : indices) {
            if (i >= 0 && i < celdas.size()) {
                nuevasCeldas.add(new Celda(celdas.get(i).getValor()));
            } else {
                throw new ExcepcionesTabla.ExcepcionIndiceInvalido(i);
            }
        }
        return new Columna(this.nombre, this.tipo, nuevasCeldas);
    }

    // Método para verificar si un valor es NA
    public boolean esNA(Object valor) {
        return valor == null || valor.equals("NA");
    }

    // Método para contar NAs en esta columna específica
    public int contarNAs() throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        int count = 0;
        for (int i = 0; i < this.getCantidadFilas(); i++) {
            if (esNA(this.getValor(i))) {
                count++;
            }
        }
        return count;
    }
    
    // Implementación de la interfaz NAs
    // Recorre y detecta valores nulos o faltantes por columna
    @Override
    public void leerNAs(Tabla tabla) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        for (Columna columna : tabla.getColumnas()) {
            for (int i = 0; i < columna.getCantidadFilas(); i++) {
                Object valor = columna.getValor(i);
                if (esNA(valor)) {
                    System.out.println("Hay NAs en " + tabla.getNombreTabla() + " columna: " + columna.getNombre());
                }
                else {
                    System.out.println("No hay NAs en " + tabla.getNombreTabla() + " columna: " + columna.getNombre());
                }
            }
        }
    }

    // Muestra los NAs encontrados en la tabla
    @Override
    public void mostrarNAs(Tabla tabla) throws ExcepcionesTabla.ExcepcionIndiceInvalido {
        for (Columna columna : tabla.getColumnas()) {
            for (int i = 0; i < columna.getCantidadFilas(); i++) {
                Object valor = columna.getValor(i);
                if (esNA(valor)) {
                    System.out.println("NA encontrado en columna '" + columna.getNombre() + "', fila " + i);
                }
            }
        }
    }

    // Reemplaza todos los NAs en la tabla con el valor dado
    @Override
    public void reemplazarNAs(Tabla tabla, Object valor) throws ExcepcionesTabla.ExcepcionTipoDato, ExcepcionesTabla.ExcepcionIndiceInvalido {
        for (Columna columna : tabla.getColumnas()) {
            for (int i = 0; i < columna.getCantidadFilas(); i++) {
                Object celdaValor = columna.getValor(i);
                if (esNA(celdaValor)) {
                    if (!columna.esValorValido(valor)) {
                        throw new ExcepcionesTabla.ExcepcionTipoDato(columna.getTipoDeDato(), valor);
                    }
                    columna.setCelda(i, valor);
                }
            }
        }
    }
}

