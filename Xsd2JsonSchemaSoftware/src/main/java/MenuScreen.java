

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

//Class qui gére le tableau d'affichage du menu de  l'outil
public class MenuScreen extends JFrame{
	private JLabel textNorme, textCodeListStandard, textCodeListLocal;
	public File fileNormeChoisi,fileStandardCodeListChoisi,fileLocalCodeListChoisi;
	public MenuScreen() {
		this.setTitle("Xsd2JsonSchema");
		this.setMinimumSize(new Dimension(600,400));
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		final JPanel content = new JPanel();
		content.setName("Sélection des fichiers");
		final JPanel panNorme = new JPanel();
		final JPanel panCodeListStandard = new JPanel();
		final JPanel panCodeListLocal = new JPanel();
	    
		
		//Section de la norme Xsd
		panNorme.setBackground(Color.white);
	    panNorme.setPreferredSize(new Dimension(this.getWidth(), 80));
	    panNorme.setBorder(BorderFactory.createTitledBorder(" CIM XSD norme")); 
	    textNorme = new JLabel("Select the CIM XSD norme :  ");
	    final JButton modifyFileNorme = new JButton("Change");
	    modifyFileNorme.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {        
	    		 //Choix du fichier localCodeList
	            JFileChooser choiceFileNorme = new JFileChooser();
	            choiceFileNorme.showOpenDialog(null);
	            fileNormeChoisi= choiceFileNorme.getSelectedFile();
	    		if(fileNormeChoisi!= null) {
	    			panNorme.removeAll();
	    			panNorme.add(new JLabel(fileNormeChoisi.getName()));
	    			panNorme.add(modifyFileNorme);
	    			panNorme.updateUI();
	    			content.updateUI();
	    		}
	          }
	    });
	    JButton addFileNorme = new JButton("Select a file");
	    addFileNorme.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {        
	    		//Choix du fichier norme
	    		JFileChooser choiceFileNorme = new JFileChooser();
	    		choiceFileNorme.showOpenDialog(null);
	    		fileNormeChoisi= choiceFileNorme.getSelectedFile();
	    		if(fileNormeChoisi!= null) {
	    			panNorme.removeAll();
	    			panNorme.add(new JLabel(fileNormeChoisi.getName()));
	    			panNorme.add(modifyFileNorme);
	    			panNorme.updateUI();
	    			content.updateUI();
	    		}
	          }
	    });
	    panNorme.add(textNorme);
	    panNorme.add(addFileNorme);
	    
	    
	    //Section de la CODE liste Standard
	    panCodeListStandard.setBackground(Color.white);
	    panCodeListStandard.setPreferredSize(new Dimension(this.getWidth(), 80));
	    panCodeListStandard.setBorder(BorderFactory.createTitledBorder(" Standard CODE list")); 
	    textCodeListStandard = new JLabel("Select the standard CODE list :  ");
	    final JButton modifyFileCodeListStandard = new JButton("Change");
	    modifyFileCodeListStandard.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {        
	    		//Choix du fichier standardCodeList
	            JFileChooser choiceStandardCodeList = new JFileChooser();
	            choiceStandardCodeList.showOpenDialog(null);
	    		fileStandardCodeListChoisi= choiceStandardCodeList.getSelectedFile();
	    		if(fileStandardCodeListChoisi!= null) {
	    			panCodeListStandard.removeAll();
	    			panCodeListStandard.add(new JLabel(fileStandardCodeListChoisi.getName()));
	    			panCodeListStandard.add(modifyFileCodeListStandard);
	    			panCodeListStandard.updateUI();
	    			content.updateUI();
	    		}
	          }
	    });
	    JButton addFileCodeListStandard = new JButton("Select a file");
	    addFileCodeListStandard.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {        
	    		//Choix du fichier standardCodeList
	            JFileChooser choiceStandardCodeList = new JFileChooser();
	            choiceStandardCodeList.showOpenDialog(null);
	    		fileStandardCodeListChoisi= choiceStandardCodeList.getSelectedFile();
	    		if(fileStandardCodeListChoisi!= null) {
	    			panCodeListStandard.removeAll();
	    			panCodeListStandard.add(new JLabel(fileStandardCodeListChoisi.getName()));
	    			panCodeListStandard.add(modifyFileCodeListStandard);
	    			panCodeListStandard.updateUI();
	    			content.updateUI();
	    		}
	          }
	    });
	    panCodeListStandard.add(textCodeListStandard);
	    panCodeListStandard.add(addFileCodeListStandard);
	    
	    
	    
	    //Section de la CODE liste locale
	    panCodeListLocal.setBackground(Color.white);
	    panCodeListLocal.setPreferredSize(new Dimension(this.getWidth(), 80));
	    panCodeListLocal.setBorder(BorderFactory.createTitledBorder(" Local CODE list")); 
	    textCodeListLocal = new JLabel("Select the local CODE list :  ");
	    final JButton modifyFileCodeListLocal = new JButton("Change");
	    modifyFileCodeListLocal.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {        
	    		 //Choix du fichier localCodeList
	            JFileChooser choiceLocalCodeList = new JFileChooser();
	            choiceLocalCodeList.showOpenDialog(null);
	            fileLocalCodeListChoisi= choiceLocalCodeList.getSelectedFile();
	    		if(fileLocalCodeListChoisi!= null) {
	    			panCodeListLocal.removeAll();
	    			panCodeListLocal.add(new JLabel(fileLocalCodeListChoisi.getName()));
	    			panCodeListLocal.add(modifyFileCodeListLocal);
	    			panCodeListLocal.updateUI();
	    			content.updateUI();
	    		}
	          }
	    });
	    JButton addFileCodeListLocal = new JButton("Select a file");
	    addFileCodeListLocal.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {        
	    		 //Choix du fichier localCodeList
	            JFileChooser choiceLocalCodeList = new JFileChooser();
	            choiceLocalCodeList.showOpenDialog(null);
	            fileLocalCodeListChoisi= choiceLocalCodeList.getSelectedFile();
	    		if(fileLocalCodeListChoisi!= null) {
	    			panCodeListLocal.removeAll();
	    			panCodeListLocal.add(new JLabel(fileLocalCodeListChoisi.getName()));
	    			panCodeListLocal.add(modifyFileCodeListLocal);
	    			panCodeListLocal.updateUI();
	    			content.updateUI();
	    		}
	          }
	    });
	    panCodeListLocal.add(textCodeListLocal);
	    panCodeListLocal.add(addFileCodeListLocal);
	    
	    
	    //Bouton "Validate"
	    JButton validateBouton = new JButton("Validate");
	    
	    validateBouton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) { 
	    	  if(fileNormeChoisi!= null && fileStandardCodeListChoisi != null && fileLocalCodeListChoisi != null ) {
	    		  synchronized (SelectFilesPhase.lock) {
	    			  setVisible(false);
			    	  dispose();
			    	  App.waitingScreen= new WaitingScreen(); 
	                  SelectFilesPhase.lock.notify();
	              }  
	    	  }
	    	  else {
	    		  JOptionPane erreurSelectFile = new JOptionPane();
	    		  erreurSelectFile.showMessageDialog(null, "Completed information are not correct! ","Error", JOptionPane.INFORMATION_MESSAGE);
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
	    content.add(panNorme);
	    content.add(panCodeListStandard);
	    content.add(panCodeListLocal);
	    content.add(validateBouton);
	    this.add(content);
		this.setVisible(true);  
		
		
	}
	
}
