package src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.json.JSONObject;

public class TaskStatusController
{
	private final Map<Long, CrawlTask> crawlTaskMap;
	
	public TaskStatusController(){
		this.crawlTaskMap = new HashMap<Long, CrawlTask>();
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
		List<Stock> stocks = PriceUpdator.getStocks(hibernateSession);
		List<String> symbols_string = new ArrayList<String>();
		for(Stock s: stocks){
			symbols_string.add(s.getSYMBOL());
		}
		CrawlTask temp = new CrawlTask(symbols_string, type);
		crawlTaskMap.put(temp.getID(), temp);
		return temp.genJSONPackageWithStocks(stocks);
	}
	
	
	public void reportInvalid(Session session, String crawl_task_id, int stock_id){
		crawlTaskMap.get(crawl_task_id).reportInvalid(session, stock_id);
	}
	
	public Collection<CrawlTask> getTasks(){
		return crawlTaskMap.values();
	}
	
	public void updateTask(String id, int status){
		crawlTaskMap.get(id).setStatus(status);
	}
	
	public void updateTaskProgress(String id, int crawled_count){
		crawlTaskMap.get(id).setCrawled_count(crawled_count);;
	}
	
	public void crawlerUpdate(String task_id, String crawler_id, int amount){
		crawlTaskMap.get(task_id).crawlerUpdate(crawler_id, amount);
	}
	
	public void addCrawler(String task_id, String crawler_id){
		crawlTaskMap.get(task_id).addCrawler(crawler_id);
	}
	
	public void archive(Session hibernateSession, String id){
		crawlTaskMap.get(id).archive(hibernateSession);
		crawlTaskMap.remove(id);
	}
	
	public CrawlTask getCrawlTask(String id){
		return crawlTaskMap.get(id);
	}
}
