import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;
//Class qui permet de récuperer des informations des CODE listes
//Récupére l'ensemble des types du schema Xsd du fichier regroupedCodeList.xsd et les stock dans la liste "ListTypeCodeList"
//Récupére l'ensemble des valeurs et leurs descriptions (Title, Definition) associées de toutes les enumerations presentes dans le fichier regroupedCodeList.xsd et les stock dans le tableau "descriptionCodeListTab"
public class GetCodeListInformationPhase extends App{
	public static InputStream inputRegroupedCodeList;
	public static XmlSchemaCollection schemaCol = new XmlSchemaCollection();
	public static XmlSchema schemaRegroupedCodeList;
	public static void run() throws IOException {
		inputRegroupedCodeList =  new FileInputStream(fileRegroupedCodeList);
		
		//Schema xsd du fichier regroupedCodeList.xsd
		schemaRegroupedCodeList = schemaCol.read(new StreamSource(inputRegroupedCodeList));
		
		//trouve la valeur de prefixXmlPrimitifType dans le fichier regroupedCodeList.xsd
		String[] prefixNamespaceList  = schemaRegroupedCodeList.getNamespaceContext().getDeclaredPrefixes();
		for(String prefixNamespace : prefixNamespaceList) { 
			 if(schemaRegroupedCodeList.getNamespaceContext().getNamespaceURI(prefixNamespace).equals("http://www.w3.org/2001/XMLSchema")) {
			 prefixTargetXsdPrimitifType = prefixNamespace;
			 };
		}
		
		getTypeCodeList();
		addDescriptionToList();
	}
	
	//Ajoute tous les types des CODE listes à la liste ListTypeCodeList
	public static void getTypeCodeList() {
		ListTypeCodeList.clear();
		
		//parcourt les clés de tous les types presents dans le schéma du fichier regroupedCodeList.xsd et en sort le nom du type correspondant
		for(QName key : schemaRegroupedCodeList.getSchemaTypes().keySet()) {
			ListTypeCodeList.add(schemaRegroupedCodeList.getSchemaTypes().get(key).getName());
		}
	}
	
