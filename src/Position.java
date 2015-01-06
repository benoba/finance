
public class Position {
	private Security stock;
	float            entryPrice;
	
	Position( Security s, float p )
	{
		stock = s;
		entryPrice = p;
	}
	
	String name() { return( stock.ticker ); }
	Security security() { return( stock ); }
	float calcGain( float np ) { return((np - entryPrice)/entryPrice * 100); }
}
