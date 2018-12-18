//Content du fichier bat "Pom.xml" qui génére les class Java des xsd
public class PomXmlContent {
	public static String content =
			"<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\r\n" + 
			"  <modelVersion>4.0.0</modelVersion>\r\n" + 
			"  <groupId>canard-programme-child</groupId>\r\n" + 
			"  <artifactId>canard-programme-child</artifactId>\r\n" + 
			"  <version>0.0.1-SNAPSHOT</version>\r\n" + 
			"  \r\n" + 
			"  <name>JD Example XSD to Java</name>\r\n" + 
			"\r\n" + 
			"	\r\n" + 
			"	<properties>\r\n" + 
			"		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\r\n" + 
			"	</properties>\r\n"+
			"	<dependencies>\r\n" +
			"		<dependency>\r\n" + 
			"		 	<groupId>Xsd2JsonSchema</groupId>\r\n" + 
			"    		<artifactId>Xsd2JsonSchemaSoftware</artifactId>\r\n" + 
			"    		<version>1.0.0</version>\r\n" + 
			"    		<scope>system</scope>\r\n" + 
			"    		<systemPath>${project.basedir}/Xsd2JsonSchemaSoftware-1.0.0-executable.jar</systemPath>	\r\n" + 
			"		</dependency>\r\n"+
			"	</dependencies>\r\n" + 
			"<build>\r\n" + 
			"		<plugins>\r\n" + 
			"		<plugin>\r\n" + 
			"				<groupId>org.codehaus.mojo</groupId>\r\n" + 
			"				<artifactId>jaxb2-maven-plugin</artifactId>\r\n" + 
			"				<version>1.5</version>\r\n" + 
			"				<dependencies>\r\n" + 
			"					<dependency>\r\n" + 
			"						<groupId>org.jvnet.jaxb2_commons</groupId>\r\n" + 
			"						<artifactId>jaxb2-basics</artifactId>\r\n" + 
			"						<version>0.6.4</version>\r\n" + 
			"						<exclusions>\r\n" + 
			"							<exclusion>\r\n" + 
			"								<groupId>com.sun.xml</groupId>\r\n" + 
			"								<artifactId>jaxb-xjc</artifactId>\r\n" + 
			"							</exclusion>\r\n" + 
			"						</exclusions>\r\n" + 
			"					</dependency>\r\n" + 
			"					<dependency>\r\n" + 
			"						<groupId>com.github.krasa</groupId>\r\n" + 
			"						<artifactId>krasa-jaxb-tools</artifactId>\r\n" + 
			"						<version>1.5</version>\r\n"+
			"						<exclusions>\r\n" + 
			"							<exclusion>\r\n" + 
			"    							<groupId>javax.xml.stream</groupId>\r\n" + 
			"    							<artifactId>stax-api</artifactId>\r\n" + 
			"							</exclusion>\r\n" + 
			"							<exclusion>\r\n" + 
			"    							<groupId>javax.xml.bind</groupId>\r\n" + 
			"    							<artifactId>jaxb-api</artifactId>\r\n" + 
			"							</exclusion>\r\n" + 
			"							<exclusion>\r\n" + 
			"								<groupId>javax.activation</groupId>\r\n" + 
			"    							<artifactId>activation</artifactId>\r\n" + 
			"							</exclusion>\r\n" + 
			"						</exclusions>\r\n" + 
			
			"					</dependency>\r\n" + 
			"			</dependencies>\r\n" + 
			"				 \r\n" + 
			"                <executions>\r\n" + 
			"					<execution>\r\n" + 
			"						<configuration>\r\n" + 
			"                   <!-- The name of your generated source package -->\r\n" + 
			"                		<outputDirectory>src/main/java</outputDirectory>\r\n" + 
			"						<packageName>generated</packageName>\r\n" +
			"    					<extension>true</extension>\r\n" + 
			"						<enableIntrospection>true</enableIntrospection>\r\n" + 
			"    					<wsdl>true</wsdl>\r\n" + 
			"    					<cl>true</cl>\r\n" + 
			"    					<xmlschema>false</xmlschema>\r\n" + 
			"						<clearOutputDir>false</clearOutputDir>\r\n"+		
			"    					\r\n" + 
			"\r\n" + 
			"						\r\n" + 
			"                		\r\n" + 
			"                		<arguments>-Xinheritance -XJsr303Annotations</arguments>\r\n" + 
			"                	 	<catalog>src/main/resources/catalog.xml</catalog>\r\n" + 
			"      \r\n" + 
			"       \r\n" + 
			"               			 </configuration>\r\n" + 
			"						<id>execution3</id>\r\n" + 
			"						<phase>clean</phase>\r\n" + 
			"						<goals>\r\n" + 
			"							<goal>xjc</goal>\r\n" + 
			"						</goals>\r\n" + 
			"					</execution>\r\n" + 
			"				\r\n" + 
			"\r\n" + 
			"				</executions>\r\n" + 
			"			</plugin>\r\n" + 	
			"	</plugins>\r\n"+
			"</build>\r\n" + 
			"</project>";
}
