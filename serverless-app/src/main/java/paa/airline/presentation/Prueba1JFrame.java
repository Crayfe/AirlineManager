package paa.airline.presentation;
import java.awt.HeadlessException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Prueba1JFrame extends JFrame {
	
	public Prueba1JFrame() {
		super("Prueba1Jframe");
		Icon icon = new ImageIcon("src/main/resources/add_icon_32x32.png");
		JButton add = new JButton("Añadir", icon);
		this.add(add);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(200,100);
		this.setVisible(true);

	}

	
	
	


}
