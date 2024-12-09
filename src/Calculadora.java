import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculadora {
    private static double resultado = 0;  // Almacena el resultado de la operación
    private static String operador = "";  // Almacena el operador actual
    private static String cuenta = "";    // Almacena la cuenta completa como una cadena
    private static boolean operacionEnCurso = false; // Para saber si estamos en medio de una operación

    public static void main(String[] args) {
        // Crear el marco principal
        JFrame frame = new JFrame("Calculadora");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configurar el tamaño y centrado del marco
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = screenSize.width / 2;
        int frameHeight = 600;
        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);

        // Crear el panel principal con un BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.decode("#0f0f0f"));  // Fondo oscuro

        // Crear panel para la pantalla de entrada
        JPanel pantallaPanel = new JPanel(new BorderLayout());
        JTextField pantallaEntrada = new JTextField();  // Para mostrar la cuenta que se está introduciendo
        pantallaEntrada.setEditable(false);  // Solo lectura
        pantallaEntrada.setFont(new Font("Arial", Font.PLAIN, 40));
        pantallaEntrada.setBackground(Color.decode("#1b1b1b"));  // Fondo gris oscuro
        pantallaEntrada.setForeground(Color.WHITE);  // Texto blanco
        pantallaEntrada.setBorder(BorderFactory.createEmptyBorder()); // Eliminar borde blanco

        // Crear panel para mostrar el dato almacenado
        JTextField pantallaMemoria = new JTextField();  // Para mostrar el dato almacenado
        pantallaMemoria.setEditable(false);  // Solo lectura
        pantallaMemoria.setFont(new Font("Arial", Font.PLAIN, 40));
        pantallaMemoria.setBackground(Color.decode("#1b1b1b"));  // Fondo gris oscuro
        pantallaMemoria.setForeground(Color.WHITE);  // Texto blanco
        pantallaMemoria.setBorder(BorderFactory.createEmptyBorder()); // Eliminar borde blanco

        // Añadir las pantallas de entrada y memoria al panel
        pantallaPanel.add(pantallaEntrada, BorderLayout.CENTER);
        pantallaPanel.add(pantallaMemoria, BorderLayout.NORTH);
        mainPanel.add(pantallaPanel, BorderLayout.NORTH);

        // Crear un JLabel con el nombre del autor
        JLabel autorLabel = new JLabel("Francisco Miser", JLabel.CENTER);
        autorLabel.setFont(new Font("Arial", Font.PLAIN, 14));  // Establecer el tamaño de la fuente
        mainPanel.add(autorLabel, BorderLayout.SOUTH);  // Añadir el nombre del autor al sur del panel principal

        // Crear el panel para los botones numéricos
        JPanel panelNumeros = new JPanel();
        panelNumeros.setLayout(new GridLayout(4, 3, 5, 5)); // 4 filas, 3 columnas
        panelNumeros.setBackground(Color.decode("#0f0f0f"));

        // Crear el panel para los botones de operadores
        JPanel panelOperadores = new JPanel();
        panelOperadores.setLayout(new GridLayout(5, 1, 5, 5)); // 4 filas, 1 columna
        panelOperadores.setBackground(Color.decode("#0f0f0f"));

        // Crear un panel que contiene ambos subpaneles
        JPanel panelBotones = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // Definir los botones
        String[] botonesNumeros = {
            "7", "8", "9",
            "4", "5", "6",
            "1", "2", "3",
            "=", "0", "C"
        };

        String[] botonesOperadores = {"+", "-", "*", "/", ","};

        // Crear los botones numéricos y agregarlos al panel correspondiente
        for (String texto : botonesNumeros) {
            JButton boton = new JButton(texto);
            boton.setBackground(Color.decode("#1b1b1b"));  // Fondo gris oscuro para los botones
            boton.setForeground(Color.WHITE);  // Texto blanco
            boton.setBorder(null);  // Eliminar borde blanco
            boton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String comando = e.getActionCommand();

                    if (comando.equals("C")) {
                        pantallaEntrada.setText("");  
                        pantallaMemoria.setText("");
                        operador = "";
                        cuenta = "";
                        resultado = 0;
                        operacionEnCurso = false;
                    } else if (comando.equals("=")) {
                        try {
                            double valor = Double.parseDouble(pantallaEntrada.getText());
                            cuenta += " " + valor;
                            switch (operador) {
                                case "+":
                                    resultado += valor;
                                    break;
                                case "-":
                                    resultado -= valor;
                                    break;
                                case "*":
                                    resultado *= valor;
                                    break;
                                case "/":
                                    if (valor != 0) {
                                        resultado /= valor;
                                    } else {
                                        pantallaEntrada.setText("Error");
                                        return;
                                    }
                                    break;
                            }
                            pantallaEntrada.setText(String.valueOf(resultado));
                            pantallaMemoria.setText(cuenta); // Mostrar la cuenta completa
                            operador = "";  // Limpiar el operador
                            cuenta = ""; // Limpiar la cuenta
                            operacionEnCurso = false; // Finalizar la operación
                        } catch (NumberFormatException ex) {
                            pantallaEntrada.setText("Error");
                        }
                    } else {
                        pantallaEntrada.setText(pantallaEntrada.getText() + comando);
                    }
                }
            });
            panelNumeros.add(boton);
        }

        // Crear los botones de operadores y agregarlos al panel correspondiente
        for (String texto : botonesOperadores) {
            JButton boton = new JButton(texto);
            boton.setBackground(Color.decode("#fc5500"));  // Fondo anaranjado brillante para los botones de operadores
            boton.setForeground(Color.WHITE);  // Texto blanco
            boton.setBorder(null);  // Eliminar borde blanco
            boton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String comando = e.getActionCommand();

                    if (operacionEnCurso) {
                        double valor = Double.parseDouble(pantallaEntrada.getText());
                        cuenta += " " + valor;
                        switch (operador) {
                            case "+":
                                resultado += valor;
                                break;
                            case "-":
                                resultado -= valor;
                                break;
                            case "*":
                                resultado *= valor;
                                break;
                            case "/":
                                if (valor != 0) {
                                    resultado /= valor;
                                } else {
                                    pantallaEntrada.setText("Error");
                                    return;
                                }
                                break;
                        }
                        pantallaEntrada.setText(String.valueOf(resultado));
                    } else {
                        resultado = Double.parseDouble(pantallaEntrada.getText());
                    }

                    operador = comando;
                    cuenta += " " + pantallaEntrada.getText() + " " + operador;
                    pantallaEntrada.setText("");  // Limpiar pantalla de entrada
                    pantallaMemoria.setText(cuenta); // Mostrar la cuenta
                    operacionEnCurso = true;  // Indicar que estamos en medio de una operación
                }
            });
            panelOperadores.add(boton);
        }

        // Configurar proporciones para los paneles
        gbc.weightx = 0.75; // Proporción del espacio horizontal para el panel de números
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelBotones.add(panelNumeros, gbc);

        gbc.weightx = 0.25; // Proporción del espacio horizontal para el panel de operadores
        gbc.gridx = 1;
        panelBotones.add(panelOperadores, gbc);

        // Añadir el contenedor de botones al panel principal
        mainPanel.add(panelBotones, BorderLayout.CENTER);

        // Agregar el panel principal al marco
        frame.add(mainPanel);

        // Mostrar el marco
        frame.setVisible(true);
    }
}
