package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.json.JSONObject;

public class TaskStatusController
{
	private final Map<String, CrawlTask> crawlTaskMap;
	
	public TaskStatusController(){
		this.crawlTaskMap = new HashMap<String, CrawlTask>();
	}
	
	public JSONObject assign(List<Symbol> stocks, String type){
		CrawlTask temp = new CrawlTask(stocks, type);
		crawlTaskMap.put(temp.getID(), temp);
		return temp.genJSONPackage();
	}
	
	public void reportInvalid(Session session, String id, String stock){
		crawlTaskMap.get(id).reportInvalid(session, stock);
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
