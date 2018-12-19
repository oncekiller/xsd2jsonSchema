import javax.swing.JFrame;
import javax.swing.JOptionPane;

//Class qui permet d'afficher les différents écrans d'erreurs
public class ErrorScreen extends JFrame{
	
	public ErrorScreen(String errorMessage, String errorTitle) {
		if(App.waitingScreen!= null) {
			App.waitingScreen.dispose();
		}
		JOptionPane.showMessageDialog(this,errorMessage,"Error "+errorTitle,JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
	
}
