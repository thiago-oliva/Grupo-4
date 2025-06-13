import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        try {
            // Leer archivo CSV original
            ArchivoCSV lector = new ArchivoCSV("C:\\Users\\anton\\Downloads\\EJEMPLO.csv");
            Tabla tabla = new Tabla(lector.getMap());

            System.out.println("=== ETIQUETAS DE FILAS ===");
            System.out.println(tabla.getEtiquetasFilas());

            // Mostrar una fila existente
            System.out.println("\n=== CONTENIDO DE LA FILA 'F1' ===");
            System.out.println(tabla.obtenerFila("F1"));

            // Insertar nueva fila
            List<Object> nuevaFila = Arrays.asList("Fran", 32, true);
            tabla.insertarFila(nuevaFila);
            System.out.println("\n>> Fila 'Fran' insertada correctamente.");

            // Eliminar una fila por etiqueta
            String etiquetaAEliminar = "F3";
            tabla.eliminarFila(etiquetaAEliminar);
            System.out.println(">> Fila con etiqueta '" + etiquetaAEliminar + "' eliminada correctamente.");


            List<String> Provincias = new ArrayList<>();
            Provincias.add("Buenos Aires");// F0
            Provincias.add("Córdoba");// F1
            Provincias.add("Mendoza");// F2
            Provincias.add("Santa Fe");// F3

            tabla.insertarColumna(tabla.getCantidadColumnas(), "Provincia", Provincias);

            // Eliminar una columna por índice
            tabla.eliminarColumna(0); // por ejemplo, elimina la primera
            System.out.println(">> Primera columna eliminada.");

            // Mostrar valores faltantes
            System.out.println("\n=== VALORES FALTANTES EN LA TABLA ===");
            tabla.mostrarNAs(tabla);

            // Reemplazar valores faltantes por "N/A"
            tabla.reemplazarNAs(tabla, "N/A");
            System.out.println(">> Todos los valores faltantes fueron reemplazados por 'N/A'.");

            // SELECCIONAR columnas y filas específicas
            System.out.println("\n=== SELECCIÓN DE COLUMNAS Y FILAS ===");
            List<String> columnasDeseadas = Arrays.asList("Edad", "Provincia");
            List<Integer> indicesFilas = Arrays.asList(0, 2); // F0 y F2
            List<List<Celda<?>>> seleccion = tabla.seleccionar(columnasDeseadas, indicesFilas);

            for (List<Celda<?>> fila : seleccion) {
                for (Celda<?> celda : fila) {
                    System.out.print(celda.getValor() + "\t");
                }
                System.out.println();
            }

            // Guardar la tabla modificada
            String rutaDestino = "C:\\Users\\anton\\Downloads\\EJEMPLO_CONCATENADO.csv";
            ArchivoCSV escritor = new ArchivoCSV(rutaDestino);
            escritor.guardarTablaEnCSV(tabla, rutaDestino);
            System.out.println("\n>> Tabla modificada guardada en: " + rutaDestino);

            System.out.println("\n=== PRIMERAS 2 FILAS DE LA TABLA ===");
            Tabla tablaHead = tabla.head(2);

            // Mostrar el contenido de la tabla head
            List<String> etiquetas = tablaHead.getEtiquetasFilas();
            for (String etiqueta : etiquetas) {
                List<Object> fila = tablaHead.obtenerFila(etiqueta);
                System.out.print(etiqueta + ": ");
                for (Object valor : fila) {
                    System.out.print(valor + "\t");
                }
                System.out.println();
            }

            System.out.println("\n=== ÚLTIMAS 2 FILAS DE LA TABLA ===");
            Tabla tablaTail = tabla.tail(2);

            // Mostrar el contenido de la tabla tail
            List<String> etiquetasTail = tablaTail.getEtiquetasFilas();
            for (String etiqueta : etiquetasTail) {
                List<Object> fila = tablaTail.obtenerFila(etiqueta);
                System.out.print(etiqueta + ": ");
                for (Object valor : fila) {
                    System.out.print(valor + "\t");
                }
                System.out.println();
            }

            // Leer dos archivos distintos para concatenar
            ArchivoCSV lector1 = new ArchivoCSV("C:\\Users\\anton\\Downloads\\EJEMPLO.csv");
            ArchivoCSV lector2 = new ArchivoCSV("C:\\Users\\anton\\Downloads\\EJEMPLO2.csv");

            // Crear tablas con los datos de esos archivos
            Tabla tabla1 = new Tabla(lector1.getMap());
            Tabla tabla2 = new Tabla(lector2.getMap());

            // Concatenar tabla1 con tabla2
            Tabla tablaConcatenada = tabla1.concatenar(tabla2);

            // Guardar la tabla concatenada en otro archivo
            String rutaDestinoConcat = "C:\\Users\\anton\\Downloads\\EJEMPLO_CONCATENADO.csv";
            ArchivoCSV escritorConcat = new ArchivoCSV(rutaDestinoConcat);
            escritorConcat.guardarTablaEnCSV(tablaConcatenada, rutaDestinoConcat);

            System.out.println(">> Archivo concatenado guardado en: " + rutaDestinoConcat);

            // 1) Filtrar columnas: provincia = Mendoza
            Predicate<Object> condicionColumnaCiudad = valor -> {
                if (valor instanceof String) {
                    return ((String) valor).equalsIgnoreCase("Mendoza");
                }
                return false;
            };

            Tabla tablaFiltradaColumnas = tabla.filtrarColumnas("Provincia", condicionColumnaCiudad);

            System.out.println("\n=== FILAS DONDE LA PROVINCIA ES 'Mendoza' ===");
            for (String etiquetaFila : tablaFiltradaColumnas.getEtiquetasFilas()) {
                System.out.println(etiquetaFila + ": " + tablaFiltradaColumnas.obtenerFila(etiquetaFila));
            }

            // 2) Filtrar filas: filas donde "Edad" > 30
            Integer indiceEdad = tabla.getMapaColumnas().get("Edad");

            Predicate<List<Object>> condicionFilas = fila -> {
                if (indiceEdad == null || fila.size() <= indiceEdad) return false;
                Object valorEdad = fila.get(indiceEdad);
                if (valorEdad instanceof Integer) {
                    return (Integer) valorEdad > 30;
                }
                return false;
            };

            Tabla tablaFiltradaFilas = tabla.filtrarFilas(condicionFilas);

            System.out.println("\n=== FILAS CON 'Edad' > 30 ===");
            for (String etiquetaFila : tablaFiltradaFilas.getEtiquetasFilas()) {
                System.out.println(etiquetaFila + ": " + tablaFiltradaFilas.obtenerFila(etiquetaFila));
            }

            // Prueba de Copia
            System.out.println("\n=== Iniciando prueba de copia de tabla ===");

            // Crear una copia de la tabla
            Tabla tablaCopia = tabla.copiar();

            // Mostrar las etiquetas de filas de la copia
            System.out.println(">> Etiquetas de las filas en la tabla copiada:");
            System.out.println(tablaCopia.getEtiquetasFilas());

            // Mostrar el contenido de una fila específica en la copia (por ejemplo, F1)
            System.out.println("\n>> Contenido de la fila 'F1' en la copia:");
            System.out.println(tablaCopia.obtenerFila("F1"));

            // Mostrar todas las filas de la copia para verificar que los datos son idénticos
            System.out.println("\n>> Contenido completo de la tabla copiada:");
            for (String etiqueta : tablaCopia.getEtiquetasFilas()) {
                System.out.println(etiqueta + ": " + tablaCopia.obtenerFila(etiqueta));
            }

            // Modificar algo en la copia y verificar que no afecta a la original
            tablaCopia.eliminarFila("F1"); // Eliminar F1 en la copia

            // Mostrar el contenido de una fila específica en la copia
            System.out.println("\n>> Contenido de la fila 'F2' en la copia:");
            System.out.println(tablaCopia.obtenerFila("F2"));
            // Mostrar el contenido de una fila específica en la original
            System.out.println("\n>> Contenido de la fila 'F2' en la tabla original:");
            System.out.println(tabla.obtenerFila("F2"));

            // Probar muestreo aleatorio
            System.out.println("\n=== INICIADO PRUEBA DE MUESTREO ALEATORIO ===");

            // Mostrar la tabla original completa
            System.out.println("\n>> Tabla original completa:");
            tabla.mostrar();

            // Realizar muestreo aleatorio de n filas
            // int n = 5; error no puede ser mayor a la cant filas de tabla
            // int n = -2; error debe ser positivo
            // int n = 0; error debe ser positivo
            int n = 3; // Número de filas a muestrear
            Tabla tablaMuestreada = tabla.muestreoAleatorio(n);

            // Mostrar la tabla muestreada
            System.out.println("\n>> Resultado del muestreo aleatorio (" + n + " filas):");
            tablaMuestreada.mostrar();

            // Verificar que la tabla muestreada tiene el número correcto de filas
            System.out.println("\n>> Cantidad de filas seleccionadas: " + tablaMuestreada.getEtiquetasFilas().size());


            // Prueba Metodos de Columna
            System.out.println("\n=== COPIANDO LAS PRIMERAS FILAS DE LA COLUMNA ===");

            // Obtener una columna específica para probar (por ejemplo, "Edad")
            String nombreColumna = "Edad";
            int indiceColumna = tabla.getMapaColumnas().get(nombreColumna);
            Columna<?> columnaOriginal = tabla.getColumnas().get(indiceColumna);

            System.out.println("\n>> Contenido completo de la columna '" + nombreColumna + "':");
            for (int i = 0; i < columnaOriginal.getCantidadFilas(); i++) {
                System.out.println("Fila " + i + ": " + columnaOriginal.getValor(i));
            }

            // Crear una copia con las primeras 2 filas
            int limite = 2;
            Columna<?> columnaTruncada = columnaOriginal.copiarPrimerasFilas(limite);

            System.out.println("\n>> Contenido de las primeras " + limite + " filas:");
            for (int i = 0; i < columnaTruncada.getCantidadFilas(); i++) {
                System.out.println("Fila " + i + ": " + columnaTruncada.getValor(i));
            }

            // Verificar que la copia tiene el número correcto de filas
            System.out.println("\n>> Número de filas en columna original: " + columnaOriginal.getCantidadFilas());
            System.out.println(">> Número de filas en columna truncada: " + columnaTruncada.getCantidadFilas());

            // Verificar que la original no fue modificada
            System.out.println("\n>> ¿Columna original sin modificaciones? " +
                (columnaOriginal.getCantidadFilas() > columnaTruncada.getCantidadFilas()));

            // Caso especial: límite mayor que el número de filas
            int limiteGrande = columnaOriginal.getCantidadFilas() + 5;
            Columna<?> columnaLimiteGrande = columnaOriginal.copiarPrimerasFilas(limiteGrande);
            System.out.println("\n>> Prueba con límite mayor a la cantidad de filas (" + limiteGrande + "):");
            System.out.println("Filas copiadas: " + columnaLimiteGrande.getCantidadFilas());

            //Prueba de copiarUltimasFilas
            System.out.println("\n=== COPIANDO LAS ÚLTIMAS FILAS DE LA COLUMNA ===");

            // Seleccionar columna para la prueba (usando nombres descriptivos únicos)
            String columnaPrueba = "Edad";
            int posicionColumna = tabla.getMapaColumnas().get(columnaPrueba);
            Columna<?> columnaBase = tabla.getColumnas().get(posicionColumna);

            System.out.println("\n>> Contenido completo de la columna base '" + columnaPrueba + "':");
            for (int indice = 0; indice < columnaBase.getCantidadFilas(); indice++) {
                System.out.println("Registro " + indice + ": " + columnaBase.getValor(indice));
            }

            // Crear copia reducida (últimos registros)
            int ultimosRegistros = 2;
            Columna<?> columnaRecortada = columnaBase.copiarUltimasFilas(ultimosRegistros);

            System.out.println("\n>> Últimos " + ultimosRegistros + " registros copiados de la columna recortada:");
            for (int pos = 0; pos < columnaRecortada.getCantidadFilas(); pos++) {
                System.out.println("Posición " + pos + ": " + columnaRecortada.getValor(pos));
            }

            // Validar integridad de datos
            System.out.println("\n>> Filas en columna base: " + columnaBase.getCantidadFilas());
            System.out.println(">> Filas en columna recortada: " + columnaRecortada.getCantidadFilas());

            System.out.println("\n>> ¿Columna base permanece sin alteraciones? " +
                (columnaBase.getCantidadFilas() > columnaRecortada.getCantidadFilas()));

            // Caso límite: solicitar más registros de los existentes
            int registroExcedente = columnaBase.getCantidadFilas() + 3;
            Columna<?> columnaMaxima = columnaBase.copiarUltimasFilas(registroExcedente);
            System.out.println("\n1. Intento con más registros de los disponibles (" + registroExcedente + "):");
            System.out.println("Filas copiadas: " + columnaMaxima.getCantidadFilas());

            // Caso cero registros
            Columna<?> columnaVacia = columnaBase.copiarUltimasFilas(0);
            System.out.println("\n2. Intento con cero registros:");
            System.out.println("Filas copiadas: " + columnaVacia.getCantidadFilas());

            // Caso valor negativo
            Columna<?> columnaInvalida = columnaBase.copiarUltimasFilas(-5);
            System.out.println("\n3. Intento con valor negativo:");
            System.out.println("Filas copiadas: " + columnaInvalida.getCantidadFilas());

            // Prueba copiarFilasPorIndices
            System.out.println("\n=== Probando copiarFilasPorIndices() ===");

            // Seleccionar columna para la prueba
            String columnaSeleccionada = "Edad";
            int posicionColumna2 = tabla.getMapaColumnas().get(columnaSeleccionada);
            Columna<?> columnaReferencia = tabla.getColumnas().get(posicionColumna2);

            System.out.println("\n>> Contenido completo de la columna referencia (" + columnaSeleccionada + "):");
            for (int idx = 0; idx < columnaReferencia.getCantidadFilas(); idx++) {
                System.out.println("Índice " + idx + ": " + columnaReferencia.getValor(idx));
            }

            // Caso 1: Copiar filas específicas (ej. índices 0 y 2)
            List<Integer> indicesValidos = Arrays.asList(0, 2);
            Columna<?> columnaFiltrada = columnaReferencia.copiarFilasPorIndices(indicesValidos);

            System.out.println("\n>> Caso 1 - Columnas copiadas (índices " + indicesValidos + "):");
            for (int pos = 0; pos < columnaFiltrada.getCantidadFilas(); pos++) {
                System.out.println("Posición " + pos + ": " + columnaFiltrada.getValor(pos));
            }

            // Caso 2: Índice inválido (mayor al tamaño)
            try {
                List<Integer> indicesInvalidos = Arrays.asList(1, 99);
                System.out.println("\n>> Caso 2 - Probando índice inválido:");
                Columna<?> columnaInvalida2 = columnaReferencia.copiarFilasPorIndices(indicesInvalidos);
            } catch (ExcepcionesTabla.ExcepcionIndiceInvalido e) {
                System.out.println("Excepción capturada >> " + e.getMessage());
            }

            // Caso 3: Lista vacía de índices
            List<Integer> indicesVacios = new ArrayList<>();
            Columna<?> columnaVacia2 = columnaReferencia.copiarFilasPorIndices(indicesVacios);
            System.out.println("\n>> Caso 3 - Lista vacía de índices:");
            System.out.println("Registros obtenidos: " + columnaVacia2.getCantidadFilas());

            // Caso 4: Índice negativo
            try {
                List<Integer> indicesNegativos = Arrays.asList(-1, 2);
                System.out.println("\n>> Caso 4 - Probando índice negativo:");
                Columna<?> columnaNegativa = columnaReferencia.copiarFilasPorIndices(indicesNegativos);
            } catch (ExcepcionesTabla.ExcepcionIndiceInvalido e) {
                System.out.println("Excepción capturada >> " + e.getMessage());
            }

            // Verificar integridad de la columna original
            System.out.println("\n=== VERIFICACIÓN FINAL ===");
            System.out.println("\n>> Total en columna referencia: " + columnaReferencia.getCantidadFilas());
            System.out.println(">> ¿Columna referencia permanece intacta? " +
                (columnaReferencia.getCantidadFilas() > columnaFiltrada.getCantidadFilas()));


        } catch (ExcepcionesTabla.ExcepcionColumnasIncompatibles e) {
            System.err.println("Columnas incompatibles: " + e.getMessage());
        } catch (ExcepcionesTabla.ExcepcionFilaNoEncontrada e) {
            System.err.println("Fila no encontrada: " + e.getMessage());
        } catch (ExcepcionesTabla.ExcepcionTipoDato e) {
            System.err.println("Error de tipo de dato: " + e.getMessage());
        } catch (ExcepcionesTabla.ExcepcionLongitudColumna e) {
            System.err.println("Error al insertar columna: " + e.getMessage());
        } catch (ExcepcionesTabla.ExcepcionColumnaNoEncontrada e) {
            System.err.println("Columna no encontrada: " + e.getMessage());
        } catch (ExcepcionesTabla.ExcepcionIndiceInvalido e) {
            System.err.println("Índice de fila inválido: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}