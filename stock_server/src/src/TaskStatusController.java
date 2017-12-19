package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class TaskStatusController
{
	private final Map<String, CrawlTask> crawlTaskMap;
	
	public TaskStatusController(){
		this.crawlTaskMap = new HashMap<String, CrawlTask>();
	}
	
	public JSONObject assign(List<String> stocks){
		CrawlTask temp = new CrawlTask(stocks);
		crawlTaskMap.put(temp.getID(), temp);
		return temp.genJSONPackage();
	}
	
	public void reportInvalid(Connection connection, String id, String stock){
		crawlTaskMap.get(id).reportInvalid(connection, stock);
	}
	
	public Collection<CrawlTask> getTasks(){
		return crawlTaskMap.values();
	}
	
	public void updateTask(String id, int status){
		crawlTaskMap.get(id).updateStatus(status);
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
	
	public PreparedStatement archive(Connection connection, String id){
		PreparedStatement statement = crawlTaskMap.get(id).genSQLArchive(connection);
		crawlTaskMap.remove(id);
		return statement;
	}
	
	public CrawlTask getCrawlTask(String id){
		return crawlTaskMap.get(id);
	}
}
