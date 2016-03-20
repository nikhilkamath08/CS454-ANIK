package cs454.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.xml.sax.SAXException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;

public class Extraction {

	 @SuppressWarnings("unchecked")
	public void extract(String c) throws IOException, SAXException, TikaException {
		 
		 
		/*String cf = "D:/crawler3-app/target/"+c;
			InputStream in = new ByteArrayInputStream(cf.getBytes("UTF-8"));*/
		 
		 
		 final InputStream input = new FileInputStream("D:/crawldata/"+c);
		 
				try {
					for (Iterator iterate = new ObjectMapper().readValues(
							new JsonFactory().createJsonParser(input), Map.class); iterate
							.hasNext();) {
						@SuppressWarnings("unchecked")
						LinkedHashMap<String, String> keyValue = (LinkedHashMap<String,String>) iterate.next();
						//System.out.println(keyValue.get("storage"));
			
						try{
							BodyContentHandler handler = new BodyContentHandler();
						      Metadata metadata = new Metadata();
						      System.out.println(keyValue.get("storage"));
						      FileInputStream inputstream = new FileInputStream(keyValue.get("storage"));
						      ParseContext pcontext = new ParseContext();
						      AutoDetectParser  msofficeparser = new AutoDetectParser(); 
						      msofficeparser.parse(inputstream, handler, metadata,pcontext);
						      
						      JSONObject obj = new JSONObject();
						      
						      obj.put("path", keyValue.get("storage"));
						      obj.put("url",keyValue.get("URL") );
						        
						          for(int i = 0; i <metadata.names().length; i++) { 
						        	  String name = metadata.names()[i]; 
						        	  obj.put(name,metadata.get(name));
						        
						        	  }
						        
					          
					         
					          File f2 = new File("D:/crawldata/extactdata.json");
					          BufferedWriter writefile = new BufferedWriter(new FileWriter(f2,true)); 
					            try {
					            	
					            	ObjectMapper mapper = new ObjectMapper();
					            	writefile.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
					           System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
					           writefile.newLine();
					                writefile.newLine();
					                writefile.newLine();
					                writefile.newLine();
					                writefile.newLine();
					               
					     
					            } catch (IOException e) {
					                e.printStackTrace();
					     
					            } finally {
					            	writefile.flush();
					            	writefile.close();
					            }
					            
							}
							catch(FileNotFoundException e){
								
							}
	                     catch(RuntimeException e){
							System.out.println(e);
						}
						
					}
			
				}
					finally {
						input.close();
					}
			
		    }
		 
		 
			
	 }
		
			
			
	 
			 
		
		
			
		 
		     
		 
		 
		 
		 
		 
		 
		 
		 
		 
		
		  

		


