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
            String rutaDestino = "C:\\Users\\anton\\Downloads\\EJEMPLO_MODIFICADO.csv";
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