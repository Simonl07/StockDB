package src;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StockPriceIndex
{
	private final Map<String, Stock> map;
	
	public StockPriceIndex(){
		map = new HashMap<String, Stock>();
	}
	
	public void put(String name_full, String name_short, Double price, Double volume){
		if(map.containsKey(name_short)){
			map.get(name_short).update(price, volume);
		}else{
			Stock temp = new Stock(name_full, name_short);
			temp.update(price, volume);
			map.put(name_short, temp);
		}
	}
	
	public Collection<Stock> getStocks(){
		return map.values();
	}
	
	
	
}
