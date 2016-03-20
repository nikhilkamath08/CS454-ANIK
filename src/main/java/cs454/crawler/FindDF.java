package cs454.crawler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

public class FindDF {

    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, Integer> findDF( String IndexFilePath )
        throws IOException
    {

        // final InputStream in = new FileInputStream("D:/crawldata/index.json");
        final InputStream in = new FileInputStream( IndexFilePath );
        LinkedHashMap<String, Integer> wordAndcount = new LinkedHashMap<String, Integer>();
        // List<String,String> wordAndDoc=new ArrayList<String,String>();
        @SuppressWarnings("unused")
        Set<String> wordsSet = new HashSet<String>();

        // Map<String, Integer> words = new HashMap<String, Integer>();

        Set<String> DocCount = new HashSet<String>();

        try
        {
            for( @SuppressWarnings("rawtypes")
            Iterator it = new ObjectMapper().readValues(
                new JsonFactory().createJsonParser( in ), Map.class ); it.hasNext(); )
            {
                LinkedHashMap<String, Object> keyValue = (LinkedHashMap<String, Object>) it.next();
                String word = (String) keyValue.get( "word" );

                String local = (String) keyValue.get( "link" );
                DocCount.add( local );

                @SuppressWarnings("unused")
                Integer frq = (Integer) keyValue.get( "frequency" );

                @SuppressWarnings("unused")
                String t = (String) keyValue.get( "title" );

                int count = 1;

                if( wordAndcount.keySet().contains( word ) )
                {
                    wordAndcount.put( word, wordAndcount.get( word ) + 1 );
                }
                else
                {
                    wordAndcount.put( word, count );
                }

            }

            wordAndcount.put( "TotalDocCount", DocCount.size() );

            for( @SuppressWarnings("rawtypes")
            Map.Entry m : wordAndcount.entrySet() )
            {
                System.out.println( "[" + m.getKey() + " : " + m.getValue()
                    + "]" );
            }

        }
        finally
        {
            in.close();
        }
        return wordAndcount;

    }
}
