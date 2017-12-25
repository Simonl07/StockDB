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
		System.out.println(updateHistory);
		Iterator<Long> it = updateHistory.iterator();
		long curr = System.currentTimeMillis();
		
		int cnt = 0; 
		Long temp;
		if(it.hasNext() && (curr - (temp = (Long)it.next())) < 5000){
			cnt++;
			while(it.hasNext() && (curr - temp) < 5000){
				System.out.println(temp);
				temp = (Long) it.next();
				cnt++;
			}
			
			return cnt/5.0;
		}else{
			return (double) 0;
		}
	}
	
	
	public Double getAverageLatency(){
		long curr = System.currentTimeMillis();
		int size = map.size();
		double total = 0;
		for(Stock s: map.values()){
			total += (curr - s.getLast_update())/1000.0;
		}
		
		return size == 0? 0: total / size;
	}
	
	
	public Collection<Stock> getStocks()
	{
		return map.values();
	}

}
