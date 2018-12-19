import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.stream.StreamSource;
import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;

//Class qui gére l'ensemble des modifications liées aux fichiers Xsd avant qu'ils soient transformés en class Java
//Regroupe le contenue de la CODE liste Standard et CODE liste locale dans le même fichier
//Créer un fichier Xsd final contenant les contenues de la norme et des codeLists au même endroit, et propre au niveau de la syntaxe xsd
//Réalise des modifications au niveau du code xsd, pour que les classes Java qui en résulte soient plus prècises et correctes
public class TransformXsdPhase extends App {
	
	public static InputStream inputNorme,inputRegroupedCodeList,inputStandardCodeList;
    public static XmlSchemaCollection schemaCol = new XmlSchemaCollection();
    //Variables representant les schemas xsd de la norme de la CODE liste Standard et des deux CODE listes réunnis
    public static XmlSchema schemaNorme,schemaRegroupedCodeList,schemaStandardCodeList;
	public static void run() throws IOException {
		//génére le fichier RegroupedCodeList.xsd
		try {
			fileRegroupedCodeList.createNewFile();
		} catch (IOException e) {
			System.out.println("Problem when creating the file RegroupedCodeList.xsd :");
			e.printStackTrace(System.out);
		}
		
		inputNorme = new FileInputStream(fileNorme);
		inputStandardCodeList = new FileInputStream(fileCodeListStandard);
		//Schema xsd de la norme
		schemaNorme = schemaCol.read(new StreamSource(inputNorme)); 
		//Schema xsd de la code List Standard
		schemaStandardCodeList = schemaCol.read(new StreamSource(inputStandardCodeList)); 
		
		try {
			//Regroupe la code List locale et standard
			regroupCodeList();
		} catch (IOException e) {
			System.out.println("Problem when trying to regroup StandardCodeList and LocalCodeList");
			e.printStackTrace(System.out);
		}
		
		inputRegroupedCodeList =  new FileInputStream(fileRegroupedCodeList);
		//Schema xsd du fichier regroupedCodeList.xsd
		schemaRegroupedCodeList = schemaCol.read(new StreamSource(inputRegroupedCodeList));
		
		//récupere les  informations des Code Listes
		GetCodeListInformationPhase.run();
		
		
		try {
			//Regroupe la norme xsd et regroupedCodeList.xsd
			regroupRegroupedCodeListAndNorme();
		} catch (IOException e) {
			System.out.println("Problem when trying to regroup regroupedCodeList and norme");
			e.printStackTrace(System.out);
		}
		
	}
	
	
	
