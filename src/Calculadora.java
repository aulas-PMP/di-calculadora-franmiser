// Importar las bibliotecas necesarias para la interfaz gráfica y otros componentes
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Calculadora {

    // Variables globales para almacenar el estado de la calculadora
    private static double resultado = 0;  // Almacena el resultado de la operación
    private static String operador = "";  // Almacena el operador actual
    private static String cuenta = "";    // Almacena la cuenta completa como una cadena
    private static boolean operacionEnCurso = false; // Para saber si estamos en medio de una operación

    // Formato para mostrar los resultados decimales con comas
    private static final DecimalFormat formatoDecimal;
    private static String modoEntrada = "Ratón"; // Por defecto, entrada por ratón

    // Establecer el formato de decimales con coma como separador decimal
    static {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator(',');
        formatoDecimal = new DecimalFormat("#.########", simbolos);
    }

    public static void main(String[] args) {
        // Crear el marco principal de la ventana
        JFrame frame = new JFrame("Calculadora de Francisco Miser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configurar el tamaño y centrado del marco
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = screenSize.width / 2;
        int frameHeight = 600;
        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);  // Centrar la ventana
        frame.setResizable(true);  // Hacer la ventana redimensionable

        // Crear el panel principal con un BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.decode("#0f0f0f"));  // Fondo oscuro

        // Crear panel para la pantalla de entrada (donde el usuario escribe)
        JPanel pantallaPanel = new JPanel(new BorderLayout());
        JTextField pantallaEntrada = new JTextField();  // Para mostrar la cuenta que se está introduciendo
        pantallaEntrada.setEditable(false);  // Solo lectura, no editable por el usuario
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

        // Crear un panel para mostrar el modo de entrada actual
        JPanel panelModoEntrada = new JPanel(new BorderLayout());
        panelModoEntrada.setBackground(Color.decode("#0f0f0f"));
        JLabel labelModoEntrada = new JLabel("Modo de Entrada: " + modoEntrada);
        labelModoEntrada.setForeground(Color.WHITE);
        labelModoEntrada.setFont(new Font("Arial", Font.PLAIN, 16));
        panelModoEntrada.add(labelModoEntrada, BorderLayout.CENTER);

        // Agregar botones para cambiar el modo de entrada (Ratón, Teclado Numérico, Libre)
        JPanel panelCambioModo = new JPanel();
        panelCambioModo.setBackground(Color.decode("#0f0f0f"));
        JButton botonRaton = new JButton("Ratón");
        JButton botonTeclado = new JButton("Teclado Numérico");
        JButton botonLibre = new JButton("Libre");

        ActionListener cambiarModoListener = e -> {
            modoEntrada = ((JButton) e.getSource()).getText();
            labelModoEntrada.setText("Modo de Entrada: " + modoEntrada);
            pantallaEntrada.requestFocusInWindow(); // Asegurar que pantallaEntrada tenga el foco
        };

        // Asignar los escuchadores de evento a los botones para cambiar el modo
        botonRaton.addActionListener(cambiarModoListener);
        botonTeclado.addActionListener(cambiarModoListener);
        botonLibre.addActionListener(cambiarModoListener);

        // Aplicar el mismo estilo de botones a los botones de selección de modo
        JButton[] botonesModo = {botonRaton, botonTeclado, botonLibre};
        for (JButton boton : botonesModo) {
            boton.setBackground(Color.decode("#1b1b1b"));  // Mismo color que los botones numéricos
            boton.setForeground(Color.WHITE);  // Texto blanco
            boton.setBorder(null);  // Eliminar borde
        }

        panelCambioModo.add(botonRaton);
        panelCambioModo.add(botonTeclado);
        panelCambioModo.add(botonLibre);
        panelModoEntrada.add(panelCambioModo, BorderLayout.SOUTH);
        mainPanel.add(panelModoEntrada, BorderLayout.SOUTH);

        // Crear el panel para los botones numéricos
        JPanel panelNumeros = new JPanel();
        panelNumeros.setLayout(new GridLayout(4, 3, 5, 5)); // 4 filas, 3 columnas
        panelNumeros.setBackground(Color.decode("#0f0f0f"));

        // Crear el panel para los botones de operadores (suma, resta, multiplicación, división)
        JPanel panelOperadores = new JPanel();
        panelOperadores.setLayout(new GridLayout(5, 1, 5, 5)); // 4 filas, 1 columna
        panelOperadores.setBackground(Color.decode("#0f0f0f"));

        // Crear un panel que contiene ambos subpaneles (botones numéricos y operadores)
        JPanel panelBotones = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // Definir los botones numéricos
        String[] botonesNumeros = {
            "7", "8", "9",
            "4", "5", "6",
            "1", "2", "3",
            "C", "0", "="
        };

        // Definir los botones de operadores (suma, resta, multiplicación, división, coma)
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
                    if (modoEntrada.equals("Ratón") || modoEntrada.equals("Libre")) {
                        String comando = e.getActionCommand();

                        // Limpiar la pantalla de entrada
                        if (comando.equals("C")) {
                            pantallaEntrada.setText("");  
                            pantallaMemoria.setText("");
                            operador = "";
                            cuenta = "";
                            resultado = 0;
                            operacionEnCurso = false;
                        } 
                        // Realizar la operación al presionar el botón igual
                        else if (comando.equals("=")) {
                            try {
                                double valor = Double.parseDouble(pantallaEntrada.getText().replace(',', '.'));
                                cuenta += " " + formatoDecimal.format(valor);
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
                                pantallaEntrada.setText(formatoDecimal.format(resultado));
                                if (resultado < 0) {
                                    pantallaEntrada.setForeground(Color.RED);  // Números negativos en rojo
                                } else {
                                    pantallaEntrada.setForeground(Color.WHITE);
                                }
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
                    if (modoEntrada.equals("Ratón") || modoEntrada.equals("Libre")) {
                        String comando = e.getActionCommand();

                        // Agregar coma (punto decimal) al número actual
                        if (comando.equals(",")) {
                            if (!pantallaEntrada.getText().contains(",")) {
                                pantallaEntrada.setText(pantallaEntrada.getText() + ",");
                            }
                            return;
                        }

                        // Si ya hay una operación en curso, realizarla
                        if (operacionEnCurso) {
                            double valor = Double.parseDouble(pantallaEntrada.getText().replace(',', '.'));
                            cuenta += " " + formatoDecimal.format(valor);
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
                            pantallaEntrada.setText(formatoDecimal.format(resultado));
                            if (resultado < 0) {
                                pantallaEntrada.setForeground(Color.RED);  // Números negativos en rojo
                            } else {
                                pantallaEntrada.setForeground(Color.WHITE);
                            }
                        } else {
                            resultado = Double.parseDouble(pantallaEntrada.getText().replace(',', '.'));
                        }

                        // Guardar el operador y actualizar la cuenta
                        operador = comando;
                        cuenta += " " + pantallaEntrada.getText() + " " + operador;
                        pantallaEntrada.setText("");  // Limpiar pantalla de entrada
                        pantallaMemoria.setText(cuenta); // Mostrar la cuenta
                        operacionEnCurso = true;  // Indicar que estamos en medio de una operación
                    }
                }
            });
            panelOperadores.add(boton);
        }

        // Configurar proporciones para los paneles de botones
        gbc.weightx = 0.75; // Panel numérico ocupará el 75% del ancho
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelBotones.add(panelNumeros, gbc);

        gbc.weightx = 0.25; // Panel de operadores ocupará el 25% del ancho
        gbc.gridx = 1;
        panelBotones.add(panelOperadores, gbc);

        // Añadir el panel de botones al panel principal
        mainPanel.add(panelBotones, BorderLayout.CENTER);

        // Agregar el panel principal al marco
        frame.add(mainPanel);

        // Agregar manejo de teclado para la entrada numérica
        pantallaEntrada.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (modoEntrada.equals("Teclado Numérico") || modoEntrada.equals("Libre")) {
                    char keyChar = e.getKeyChar();
                    int keyCode = e.getKeyCode();

                    // Verificar si es un número o la coma (punto decimal)
                    if (Character.isDigit(keyChar) || keyChar == ',' ||
                        (keyCode >= KeyEvent.VK_NUMPAD0 && keyCode <= KeyEvent.VK_NUMPAD9)) {

                        // Convertir teclas del teclado numérico a su valor correspondiente
                        if (keyCode >= KeyEvent.VK_NUMPAD0 && keyCode <= KeyEvent.VK_NUMPAD9) {
                            keyChar = (char) ('0' + (keyCode - KeyEvent.VK_NUMPAD0));
                        }

                        pantallaEntrada.setText(pantallaEntrada.getText() + keyChar);
                    }
                    // Verificar si la tecla presionada es un operador
                    else if (keyCode == KeyEvent.VK_ADD || keyCode == KeyEvent.VK_SUBTRACT ||
                             keyCode == KeyEvent.VK_MULTIPLY || keyCode == KeyEvent.VK_DIVIDE) {

                        // Determinar el operador según la tecla presionada
                        String operador = "";
                        if (keyCode == KeyEvent.VK_ADD) {
                            operador = "+";
                        } else if (keyCode == KeyEvent.VK_SUBTRACT) {
                            operador = "-";
                        } else if (keyCode == KeyEvent.VK_MULTIPLY) {
                            operador = "*";
                        } else if (keyCode == KeyEvent.VK_DIVIDE) {
                            operador = "/";
                        }

                        // Si ya hay una operación en curso, realizarla
                        if (operacionEnCurso) {
                            double valor = Double.parseDouble(pantallaEntrada.getText().replace(',', '.'));
                            cuenta += " " + formatoDecimal.format(valor);
                            switch (Calculadora.operador) {
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
                            pantallaEntrada.setText(formatoDecimal.format(resultado));
                            if (resultado < 0) {
                                pantallaEntrada.setForeground(Color.RED);  // Números negativos en rojo
                            } else {
                                pantallaEntrada.setForeground(Color.WHITE);
                            }
                        } else {
                            resultado = Double.parseDouble(pantallaEntrada.getText().replace(',', '.'));
                        }

                        // Guardar el operador y actualizar la cuenta
                        Calculadora.operador = operador;
                        cuenta += " " + pantallaEntrada.getText() + " " + operador;
                        pantallaEntrada.setText("");  // Limpiar pantalla de entrada
                        pantallaMemoria.setText(cuenta); // Mostrar la cuenta
                        operacionEnCurso = true;  // Indicar que estamos en medio de una operación
                    }
                    // Verificar si la tecla es el Enter (igual)
                    else if (keyCode == KeyEvent.VK_ENTER) {
                        try {
                            double valor = Double.parseDouble(pantallaEntrada.getText().replace(',', '.'));
                            cuenta += " " + formatoDecimal.format(valor);
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
                            pantallaEntrada.setText(formatoDecimal.format(resultado));
                            if (resultado < 0) {
                                pantallaEntrada.setForeground(Color.RED);  // Números negativos en rojo
                            } else {
                                pantallaEntrada.setForeground(Color.WHITE);
                            }
                            pantallaMemoria.setText(cuenta); // Mostrar la cuenta
                            operador = "";  // Limpiar el operador
                            cuenta = ""; // Limpiar la cuenta
                            operacionEnCurso = false; // Finalizar la operación
                        } catch (NumberFormatException ex) {
                            pantallaEntrada.setText("Error");
                        }
                    }
                    // Verificar si la tecla es la tecla "Escape" (borrar todo)
                    else if (keyCode == KeyEvent.VK_ESCAPE) {
                        pantallaEntrada.setText("");  
                        pantallaMemoria.setText("");
                        operador = "";
                        cuenta = "";
                        resultado = 0;
                        operacionEnCurso = false;
                    }
                }
            }
        });

        // Hacer visible la ventana
        frame.setVisible(true);
    }
}
