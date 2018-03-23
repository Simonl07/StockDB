package src;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class PriceUpdator
{
	private TimeLinkedList updateHistory = new TimeLinkedList();
	private static List<Stock> cached_stocks = new ArrayList<Stock>();
	private long last_cache;
	
	
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
	
	
	
	
	public PriceUpdator()
	{
		this.last_cache = System.currentTimeMillis();
	}
	
	public void updatePrice(int id, Double price, Long volume, int crawlTaskID, Session hibernateSession)
	{
		assert hibernateSession.getTransaction().isActive();
		long currentTime = System.currentTimeMillis();
		
		Stock stock = (Stock)hibernateSession.get(Stock.class, id);
		stock.setLatest_price(price);
		stock.setLast_update(LocalDateTime.now());
		stock.setLatest_volume(volume);
		stock.setLastCrawl(crawlTaskID);
		
		updateHistory.insert(currentTime);
		updateHistory.chopAfter(10);
		
		if(System.currentTimeMillis() - this.last_cache > 10000){
			cached_stocks = PriceUpdator.getStocks_helper(hibernateSession);
			this.last_cache = System.currentTimeMillis();
			System.out.println("!\n!\n!\n!\n!\n!\n!\n!\n!\n!\n!\n!\n!\n!\n");
		}
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
		return 0.0;//TODO
	}
	
	public List<Stock> getStocks(Session hibernateSession){
		if(System.currentTimeMillis() - this.last_cache > 10000){
			cached_stocks = PriceUpdator.getStocks_helper(hibernateSession);
			this.last_cache = System.currentTimeMillis();
			System.out.println("!\n!\n!\n!\n!\n!\n!\n!\n!\n!\n!\n!\n!\n!\n");
		}
		return cached_stocks;
	}
	
	
	public static List<Stock> getStocks_helper(Session hibernateSession){
		assert hibernateSession.getTransaction().isActive();
		
		@SuppressWarnings("unchecked")
		Query<Stock> query = hibernateSession.createQuery("from Stock");
		return query.list();
	}

}
