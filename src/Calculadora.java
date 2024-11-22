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
        frame.setResizable(false);

        // Crear el panel principal con un BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Crear panel para la pantalla de entrada
        JPanel pantallaPanel = new JPanel(new BorderLayout());
        JTextField pantallaEntrada = new JTextField();  // Para mostrar la cuenta que se está introduciendo
        pantallaEntrada.setEditable(false);  // Solo lectura
        pantallaEntrada.setFont(new Font("Arial", Font.PLAIN, 30));

        // Crear panel para mostrar el dato almacenado
        JTextField pantallaMemoria = new JTextField();  // Para mostrar el dato almacenado
        pantallaMemoria.setEditable(false);  // Solo lectura
        pantallaMemoria.setFont(new Font("Arial", Font.PLAIN, 20));

        // Añadir las pantallas de entrada y memoria al panel
        pantallaPanel.add(pantallaEntrada, BorderLayout.CENTER);
        pantallaPanel.add(pantallaMemoria, BorderLayout.NORTH);
        mainPanel.add(pantallaPanel, BorderLayout.NORTH);

        // Crear un JLabel con el nombre del autor
        JLabel autorLabel = new JLabel("Francisco Miser", JLabel.CENTER);
        autorLabel.setFont(new Font("Arial", Font.PLAIN, 14));  // Establecer el tamaño de la fuente
        mainPanel.add(autorLabel, BorderLayout.SOUTH);  // Añadir el nombre del autor al sur del panel principal

        // Crear el panel para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(4, 4, 5, 5)); // 4 filas, 4 columnas

        // Definir los botones
        String[] botones = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", "C", "=", "+"
        };

        // Crear los botones y agregarlos al panel
        for (String texto : botones) {
            JButton boton = new JButton(texto);
            boton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String comando = e.getActionCommand();

                    // Acciones para cada tipo de botón
                    if (comando.equals("C")) {
                        // Limpiar pantalla de entrada y restablecer valores
                        pantallaEntrada.setText("");  
                        pantallaMemoria.setText("");
                        operador = "";
                        cuenta = "";
                        resultado = 0;
                        operacionEnCurso = false;
                    } else if (comando.equals("=")) {
                        // Realizar la operación cuando se presiona "="
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
                    } else if ("+-*/".contains(comando)) {
                        // Si ya hay una operación en curso, calculamos la operación anterior
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

                        // Establecer el nuevo operador
                        operador = comando;
                        cuenta += " " + pantallaEntrada.getText() + " " + operador;
                        pantallaEntrada.setText("");  // Limpiar pantalla de entrada
                        pantallaMemoria.setText(cuenta); // Mostrar la cuenta
                        operacionEnCurso = true;  // Indicar que estamos en medio de una operación
                    } else {
                        // Agregar el texto del botón a la pantalla de entrada
                        pantallaEntrada.setText(pantallaEntrada.getText() + comando);
                    }
                }
            });
            panelBotones.add(boton);
        }

        // Añadir el panel de botones al panel principal
        mainPanel.add(panelBotones, BorderLayout.CENTER);

        // Agregar el panel principal al marco
        frame.add(mainPanel);

        // Mostrar el marco
        frame.setVisible(true);
    }
}
