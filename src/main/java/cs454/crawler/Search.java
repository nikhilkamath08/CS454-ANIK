package crawler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Search")
public class Search extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public Search()
    {
    }

    protected void doGet( HttpServletRequest request,
        HttpServletResponse response ) throws ServletException, IOException
    {

        response.sendRedirect( "SearchPage.jsp" );

    }

    protected void doPost( HttpServletRequest request,
        HttpServletResponse response ) throws ServletException, IOException
    {

        String searchword = request.getParameter( "searchword" );
        System.out.println(searchword);

        try
        {
            Map<Double, String> result = new RankingTest().rank2( searchword );
            System.out.println( "---->" + result.size() );

            request.setAttribute( "result", result );

            request.getRequestDispatcher( "SearchPage.jsp" ).forward( request,
                response );
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
    }

}
