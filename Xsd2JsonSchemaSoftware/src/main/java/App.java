import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

//Classe principale de l'application
public class App 
{	
	//String du path vers le dossier d'exécution de l'outil
	public static String path=new File("").getAbsolutePath(); 
	
	//file contenant la norme Xsd toute seule
	public static File fileNorme = new File(path+"\\resources/xsd/Norme.xsd");
	
	//file de la CODE liste Standard Xsd
	public static File fileCodeListStandard = new File(path+"\\resources/xsd/StandardCodeList.xsd");
    
	//File de CODE liste locale en Xsd
	public static File fileCodeListLocal = new File(path+"\\resources/xsd/LocalCodeList.xsd");
	
	//file regroupant le contenue de la CODE liste standard et de la CODE liste locale en Xsd
	public static File fileRegroupedCodeList = new File(path+"\\resources/xsd/RegroupedCodeList.xsd");
	
	//file regroupant le contenue de la norme de la CODE liste standard et de la CODE liste locale en Xsd
	public static File fileResultXsd = new File(path+"\\resources/xsd/ResultXsd.xsd");
	
	//Copie de fileResultXsd dans le dossier "src/main/xsd/"
    public static File fileResultXsdBis = new File(path+"\\src/main/xsd/ResultXsd.xsd");
    
    //variables représentant le préfixe de la targerNamespace du schéma Xsd et le préfixe des types primitifs xml du fichier Xsd
	public static String prefixTargetNamespace="" , prefixTargetXsdPrimitifType="";
	
	//Tableau contenant l'ensemble des descriptions des CODE listes
	public static Map<String, HashMap<String, String[]>> descriptionCodeListTab = new HashMap<String, HashMap<String, String[]>>();
	
	//Liste de tous les types des CODE listes
	public static  ArrayList<String>ListTypeCodeList = new ArrayList<String>();
	
	//liste des classes JAVA générées qui ne sont pas des classes des éléments de la CODE liste mais du schéma de la norme
	public static ArrayList<File> fileNormeClassList;
	
	//nom de la classe représentant l'élément root du schéma Xsd
	public static String nameRootClassJava;
	
	//Un élément de la classe WaitingScreen
	public static WaitingScreen waitingScreen;
	
	//Un élément de la classe InitScreen
	public static InitScreen initScreen;
	
	//Un élément de la classe FinalScreen
	public static FinalScreen finalScreen;
	
	public static void main( String[] args ) throws IOException{	
		
		try {
			initScreen = new InitScreen();
		}catch(Throwable e) {
			e.printStackTrace();
			new ErrorScreen(e.getMessage(), "Running initScreen");
		}
		
		try {
			InitPhase.run();
		}catch(Throwable e) {
			e.printStackTrace();
			new ErrorScreen(e.getMessage(), "InitRun");
		}
		
        initScreen.setVisible(false);
		initScreen.dispose();
		
		try {
			SelectFilesPhase.run();
		}catch(Throwable e) {
			e.printStackTrace();
			new ErrorScreen(e.getMessage(), "SelectFilesRun");
		}
        
		try {
			TransformXsdPhase.run();
		}catch(Throwable e) {
			e.printStackTrace();
			new ErrorScreen(e.getMessage(), "TransformXsdPhaseRun");
		}
        
        //Execute un clean du fichier pom qui lance la librairie qui créer les classes java a partir du fichier resultXsd.xsd
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(path+"\\pom.xml"));
        request.setGoals(Collections.singletonList("clean"));
        Invoker invoker = new DefaultInvoker();
        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            e.printStackTrace();
            new ErrorScreen(e.getMessage(), "MavenGenerateJavaClass");
        }
        
        try {
        	TransformJavaClassGeneratedPhase.run();
        } catch(Throwable e) {
        	e.printStackTrace();
			new ErrorScreen(e.getMessage(), "TransformJavaClassPhaseRun");
        }
         
        
        //Execute le fichier bat "CompilCreatedClass.bat" qui compil les class java générées
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(path+"\\CompilCreatedClass.bat");
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
			new ErrorScreen(e.getMessage(), "CompilCreatedClass");
		}
		
		
		waitingScreen.dispose();
		
		try {
			finalScreen = new FinalScreen();
		}catch(Throwable e) {
			e.printStackTrace();
			new ErrorScreen(e.getMessage(), "Running finalScreen");
		}
		
		
		
		
	}
}
