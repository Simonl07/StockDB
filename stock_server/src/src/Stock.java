package src;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.json.JSONArray;
import org.json.JSONObject;

@Entity
@SequenceGenerator(name="stock_seq", sequenceName="stock_private_sequence")
public class Stock
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="stock_seq")
	private int id;
	private String NAME_FULL;
	private String SYMBOL;

	// Price and Volume
	private Double latest_price;
	private Long latest_volume;
	@ElementCollection
	private Map<LocalDateTime, Double> historical_price = new HashMap<>();
	@ElementCollection
	private Map<LocalDateTime, Long> historical_volume = new HashMap<>();
	private LocalDateTime last_update;
	private Integer last_updated_by;

	// Profile
	private String address1;
	private String zip;
	private String city;
	private String state;
	private String country;
	private String website;
	private String sector;
	private String industry;
	private int employee;
	@Column(length = 16384)
	private String description;

	public Stock()
	{
	}

	public Stock(String name_full, String symbol)
	
	{
		this.SYMBOL = symbol;
		this.NAME_FULL = name_full;
	}
	
	
	public boolean is_price_changed(String md5)
	{
		return !Utils.MD5(this.getSYMBOL() + latest_price + latest_volume).equals(md5);
	}

	public void update(Double price, Long volume, int crawlTaskID)
	{
		this.latest_price = price;
		this.latest_volume = volume;
		this.last_update = LocalDateTime.now();
		this.last_updated_by = crawlTaskID;
		historical_price.put(this.last_update, price);
		historical_volume.put(this.last_update, volume);
	}
	
	public String getNAME_FULL()
	{
		return NAME_FULL;
	}

	public String getSYMBOL()
	{
		return SYMBOL;
	}

	public String getAddress1()
	{
		return address1;
	}

	public String getZip()
	{
		return zip;
	}

	public String getCity()
	{
		return city;
	}

	public String getState()
	{
		return state;
	}

	public String getCountry()
	{
		return country;
	}

	public String getWebsite()
	{
		return website;
	}

	public String getSector()
	{
		return sector;
	}

	public String getIndustry()
	{
		return industry;
	}

	public int getEmployee()
	{
		return employee;
	}

	public String getDescription()
	{
		return description;
	}

	public void setAddress1(String address1)
	{
		this.address1 = address1;
	}

	public void setZip(String zip)
	{
		this.zip = zip;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public void setWebsite(String website)
	{
		this.website = website;
	}

	public void setSector(String sector)
	{
		this.sector = sector;
	}

	public void setIndustry(String industry)
	{
		this.industry = industry;
	}

	public void setEmployee(int employee)
	{
		this.employee = employee;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getId()
	{
		return id;
	}

	public Double getLatest_price()
	{
		return latest_price;
	}

	public void setLatest_price(Double latest_price)
	{
		this.latest_price = latest_price;
		this.historical_price.put(LocalDateTime.now(), latest_price);
	}

	public Long getLatest_volume()
	{
		return latest_volume;
	}

	public void setLatest_volume(Long latest_volume)
	{
		this.latest_volume = latest_volume;
		this.historical_volume.put(LocalDateTime.now(), latest_volume);
	}

	public LocalDateTime getLast_update()
	{
		return last_update;
	}

	public void setLast_update(LocalDateTime last_update)
	{
		this.last_update = last_update;
	}

	public int getLastCrawl()
	{
		return last_updated_by;
	}

	public void setLastCrawl(int lastCrawl)
	{
		this.last_updated_by = lastCrawl;
	}

	public Map<LocalDateTime, Double> getHistorical_price()
	{
		return historical_price;
	}

	public Map<LocalDateTime, Long> getHistorical_volume()
	{
		return historical_volume;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Stock == false)
		{
			return false;
		}
		Stock target = (Stock) obj;
		return target.getId() == this.getId();
	}

	@Override
	public int hashCode()
	{
		return this.id;
	}
	
	
	
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		
		json.put("name_full", this.NAME_FULL);
		json.put("symbol", this.SYMBOL);
		json.put("latest_price", this.latest_price);
		json.put("latest_volume", this.latest_volume);
		json.put("last_update", this.last_update);
		json.put("last_updated_by", this.last_updated_by);
		
		json.put("address1", this.address1);
		json.put("zip", this.zip);
		json.put("city", this.city);
		json.put("state", this.state);
		json.put("country", this.country);
		json.put("website", this.website);
		json.put("sector", this.sector);
		json.put("industry", this.industry);
		json.put("employee", this.employee);
		JSONObject description = new JSONObject();
		description.put("raw", this.description);
		json.put("description", description);
		json.put("address1", this.address1);
		
		JSONArray price = new JSONArray();
		for(LocalDateTime time: this.historical_price.keySet()){
			JSONObject temp = new JSONObject();
			temp.accumulate(time.toString(), historical_price.get(time));
			price.put(temp);
		}
		json.put("price_data", price);
		
		JSONArray volume = new JSONArray();
		for(LocalDateTime time: this.historical_volume.keySet()){
			JSONObject temp = new JSONObject();
			temp.accumulate(time.toString(), historical_volume.get(time));
			volume.put(temp);
		}
		json.put("volume_data", volume);
		
		return json;
	}
}
