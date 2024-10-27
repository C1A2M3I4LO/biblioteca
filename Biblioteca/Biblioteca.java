import javax.swing.*;
import java.util.*;

class Usuario {
    String nombre;
    int id;

    public Usuario(String nombre, int id) {
        this.nombre = nombre;
        this.id = id;
    }
}

class Libro {
    int id;
    String titulo;
    String autor;
    boolean prestado;

    public Libro(int id, String titulo, String autor) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.prestado = false;
    }
}

class Grafo {
    private Map<Integer, Map<Integer, Integer>> adjList; // Nodo origen -> (Nodo destino -> peso)

    public Grafo() {
        adjList = new HashMap<>();
    }

    public void insertarNodo(int id) {
        adjList.putIfAbsent(id, new HashMap<>());
    }

    public void insertarArista(int origen, int destino, int peso) {
        adjList.get(origen).put(destino, peso);
    }

    public void eliminarArista(int origen, int destino) {
        if (adjList.containsKey(origen)) {
            adjList.get(origen).remove(destino);
        }
    }

    public void eliminarNodo(int id) {
        adjList.values().forEach(e -> e.remove(id));
        adjList.remove(id);
    }

    public Map<Integer, Integer> obtenerAdyacentes(int id) {
        return adjList.get(id);
    }

    public boolean contieneNodo(int id) {
        return adjList.containsKey(id);
    }

    public String mostrarGrafo() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : adjList.entrySet()) {
            sb.append("Nodo ").append(entry.getKey()).append(":\n");
            for (Map.Entry<Integer, Integer> adj : entry.getValue().entrySet()) {
                sb.append("  -> ").append(adj.getKey()).append(" (peso: ").append(adj.getValue()).append(")\n");
            }
        }
        return sb.toString();
    }
}

public class Biblioteca {
    static Grafo grafo = new Grafo();
    static Map<Integer, Usuario> usuarios = new HashMap<>();
    static Map<Integer, Libro> libros = new HashMap<>();
    static int ultimoIdUsuario = 0;

    public static void main(String[] args) {
        short opcion = 0;

        do {
            try {
                opcion = Short.parseShort(JOptionPane.showInputDialog(null, "Escoga lo que desea hacer en la biblioteca:\n"
                        + "1. Agregar Libro.\n"
                        + "2. Agregar Usuario.\n"
                        + "3. Mostrar Grafo.\n"
                        + "4. Prestar Libro.\n"
                        + "5. Devolver Libro.\n"
                        + "6. Eliminar Libro.\n"
                        + "7. Eliminar Usuario.\n"
                        + "8. Salir", "Biblioteca", JOptionPane.PLAIN_MESSAGE));

                switch (opcion) {
                    case 1:
                        int idLibro = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el ID del libro:", "Agregando Libro", JOptionPane.PLAIN_MESSAGE));
                        String titulo = JOptionPane.showInputDialog(null, "Ingrese el título del libro:", "Agregando Libro", JOptionPane.PLAIN_MESSAGE);
                        String autor = JOptionPane.showInputDialog(null, "Ingrese el autor del libro:", "Agregando Libro", JOptionPane.PLAIN_MESSAGE);
                        Libro libro = new Libro(idLibro, titulo, autor);
                        libros.put(idLibro, libro);
                        grafo.insertarNodo(idLibro);
                        break;

                    case 2:
                        String nombreUsuario = JOptionPane.showInputDialog(null, "Ingrese su nombre:", "Crear Usuario", JOptionPane.PLAIN_MESSAGE);
                        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
                            ultimoIdUsuario++;
                            Usuario usuario = new Usuario(nombreUsuario, ultimoIdUsuario);
                            usuarios.put(ultimoIdUsuario, usuario);
                            grafo.insertarNodo(ultimoIdUsuario);
                        } else {
                            JOptionPane.showMessageDialog(null, "Debe ingresar un nombre válido.", "Crear Usuario", JOptionPane.ERROR_MESSAGE);
                        }
                        break;

                    case 3:
                        JOptionPane.showMessageDialog(null, "Estructura del Grafo:\n" + grafo.mostrarGrafo(), "Biblioteca", JOptionPane.PLAIN_MESSAGE);
                        break;

                    case 4:
                        int idUsuarioPrestamo = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el ID del usuario:", "Prestar Libro", JOptionPane.PLAIN_MESSAGE));
                        int idLibroPrestamo = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el ID del libro que desea prestar:", "Prestar Libro", JOptionPane.PLAIN_MESSAGE));
                        if (usuarios.containsKey(idUsuarioPrestamo) && libros.containsKey(idLibroPrestamo) && !libros.get(idLibroPrestamo).prestado) {
                            grafo.insertarArista(idUsuarioPrestamo, idLibroPrestamo, 1);
                            libros.get(idLibroPrestamo).prestado = true;
                            JOptionPane.showMessageDialog(null, "Libro prestado exitosamente.", "Prestar Libro", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "No se puede prestar el libro. Verifique los IDs ingresados.", "Prestar Libro", JOptionPane.ERROR_MESSAGE);
                        }
                        break;

                    case 5:
                        int idUsuarioDevolucion = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el ID del usuario:", "Devolver Libro", JOptionPane.PLAIN_MESSAGE));
                        int idLibroDevolucion = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el ID del libro que desea devolver:", "Devolver Libro", JOptionPane.PLAIN_MESSAGE));
                        if (grafo.contieneNodo(idUsuarioDevolucion) && grafo.contieneNodo(idLibroDevolucion) && libros.get(idLibroDevolucion).prestado) {
                            grafo.eliminarArista(idUsuarioDevolucion, idLibroDevolucion);
                            libros.get(idLibroDevolucion).prestado = false;
                            JOptionPane.showMessageDialog(null, "Libro devuelto exitosamente.", "Devolver Libro", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "No se puede devolver el libro. Verifique los IDs ingresados.", "Devolver Libro", JOptionPane.ERROR_MESSAGE);
                        }
                        break;

                    case 6:
                        int idLibroElim = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el ID del libro a eliminar:", "Eliminar Libro", JOptionPane.PLAIN_MESSAGE));
                        grafo.eliminarNodo(idLibroElim);
                        libros.remove(idLibroElim);
                        break;

                    case 7:
                        int idUsuarioElim = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el ID del usuario a eliminar:", "Eliminar Usuario", JOptionPane.PLAIN_MESSAGE));
                        grafo.eliminarNodo(idUsuarioElim);
                        usuarios.remove(idUsuarioElim);
                        break;

                    case 8:
                        JOptionPane.showMessageDialog(null, "Gracias por usar la aplicación. ¡Hasta luego!", "Biblioteca", JOptionPane.PLAIN_MESSAGE);
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "La opción ingresada no es válida. Por favor, seleccione una opción válida.", "Biblioteca", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error al ingresar el número. Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } while (opcion != 8);
    }
}
