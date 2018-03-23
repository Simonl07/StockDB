package src;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.json.JSONObject;

public class TaskStatusController
{
	private final Map<Long, CrawlTask> crawlTaskMap;
	
	public TaskStatusController(){
		this.crawlTaskMap = new HashMap<Long, CrawlTask>();
	}
	
	public TaskStatusController(Session hibernateSession){
		this();
		@SuppressWarnings("unchecked")
		Query<CrawlTask> query = hibernateSession.createQuery("from CrawlTask");
		List<CrawlTask> tasks = query.list();
		for(CrawlTask c: tasks){
			this.crawlTaskMap.put(c.getID(), c);
		}
		System.out.println("crawlTaskMapRestored, " + this.crawlTaskMap.size());
	}
	
	public JSONObject assign(List<Symbol> symbols, String type){
		List<String> symbols_string = new ArrayList<String>();
		for(Symbol s: symbols){
			symbols_string.add(s.getSymbol());
		}
		CrawlTask temp = new CrawlTask(symbols_string, type);
		crawlTaskMap.put(temp.getID(), temp);
		return temp.genJSONPackageWithSymbols(symbols);
	}
	
	public JSONObject assign(Session hibernateSession, String type){
		assert hibernateSession.getTransaction().isActive();
		List<Stock> stocks = PriceUpdator.getStocks_helper(hibernateSession);
		List<String> symbols_string = new ArrayList<String>();
		for(Stock s: stocks){
			symbols_string.add(s.getSYMBOL());
		}
		CrawlTask temp = new CrawlTask(symbols_string, type);
		temp.setEnd_time(LocalDateTime.now());
		hibernateSession.save(temp);
		crawlTaskMap.put(temp.getID(), temp);
		return temp.genJSONPackageWithStocks(stocks);
	}
	
	
	public void reportInvalid(Session session, long crawl_task_id, int stock_id){
		crawlTaskMap.get(crawl_task_id).reportInvalid(session, stock_id);
	}
	
	public Collection<CrawlTask> getTasks(){
		return crawlTaskMap.values();
	}
	
	public void updateTask(long id, int status){
		crawlTaskMap.get(id).setStatus(status);
	}
	
	public void updateTaskProgress(long id, int crawled_count){
		crawlTaskMap.get(id).setCrawled_count(crawled_count);;
	}
	
	public void crawlerUpdate(long task_id, String crawler_id, int amount){
		crawlTaskMap.get(task_id).crawlerUpdate(crawler_id, amount);
	}
	
	public void addCrawler(long task_id, String crawler_id){
		crawlTaskMap.get(task_id).addCrawler(crawler_id);
	}
	
	public void archive(Session hibernateSession, long id){
		assert hibernateSession.getTransaction().isActive();
		crawlTaskMap.get(id).archive(hibernateSession);
		crawlTaskMap.remove(id);
	}
	
	public CrawlTask getCrawlTask(String id){
		return crawlTaskMap.get(id);
	}
}
