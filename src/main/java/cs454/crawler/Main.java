package cs454.crawler;

import java.io.File;
import java.io.IOException;

import netscape.ldap.util.GetOpt;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ToStringStyle;


public class Main {

	public static void main(String[] args) throws Exception {
		//String v = args[0];
	   // String d = args[1];
	     /* GetOpt options = new GetOpt( "u:d:H", args );
	       //Get the arguments specified for each option.
	      String hostname = options.getOptionParam( 'u' );*/
	      
	     //  String dep = options.getOptionParam( 'd' );
	    new Crawl().crawl("http://www.calstatela.edu/","1");
      //new Crawl().crawl(hostname,dep);
}
}