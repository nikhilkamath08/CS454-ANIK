package index;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;

public class Ranking {

    int i = 1;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void rank( LinkedHashMap<String, Integer> WordsAndDocCoutnt )
        throws IOException
    {

        final InputStream in = new FileInputStream( "D:/Anisha/Indexed.json" );

        // Map<String, Integer> words = new HashMap<String, Integer>();
        

        try
        {
            for( Iterator it = new ObjectMapper().readValues(
                new JsonFactory().createJsonParser( in ), Map.class ); it.hasNext(); )
            {
                LinkedHashMap<String, Object> keyValue = (LinkedHashMap<String, Object>) it.next();
                String word = (String) keyValue.get( "word" );
                String link = (String) keyValue.get( "link" );
                String url = (String) keyValue.get( "url" );
                Integer frq = (Integer) keyValue.get( "frequency" );

                @SuppressWarnings("unused")
                String t = (String) keyValue.get( "title" );
                Integer totalWords = (Integer) keyValue.get( "TotalWords" );
                Double tf = 0.000;
                Double df_d = 0.000;
                Double idf = 0.000;
                Double weight = 0.000;

                // Find TF

                if( frq != null || totalWords != null )
                {
                    if( frq != 0 || totalWords != 0 )
                    {
                        Double freq_d = (double) frq;
                        Double totalWords_d = (double) totalWords;

                        tf = freq_d / totalWords_d;
                        // Math.a
                        DecimalFormat _numberFormat = new DecimalFormat(
                            "#0.000" );;
                        System.out.println( _numberFormat.format( (frq / totalWords) ) );
                        // BigDecimal bigDecimal=new BigDecimal(tf);
                        System.out.println( "---------------TF-------------------" );
                        System.out.println( "frq:" + frq );
                        System.out.println( "totalWords:" + totalWords );
                        System.out.println( "TF--->" + tf );
                    }
                }

                // Find DF

                Integer df = WordsAndDocCoutnt.get( word );
                df_d = (double) df;

                // Find IDF

                Integer TotalDocCount = WordsAndDocCoutnt.get( "TotalDocCount" );
                Double TotalDocCount_d = (double) TotalDocCount;

                idf = Math.log10( TotalDocCount_d / df_d );

                System.out.println( "IDF->" + idf );

                // Find Weight

                weight = tf * idf;
                System.out.println( "Weight-->" + weight );

                JSONObject obj = new JSONObject();
                obj.put( "word", word );
                obj.put( "link", link );
                obj.put( "tf", tf );
                obj.put( "idf", idf );
                obj.put( "weight", weight );
                obj.put( "url", url );

                File f4 = new File( "D:/Anisha/ranking.json" );
                BufferedWriter file2 = new BufferedWriter( new FileWriter( f4,
                    true ) );
                try
                {

                    ObjectMapper mapper = new ObjectMapper();
                    file2.write( mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString( obj ) );
                    System.out.println( mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString( obj ) );
                    file2.newLine();
                    file2.newLine();

                }

                catch( IOException e1 )
                {
                    e1.printStackTrace();

                }
                finally
                {
                    file2.flush();
                    file2.close();
                }
            }// End of FOR
        }
        finally
        {
            in.close();
        }

    }

}
