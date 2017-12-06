
public class Stock
{
	private final String NAME_FULL;
	private final String NAME_SHORT;
	private Double price;
	private Double volume;
	
	
	public Stock(String name_short, String name_full){
		this.NAME_SHORT = name_short;
		this.NAME_FULL = name_full;
	}
	
	
	public boolean is_different(String md5){
		return StockInsertionUtils.MD5(NAME_SHORT + price + volume).equals(md5);
	}
	
	public void update(Double price, Double volume){
		this.price = price;
		this.volume = volume;
	}

	public Double getPrice()
	{
		return price;
	}
	public Double getVolume()
	{
		return volume;
	}


	public String getNAME_FULL()
	{
		return NAME_FULL;
	}


	public String getNAME_SHORT()
	{
		return NAME_SHORT;
	}
	
	
	
	
	
	
	
}
