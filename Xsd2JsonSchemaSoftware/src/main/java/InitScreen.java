import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//Screen initialisation 
public class InitScreen extends JFrame{
	public InitScreen() {
		this.setTitle("Initialization");
		this.setSize(400,150);
		this.setLocationRelativeTo(null);
		final JPanel content = new JPanel();
		JLabel waitingText = new JLabel("Initialization...");
		waitingText.setFont(new Font("Serif", Font.PLAIN, 20));
		content.add(waitingText);
		this.add(content);
		this.setVisible(true);
	}
}
