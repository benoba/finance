import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Security 
{
	String ticker;
	PriceHistory history;
	
	public PriceHistory getHistory() { return( history ); }
	public String name() { return( ticker ); }
	
	Security( String symb )
	{
		ticker = symb;
		history = new PriceHistory();
	}
	
	public void init( GregorianCalendar cstart, GregorianCalendar cstop )
	{
		SimpleDateFormat fmt = new SimpleDateFormat("YYYY-MM-dd");
		System.out.println( "init Security " + ticker + " from " + fmt.format(cstart.getTime()) + " to " + fmt.format(cstop.getTime() ) );
		String query = "http://real-chart.finance.yahoo.com/table.csv?s=" + ticker;
		query += "&a=" + cstart.get(Calendar.MONTH) + "&b=" + cstart.get(Calendar.DAY_OF_MONTH) + "&c=" + cstart.get(Calendar.YEAR);
		query += "&d=" +  cstop.get(Calendar.MONTH) + "&e=" +  cstop.get(Calendar.DAY_OF_MONTH) + "&f=" +  cstop.get(Calendar.YEAR) + "&g=d&ignore=.csv";
 
		URL url = null;
		try {
			url = new URL(query);
		} catch (MalformedURLException e) {
			System.out.println( "Bad query: " + query );
			e.printStackTrace();
		}

		URLConnection openConnection = null;
		try {
			openConnection = url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}

		InputStream inputStream = null;
		try {
			inputStream = openConnection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		history.loadPriceHistory( inputStream );
	}
}
