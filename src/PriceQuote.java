import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PriceQuote 
{
	private Date date;
	private float close;
	PriceQuote( String d, String p ) throws ParseException
	{
		SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd" );
		date = formatter.parse( d );
		close = Float.parseFloat( p );
	}
	public Date getDate() { return date; }
	public float getPrice() { return close; }
	public String toString() 
	{ 
		SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd" );
		return( formatter.format(date) + ": " + String.valueOf( close ) ); 
	}
}
