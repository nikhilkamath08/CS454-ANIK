package cs454.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.Link;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xml.sax.ContentHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Crawl {
	  int h = 1;
	  int i = 1;
	 
    @SuppressWarnings("unchecked")
    public void crawl(String Url,String depth)throws Exception{
    	TrustManager[] trustAllCerts = new TrustManager[]{
    			//cited, stackoverflow
                new X509TrustManager() {

                    public java.security.cert.X509Certificate[] getAcceptedIssuers()
                    {
                        return null;
                    }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                    {
                        //No need to implement.
                    }
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                    {
                        //No need to implement.
                    }
                }
        };
        URL url = new URL(Url);
        
       
        URLConnection Con = url.openConnection();
        
        InputStream input = url.openStream();
        
        File theDir = new File("D:\\crawldata");
        if (!theDir.exists()) {            
            boolean result = false;
            try{
                theDir.mkdir();
                result = true;
             } catch(SecurityException se){
                
             }             
          } 
        
        LinkContentHandler linkHandler = new LinkContentHandler();
        ContentHandler textHandler = new BodyContentHandler();
        ToHTMLContentHandler toHTMLHandler = new ToHTMLContentHandler();
        
        TeeContentHandler teeHandler = new TeeContentHandler(linkHandler, textHandler, toHTMLHandler);
        
        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();
        AutoDetectParser parser = new AutoDetectParser();
        
        parser.parse(input, teeHandler, metadata, parseContext);
         
        JSONObject obj = new JSONObject();
        
        JSONArray  array1 = new JSONArray();
        
        
        List<Link> links = linkHandler.getLinks();
      
        ArrayList<String> arraylist1 = new ArrayList<String>();
        Queue<String> myQueue = new LinkedList<String>();
        obj.put("Title", metadata.get("title"));
        obj.put("URL", url);
        obj.put("Content-Type",Con.getContentType());
      
        try{
        String w = Con.getContentType();
        String id = UUID.randomUUID().toString();
        
        	
        if(w.contains("html"))
        {
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream("D:/crawldata/"+id+".html");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        String path = "D:/crawldata/"+id+".html";
        arraylist1.add(path);
        i++;
        obj.put("storage", path);
        }
        else if(w.contains("png"))
        {
        	ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("D:/crawldata/"+id+".png");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            String path = "D:/crawldata/"+id+".png";
            arraylist1.add(path);
            i++;
            obj.put("storage", path);
        }
        else if(w.contains("gif"))
        {
        	ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("Storage/"+id+".gif");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            String path = "Storage/"+id+".gif";
            arraylist1.add(path);
            i++;
            obj.put("storage", path);
        }
        else if(w.contains("jpeg")||w.contains("jpg"))
        {
        	ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("Storage/"+id+".jpg");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            String path = "Storage/"+id+".jpg";
            arraylist1.add(path);
            i++;
            obj.put("storage", path);
        }
        else if(w.contains("pdf"))
        {
        	ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("Storage/"+id+".pdf");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            String path = "Storage/"+id+".pdf";
            arraylist1.add(path);
            i++;
            obj.put("storage", path);
        }
        else if(w.contains("doc"))
        {
        	ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("Storage/"+id+".doc");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            String path = "Storage/"+id+".doc";
            arraylist1.add(path);
            i++;
            obj.put("storage", path);
        }
        else if(w.contains("audio"))
        {
        	
            InputStream is = Con.getInputStream();

            OutputStream outstream = new FileOutputStream(new File("Storage/"+id+".mp3"));
            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) > 0) {
                outstream.write(buffer, 0, len);
            }
            outstream.close();
            String path = "Storage/"+id+".mp3";
            arraylist1.add(path);
            i++;
            obj.put("storage", path);
        }
        
       
       
        }
        catch(NullPointerException e)
        {
        	
        }
   
        for (Link link: links)
        {
        	String anchor = link.getUri();
        	if(!anchor.startsWith("htt"))
        	{
        		anchor = url+anchor;
        	}
        	

        	JSONObject obj2 = new JSONObject();
        	
       
        
        	
        	int dep=Integer.parseInt(depth);
        	if(h<dep){        	
        		
        			if(!myQueue.contains(anchor))
        			{
              myQueue.add(anchor);
        			}
        		
        		}
        	
            String name = link.getText();
        	
        	
        	obj2.put("text", name);
           
        	obj2.put("URL", anchor);
        	array1.add(obj2);
        }
        obj.put("Links", array1);
      
        h++;
  
        for(String v: myQueue)
        {
        	if(!v.startsWith("#") && !v.startsWith("/")||v.startsWith("http")||v.startsWith("https"))
        		try{
        		
        	crawl(v,depth);
        	}
        	catch( MalformedURLException malformedException){
        		
        	}
        	catch (FileNotFoundException e){
        	    
        	   }
        	catch(IOException ex){
        }
        }
     
    
      
      File f = new File("D:/crawldata/crawl.json");
      
      BufferedWriter file = new BufferedWriter(new FileWriter(f,true)); 
        try {
        	ObjectMapper mapper = new ObjectMapper();
           file.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
           System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
        
            file.newLine();
            file.newLine();
            file.newLine();
            file.newLine();
         
 
        } catch (IOException e) {
            e.printStackTrace();
 
        } finally {
            file.flush();
            file.close();
        }
        
        
  
}
}