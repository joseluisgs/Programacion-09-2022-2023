package dev.joseluisgs.views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HolaSwingDesigner extends JFrame {
    private JPanel panelMain;
    private JButton botonSaludo;
    private JLabel labelNombre;
    private JTextField textNombre;

    public HolaSwingDesigner() {
        super("Hola Swing Designer");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack(); // Ajusta el tamaño de la ventana al tamaño de los componentes
        // Centramos la ventana
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        botonSaludo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Hola " + textNombre.getText());
            }
        });
    }
}
