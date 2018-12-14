import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.stream.StreamSource;

import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;

//Class qui gére l'ensemble des modifications à faire liées aux class Java générées
public class TransformJavaClassGeneratedPhase extends App{
	//Liste des noms des types primitifs xml ou l'annotion @Custom doit être insérée
	public static String[] listTypeAnnotation = {"Duration","int","long","float","double"};
	public static XmlSchemaCollection schemaCol = new XmlSchemaCollection();
	public static InputStream inputResultXsd;
	public static XmlSchema schemaResultXsd;
	public static void run() throws IOException {
		inputResultXsd = new FileInputStream(fileResultXsd);
		schemaResultXsd = schemaCol.read(new StreamSource(inputResultXsd));
		
		//On remplie la liste "fileNormeClassList" des class java de la norme xsd
		fileNormeClassList = findJavaClassNorme();
		
		//On parcourt un a un les fichiers de la liste
		//On lit leurs contenues ligne par ligne
		//On réécrit leurs contenues si necessaire
		for(File fileNormeClass : fileNormeClassList) {
			BufferedReader readerFileNormeClass = new BufferedReader(new FileReader(fileNormeClass));
			String lineFileNormeClass = "", newContentFileNormeClass="";
			int checkImport=0;
			//Parcourt les lignes du fichier une par une
		    while((lineFileNormeClass = readerFileNormeClass.readLine()) != null){
		    	//Import les annotations customs @Custom et @CodeList du package "com.kjetland.jackson.jsonSchema.annotations"
		    	if(lineFileNormeClass.startsWith("import")&& checkImport==0) {
		    		checkImport=1;
		    		newContentFileNormeClass+="import com.kjetland.jackson.jsonSchema.annotations.*;\r\n";
		    	}
		    	//detecte si la ligne parcourue possède un attribut avec un type appartenant à la liste "listTypeAnnotation"
		    	//Ajoute l'annotation @Custom("nomDuType") juste avant l'attribut
		    	for(String typeAnnotation : listTypeAnnotation) {
		    		if(lineFileNormeClass.contains("protected "+typeAnnotation)) {
		    			//Ajoute l annotation correspondante
		    			newContentFileNormeClass+="\t@Custom(name = \""+typeAnnotation+"\")\r\n";
		    		}
		    	}
		    	//Detecte si la ligne parcourue possède un attribut avec un type appartenant à la liste des types des codeList "ListTypeCodeList"
		    	//Ajoute l'annotation @CodeList("nomDuType") juste avant l'attribut
		    	for(String typeCodeList : ListTypeCodeList) {
		    		if(lineFileNormeClass.contains("protected "+typeCodeList+" ")) {
		    			newContentFileNormeClass+="@CodeList(\""+typeCodeList+"\")\r\n";
		    		}
		    	}
		    	//Permet de verifier que les getters aient des noms coherent avec les noms des attributs modifie par la premiere librairie,
		    	//Si c'est pas le cas modifie le getter
		    	Pattern pattern = Pattern.compile("get.+\\s*\\(\\)\\s*\\{");
			    Matcher matcher = pattern.matcher(lineFileNormeClass);
		    	if(matcher.find()) {
		    		String linePassed="";
	    			String nextLine ="";
	    			while(!nextLine.contains("return")) {
	    				nextLine =readerFileNormeClass.readLine();
	    				linePassed+=nextLine+"\r\n";
	    			}
	    			String nameChanged=nextLine.replaceAll(" ", "").replaceAll("return", "").replaceAll(";", "").replaceAll("this.","");
	    			lineFileNormeClass = lineFileNormeClass.substring(0,matcher.start())+"get"+nameChanged+"(){\r\n"+linePassed;
		    	}
		    	newContentFileNormeClass+=lineFileNormeClass+"\r\n";
		    }
		    FileWriter writerFileNormeClass = new FileWriter(fileNormeClass);
	        writerFileNormeClass.write(newContentFileNormeClass);
	        writerFileNormeClass.flush();
	        writerFileNormeClass.close();
	        readerFileNormeClass.close();
		}
	}
	
	
	//Permet de récupérer l'ensemble des class java générées qui ne sont pas des class des élément de la codeList mais de la norme
	public static ArrayList<File> findJavaClassNorme() throws IOException {
		 //"javaclassNormeList" contient les fichiers java qui correspondent à des  éléments du schema de la norme Xsd
		 ArrayList<File> javaclassNormeList = new ArrayList<File>();
		 //Nom de l'élément root du schema xsd du fichier "resultXsd.xsd"
		 String nameElementRoot= schemaResultXsd.getElements().get(schemaResultXsd.getElements().keySet().toArray()[0]).getName();
		 File rep = new File(path+"\\src/main/java/generated");
	     //permet de creer une liste des fichiers java contenues dans le dossier src/main/java/generated
	     File[] fichiersJavaRep = rep.listFiles(new FilenameFilter(){
	       public boolean accept(File dir, String name) {
	         return name.endsWith(".java");
	       }
	     });
	     
	     //On parcourt les différents fichiers java de la liste
	     //On compare leurs nom avec les noms des types de la codeList présent dan la liste "ListTypeCodeList"
	     //Si il n'y a pas de correspondance on sait que le fichier parcourue correspond à un élément du schema de la norme Xsd
	     //On ajoute alors ce fichier à la liste "javaclassNormeList"
	     int checkFindNameRootClass=0;
	     for(File file : fichiersJavaRep) {
		    	int checkElementNorme=0;
		    	for(String nameCodeList : ListTypeCodeList) {
		    		if(nameCodeList.concat(".java").contentEquals(file.getName())) {
		    			checkElementNorme=1;
		    		}
		    	}
		    	if(checkElementNorme==0) {
		    		javaclassNormeList.add(file);
		    	}
		 }
	     //On renvoie la liste 
	     return javaclassNormeList;
	}
}
