package index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.codehaus.jackson.JsonFactory;
import org.json.simple.JSONObject;
import org.tartarus.snowball.ext.PorterStemmer;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.codehaus.jackson.map.ObjectMapper;

public class Index {

    @SuppressWarnings("unchecked")
    public void listf( String directoryname, ArrayList<File> files )
        throws Exception
    {
        System.out.println( "inside listf" );
        // get all the files from a directory
        JSONObject obj = new JSONObject();
        File directory = new File( directoryname );
        File[] fList = directory.listFiles();
        for( File file : fList )
        {
            if( file.isFile() )
            {
                files.add( file );
                // perform replacing of \\ to / here
                System.out.println( "files:" + files );
                String f1 = file.toString();

                String files1 = f1.replace( "\\", "/" );
                System.out.println( "files1:" + files1 );
                // writing to JSON
                obj.put( "path", files1 );
                File f = new File( "D:/Anisha/linkshw3.json" );
                BufferedWriter file1 = new BufferedWriter( new FileWriter( f,
                    true ) );
                try
                {
                    ObjectMapper mapper = new ObjectMapper();
                    file1.write( mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString( obj ) );
                    System.out.println( mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString( obj ) );
                }
                catch( IOException e )
                {
                    e.printStackTrace();
                }
                finally
                {
                    file1.flush();
                    file1.close();
                }
                files.remove( file );
            }
            else if( file.isDirectory() )
            {
                listf( file.getAbsolutePath(), files );
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void index( String c, String stw, String index, String rank )
        throws IOException
    {
        System.out.println( "test inside index" );
        ArrayList<String> ranking = new ArrayList<String>();
        final InputStream in = new FileInputStream( "D:/Anisha/" + c );
        try
        {
            for( @SuppressWarnings("rawtypes")
            // iterate over files
            Iterator it = new ObjectMapper().readValues(
                new JsonFactory().createJsonParser( in ), Map.class ); it.hasNext(); )
            {
                System.out.println( "it has next" + it.hasNext() );
                LinkedHashMap<String, String> keyValue = (LinkedHashMap<String, String>) it.next();
                System.out.println( "path " + keyValue.get( "path" ) );
                System.out.println( "Title" + keyValue.get( "title" ) );
                try
                {
                    InputStream input = new FileInputStream(
                        keyValue.get( "path" ) );
                    ContentHandler handler = new BodyContentHandler();
                    Metadata metadata = new Metadata();
                    // Parser initialize
                    new HtmlParser().parse( input, handler, metadata,
                        new ParseContext() );
                    System.out.println( "Handler" + handler );
                    System.out.println( "Meta Data :" + metadata );
                    System.out.println( "new parse context"
                        + new ParseContext() );
                    String plainText = handler.toString();
                    System.out.println( "Plain Text : Handler to String"
                        + handler.toString() );
                    JSONObject obj = new JSONObject();
                    ArrayList<String> meta = new ArrayList<String>();
                    // iterate over metadata and add metadata values to meta
                    // array
                    for( int i = 0; i < metadata.names().length; i++ )
                    {
                        String name = metadata.names()[i];
                        System.out.println( "metadata names"
                            + metadata.names()[i] );
                        meta.add( metadata.get( name ) );
                    }
                    String listString = "";
                    // seperate sentences
                    for( String s : meta )
                    {
                        listString += s + "\t";
                    }
                    // concate metadata and body content of html
                    String indexing = listString + plainText;
                    String result = indexing.replaceAll( "[,]", "" );
                    String result1 = result.toLowerCase();

                    // seperate sentences to single words
                    /*
                     * String[] stringArray = result.split( "\\s+" );
                     * List<String> wordList = Arrays.asList( stringArray ); //
                     * add words if not in ranking array for( String word :
                     * stringArray ) { if( !ranking.contains( word ) ) {
                     * ranking.add( word ); } ArrayList<String> Stop_Words_List
                     * = new ArrayList<String>();
                     */

                    // --------------------------------------------------------------

                    String[] stringArray = result1.split( "\\s+" );
                    List<String> wordList = Arrays.asList( stringArray );
                    // we have list of all words..
                    // we stemm the words and maintain them in List..
                   // final PorterStemmer stemmer = new PorterStemmer();

                    /*List<String> wordList = new ArrayList<String>();

                    for( String s : wordList )
                    {
                        stemmer.setCurrent( s );
                        stemmer.stem();
                        final String current = stemmer.getCurrent();
                        wordList.add( current );
                        System.out.println( s + " : " + current );
                    }*/

                    int TotalWordsInDOC = wordList.size();

                    // Then make the set so that entry dosent repeats...

                    Set<String> StemmedSET = new HashSet<String>();
                    for( String word1 : wordList )
                    {
                        
                        StemmedSET.add( word1.toLowerCase() );

                    }
                    for( String word : StemmedSET )
                    {
                        // System.out.println(str);
                        if( !ranking.contains( word ) )
                        {
                            ranking.add( word );
                        }

                        ArrayList<String> Stop_Words_List = new ArrayList<String>();
                        FileInputStream fstream = new FileInputStream(
                            "D:/Anisha/" + stw );
                        BufferedReader br = new BufferedReader(
                            new InputStreamReader( fstream ) );
                        String strLine;
                        // Read File Line By Line
                        while( (strLine = br.readLine()) != null )
                        {
                            Stop_Words_List.add( strLine.toLowerCase() );
                        }
                        // Close the input stream
                        br.close();
                        // perform frequency and meta data extraction if word
                        // not in stop words file
                        if( !Stop_Words_List.contains( word ) )
                        {
                            //String Word = word.toLowerCase();
                            obj.put( "link", keyValue.get( "path" ) );
                            obj.put( "title", keyValue.get( "title" ) );
                            obj.put( "url", keyValue.get( "url" ) );
                            obj.put( "word", word );
                            obj.put( "frequency",
                               // Collections.frequency( ranking, word ) );
                                Collections.frequency( wordList, word ) );
                            obj.put("TotalWords", TotalWordsInDOC);
                        }
                        File f2 = new File( "D:/Anisha/Indexed.json" );
                        BufferedWriter file2 = new BufferedWriter(
                            new FileWriter( f2, true ) );
                        try
                        {
                            // write to new index json file
                            ObjectMapper mapper = new ObjectMapper();
                            file2.write( mapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString( obj ) );
                            System.out.println( mapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString( obj ) );
                            file2.newLine();
                            file2.newLine();
                        }
                        catch( IOException e )
                        {
                            e.printStackTrace();
                        }
                        finally
                        {
                            file2.flush();
                            file2.close();
                        }
                    }
                }
                // Calculate TF
                /*
                 * TF(t) = (Number of times term t appears in a document) /
                 * (Total number of terms in the document).
                 */
                catch( FileNotFoundException e )
                {
                }
                catch( Exception e )
                {
                }
            }
        }
        finally
        {
            in.close();
        }
    }

    public static void main( String[] args ) throws IOException, SAXException,
        TikaException
    {
        // To Crawl documents inside folders of folders
        ArrayList<File> files = new ArrayList<File>();
        String directoryname = "D:/Anisha/en";
        Index f = new Index();
        try
        {
          f.listf( directoryname, files );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        System.out.println( "test" );
        String controlfile = "linkshw3.json";
        String stw = "Stop_Words_List.txt";
        String index = "index.json";
        String rank = "rank";
         new Index().index( controlfile, stw, index, rank );
    }
}