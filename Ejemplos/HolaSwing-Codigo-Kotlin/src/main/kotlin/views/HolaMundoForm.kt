package views

import java.awt.FlowLayout
import javax.swing.*

class HolaMundoForm : JFrame() {
    // Mis componentes
    val panel = JPanel() // Panel principal
    val buttonSaludo = JButton("Pulsa aquí") // Boton
    val textInputNombre = JTextField() // Campo de texto
    val labelNombre = JLabel("Tu nombre:") // Etiqueta

    init {
        // Configuramos la ventana
        title = "Hola Mundo con Codigo Kotlin" // Titulo de la ventana
        defaultCloseOperation = EXIT_ON_CLOSE // Cerrar la ventana
        setSize(400, 100) // Tamaño de la ventana
        setLocationRelativeTo(null) // Centrar la ventana
        isVisible = true

        // Indicamos al panel que use un layout de tipo FlowLayout
        panel.layout = FlowLayout()

        // TextField de un tamaño de 10
        textInputNombre.columns = 10

        // Agregamos los componentes al panel
        panel.add(labelNombre)
        panel.add(textInputNombre)
        panel.add(buttonSaludo)

        // Agregamos el panel a la ventana
        contentPane = panel

        // Agregamos el evento al boton y mostramos el mensaje
        buttonSaludo.addActionListener {
            JOptionPane.showMessageDialog(this, "Hola ${textInputNombre.text}")
        }
    }
}
