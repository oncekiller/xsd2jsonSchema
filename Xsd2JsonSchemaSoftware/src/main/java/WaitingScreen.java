import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
//Screen waiting
public class WaitingScreen extends JFrame{
	public WaitingScreen() {
		this.setTitle("Loading");
		this.setSize(400,150);
		this.setLocationRelativeTo(null);
		final JPanel content = new JPanel();
		JLabel waitingText = new JLabel("Loading...");
		waitingText.setFont(new Font("Serif", Font.PLAIN, 20));
		content.add(waitingText);
		this.add(content);
		this.setVisible(true);
	}
}
