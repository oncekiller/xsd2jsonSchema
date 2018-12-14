import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;


//Class qui permet de générer les fichiers json de la norme et des CodeList
//Modifie également ces fichiers créés
public class JavaClassToJsonSchemaPhase extends App{
	public static String path=new File("").getAbsolutePath();

	public static InputStream inputRegroupedCodeList, inputResultXsd;
	public static XmlSchemaCollection schemaCol = new XmlSchemaCollection();
	public static XmlSchema schemaRegroupedCodeList, schemaResultXsd;
	
	public static void main(String[] args) throws IOException {
		try {
			GetCodeListInformationPhase.run();
		}catch(Throwable e) {
        	e.printStackTrace();
			new ErrorScreen(e.getMessage(), "TransformJavaClassPhaseRun");
        }
		
		
		//On récupert le nom de la class de l'élément root
		//Nom de l'élément root du schema xsd du fichier "resultXsd.xsd" 
		inputResultXsd = new FileInputStream(fileResultXsd);
		schemaResultXsd = schemaCol.read(new StreamSource(inputResultXsd));
		String nameElementRoot= schemaResultXsd.getElements().get(schemaResultXsd.getElements().keySet().toArray()[0]).getName();
		 File rep = new File(path+"\\src/main/java/generated");
	     //permet de creer une liste des fichiers java contenues dans le dossier src/main/java/generated
	     File[] fichiersJavaRep = rep.listFiles(new FilenameFilter(){
	       public boolean accept(File dir, String name) {
	         return name.endsWith(".java");
	       }
	     });
	     
	     int checkFindNameRootClass=0;
	     for(File file : fichiersJavaRep) {
		    	//regarde si le nom du fichier parcourru est celui de la class root
	    		if(file.getName().contentEquals(nameElementRoot.concat(".java"))){
	    			nameRootClassJava=nameElementRoot.concat(".java");
	    			checkFindNameRootClass=1;
	    		}
		 }
	     //Si on ne trouve pas le nom de la class root avec la méthode précedente on parcourt tous les fichier java créés un a un et on voit lequel est le root
	     //Celui qui possède l'annotation "@XmlRootElement"
	     if(checkFindNameRootClass!=1) {
	    	 for(File file : fichiersJavaRep) {
	    		 BufferedReader reader = new BufferedReader(new FileReader(file));
	    		  String line = "";
	 		     while((line = reader.readLine()) != null){
	 		    	if(line.contains("@XmlRootElement")) {
	 		    		nameRootClassJava=file.getName();
	 		    	}
	 		     }
		     }    
	     }
	     
	    //Creer le fichier jsonSchema de la norme
		transformJavaClasstoJsonSchema(nameRootClassJava,true);
		//Creer les fichier jsonSchema des codeList
		for(String type : ListTypeCodeList) {
			transformJavaClasstoJsonSchema(type,false);
			addDescriptionToJSchemaCodeList(type);
		}
		
		 
	}
	
	
	
	//Permet la création des fichier json et de leurs contenus
	//Prend un argument le nom d'une classe java et si elle appartient au élèments de la norme ou de la CodeList
	//Ressort le fichier json correspondant
	public static void transformJavaClasstoJsonSchema(String javaClassName, Boolean norme ) throws IOException {
		//Fichier Json final créé a partir des arguments de la fonction
		File fichierJsonFinal;
        
		ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule());
        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
        //récupére la class Java avec le nom indiqué en attribut 
        Class<?> rootClassJava = null;
        JsonNode jsonSchema= null;
        try {
        rootClassJava = Class.forName("generated."+javaClassName.replace(".java", ""));
        jsonSchema = schemaGen.generateJsonSchema(rootClassJava);
        }
        catch(ClassNotFoundException e) {
        	e.printStackTrace();
			new ErrorScreen(e.getMessage(), "Generate Json File");
        }
        
        //Créer le fichier .json au bon endroit dans le dossier
        //Dossier "resources/json/" pour la norme
        //Dossier "resources/json/codeList" pour les codeList
        
        //Si le fichier créé est celui de la norme
        if(norme) {
        	fichierJsonFinal = new File(path+"\\resources/json/"+javaClassName.replace(".java", "")+".json");
        }
        //Si le fichier créé est un élément de la CodeList
        else {
        	fichierJsonFinal = new File(path+"\\resources/json/codeList/"+javaClassName.replace(".java", "")+".json");
        }
        fichierJsonFinal.createNewFile();
		
        //Copie le schema Json dans le fichier créé
	    BufferedWriter writerFichierJsonFinal = new BufferedWriter(new FileWriter(fichierJsonFinal));    	
	    writerFichierJsonFinal.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema));   	 
	    writerFichierJsonFinal.close();
	    
	    
	    // Pour le fichier json de la norme enlever les enumerations des CodeList qui ne servent a rien
	    if (norme) {
	    	BufferedReader reader = new BufferedReader(new FileReader(fichierJsonFinal));
	    	String line="" ,previousLine="", newContent="";
	    	while((line = reader.readLine()) != null){
	    		//Faire gaf si enum autres que codeList ca les efface
	    		if(line.contains("\"enum\" : [")) {
	    			if(!line.endsWith(",")&& previousLine.contains("\"$ref\" : \"codeList")) {
	    				newContent=newContent.substring(0,newContent.length()-3)+"\r\n";
	    			}
	    		}
	    		else {
	    			newContent+=line+"\r\n";
	    		}
	    		previousLine = line;
	    	}
	    	FileWriter writer = new FileWriter(fichierJsonFinal);
	        writer.write(newContent);
	        writer.flush();
	        writer.close();
	    }
		 
	}
	
	
	
	//Fonction qui ajoute les descriptions du tableau de description "descriptionCodeListTab" aux fichiers Json des CodeLists
	public static void addDescriptionToJSchemaCodeList(String nameCodeList) throws IOException {
		File fichierJson = new File(path+"\\resources/json/codeList/"+nameCodeList+".json");
		//On recupert un tableau de toutes les descriptions
		BufferedReader reader = new BufferedReader(new FileReader(fichierJson));
		String line = "",oldtext="";
   	 	while((line = reader.readLine()) != null){
   	 		oldtext +=line+"\r\n";
   	 		if(line.contains("\"title\" :")) {
   	 			oldtext+= "  \"description\" : \" ";
			
   	 			for(String value: descriptionCodeListTab.get(nameCodeList).keySet()) {
   	 				oldtext+=value+ " : ( " ;
   	 				
   	 				oldtext+="TITLE : "+ descriptionCodeListTab.get(nameCodeList).get(value)[0]+" / ";
   	 				oldtext+="DEFINITION : "+ descriptionCodeListTab.get(nameCodeList).get(value)[1]+" ) | ";
   	 			}
   	 			oldtext= oldtext.substring(0,oldtext.length()-3);
		
   	 			oldtext +=" \",\t\n";
   	 		}	
		}
   	 	FileWriter writer = new FileWriter(fichierJson);
   	 	writer.write(oldtext);
   	 	writer.flush();
   	 	writer.close();
    
   	 	BufferedReader reader2 = new BufferedReader(new FileReader(fichierJson));
   	 	String line2="" , oldtext2="";
   	 	while((line2 = reader2.readLine()) != null){
			//Enleve les TRANSFORM_ pour les valeurs des enums qui ont ete modifie
			line2=line2.replaceAll("TRANSFORM_", "");
			oldtext2+=line2+"\r\n";
   	 	}
   	 	FileWriter writer2 = new FileWriter(fichierJson);
   	 	writer2.write(oldtext2);
   	 	writer2.flush();
   	 	writer2.close();		
	}
}
