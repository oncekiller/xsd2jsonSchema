import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

//Class qui créer une interface permettant à l'utilisateur de selectionner ses fichiers après initialisation pour lancer le programme
public class SelectFilesPhase extends App{
	public static Object lock = new Object();
	public static void run() throws IOException {
		fileNorme.createNewFile();
        fileCodeListStandard.createNewFile();
        fileCodeListLocal.createNewFile();
        
        //Créer un écran de menu
        MenuScreen menuScreen = new MenuScreen();
       
        synchronized(lock) {
               while (menuScreen.isVisible()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        new ErrorScreen(e.getMessage(), "");
                    }  
               }
       }
        
        
        //Copie le fichier norme Xsd choisi dans le fichier fileNorme ligne par ligne
        File fileNormeChoisi= menuScreen.fileNormeChoisi;
        BufferedReader readerNorme = new BufferedReader(new FileReader(fileNormeChoisi));
        String lineNorme = "",oldtextNorme="";
	    while((lineNorme = readerNorme.readLine()) != null){
	    	oldtextNorme+=lineNorme+"\r\n";
	    }
	    FileWriter writerNorme = new FileWriter(fileNorme);
        writerNorme.write(oldtextNorme);
        writerNorme.flush();
        writerNorme.close();
        readerNorme.close(); 
        
        
        
        //Copie le fichier CODE liste Standard choisi dans le fichier fileCodeListStandard ligne par ligne
        File fileStandardCodeListChoisi= menuScreen.fileStandardCodeListChoisi;
        BufferedReader readerStandardCodeList = new BufferedReader(new FileReader(fileStandardCodeListChoisi));
        String lineStandardCodeList = "",oldtextStandardCodeList="";
	    while((lineStandardCodeList = readerStandardCodeList.readLine()) != null){
	    	oldtextStandardCodeList+=lineStandardCodeList+"\r\n";
	    }
	    FileWriter writerStandardCodeList = new FileWriter(fileCodeListStandard);
	    writerStandardCodeList.write(oldtextStandardCodeList);
	    writerStandardCodeList.flush();
	    writerStandardCodeList.close();
        readerStandardCodeList.close(); 
        
        //Copie le fichier a la racine du dossier avec le nom exacte pour que le include du xsd parent ne fasse pas une erreur//
        String nameCodeListStandard = fileStandardCodeListChoisi.getName();
        File fileCodeListStandardRacine = new File(path +"\\"+nameCodeListStandard);
        FileWriter writerStandardCodeListRacine = new FileWriter(fileCodeListStandardRacine);
        writerStandardCodeListRacine.write(oldtextStandardCodeList);
        writerStandardCodeListRacine.flush();
        writerStandardCodeListRacine.close();
        
        
        
        
        //Copie le fichier CODE liste locale choisi dans le fichier fileCodeListLocal ligne par ligne
        File fileLocalCodeListChoisi= menuScreen.fileLocalCodeListChoisi;
        BufferedReader readerLocalCodeList = new BufferedReader(new FileReader(fileLocalCodeListChoisi));
        String lineLocalCodeList = "",oldtextLocalCodeList="";
	    while((lineLocalCodeList = readerLocalCodeList.readLine()) != null){
	    	oldtextLocalCodeList+=lineLocalCodeList+"\r\n";
	    }
	    FileWriter writerLocalCodeList = new FileWriter(fileCodeListLocal);
	    writerLocalCodeList.write(oldtextLocalCodeList);
	    writerLocalCodeList.flush();
	    writerLocalCodeList.close();
        readerLocalCodeList.close(); 
        
        //Copie le fichier a la racine du dossier avec le nom exacte pour que le include du xsd parent ne fasse pas une erreur//
        String nameCodeListLocal = fileLocalCodeListChoisi.getName();
        File fileCodeListLocalRacine = new File(path +"\\"+nameCodeListLocal);
        FileWriter writerLocalCodeListRacine = new FileWriter(fileCodeListLocalRacine);
        writerLocalCodeListRacine.write(oldtextLocalCodeList);
        writerLocalCodeListRacine.flush();
        writerLocalCodeListRacine.close();
	}
}
