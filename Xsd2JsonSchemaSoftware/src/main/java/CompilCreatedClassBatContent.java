//Content du fichier bat "CompilCreatedClass.bat" qui compile les class java générées et modififiées 
public class CompilCreatedClassBatContent {
	static String content= 
			"javac -classpath Xsd2JsonSchemaSoftware-1.0.0-executable.jar; ./src/main/java/*.java\r\n" + 
			"javac -classpath Xsd2JsonSchemaSoftware-1.0.0-executable.jar; ./src/main/java/generated/*.java\r\n"+ 
			"Java -cp Xsd2JsonSchemaSoftware-1.0.0-executable.jar;src/main/java JavaClassToJsonSchemaPhase";
}
