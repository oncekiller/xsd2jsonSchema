//Content du fichier global.xjb
public class GlobalXjbContent {
	public static String content =
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
			"<jaxb:bindings version=\"2.0\"\r\n" + 
			"  xmlns:jaxb=\"http://java.sun.com/xml/ns/jaxb\"\r\n" + 
			"  xmlns:xjc=\"http://java.sun.com/xml/ns/jaxb/xjc\"\r\n" + 
			"  xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"\r\n" + 
			" \r\n" + 
			"  jaxb:extensionBindingPrefixes=\"xjc\">\r\n" + 
			"  \r\n" + 
			"\r\n" + 
			"\r\n" + 
			" \r\n" + 
			" <jaxb:globalBindings  typesafeEnumMemberName=\"generateName\"  >\r\n" + 
			" 	\r\n" + 
			"    <xjc:simple />\r\n" + 
			"    <xjc:serializable uid=\"1\" />\r\n" + 
			"    \r\n" + 
			"	\r\n" + 
			
			"  </jaxb:globalBindings>\r\n" + 
			"  \r\n" + 
			"  <jaxb:bindings schemaLocation=\"../xsd/ResultXsd.xsd\">\r\n" + 
			"        <jaxb:schemaBindings>\r\n" + 
			"            <jaxb:package name=\"generated\" />\r\n" + 
			"        </jaxb:schemaBindings>\r\n" + 
			"  </jaxb:bindings>\r\n"+
			"</jaxb:bindings>";
	
}
