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
	public static String path=new File("").getAbsolutePath(); 
	//file contenant la norme en Xsd toute seule
	public static File fileNorme = new File(path+"\\resources/xsd/Norme.xsd");
	 //file de la standardCodeList en xsd
	public static File fileCodeListStandard = new File(path+"\\resources/xsd/StandardCodeList.xsd");
    //file de la localCodeList en xsd
	public static File fileCodeListLocal = new File(path+"\\resources/xsd/LocalCodeList.xsd");
	//file regroupant la standard codeList et la local codeList en Xsd
	public static File fileRegroupedCodeList = new File(path+"\\resources/xsd/RegroupedCodeList.xsd");
	//file regroupant la norme et les deux codeLists regroupées en Xsd 
	public static File fileResultXsd = new File(path+"\\resources/xsd/ResultXsd.xsd");
	//Copie de fileResultXsd dans le dossier "src/main/xsd/"
    public static File fileResultXsdBis = new File(path+"\\src/main/xsd/ResultXsd.xsd");
    //variables représentant le prefix de la targerNamespace et préfix des type primitifs xml du fichier xsd
	public static String prefixTargetNamespace="" , prefixTargetXsdPrimitifType="";
	//Tableau contenant l'ensemble des descriptions des codeLists
	public static Map<String, HashMap<String, String[]>> descriptionCodeListTab = new HashMap<String, HashMap<String, String[]>>();
	//Liste de tous les types des codeList
	public static  ArrayList<String>ListTypeCodeList = new ArrayList<String>();
	//liste des class java générées qui ne sont pas des class des élément de la codeList mais de la norme
	public static ArrayList<File> fileNormeClassList;
	//nom de la class de l'élément root
	public static String nameRootClassJava;
	public static WaitingScreen waitingScreen;
	public static InitScreen initScreen;
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
