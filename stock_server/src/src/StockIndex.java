package src;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class StockIndex
{
	private final Map<String, Stock> index = new HashMap<String, Stock>();
	private TimeLinkedList updateHistory = new TimeLinkedList();

	public class TimeLinkedList{
		private TimeNode head;
		private TimeNode tail;
		
		class TimeNode{
			Long time;
			TimeNode next;
			
			public TimeNode(Long time, TimeNode next){
				this.time = time;
				this.next = next;
			}
		}
		public TimeLinkedList(){}
		
		public void insert(Long time){
			TimeNode node = new TimeNode(time, this.head);
			this.head = node;
		}
		
		public void append(Long time){
			TimeNode node = new TimeNode(time, null);
			this.tail.next = node;
			this.tail = node;
		}
		
		public int sizeAfter(int secondsAgo){
			TimeNode node = this.head;
			int cnt = 0;
			Long deadline = System.currentTimeMillis() - secondsAgo * 1000;
			while(node != this.tail && node.time > deadline){
				cnt++;
				node = node.next;
			}
			return cnt;
		}
		
		public void chopAfter(int secondsAgo){
			TimeNode node = this.head;
			Long deadline = System.currentTimeMillis() - secondsAgo * 1000;
			while(node != this.tail && node.time > deadline){
				node = node.next;
			}
			this.tail = node;
		}
		
		public int size(){
			TimeNode node = this.head;
			int cnt = 0;
			while(node != this.tail){
				node = node.next;
				cnt++;
			}
			return cnt;
		}
	}
	
	
	
	
	public StockIndex()
	{
	}

	public StockIndex(Map<String, Stock> input)
	{
		index.putAll(input);
	}

	public void updatePrice(String name_full, String name_short, Double price, Long volume, String crawlTaskID)
	{
		long currentTime = System.currentTimeMillis();
		if (index.containsKey(name_full))
		{
			index.get(name_full).update(price, volume, crawlTaskID);
		}else{
			Stock temp = new Stock(name_full, name_short);
			temp.update(price, volume, crawlTaskID);
			index.put(name_full, temp);
		}
		updateHistory.insert(currentTime);
		updateHistory.chopAfter(10);
	}


	public Double getUpdateRate()
	{
		System.out.println(updateHistory.size());
		int count = updateHistory.sizeAfter(10);
		updateHistory.chopAfter(10);
		return count/10.0;
		
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

	public Collection<String> getIdList()
	{
		return index.keySet();
	}

	public void addStock(Stock stock)
	{
		index.put(stock.getNAME_FULL(), stock);
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
