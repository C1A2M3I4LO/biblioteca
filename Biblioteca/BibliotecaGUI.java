/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.bibliotecagui;

/**
 *
 * @author Felipe
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BibliotecaGUI extends JFrame {
    private Libro[] libros; // Arreglo para almacenar los libros
    private int numLibros; // Contador de libros

    // Componentes de la interfaz
    private JTextField txtTitulo, txtAutor, txtBuscar;
    private JButton btnAgregar, btnBuscar;
    private JPanel panelLibros;
    private int libroSeleccionadoIndex = -1;

    // Constructor de la interfaz
    public BibliotecaGUI() {
        libros = new Libro[100]; // Se inicializa con capacidad para 100 libros
        numLibros = 0;

        setTitle("Gestión de Biblioteca");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de entrada con campos de texto y botones
        JPanel panelEntrada = new JPanel();
        panelEntrada.setLayout(new GridLayout(4, 2));
        
        panelEntrada.add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        panelEntrada.add(txtTitulo);

        panelEntrada.add(new JLabel("Autor:"));
        txtAutor = new JTextField();
        panelEntrada.add(txtAutor);
        
        panelEntrada.add(new JLabel("Buscar por título o autor:"));
        txtBuscar = new JTextField();
        panelEntrada.add(txtBuscar);

        btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> agregarLibro());
        panelEntrada.add(btnAgregar);

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarLibros(txtBuscar.getText()));
        panelEntrada.add(btnBuscar);

        add(panelEntrada, BorderLayout.NORTH);

        // Panel para mostrar la lista de libros
        panelLibros = new JPanel();
        panelLibros.setLayout(new BoxLayout(panelLibros, BoxLayout.Y_AXIS)); // Eje Y para la lista de libros
        JScrollPane scrollPane = new JScrollPane(panelLibros);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Método para agregar un nuevo libro
    private void agregarLibro() {
        String titulo = txtTitulo.getText();
        String autor = txtAutor.getText();

        if (!titulo.isEmpty() && !autor.isEmpty()) {
            if (numLibros < libros.length) {
                libros[numLibros++] = new Libro(titulo, autor);
                txtTitulo.setText("");
                txtAutor.setText("");
                actualizarListaLibros();
                JOptionPane.showMessageDialog(this, "Libro agregado correctamente");
            } else {
                JOptionPane.showMessageDialog(this, "La biblioteca está llena, no se pueden agregar más libros.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos");
        }
    }

    // Método para buscar libros por título o autor
    private void buscarLibros(String textoBusqueda) {
        panelLibros.removeAll();
        for (int i = 0; i < numLibros; i++) {
            Libro libro = libros[i];
            if (libro.getTitulo().toLowerCase().contains(textoBusqueda.toLowerCase()) ||
                libro.getAutor().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                agregarPanelLibro(libro);
            }
        }
        panelLibros.revalidate();
        panelLibros.repaint();
    }

    // Método para actualizar la lista de libros mostrada en la interfaz
    private void actualizarListaLibros() {
        panelLibros.removeAll();
        for (int i = 0; i < numLibros; i++) {
            agregarPanelLibro(libros[i]);
        }
        panelLibros.revalidate();
        panelLibros.repaint();
    }

    // Método para agregar un panel que representa un libro en la interfaz
    private void agregarPanelLibro(Libro libro) {
        JPanel panelLibro = new JPanel();
        panelLibro.setLayout(new BoxLayout(panelLibro, BoxLayout.X_AXIS)); // Eje X para cada libro
        panelLibro.setBorder(BorderFactory.createEtchedBorder());

        // JLabel para mostrar el título y autor del libro
        JLabel labelLibro = new JLabel(libro.toString());
        panelLibro.add(labelLibro);

        // Botones para editar, eliminar y alquilar/devolver el libro
        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarLibro(libro));
        panelLibro.add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarLibro(libro));
        panelLibro.add(btnEliminar);

        JButton btnAlquilar = new JButton(libro.isAlquilado() ? "Devolver" : "Alquilar");
        btnAlquilar.addActionListener(e -> {
            if (!libro.isAlquilado()) {
                libro.setAlquilado(true);
                actualizarListaLibros();
                // Cambiar color del nombre del libro y autor a rojo cuando se alquila
                labelLibro.setForeground(Color.RED);
            } else {
                libro.setAlquilado(false);
                actualizarListaLibros();
                // Restaurar color original del nombre del libro y autor
                labelLibro.setForeground(Color.BLACK);
            }
        });
        panelLibro.add(btnAlquilar);

        panelLibros.add(panelLibro);
    }

    // Método para editar un libro
    private void editarLibro(Libro libro) {
        String titulo = JOptionPane.showInputDialog(this, "Nuevo título:", libro.getTitulo());
        String autor = JOptionPane.showInputDialog(this, "Nuevo autor:", libro.getAutor());

        if (titulo != null && autor != null) {
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            actualizarListaLibros();
        }
    }

    // Método para eliminar un libro
    private void eliminarLibro(Libro libro) {
        int index = -1;
        for (int i = 0; i < numLibros; i++) {
            if (libros[i] == libro) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            for (int i = index; i < numLibros - 1; i++) {
                libros[i] = libros[i + 1];
            }
            numLibros--;
            actualizarListaLibros();
        }
    }

    // Método principal para iniciar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BibliotecaGUI().setVisible(true));
    }
}

// Clase que representa un libro
class Libro {
    private String titulo;
    private String autor;
    private boolean alquilado; // Indica si el libro está alquilado o no

    // Constructor de la clase Libro
    public Libro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
        this.alquilado = false; // Inicialmente el libro no está alquilado
    }

    // Métodos para obtener y establecer el título del libro
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Métodos para obtener y establecer el autor del libro
    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    // Método para verificar si el libro está alquilado
    public boolean isAlquilado() {
        return alquilado;
    }

    // Método para establecer el estado de alquiler del libro
    public void setAlquilado(boolean alquilado) {
        this.alquilado = alquilado;
    }

    // Método para representar el libro como una cadena de texto
    @Override
    public String toString() {
        // Se muestra el título y autor del libro, y si está alquilado se agrega un indicador
        return "Título: " + titulo + ", Autor: " + autor + (alquilado ? " (Alquilado)" : "");
    }
}
