package src;

import java.util.HashMap;
import java.util.Map;

public class Stock
{
	private final String NAME_FULL;
	private final String NAME_SHORT;
	public Long getLast_update()
	{
		return last_update;
	}
	public String getLastCrawl()
	{
		return lastCrawl;
	}


	private Double price;
	private Long volume;
	private Map<Long, Double> historical_price;
	private Map<Long, Long> historical_volume;
	private Long last_update;
	private String lastCrawl;
	
	public Stock(String name_full, String name_short){
		this.NAME_SHORT = name_short;
		this.NAME_FULL = name_full;
		this.historical_price = new HashMap<>();
		this.historical_volume = new HashMap<>();	
	}
	public boolean is_different(String md5){
		return Utils.MD5(NAME_SHORT + price + volume).equals(md5);
	}
	
	public void update(Double price, Long volume, String crawlTaskID){
		this.price = price;
		this.volume = volume;
		this.last_update = System.currentTimeMillis();
		this.lastCrawl = crawlTaskID;
		historical_price.put(this.last_update, price);
		historical_volume.put(this.last_update, volume);
	}

	public Double getPrice()
	{
		return price;
	}
	public Long getVolume()
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
