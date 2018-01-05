package src;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Stock
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String NAME_FULL;
	private String SYMBOL;

	// Price and Volume
	private Double latest_price;
	private Long latest_volume;
	@ElementCollection
	private Map<Long, Double> historical_price = new HashMap<>();
	@ElementCollection
	private Map<Long, Long> historical_volume = new HashMap<>();
	private Long lastUpdate;
	private String lastCrawl;

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
	@Column(length = 2048)
	private String description;

	public Stock()
	{
	}

	public Stock(String name_full, String name_short)
	{
		this.SYMBOL = name_short;
		this.NAME_FULL = name_full;
	}
	
	
	public boolean is_price_changed(String md5)
	{
		return !Utils.MD5(getSYMBOL() + latest_price + latest_volume).equals(md5);
	}

	public void update(Double price, Long volume, String crawlTaskID)
	{
		this.latest_price = price;
		this.latest_volume = volume;
		this.lastUpdate = System.currentTimeMillis();
		this.lastCrawl = crawlTaskID;
		historical_price.put(this.lastUpdate, price);
		historical_volume.put(this.lastUpdate, volume);
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
	}

	public Long getLatest_volume()
	{
		return latest_volume;
	}

	public void setLatest_volume(Long latest_volume)
	{
		this.latest_volume = latest_volume;
	}

	public Long getLast_update()
	{
		return lastUpdate;
	}

	public void setLast_update(Long last_update)
	{
		this.lastUpdate = last_update;
	}

	public String getLastCrawl()
	{
		return lastCrawl;
	}

	public void setLastCrawl(String lastCrawl)
	{
		this.lastCrawl = lastCrawl;
	}

	public Map<Long, Double> getHistorical_price()
	{
		return historical_price;
	}

	public Map<Long, Long> getHistorical_volume()
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
}
