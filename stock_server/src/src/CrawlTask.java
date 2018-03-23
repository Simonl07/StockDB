 package src;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

@Entity
@SequenceGenerator(name="crawltask_seq", sequenceName="crawltask_private_sequence")
public class CrawlTask
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="crawltask_seq")
	private long ID;
	private long TIMESTAMP;
	private int SIZE;
	
	@Transient
	private List<String> SYMBOL_LIST;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Map<String, Integer> CRAWLER_MAP;
	private String TYPE;

	private int crawled_count;
	private LocalDateTime end_time;
	private float success_rate;
	private int status;
	@ElementCollection
	private List<Integer> invalid_stocks;
	private int invalid_count;

	
	public CrawlTask(){
		
	}
	
	public CrawlTask(List<String> stock_list, String type)
	{
		this.invalid_count = 0;
		this.TYPE = type;
		this.TIMESTAMP = System.currentTimeMillis();
		this.SIZE = stock_list.size();
		this.SYMBOL_LIST = stock_list;
		this.status = 0;
		this.end_time = null;
		this.invalid_stocks = new ArrayList<Integer>();
		this.CRAWLER_MAP = new HashMap<String, Integer>();
		this.success_rate = 1;
		this.crawled_count = 0;
	}


	public void addCrawler(String id){
		CRAWLER_MAP.put(id, 0);
	}
	
	public Map<String, Integer> getCRAWLER_MAP()
	{
		return CRAWLER_MAP;
	}

	public void crawlerUpdate(String id, int amount){
		CRAWLER_MAP.put(id, amount);
		crawled_count = CRAWLER_MAP.values().stream().reduce(0, (x, y)-> x + y);
	}
	
	public void reportInvalid(Session hibernateSession, int stock)
	{
		this.invalid_stocks.add(stock);
		this.invalid_count++;
		this.success_rate = ((float)this.SIZE - this.invalid_stocks.size())/(float)this.SIZE;
		System.out.println("Success rate decreased to " + this.success_rate);
		removeStock(hibernateSession, stock);
	}



	public JSONObject genJSONPackageWithSymbols(List<Symbol> symbols)
	{
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		for (Symbol stock: symbols)
		{
			JSONObject obj = new JSONObject();
			obj.accumulate("id", stock.getId());
			obj.accumulate("symbol", stock.getSymbol());
			array.put(obj);
		}

		json.put("id", this.ID);
		json.put("assignment_begin", this.TIMESTAMP);
		json.put("assignment_size", this.SIZE);
		json.put("stocks", array);
		return json;
	}
	
	public JSONObject genJSONPackageWithStocks(List<Stock> stocks)
	{
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		for (Stock stock: stocks)
		{
			JSONObject obj = new JSONObject();
			obj.accumulate("id", stock.getId());
			obj.accumulate("symbol", stock.getSYMBOL());
			array.put(obj);
		}

		json.put("id", this.ID);
		json.put("assignment_begin", this.TIMESTAMP);
		json.put("assignment_size", this.SIZE);
		json.put("stocks", array);
		return json;
	}


	public void archive(Session hibernateSession)
	{
		assert hibernateSession.getTransaction().isActive();
		this.setStatus(2);
		this.setEnd_time(end_time);
		hibernateSession.update(this);
	}

	public static void removeStock(Session hibernateSession, int id)
	{
		assert hibernateSession.getTransaction().isActive();
		hibernateSession.remove(hibernateSession.get(Symbol.class, id));
	}


	/**
	 * @return the iD
	 */
	public long getID() {
		return ID;
	}


	/**
	 * @return the tIMESTAMP
	 */
	public long getTIMESTAMP() {
		return TIMESTAMP;
	}


	/**
	 * @return the sIZE
	 */
	public int getSIZE() {
		return SIZE;
	}


	/**
	 * @return the tYPE
	 */
	public String getTYPE() {
		return TYPE;
	}


	/**
	 * @return the crawled_count
	 */
	public int getCrawled_count() {
		return crawled_count;
	}


	/**
	 * @return the end_time
	 */
	public LocalDateTime getEnd_time() {
		return end_time;
	}


	/**
	 * @return the success_rate
	 */
	public float getSuccess_rate() {
		return success_rate;
	}


	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}


	/**
	 * @param crawled_count the crawled_count to set
	 */
	public void setCrawled_count(int crawled_count) {
		this.crawled_count = crawled_count;
	}


	/**
	 * @param end_time the end_time to set
	 */
	public void setEnd_time(LocalDateTime end_time) {
		this.end_time = end_time;
	}


	/**
	 * @param success_rate the success_rate to set
	 */
	public void setSuccess_rate(float success_rate) {
		this.success_rate = success_rate;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getInvalidCount(){
		return this.invalid_count;
	}
	
	
}
