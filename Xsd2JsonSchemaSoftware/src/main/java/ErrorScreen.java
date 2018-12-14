import javax.swing.JFrame;
import javax.swing.JOptionPane;

//Classe qui permet d'afficher les différents écrans d'erreurs
public class ErrorScreen extends JFrame{
	
	public ErrorScreen(String errorMessage, String errorTitle) {
		if(App.waitingScreen!= null) {
			App.waitingScreen.dispose();
		}
		JOptionPane.showMessageDialog(this,errorMessage,"Error "+errorTitle,JOptionPane.ERROR_MESSAGE);
	}
	
}