	//Ajoute les valeurs et leurs description associée de toutes les énumerations de tous les type de ListTypeCodeList au tableau "descriptionCodeListTab"
	public static void addDescriptionToList() throws IOException {
		descriptionCodeListTab.clear();
		
		//Parcourt tous les noms des types de la liste ListTypeCodeList
		for(String nameTypeCodeList : ListTypeCodeList) {
			
			//On lit le fichier regroupedCodeList.xsd ligne par ligne
			BufferedReader readerFileRegroupedCodeList = new BufferedReader(new FileReader(fileRegroupedCodeList));
			
			//la variable valueEnum reprèsente la valeur de l'enumeration parcourue
			//la variable DescriptionTitle correspond au "Title" indiqué en description de l'enumeration parcourue
			//la variable DescriptionDefinition correspond a la "Definition" indiqué en description de l'enumeration parcourue
			String lineFileRegroupedCodeList = "", descriptionTitle= "", descriptionDefinition= "",valueEnum="";
	    	
			//subMap est un sous tableau de "descriptionCodeListTab" qui contiendra la valeur de valueEnum et une liste composée des valeurs de DescriptionTitle et de DescriptionDefinition correspondantes à valueEnum
			HashMap<String,String[]>subMap= new HashMap<String,String[]>();
	    	
			//compteur check pour aider à savoir dans quel type de balise le Reader est, à chaque itération de la boucle while
			int check = -1;
	    	while((lineFileRegroupedCodeList = readerFileRegroupedCodeList.readLine()) != null){
	    		 	if(check!=-1 && lineFileRegroupedCodeList.contains("</"+prefixTargetXsdPrimitifType+":simpleType>")) {
	    		 		check=6;
	    		 	}
	    		 	if(check==4) {
	    		 		descriptionDefinition+=lineFileRegroupedCodeList;
	    		 	}
	    		 	
	    		 	//détecte une balise <Definition> et récupère une première valeur grossière de la definition correspondante
			    	if(check==3 && lineFileRegroupedCodeList.contains("<Definition>")) {
			    		descriptionDefinition+=lineFileRegroupedCodeList;
			    		check=4;
			    	}
			    	
			    	//Affine les valeurs de descriptionTitle, descriptionDefinition, valueEnum pour ne recupérer le contenue que de l'intérieur des balises,
			    	//Met en forme ces différents contenues  
			    	if(check==4 && lineFileRegroupedCodeList.contains("</Definition>")) {
			    		
			    		//Title
			    		//Pattern pour ne prendre que le contenue présent entre les balises <Title></Title>
			    		Pattern patternTitle = Pattern.compile("<Title>.*</Title>");
					    Matcher matcherTitle = patternTitle.matcher(descriptionTitle);
				    	if(matcherTitle.find()) {
			    			descriptionTitle = descriptionTitle.substring(matcherTitle.start(),matcherTitle.end());
				    	}
				    	
			    		descriptionTitle = descriptionTitle.replace("<Title>", "");
			    		descriptionTitle = descriptionTitle.replace("</Title>", "");
			    		descriptionTitle = descriptionTitle.replace("\"","'");
			    		descriptionTitle=  descriptionTitle.replace(","," ");
			    		
			    		
			    	
			    		//Definition
			    		//Pattern pour ne prendre que le contenue présent entre les balises <Definition></Definition>
			    		Pattern patternDefinition = Pattern.compile("<Definition>.*</Definition>");
					    Matcher matcherDefinition= patternDefinition.matcher(descriptionDefinition);
				    	if(matcherDefinition.find()) {
				    		descriptionDefinition = descriptionDefinition.substring(matcherDefinition.start(),matcherDefinition.end());
				    	}
				    	
			    		descriptionDefinition = descriptionDefinition.replace("<Definition>", "");
			    		descriptionDefinition = descriptionDefinition.replace("</Definition>", "");
			    		descriptionDefinition = descriptionDefinition.replace("\"","'");
			    		descriptionDefinition = descriptionDefinition.replace(",", " ");
			    		
			  
			    		
			    		
			    		//Value
			    		//Pattern pour ne prendre que le contenue de la "value" de l'énumeration
			    		Pattern patternValue = Pattern.compile("value(\\s)*=(\\s)*\"((\\w|\\s|\\d|-|\\.))+\"");
					    Matcher matcherValue= patternValue.matcher(valueEnum);
				    	if(matcherValue.find()) {
				    		int start = matcherValue.group().indexOf("\"");
					    	int end = matcherValue.group().lastIndexOf("\"");
					    	valueEnum = valueEnum.substring(matcherValue.start()+start+1,matcherValue.start()+end);
				    	}
				    	
			    		valueEnum = valueEnum.replace("\"","'");
			    		valueEnum = valueEnum.replace(",", " ");
			    		
			    
			    		
			    		//Insére les valeurs dans subMap, puis réinitialise les variables et le compteur pour la prochaine enumeration
			    		String[] subDescription = {descriptionTitle,descriptionDefinition};
			    		
			    		    		
			    		subMap.put(valueEnum, subDescription);
			    		descriptionTitle= ""; 
			    		descriptionDefinition= "";
			    		valueEnum="";
			    				
			    		check=0;
			    	}
	    		 	if(check==2) {
	    		 		descriptionTitle+=lineFileRegroupedCodeList;
			    	}
	    		 	//Détecte une balise <Title> et récupère une première valeur grossière du titre correspondant
	    		 	if(check==1 && lineFileRegroupedCodeList.contains("<Title>")) {
	    		 		descriptionTitle+=lineFileRegroupedCodeList;
			    		check=2;
			    	}
	    		 	//Détecte la fin de la balise <Title>
	    		 	if(check==2 && lineFileRegroupedCodeList.contains("</Title>")) {
			    		check=3;
			    	}
	    		 	//Détecte une enumeration du type parcouru et récupère une première valeur grossière de la value correspondante
	    		 	if(check==0 && lineFileRegroupedCodeList.contains("<"+prefixTargetXsdPrimitifType+":enumeration value=\"")&& !lineFileRegroupedCodeList.contains("/>")) {
	    		 		valueEnum+=lineFileRegroupedCodeList;
			    		check=1;
			    	}
	    		 	//Détecte si on parcourt un simpleType avec un nom équivalent à nameTypeCodeList
	    		 	if(check==-1 && lineFileRegroupedCodeList.contains("<"+prefixTargetXsdPrimitifType+":simpleType name=\""+nameTypeCodeList+"\">")) {
			    		check=0;
			    	}
			 }
	    	 
	    	 //On ajoute une valeur au tableau puis on passe au type suivant
	    	 descriptionCodeListTab.put(nameTypeCodeList,subMap);
	    	
	    	 readerFileRegroupedCodeList.close();
	    }
	}

}
