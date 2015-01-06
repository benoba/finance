import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;


public class PriceHistory 
{
	private ArrayList<PriceQuote> prices;
	PriceHistory() { prices = new ArrayList<PriceQuote>(); }
	
	public float getPrice( Date date )
	{
		int i;
		//System.out.println( prices.size() + "Prices in this history.");
		SimpleDateFormat fmt = new SimpleDateFormat( "YYYY-MM-dd");
		
		for ( i=0; i<prices.size(); i++ )
		{
			PriceQuote q = (PriceQuote) prices.get(i);
			//System.out.println( "Try Price on " + q.toString() );

			if ( ! q.getDate().after(date) )
			{
				//System.out.println( "Get Price on " + fmt.format(date) + "-" + q.toString() );
				return( q.getPrice() );
			}
		}
		return( 0 );
	}
	
	public float getPerformance( Date entry, Date exit )
	{
		SimpleDateFormat fmt = new SimpleDateFormat( "YYYY-MM-dd" );
		float entry_p = getPrice( entry );
		float exit_p  = getPrice( exit );
		System.out.println( "getPerformance() from " + fmt.format(entry) + ": " + String.valueOf(entry_p) +
				            " to " + fmt.format(exit) + ": " + String.valueOf(exit_p));
		return( getPrice( exit ) / getPrice( entry ) * 100 );
	}
	
	public void loadPriceHistory( InputStream in )
	{
		try{ 
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			// Read the header:  Date,Open,High,Low,Close,Volume,Adj Close
			String 	line = reader.readLine();
			
			do 
			{
				line = reader.readLine() ;  // Get the header:
				//System.out.println( line );
				if ( line != null )
				{
					//System.out.println( line );
					StringTokenizer st = new StringTokenizer( line, ",");
					String dstring = st.nextToken();
					String ostring = st.nextToken();
					String hstring = st.nextToken();
					String lstring = st.nextToken();
					String cstring = st.nextToken();
					prices.add( new PriceQuote( dstring, cstring ) );
					
					//System.out.println( prices.get(prices.size()-1).toString() );
				}
			} while( line != null );
			
		} catch (IOException e ) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}
