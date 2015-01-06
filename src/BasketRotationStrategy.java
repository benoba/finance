import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class BasketRotationStrategy 
{
	GregorianCalendar 	start;
	GregorianCalendar 	end;
	ArrayList<Security> securityUniverse;
	ArrayList<Position> securitiesHeld;
	int                 basketSize;
	int                 evalUnits;
	int                 evalInterval;
	int                 xactionUnits;
	int                 xactionInterval; 
	
	BasketRotationStrategy() 
	{
		securityUniverse = new ArrayList<Security>();
		securitiesHeld   = new ArrayList<Position>();
		evalUnits = Calendar.MONTH;
		evalInterval = 1;   
		xactionUnits = Calendar.DAY_OF_WEEK;
		xactionInterval = 7; 
	}
	
	public void addSecurity( String ticker )
	{
		Security sec = new Security( ticker );
		sec.init( start,  end );
		securityUniverse.add( sec );
	}
	
	public void setDateRange( GregorianCalendar s, GregorianCalendar e )
	{
		start = s;
		end = e;
	}
	
	public void setBasketSize( int size ) { basketSize = size; }
	
	private void orderSecurity( Security sec, GregorianCalendar day, Security[] best )
	{
		int s;
		GregorianCalendar entry = (GregorianCalendar)day.clone();
		entry.add( evalUnits, evalInterval );
		float thisperf = sec.getHistory().getPerformance( entry.getTime(), day.getTime() );
		System.out.println( "orderSecurity: " + sec.name() + "perf:"+String.valueOf(thisperf));
		Security tmpsec = null;
		Security thissec = sec;
		for(s=0;s<basketSize;s++)
		{
			if ( best[s] == null ) 
			{ 
				best[s] = sec;
				System.out.println( "Default add " + s + ":" + sec.name() );
				return; 
			}
			float bestperf = best[s].getHistory().getPerformance( entry.getTime(), day.getTime() );
			System.out.println( " best="+String.valueOf(bestperf)+"  this="+String.valueOf(thisperf) );
			
			if ( thisperf > bestperf )
			{
				tmpsec = best[s];
				best[s] = thissec;
				thissec = tmpsec;
				thisperf = thissec.getHistory().getPerformance( entry.getTime(),  day.getTime() );
			}
		}
		
	}
	
	void reconcilePositions( GregorianCalendar sim_date, Security[] bestSec )
	{
		int sh, bs;
		for( sh=0; sh<securitiesHeld.size(); sh++ )
		{
			for( bs=0; bs<bestSec.length; bs++ )
				if ( bestSec[bs] == securitiesHeld.get(sh).security() ) break;
			if ( bs >= bestSec.length )
			{
				// SELL SELL SELL.
				System.out.println( "SELL: " + securitiesHeld.get(sh).name() );
				securitiesHeld.remove( sh );
			}
		}
		for( bs=0; bs<bestSec.length; bs++ )
		{
			for( sh=0; sh<securitiesHeld.size(); sh++ )
				if ( bestSec[bs] == securitiesHeld.get(sh).security() ) break;
			if ( sh >= securitiesHeld.size() )
			{
				// BUY BUY BUY.
				System.out.println( "BUY: " + bestSec[bs].name() );
				securitiesHeld.add( new Position( bestSec[bs], bestSec[bs].getHistory().getPrice(sim_date.getTime()) ) );
			}
	
		}
	}

	public void runStrategy( )
	{
		SimpleDateFormat fmt = new SimpleDateFormat( "YYYY-MM-dd" );
		
		GregorianCalendar sim_date = (GregorianCalendar)start.clone();
		sim_date.add( evalUnits, evalInterval );
		Security bestSec[] = new Security[basketSize]; 
		int s;
		for(s=0;s<basketSize;s++)
			bestSec[s] = null;
		
		while( sim_date.before( end ) )
		{
			System.out.println( "Sim on " + fmt.format( sim_date.getTime() ) );
			for(s=0;s<securityUniverse.size();s++)
			{
				orderSecurity( securityUniverse.get(s), sim_date, bestSec);
			}
			for(s=0;s<bestSec.length;s++)
				System.out.println( "     "+s+":"+bestSec[s].name() );
			reconcilePositions( sim_date, bestSec );
			sim_date.add( xactionUnits,  xactionInterval );
		}
	}
}
