import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//Classe qui permet d'initialiser le projet, de créer les bons repertoires et les bons fichiers au bon endroit
//Permet de clean le dossier s'il y a deja eut une utilisation au préalable pour ne pas avoir de conflis entre les fichiers
//Afin de pouvoir executer le programme correctement

public class InitPhase extends App{
	public static void run() {
		//Fonction qui néttoie le dossier
		clean();
		
		//creer les dossiers pour l'initialisation
		
		//dossier contenant les CODE listes en Json SCHEMA créés
		File dirResourcesJsonCodelist = new File (path+"\\resources/json/codeList");
		dirResourcesJsonCodelist.mkdirs();
		
		//dossier contenant l'ensemble des fichiers Xsd utilisés et créés
		File dirResourcesXsd = new File (path+"\\resources/xsd");
		dirResourcesXsd.mkdirs();
		
		//dossier contenant l'ensemble des mains class java
		File dirSrcMainJava = new File (path+"\\src/main/java");
		dirSrcMainJava.mkdirs();
		
		//dossier contenant le fichier Xsd qui sera interpreté afin de créer les class Java 
		File dirSrcMainXsd = new File (path+"\\src/main/xsd");
		dirSrcMainXsd.mkdirs();
		
		//Dossier contenant le fichier xjb de binding pour la création de class Java
		File dirSrcMainXjb = new File (path+"\\src/main/xjb");
		dirSrcMainXjb.mkdirs();
		
		//Fichier qui permettra de compiler les class Java créées
		File fileDestinationBat = new File(path+"\\CompilCreatedClass.bat");
		
	    //Fichier pom pour exécuter les fonctions de la librairie krasa-jaxB-tool afin de générer les class Java
		File fileDestinationPom1=new File(path+"\\pom.xml");   
		
	    //Fichier xjb de binding pour la création de class Java 
		File fileDestinationGlobalXjb=new File(path+"\\src/main/xjb/global.xjb"); 
		
		//Ecrit le contenu du fichier pom dynamiquement
		try {
			FileWriter writer= new FileWriter(fileDestinationPom1);
			writer.write(PomXmlContent.content);
			writer.flush();
		    writer.close();
		} catch (Exception e) {
			 e.printStackTrace();
             new ErrorScreen(e.getMessage(), "");		
        }
		
		//Ecrit le contenu du fichier xjb dynamiquement 
		try {
			FileWriter writer= new FileWriter(fileDestinationGlobalXjb); 	 
			writer.write(GlobalXjbContent.content);
			writer.flush();
		    writer.close();
		} catch (Exception e) {
			 e.printStackTrace();
             new ErrorScreen(e.getMessage(), "");
		}
		
		//Ecrit dynamiquement le contenu du fichier bat qui compil les class Java générées 
		try {
			FileWriter writer= new FileWriter(fileDestinationBat);
			writer.write(CompilCreatedClassBatContent.content);
			writer.flush();
		    writer.close();
		} catch (Exception e) {
			 e.printStackTrace();
             new ErrorScreen(e.getMessage(), "");
		}
		
		
		 
	}
	
	//Permet de nettoyer les fichers et dossiers du dossier racine si besoin
	public static void clean() {
		
		//Clean le dossier contenant les class java créées
		File dirSrcMainJavaGenerated = new File(path+"\\src/main/java/generated");
	    if(dirSrcMainJavaGenerated.exists()) {
	    	System.out.println("Cleaning folder of JavaClass generated");
	    	try{
	    		File[] listFiles = dirSrcMainJavaGenerated.listFiles();
	    		for (File file : listFiles) {
		    		file.delete();
		    	}
	    	}catch(Exception e) {
	    		 e.printStackTrace();
                 new ErrorScreen(e.getMessage(), "");
	    	}
	    }
	    
	    //Clean le dossier contenant le fichier Json Schema de la norme créé
	    File dirResourcesJson = new File(path+"\\resources/json");
	    if(dirResourcesJson.exists()) {
	    	System.out.println("Cleaning folder of JsonSchema norme created");
	    	try{
	    		File[] listFiles = dirResourcesJson.listFiles();
	    		for (File file : listFiles) {
	    			if(file.getName().endsWith(".json")) {
	    				file.delete();
	    			}
		    		 
		    	}
	    	}catch(Exception e) {
	    		 e.printStackTrace();
                 new ErrorScreen(e.getMessage(), "");
	    	}
	    }
	    
	    //Clean le dossier contenant les fichiers Json Schema des CODE listes créés
	    File dirResourcesJsonCodelist = new File (path+"\\resources/json/codeList");
	    if(dirResourcesJsonCodelist.exists()) {
	    	System.out.println("Cleaning folder of JsonSchema codeLists created");
	    	try{
	    		File[] listFiles = dirResourcesJsonCodelist.listFiles();
	    		for (File file : listFiles) {
	    			if(file.getName().endsWith(".json")) {
	    				file.delete();
	    			}
		    	}
	    	}catch(Exception e) {
	    		 e.printStackTrace();
                 new ErrorScreen(e.getMessage(), "");
	    	}
	    }
	    
	    //Clean le dossier contenant les fichiers xsd
	    File dirResourcesXsd = new File (path+"\\resources/xsd");
	    if(dirResourcesXsd.exists()) {
	    	System.out.println("Cleaning folder of xsd");
	    	try{
	    		File[] listFiles = dirResourcesXsd.listFiles();
	    		for (File file : listFiles) {
		    		file.delete();
		    	}
	    	}catch(Exception e) {
	    		 e.printStackTrace();
                 new ErrorScreen(e.getMessage(), "");
	    	}
	    }
	    
	    //Clean les fichiers xsd des CODE listes présents dans le dossier racine
	    File dirRacine = new File (path);
	    if(dirRacine.exists()) {
	    	System.out.println("Cleaning xsd in base folder");
	    	try{
	    		File[] listFiles = dirRacine.listFiles();
	    		for (File file : listFiles) {
	    			if(file.getName().endsWith(".xsd")) {
	    				file.delete();
	    			}
		    	}
	    	}catch(Exception e) {
	    		 e.printStackTrace();
                 new ErrorScreen(e.getMessage(), "");
	    	}
	    }
	}
}
