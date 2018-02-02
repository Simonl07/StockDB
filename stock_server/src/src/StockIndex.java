package src;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class StockIndex
{
	private final Map<Integer, Stock> index = new HashMap<Integer, Stock>();
	private LinkedList<Long> updateHistory = new LinkedList<Long>();

	public StockIndex()
	{
	}

	public StockIndex(Map<Integer, Stock> input)
	{
		index.putAll(input);
	}

	public void updatePrice(int stock_id, Double price, Long volume, String crawlTaskID)
	{
		long currentTime = System.currentTimeMillis();
		if (index.containsKey(stock_id))
		{
			index.get(stock_id).update(price, volume, crawlTaskID);
		}
		clearHistory(currentTime);
	}

	private void clearHistory(long curr)
	{
		
		int index = 0;
		Iterator<Long> it = updateHistory.iterator();
		Long temp;

		if (it.hasNext() && (curr - (temp = (Long) it.next())) < 5000)
		{
			index++;
			while (it.hasNext() && (curr - temp) < 5000)
			{
				temp = (Long) it.next();
				index++;
			}
		}

		updateHistory.subList(index, updateHistory.size()).clear();

	}

	public Double getUpdateRate()
	{
		System.out.println(updateHistory);
		Iterator<Long> it = updateHistory.iterator();
		long curr = System.currentTimeMillis();

		int cnt = 0;
		Long temp;
		if (it.hasNext() && (curr - (temp = (Long) it.next())) < 5000)
		{
			cnt++;
			while (it.hasNext() && (curr - temp) < 5000)
			{
				temp = (Long) it.next();
				cnt++;
			}

			return cnt / 5.0;
		} else
		{
			return (double) 0;
		}
	}

	public Double getAverageLatency()
	{
		long curr = System.currentTimeMillis();
		int size = index.size();
		double total = 0;
		for (Stock s: index.values())
		{
			total += (curr - s.getLast_update()) / 1000.0;
		}

		return size == 0 ? 0 : total / size;
	}

	public Stock getStock(String id)
	{
		return index.get(id);
	}

	public Collection<Stock> getStockList()
	{
		return index.values();
	}

	public Collection<Integer> getIdList()
	{
		return index.keySet();
	}

	public void addStock(Stock stock)
	{
		index.put(stock.getId(), stock);
	}

	public boolean hasId(int id)
	{
		return index.containsKey(id);
	}

	public boolean hasStock(Stock stock)
	{
		return index.containsValue(stock);
	}
}