	//fonction qui regroupe les deux code Listes dans un même fichier
	public static void regroupCodeList() throws IOException {
		//trouve la valeur de prefixXmlPrimitifType dans la Code Liste standard
		String[] prefixNamespaceList  = schemaStandardCodeList.getNamespaceContext().getDeclaredPrefixes();
		for(String prefixNamespace : prefixNamespaceList) { 
	     	  if(schemaStandardCodeList.getNamespaceContext().getNamespaceURI(prefixNamespace).equals("http://www.w3.org/2001/XMLSchema")) {
	     		  prefixTargetXsdPrimitifType = prefixNamespace;
	     	   };
	     }
		
	    //Variable qui lit le fichier de la CODE liste Standard
		BufferedReader readerfileCodeListStandard = new BufferedReader(new FileReader(fileCodeListStandard));
		
		//La variable line est un string qui va représenter tour à tour une ligne du fichier actuellemennt lu par la variable reader
		//La variable previousline est un string qui va représenter la ligne précédent celle qui est en train d'être lu par le reader
		//la variable contentRegroupedCodeList représente le contenue du nouveau fichier xsd créé (regroupedCodeList) qui est initialisé
	    String linefileCodeListStandard = "",previousLinefileCodeListStandard = "", 
	    	   contentRegroupedCodeList = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" + 
	     		"<xsd:schema xmlns:ecl=\"urn:entsoe.eu:wgedi:codelists\" attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\r\n";
	    
	    //On parcourt une a une les lignes de la standardCodeList et on récupére leurs valeurs sous forme de string avec la variable "ligne"
	    while((linefileCodeListStandard = readerfileCodeListStandard.readLine()) != null){ 	
	         linefileCodeListStandard = cleanPrefix(linefileCodeListStandard);
	         
	         //On regarde si la ligne possède un élement xs:union permettant de regrouper des éléments des deux code listes
	         if(findUnion(linefileCodeListStandard)) {
	        	 
	        	 	//On recupere le nom de l'élèment de la CODE liste Standard de cet union
	        		String nameStandardCodeList=findNameStandardCodeList(linefileCodeListStandard);
	        		
	        		//On recupere le nom de l'élèment de la CODE liste Locale de cet union
	        		String nameLocalCodeList=findNameLocalCodeList(linefileCodeListStandard);
	        		
	        		//Variable représentant le contenue de l'élèment de la CODE liste standard présent dans l'union
	        		String contentStandardCodeList="";     		
					try {
						//On recupère le contenue de l'élément de la CODE liste Standard de l'union à partir de son nom
						contentStandardCodeList = findStandardCodeListContent(nameStandardCodeList,fileCodeListStandard);
					} catch (IOException e) {
						System.out.println("Problem when when trying to recover the content of the standardCodeList element :");
						e.printStackTrace(System.out);
					}
					
					 //Variable représentant le contenue de l'élèment de la CODE liste locale présent dans l'union
	        		 String contentLocalCodeList="";
					try {
						//On recupère le contenue de l'élément de la CODE liste locale de l'union à partir de son nom
						contentLocalCodeList = findLocalCodeListContent(nameLocalCodeList,fileCodeListLocal);
					} catch (IOException e) {
						System.out.println("Problem when when trying to recover the content of the localCodeList element :");
						e.printStackTrace(System.out);
					}
					
	        		 //On ajoute les éléments récupérés, à la variable contentRegroupedCodeList en complétant avec les balises nécessaires pour une syntaxe de ficher xsd correcte 
					 contentRegroupedCodeList+= previousLinefileCodeListStandard+"\r\n";
					 contentRegroupedCodeList+= "<xsd:restriction base=\"xsd:NMTOKEN\">\r\n";
					 contentRegroupedCodeList+=contentStandardCodeList+contentLocalCodeList+"\r\n";
					 contentRegroupedCodeList+= "</xsd:restriction>\r\n";
					 contentRegroupedCodeList+="</xsd:simpleType>\r\n";
	         }
	         else {
	        	 previousLinefileCodeListStandard=linefileCodeListStandard;
	         }
	     }
	     contentRegroupedCodeList+="</xsd:schema>";
	     //On copie la valeure de contentRegroupedCodeList au fichier regroupedCodeList.xsd
	     readerfileCodeListStandard.close(); 
	     FileWriter writer = new FileWriter(fileRegroupedCodeList);
	     writer.write(contentRegroupedCodeList);
	     writer.flush();
         writer.close();
	}
	
	
	
	//Supprime les prefixs ecl et cl d'une ligne 
	//Comme les deux CODE listes sont au même endroit il n'y a plus d'Import de CODE liste locale et donc les préfixe cl: ou ecl: sont innutiles
	public static String cleanPrefix(String line) {
		line = line.replaceAll(" ecl:", "\t	");
		line = line.replaceAll("\"ecl:", "\"");
	    line = line.replaceAll("\"cl:", "");
	    return line;
	}
	
	
	//Détecte la presence d'un élément xs:union dans la ligne
	public static boolean findUnion(String line) {
		if(line.contains("<"+prefixTargetXsdPrimitifType+":union")) {
			return true;
		}
		return false;	
	}
	
	//Trouve le nom de l'élèment de la CODE liste Standard présent dans une ligne
	public static String findNameStandardCodeList(String line) {
		//pattern correspondant un nom de la CODE liste Standard
		Pattern pattern = Pattern.compile("Standard((\\w|-|\\.))+");
		Matcher matcher = pattern.matcher(line);
		//Si le pattern est détecté dans la ligne le retourne
		if(matcher.find()) {
			return matcher.group();
		}
		//Sinon affiche une erreur not found
		System.out.println("name of the standardCodeList not found");
		return null;
	}
	
