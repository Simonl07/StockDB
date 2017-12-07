package src;

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
}
