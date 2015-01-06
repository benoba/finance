import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class YQLTester {
	
	public static void main( String[] args )
	{
		SimpleDateFormat fmt = new SimpleDateFormat( "YYYY-MM-dd");
		Security mrvl = new Security( "MRVL" );
		GregorianCalendar start = new GregorianCalendar(2014,8,1);
		GregorianCalendar end   = new GregorianCalendar();
		
		mrvl.init( start, end );
		GregorianCalendar testDate = (GregorianCalendar)start.clone();
		testDate.add( Calendar.DAY_OF_MONTH,  2 );
		System.out.println( "Get Price on " + fmt.format( testDate.getTime() ) );
		float price = mrvl.getHistory().getPrice( testDate.getTime() );
		System.out.println( "Price: " + String.valueOf(price) );
		
		BasketRotationStrategy strat = new BasketRotationStrategy();
		strat.setDateRange( start,  end );
		strat.addSecurity( "XLY" );
		strat.addSecurity( "XLP" );
		strat.addSecurity( "XLE" );
		strat.addSecurity( "XLF" );
		strat.addSecurity( "XLV" );
		strat.addSecurity( "XLI" );
		strat.addSecurity( "XLB" );
		strat.addSecurity( "XLK" );
		strat.addSecurity( "XLU" );
		strat.runStrategy();
	}

}
