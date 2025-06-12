import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        try {
            // Leer archivo CSV original
            ArchivoCSV lector = new ArchivoCSV("C://Users//USUARIO//Downloads//EJEMPLO.csv");
            Tabla tabla = new Tabla(lector.getMap());

            System.out.println("Etiquetas de filas:");
            System.out.println(tabla.getEtiquetasFilas());

            // Mostrar una fila existente
            System.out.println("\nContenido de la fila F1:");
            System.out.println(tabla.obtenerFila("F1"));

            // Insertar nueva fila
            List<Object> nuevaFila = Arrays.asList("Fran", 32, true);
            tabla.insertarFila(nuevaFila);
            System.out.println("\nFila 'Fran' insertada correctamente.");

            // Eliminar una fila por etiqueta
            String etiquetaAEliminar = "F3";
            tabla.eliminarFila(etiquetaAEliminar);
            System.out.println("Fila con etiqueta " + etiquetaAEliminar + " eliminada correctamente.");


            List<String> Provincias = new ArrayList<>();
            Provincias.add("Buenos Aires");// F0
            Provincias.add("Córdoba");// F1
            Provincias.add("Mendoza");// F2
            Provincias.add("Santa Fe");// F3

            tabla.insertarColumna(tabla.getCantidadColumnas(), "Provincia", Provincias);

            // Eliminar una columna por índice
            tabla.eliminarColumna(0); // por ejemplo, elimina la primera
            System.out.println("Primera columna eliminada.");

            // Mostrar valores faltantes
            System.out.println("\nValores faltantes:");
            tabla.mostrarNAs(tabla);

            // Reemplazar valores faltantes por "N/A"
            tabla.reemplazarNAs(tabla, "N/A");
            System.out.println("NAs reemplazados por 'N/A'.");

            // SELECCIONAR columnas y filas específicas
            System.out.println("\nSelección de columnas y filas:");
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
            String rutaDestino = "C://Users//USUARIO//Downloads//EJEMPLO_MODIFICADO.csv";
            ArchivoCSV escritor = new ArchivoCSV(rutaDestino);
            escritor.guardarTablaEnCSV(tabla, rutaDestino);
            System.out.println("Archivo CSV guardado en: " + rutaDestino);

            System.out.println("\nMostrando primeras 2 filas:");
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

            System.out.println("\nMostrando últimas 2 filas:");
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
            ArchivoCSV lector1 = new ArchivoCSV("C://Users//USUARIO//Downloads//EJEMPLO.csv");
            ArchivoCSV lector2 = new ArchivoCSV("C://Users//USUARIO//Downloads//EJEMPLO2.csv");

            // Crear tablas con los datos de esos archivos
            Tabla tabla1 = new Tabla(lector1.getMap());
            Tabla tabla2 = new Tabla(lector2.getMap());

            // Concatenar tabla1 con tabla2
            Tabla tablaConcatenada = tabla1.concatenar(tabla2);

            // Guardar la tabla concatenada en otro archivo
            String rutaDestinoConcat = "C://Users//USUARIO//Downloads//EJEMPLO_CONCATENADO.csv";
            ArchivoCSV escritorConcat = new ArchivoCSV(rutaDestinoConcat);
            escritorConcat.guardarTablaEnCSV(tablaConcatenada, rutaDestinoConcat);

            System.out.println("Archivo concatenado guardado en: " + rutaDestinoConcat);

            // 1) Filtrar columnas: provincia = Mendoza
            Predicate<Object> condicionColumnaCiudad = valor -> {
                if (valor instanceof String) {
                    return ((String) valor).equalsIgnoreCase("Mendoza");
                }
                return false;
            };

            Tabla tablaFiltradaColumnas = tabla.filtrarColumnas("Provincia", condicionColumnaCiudad);

            System.out.println("Filas con provincia = Mendoza:");
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

            System.out.println("\nFilas con Edad > 30:");
            for (String etiquetaFila : tablaFiltradaFilas.getEtiquetasFilas()) {
                System.out.println(etiquetaFila + ": " + tablaFiltradaFilas.obtenerFila(etiquetaFila));
            }
            
            // Prueba de Copia 
            System.out.println("\n=== Probando Copia ===");

            // Crear una copia de la tabla
            Tabla tablaCopia = tabla.copiar();
            System.out.println("\nTabla copiada:");

            // Mostrar las etiquetas de filas de la copia
            System.out.println("Etiquetas de filas de la copia:");
            System.out.println(tablaCopia.getEtiquetasFilas());

            // Mostrar el contenido de una fila específica en la copia (por ejemplo, F1)
            System.out.println("\nContenido de la fila F1 en la copia:");
            System.out.println(tablaCopia.obtenerFila("F1"));

            // Mostrar todas las filas de la copia para verificar que los datos son idénticos
            System.out.println("\nContenido completo de la tabla copiada:");
            for (String etiqueta : tablaCopia.getEtiquetasFilas()) {
                System.out.println(etiqueta + ": " + tablaCopia.obtenerFila(etiqueta));
            }

            // Modificar algo en la copia y verificar que no afecta a la original
            tablaCopia.eliminarFila("F1"); // Eliminar F1 en la copia

            // Mostrar el contenido de una fila específica en la copia 
            System.out.println("\nContenido de la fila F2 en la copia:");
            System.out.println(tablaCopia.obtenerFila("F2"));
            // Mostrar el contenido de una fila específica en la original
            System.out.println("\nContenido de la fila F2 en la tabla original:");
            System.out.println(tabla.obtenerFila("F2"));


            // Prueba Muestreo Aleatorio

            // Probar muestreo aleatorio
            System.out.println("\n=== Probando muestreo aleatorio ===");
            
            // Mostrar la tabla original completa
            System.out.println("\nTabla original:");
            tabla.mostrar();
            
            // Realizar muestreo aleatorio de n filas 
            // int n = 5; error no puede ser mayor a la cant filas de tabla
            // int n = -2; error debe ser positivo
            // int n = 0; error debe ser positivo
            int n = 3; // Número de filas a muestrear
            Tabla tablaMuestreada = tabla.muestreoAleatorio(n);
            
            // Mostrar la tabla muestreada
            System.out.println("\nTabla muestreada (" + n + " filas aleatorias):");
            tablaMuestreada.mostrar();
            
            // Verificar que la tabla muestreada tiene el número correcto de filas
            System.out.println("\nNúmero de filas en tabla muestreada: " + tablaMuestreada.getEtiquetasFilas().size());


            // Prueba Metodos de Columna
            System.out.println("\n=== Probando Métodos de Columna ===");

            System.out.println("\n=== Probando copiarPrimerasFilas() ===");

            // Obtener una columna específica para probar (por ejemplo, "Edad")
            String nombreColumna = "Edad";
            int indiceColumna = tabla.getMapaColumnas().get(nombreColumna);
            Columna<?> columnaOriginal = tabla.getColumnas().get(indiceColumna);
            
            System.out.println("\nContenido completo de la columna original (" + nombreColumna + "):");
            for (int i = 0; i < columnaOriginal.getCantidadFilas(); i++) {
                System.out.println("Fila " + i + ": " + columnaOriginal.getValor(i));
            }
            
            // Crear una copia con las primeras 2 filas
            int limite = 2;
            Columna<?> columnaTruncada = columnaOriginal.copiarPrimerasFilas(limite);
            
            System.out.println("\nContenido de la columna truncada (primeras " + limite + " filas):");
            for (int i = 0; i < columnaTruncada.getCantidadFilas(); i++) {
                System.out.println("Fila " + i + ": " + columnaTruncada.getValor(i));
            }
            
            // Verificar que la copia tiene el número correcto de filas
            System.out.println("\nNúmero de filas en columna original: " + columnaOriginal.getCantidadFilas());
            System.out.println("Número de filas en columna truncada: " + columnaTruncada.getCantidadFilas());
            
            // Verificar que la original no fue modificada
            System.out.println("\n¿La columna original sigue intacta? " + 
                (columnaOriginal.getCantidadFilas() > columnaTruncada.getCantidadFilas()));
            
            // Caso especial: límite mayor que el número de filas
            int limiteGrande = columnaOriginal.getCantidadFilas() + 5;
            Columna<?> columnaLimiteGrande = columnaOriginal.copiarPrimerasFilas(limiteGrande);
            System.out.println("\nProbando con límite mayor que filas existentes (" + limiteGrande + "):");
            System.out.println("Filas obtenidas: " + columnaLimiteGrande.getCantidadFilas());

            //Prueba de copiarUltimasFilas
            System.out.println("\n=== Probando copiarUltimasFilas() ===");
    
            // Seleccionar columna para la prueba (usando nombres descriptivos únicos)
            String columnaPrueba = "Edad";
            int posicionColumna = tabla.getMapaColumnas().get(columnaPrueba);
            Columna<?> columnaBase = tabla.getColumnas().get(posicionColumna);
            
            System.out.println("\nDatos completos de la columna base (" + columnaPrueba + "):");
            for (int indice = 0; indice < columnaBase.getCantidadFilas(); indice++) {
                System.out.println("Registro " + indice + ": " + columnaBase.getValor(indice));
            }
            
            // Crear copia reducida (últimos registros)
            int ultimosRegistros = 2;
            Columna<?> columnaRecortada = columnaBase.copiarUltimasFilas(ultimosRegistros);
            
            System.out.println("\nContenido de la columna recortada (últimos " + ultimosRegistros + " registros):");
            for (int pos = 0; pos < columnaRecortada.getCantidadFilas(); pos++) {
                System.out.println("Posición " + pos + ": " + columnaRecortada.getValor(pos));
            }
            
            // Validar integridad de datos
            System.out.println("\nTotal en columna base: " + columnaBase.getCantidadFilas());
            System.out.println("Total en columna recortada: " + columnaRecortada.getCantidadFilas());
            
            System.out.println("\n¿La columna base permanece inalterada? " + 
                (columnaBase.getCantidadFilas() > columnaRecortada.getCantidadFilas()));
            
            // Caso límite: solicitar más registros de los existentes
            int registroExcedente = columnaBase.getCantidadFilas() + 3;
            Columna<?> columnaMaxima = columnaBase.copiarUltimasFilas(registroExcedente);
            System.out.println("\nPrueba con límite excedente (" + registroExcedente + "):");
            System.out.println("Registros obtenidos: " + columnaMaxima.getCantidadFilas());
            
            // Caso cero registros
            Columna<?> columnaVacia = columnaBase.copiarUltimasFilas(0);
            System.out.println("\nPrueba con cero registros solicitados:");
            System.out.println("Registros obtenidos: " + columnaVacia.getCantidadFilas());
            
            // Caso valor negativo
            Columna<?> columnaInvalida = columnaBase.copiarUltimasFilas(-5);
            System.out.println("\nPrueba con valor negativo:");
            System.out.println("Registros obtenidos: " + columnaInvalida.getCantidadFilas());

            // Prueba copiarFilasPorIndices
            System.out.println("\n=== Probando copiarFilasPorIndices() ===");
    
            // Seleccionar columna para la prueba
            String columnaSeleccionada = "Edad";
            int posicionColumna2 = tabla.getMapaColumnas().get(columnaSeleccionada);
            Columna<?> columnaReferencia = tabla.getColumnas().get(posicionColumna2);

            System.out.println("\nContenido completo de la columna referencia (" + columnaSeleccionada + "):");
            for (int idx = 0; idx < columnaReferencia.getCantidadFilas(); idx++) {
                System.out.println("Índice " + idx + ": " + columnaReferencia.getValor(idx));
            }
            
            // Caso 1: Copiar filas específicas (ej. índices 0 y 2)
            List<Integer> indicesValidos = Arrays.asList(0, 2);
            Columna<?> columnaFiltrada = columnaReferencia.copiarFilasPorIndices(indicesValidos);
            
            System.out.println("\nCaso 1 - Columnas copiadas (índices " + indicesValidos + "):");
            for (int pos = 0; pos < columnaFiltrada.getCantidadFilas(); pos++) {
                System.out.println("Posición " + pos + ": " + columnaFiltrada.getValor(pos));
            }
            
            // Caso 2: Índice inválido (mayor al tamaño)
            try {
                List<Integer> indicesInvalidos = Arrays.asList(1, 99);
                System.out.println("\nCaso 2 - Probando índice inválido:");
                Columna<?> columnaInvalida2 = columnaReferencia.copiarFilasPorIndices(indicesInvalidos);
            } catch (ExcepcionesTabla.ExcepcionIndiceInvalido e) {
                System.out.println("Correcto: Excepción capturada - " + e.getMessage());
            }
            
            // Caso 3: Lista vacía de índices
            List<Integer> indicesVacios = new ArrayList<>();
            Columna<?> columnaVacia2 = columnaReferencia.copiarFilasPorIndices(indicesVacios);
            System.out.println("\nCaso 3 - Lista vacía de índices:");
            System.out.println("Registros obtenidos: " + columnaVacia2.getCantidadFilas());
            
            // Caso 4: Índice negativo
            try {
                List<Integer> indicesNegativos = Arrays.asList(-1, 2);
                System.out.println("\nCaso 4 - Probando índice negativo:");
                Columna<?> columnaNegativa = columnaReferencia.copiarFilasPorIndices(indicesNegativos);
            } catch (ExcepcionesTabla.ExcepcionIndiceInvalido e) {
                System.out.println("Correcto: Excepción capturada - " + e.getMessage());
            }
            
            // Verificar integridad de la columna original
            System.out.println("\nVerificación final:");
            System.out.println("Total en columna referencia: " + columnaReferencia.getCantidadFilas());
            System.out.println("¿Columna referencia permanece intacta? " + 
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