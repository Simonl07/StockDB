package src;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class StockPriceIndex
{
	private final Map<String, Stock> map;
	private LinkedList<Long> updateHistory;

	public StockPriceIndex()
	{
		map = new HashMap<String, Stock>();
		updateHistory = new LinkedList<Long>();
	}

	public void put(String name_full, String name_short, Double price, Long volume, String crawlTaskID)
	{
		updateHistory.addFirst(System.currentTimeMillis());
		if (map.containsKey(name_short))
		{
			map.get(name_short).update(price, volume, crawlTaskID);
		} else
		{
			Stock temp = new Stock(name_full, name_short);
			temp.update(price, volume, crawlTaskID);
			map.put(name_short, temp);
		}
	}

	
	public Double getUpdateRate(){
		
		Iterator<Long> it = updateHistory.iterator();
		long curr = System.currentTimeMillis();
		
		int cnt = 0; 
		
		if(it.hasNext()){
			Long temp = (Long) it.next();
			cnt++;
			while(it.hasNext() && (curr - temp) < 5000){
				temp = (Long) it.next();
				cnt++;
			}
			return cnt/5.0;
		}else{
			return (double) 0;
		}
		
		
	}
	
	
	public Collection<Stock> getStocks()
	{
		return map.values();
	}

}