	//Trouve le nom de l'élèment de la CODE liste locale présent dans une ligne
	public static String findNameLocalCodeList(String line) {
		//pattern correspondant un nom de la CODE liste locale
		Pattern pattern = Pattern.compile("Local((\\w|-|\\.))+");
		Matcher matcher = pattern.matcher(line);
		//Si le pattern est détecté dans la ligne le retourne
		if(matcher.find()) {	
			return matcher.group();
		}
		//Sinon affiche une erreur not found
		System.out.println("name of the localCodeList not found");		 
		return null;
	}
	
	//Trouve le content d'un élément de la CODE liste Standard à partir de son nom
	public static String findStandardCodeListContent (String nameStandardCodeList, File fileCodeListStandard) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileCodeListStandard));
		String line = "", contentStandardCodeList = "";
		int  check=0;
		//récupére tout le text du contenue entre deux balises xsd:restriction avec un nom égale à celui de la CODE liste Standard puis renvoie ce contenu 
		while((line = reader.readLine()) != null){		    		    	
			if(line.contains("</"+prefixTargetXsdPrimitifType+":restriction>")&& check==2) { 		
			    return contentStandardCodeList;
			}   	
			if(check==2) {
			    //Insére un "TRANSFORM_" au niveau des valeurs des énums juste avant le premier caractére numérique de la valeur
			    Pattern pattern = Pattern.compile("[^0-9]?[0-9]");
			    Matcher matcher = pattern.matcher(line);
			    if(matcher.find()&& line.contains("enumeration")) {	
			    	line = line.replaceFirst("[0-9]","TRANSFORM_"+line.substring(matcher.start()+1, matcher.start()+2	));			 
			    }
			    contentStandardCodeList += line + "\r\n";
			}
			if(check==1 && line.contains("<"+prefixTargetXsdPrimitifType+":restriction")) {
			    check=2;
			}
			if(line.contains(" name=\""+nameStandardCodeList)) {
			    check=1;	
			}	         	 
		}
		//Si le content n'est pas correctement trouvé affiche une erreur not found
		System.out.println("content of the standardCodeList not found");		 
		return contentStandardCodeList;
	}
	
	
	//Trouve le content d'un élément de la CODE liste locale à partir de son nom
	public static String findLocalCodeListContent (String nameLocalCodeList, File fileCodeListLocal) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileCodeListLocal));
		String line = "", contentLocalCodeList = "";
		int  check=0;
		//récupére le texte de contenue situé entre deux balises xsd:restriction avec un nom égale à celui de la CODE liste locale puis renvoie ce contenu 
		while((line = reader.readLine()) != null){		    		    	
			if(line.contains("</"+prefixTargetXsdPrimitifType+":restriction>")&& check==2) { 		
				return contentLocalCodeList;
			}   	
			if(check==2) {
				//Insére un "TRANSFORM_" au niveau des valeurs des énums juste avant le premier caractére numérique de la valeur
				Pattern pattern = Pattern.compile("[^0-9]?[0-9]");
				Matcher matcher = pattern.matcher(line);
				if(matcher.find()&& line.contains("enumeration")) {	
				   line = line.replaceFirst("[0-9]","TRANSFORM_"+line.substring(matcher.start()+1, matcher.start()+2	));			 
				}
				contentLocalCodeList += line + "\r\n";
			}
			if(check==1 && line.contains("<"+prefixTargetXsdPrimitifType+":restriction")) {
				check=2;
			}
			if(line.contains(" name=\""+nameLocalCodeList)) {
				check=1;	
			}	         	 
		}
		//Si le contenue n'est pas correctement trouvé affiche une erreur not found
		System.out.println("content of the localCodeList not found");		 
		return contentLocalCodeList;
	}
	
	
	
	
	
	
	
	
	
	//fonction qui regroupe le contenue de  regroupedCodeList.xsd et la Norme.xsd
	public static void regroupRegroupedCodeListAndNorme() throws IOException {
			
		//trouve la valeur de prefixXmlPrimitifType  et prefixTargetNamespace dans le fichier Xsd de la norme
		String[] prefixNamespaceList  = schemaNorme.getNamespaceContext().getDeclaredPrefixes();
	    for(String prefixNamespace : prefixNamespaceList) {
	    	if(schemaNorme.getNamespaceContext().getNamespaceURI(prefixNamespace).equals(schemaNorme.getTargetNamespace())) {
	    		prefixTargetNamespace = prefixNamespace;
	     	};
	     	if(schemaNorme.getNamespaceContext().getNamespaceURI(prefixNamespace).equals("http://www.w3.org/2001/XMLSchema")) {
	     		prefixTargetXsdPrimitifType = prefixNamespace;
	     	};
	    }
	    
	    //On évite d'avoir deux types avec le même nom après avoir regroupé le contenue des deux fichiers
	    for(String typeCodeList :ListTypeCodeList) {
	    	if(schemaNorme.getTypeByName(typeCodeList)!=null) {
	    		schemaNorme.getItems().remove(schemaNorme.getTypeByName(typeCodeList));
	    	}
	    }
	    
	    //Ajoute de facon brute tous les éléments de la regroupedCodeList au schema xsd de la norme 
	    for(int i =0 ; i<schemaRegroupedCodeList.getItems().size(); i++) {
	    	schemaNorme.getItems().add(schemaRegroupedCodeList.getItems().get(i));
	    }
	        
	    //On copie ensuite ce schema xsd brute au fichier ResultXsd.xsd
	    BufferedWriter writer = new BufferedWriter(new FileWriter(fileResultXsd));
	    schemaNorme.write(writer);
	    writer.close();
	     	
	     	
	    //On relie ensuite le contenue du fichier ResultXsd.xsd ligne par ligne
	    //Afin d'apporter des modifications au schema pour qu'il soit le mieux retranscrit possible en class Java puis en fichier json
	    BufferedReader readerFileResultXsd = new BufferedReader(new FileReader(fileResultXsd));
	    String lineFileResultXsd = "", newContentResultXsd = "";
	    
	    //On modifie tous ce qui était lié à l'importation des CODE listes qui n'a plus lieu d'être
	    while((lineFileResultXsd = readerFileResultXsd.readLine()) != null){
	    	if(!lineFileResultXsd.contains("<"+prefixTargetXsdPrimitifType+":import")) {
	    		//On supprime ou remplace les préfixe des éléments des CODE liste pour que la syntaxe soit bonne
	    		lineFileResultXsd = lineFileResultXsd.replaceAll(" ecl:", "\t	");
	    		lineFileResultXsd = lineFileResultXsd.replaceAll("\"ecl:", "\"");
      	 		//permet de savoir si on doit rajouter les ":" ou non s'il y a un prefix ou non
      	 		if(prefixTargetNamespace!="") {
      	 			lineFileResultXsd = lineFileResultXsd.replaceAll("\"cl:", "\""+prefixTargetNamespace+":");
      	 		}
      	 		 else {
      	 			lineFileResultXsd = lineFileResultXsd.replaceAll("\"cl:", "\"");
      	 		}
      	 		
      	 		
      	 		
      	 		//Si il y a detection d'un attribut avec une valeur fixed on modifie le code xsd de cet attribut
      	 		//Afin que la valeur fixed soit bien retranscrite au niveau des classes Java et en Json Schema
      	 		if(lineFileResultXsd.contains("<"+prefixTargetXsdPrimitifType+":attribute")&&lineFileResultXsd.contains("fixed") &&lineFileResultXsd.contains("/>")) {
      	 			lineFileResultXsd=lookForFixedValue(lineFileResultXsd);
		    	}
      	 		//On ajoute la ligne modifiée ou non au nouveau content du fichier resultXsd
      	 		newContentResultXsd +=lineFileResultXsd + "\r\n";
      	 		
      	 		
      	 		//Si il y a detection d'une restriction avec un pattern on regarde si le type de base de cette restriction est différent d'un string
      	 		//Afin de le modifier si necessaire pour que la restriction soit bien retranscrite en class Java et en json Schema
      	 		if(lineFileResultXsd.contains("<"+prefixTargetXsdPrimitifType+":pattern")) {
      	 			newContentResultXsd=lookForNotStringPattern(newContentResultXsd);
       	 		}
	    	}
	    }
	    // On copie le contenue du fichier modifié pour sauvegarder les modifications
	    FileWriter writerfileResultXsd= new FileWriter(fileResultXsd);
	    FileWriter writerfileResultXsdBis= new FileWriter(fileResultXsdBis);
	    writerfileResultXsd.write(newContentResultXsd);
	    writerfileResultXsd.flush();
	    writerfileResultXsd.close();

	    writerfileResultXsdBis.write(newContentResultXsd);
	    writerfileResultXsdBis.flush();
	    writerfileResultXsdBis.close();
	}
	
	
	
	
	//S'il y a un attribut avec une valeur fixed en xsd
	//Transforme l'attribut en recréant son type. Type qui possedera une restriction pattern égale à la valeur fixed
	//Le pattern est bien retranscrit en json SCHEMA ce qui oblige à mettre la valeur fixed
	//Cette fonction prend en argument une ligne du fichier sous forme de string et renvoi un string de cette ligne après modification de celle-ci
	public static String lookForFixedValue(String line){		
		try{
			//variable qui représente la valeur fixed
			String fixedValue="";
			//On récupère cette valeur grace à un Regex
			Pattern pattern = Pattern.compile("fixed(\\s)*=(\\s)*\"((\\w|\\s|\\d|-|\\.))+\"");
		    Matcher matcher = pattern.matcher(line);
		    if(matcher.find()) {
		    	int start = matcher.group().indexOf("\"");
		    	int end = matcher.group().lastIndexOf("\"");
		    	fixedValue = line.substring(matcher.start()+start+1,matcher.start()+end);
		    	line = line.substring(0,matcher.start())+line.substring(matcher.end(),line.length())+"\r\n";
		    }
		    //Si on ne trouve pas cette valeur on renvoie la ligne directement sans aucune modifications
		    else {
		    	return line;
		    }
		    //On cherche ensuite le type de l'attribut grace à un Regex
		    Pattern pattern2 = Pattern.compile("type(\\s)*=(\\s)*\"((\\w|\\s|\\d|-|\\.))+\"");
		    Matcher matcher2 = pattern2.matcher(line);
		    if(matcher2.find()) {
		    	//Si on trouve ce type on le supprime de la ligne puis on ajoute un nouveau simpleType en local
		    	//Ce nouveau simpleType possèdera une restriction avec un pattern equivalent à ne pouvoir insérer que la valeur fixed
		    	line = line.substring(0,matcher2.start())+line.substring(matcher2.end(),line.length()).replaceAll("/>",">")+"\r\n"
		    		   +"<"+prefixTargetXsdPrimitifType+":simpleType>\r\n"
		    		   +"<"+prefixTargetXsdPrimitifType+":restriction base=\""+prefixTargetXsdPrimitifType+":string\" value=\"^"+fixedValue+"$\">\r\n"
		    		   +"<"+prefixTargetXsdPrimitifType+":pattern value=\"^"+fixedValue+"$\"/>\r\n"
		    		   +"</"+prefixTargetXsdPrimitifType+":restriction>\r\n"
		    		   +"</"+prefixTargetXsdPrimitifType+":simpleType>\r\n"
		    		   +"</"+prefixTargetXsdPrimitifType+":attribute>\r\n";   			
		    	}
		    else {
		    	line = line.replaceAll("/>",">")+"\r\n"
			    		   +"<"+prefixTargetXsdPrimitifType+":simpleType>\r\n"
			    		   +"<"+prefixTargetXsdPrimitifType+":restriction base=\""+prefixTargetXsdPrimitifType+":string\" value=\"^"+fixedValue+"$\">\r\n"
			    		   +"<"+prefixTargetXsdPrimitifType+":pattern value=\"^"+fixedValue+"$\"/>\r\n"
			    		   +"</"+prefixTargetXsdPrimitifType+":restriction>\r\n"
			    		   +"</"+prefixTargetXsdPrimitifType+":simpleType>\r\n"
			    		   +"</"+prefixTargetXsdPrimitifType+":attribute>\r\n";   	
		    }
		}catch (Exception e){
			return line;
		}
		//On retrourne la ligne modifiée
		return line;
	}
	
	
	
	//pour une restriction de type pattern en xsd cette fonction regarde le primitif type de la restriction
	//Si le type primitif est different de xs:string (ex: xs:integer) modifie ce type en xs:string
	//En xsd la restriction de type pattern peut s'appliquer avec des types primitifs != d'un string (ex:xs:dateTime) alors qu'en json Schema#4 on ne peut appliquer un pattern que sur les strings
	//Cette fonction prend en argument un string correspondant au contenu du fichier resultXsd deja parcouru par le reader
	//La fonction renvoie un string modifié si necessaire du contenu
	public static String lookForNotStringPattern(String oldtext){
		//On split ligne par ligne le contenu en argument
		String[] linesOldtext= oldtext.split("\n");
		//La variable newText reprèsente le string du nouveau contenu
		//La variable modifiedText reprèsente juste la portion du contenu inital modifié par cette fonction
		String newText = "", modifiedText="";
		//On parcourt le contenu mis en argument ligne par ligne en sens inverse de la dernière ligne vers la première
		int i = linesOldtext.length-1;
		int check=0, compteurLine=0;
		while(i >=0 && check!=1){
			String lineOldtext = linesOldtext[i];
			
			if(lineOldtext.contains("<"+prefixTargetXsdPrimitifType+":restriction") && lineOldtext.contains("base=")) {
				//Aussi tot que l'on détecte la premiére balise restriction du code	on cherche la valeur de la base de cette balise grace a un regex 		
				try{
					Pattern pattern = Pattern.compile("base\\s*=\\s*\".*\"");
				    Matcher matcher = pattern.matcher(lineOldtext);
				    //Si on la trouve on la change automatiquement en xs:string
				    if(matcher.find()) {	
				    	int start = matcher.group().indexOf("\"");
				    	int end = matcher.group().lastIndexOf("\"");
				    	String primitiveType = lineOldtext.substring(matcher.start()+start+1,matcher.start()+end);
				    	lineOldtext=lineOldtext.replace(primitiveType,prefixTargetXsdPrimitifType+":string");
				    }
				     
				    //On ajoute cette nouvelle ligne modifiée à la variable modifiedText		
				    modifiedText+=lineOldtext+"\n";
					compteurLine+=1;
					check=1;
				}catch (Exception e){
					//si on detecte une erreur on renvoie la ligne sans l'avoir modifiée
					modifiedText+=lineOldtext+"\n";
					compteurLine+=1;
					check=1;
				}
			}
			else {
				modifiedText+=lineOldtext+"\n";
				compteurLine+=1;
			}
			//On decrémente le compteur des lignes parcourues à l'envers dans le code xsd
			i--;
		}
				 
		
		//On ré-inverse le contenu de modifiedText qui était inversé par rapport au contenu initial
				 
		String[] linesmodifiedText= modifiedText.split("\n");
		List<String> linesmodifiedTextList = Arrays.asList(linesmodifiedText);
		Collections.reverse(linesmodifiedTextList);
		linesmodifiedText = (String[]) linesmodifiedTextList.toArray(); 
		modifiedText="";
		for(String linemodifiedText : linesmodifiedText) {
			modifiedText+=linemodifiedText;
		}
		
		//Puis on créer le contenu global de la valeur newtext a partir de la portion non modifiée du contenu initial mis en argument, plus la partie modifiée (modifiedText)
				
		for(int j=0;j<linesOldtext.length-compteurLine;j++) {
			newText+=linesOldtext[j];
		}		 
		newText +=modifiedText;
		//On renvoie alors ce nouveau contenu	
		return newText;
				
	}
	
	
}
