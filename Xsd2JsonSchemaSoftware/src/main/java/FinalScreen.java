import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//Screen Final
public class FinalScreen extends JFrame{
	public static File[] fichiersJsonRep;
	public FinalScreen() {
		
		//On regarde si le fichier final json de la norme a été créé sinon on affiche un message d'erreur
		//Si le fichier existe on affiche un screnn de succès avec la localisation du fichier créé et un bouton "ok"
		//Si on appuie sur le bouton "ok" le fichier s'ouvre automatiquement avec l'outil par default de l'ordinateur de l'utilisateur pour les fichiers json
		//Si l'utilisateur ne possède pas d'outil par default affiche un message d'erreur
		File rep = new File(App.path+"\\resources/json");
		fichiersJsonRep = rep.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name) {
	         		return name.endsWith(".json");
	       		}
	    });
		try {
			if(fichiersJsonRep[0].exists()) {
				System.out.println(fichiersJsonRep);
				this.setTitle("Success!");
				this.setMinimumSize(new Dimension (600, 160));
				this.setLocationRelativeTo(null);
				final JPanel content = new JPanel();
				content.setLayout(new BorderLayout()); 
				JLabel finalText1 = new JLabel("The file was generated correctly in the following folder :");
				JTextArea finalText2 = new JTextArea(App.path +"\\resources\\Json");
				finalText1.setFont(new Font("Serif", Font.PLAIN, 20));
				finalText2.setFont(new Font("Serif", Font.PLAIN, 20));
				finalText2.setEditable(false);	
				JScrollPane ScrollPane = new JScrollPane(finalText2);
				ScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
				ScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				
				JButton okBouton = new JButton("ok");
				okBouton.setSize(100,20);
				okBouton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) { 
		    			try { 
		    				Desktop.getDesktop().open(fichiersJsonRep[0]);
		    				dispose();
		    			} catch (IOException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    				new ErrorScreen(e.getMessage(), "Opening Json File");
		    			}
		    			finally {
		    				dispose();
		    			}
		    		
					}
				});
				//Close button
				this.addWindowListener(new java.awt.event.WindowAdapter() {
			        @Override
			        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			                System.exit(0);
			        }
			    });
				
				content.add(finalText1 ,BorderLayout.NORTH);
				content.add(ScrollPane,BorderLayout.CENTER);
				this.add(content );
				this.add(okBouton,BorderLayout.SOUTH);
				this.setVisible(true);
			}
			else {
				new ErrorScreen("Final json file was not correctly generated", "Opening final file json");
			}
		}catch(Throwable e) {
			new ErrorScreen("Final json file was not correctly generated", "Opening final file json");
			System.exit(1);
		}
	}
}
