package src;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
@Entity
public class Record {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

	
	private LocalDateTime instock_timestamp;
	private String name_long;
	private String name_short;
	private String Symbol;

	private Double price_close;
	private Double price_open;
	private Double range_day_high;
	private Double range_day_low;
	private Double range_52w_high;
	private Double range_52w_low;
	private Double volume;
	private Double volume_avg;
	private Double market_cap;
	private Double beta;
	private Double pe_ratio;
	private Double eps;
	private Double dividend;
	private Double target_est_1Y;
	private Double dividend_yield;
	private LocalDate ex_dividend_date;
	private LocalDate earnings_date_begin;
	private LocalDate earnings_date_end;

	public Record() {

	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the instock_timestamp
	 */
	public LocalDateTime getInstock_timestamp() {
		return instock_timestamp;
	}

	/**
	 * @return the name_long
	 */
	public String getName_long() {
		return name_long;
	}

	/**
	 * @return the name_short
	 */
	public String getName_short() {
		return name_short;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return Symbol;
	}

	/**
	 * @return the price_close
	 */
	public Double getPrice_close() {
		return price_close;
	}

	/**
	 * @return the price_open
	 */
	public Double getPrice_open() {
		return price_open;
	}

	/**
	 * @return the range_day_high
	 */
	public Double getRange_day_high() {
		return range_day_high;
	}

	/**
	 * @return the range_day_low
	 */
	public Double getRange_day_low() {
		return range_day_low;
	}

	/**
	 * @return the range_52w_high
	 */
	public Double getRange_52w_high() {
		return range_52w_high;
	}

	/**
	 * @return the range_52w_low
	 */
	public Double getRange_52w_low() {
		return range_52w_low;
	}

	/**
	 * @return the volume
	 */
	public Double getVolume() {
		return volume;
	}

	/**
	 * @return the volume_avg
	 */
	public Double getVolume_avg() {
		return volume_avg;
	}

	/**
	 * @return the market_cap
	 */
	public Double getMarket_cap() {
		return market_cap;
	}

	/**
	 * @return the beta
	 */
	public Double getBeta() {
		return beta;
	}

	/**
	 * @return the pe_ratio
	 */
	public Double getPe_ratio() {
		return pe_ratio;
	}

	/**
	 * @return the eps
	 */
	public Double getEps() {
		return eps;
	}

	/**
	 * @return the dividend
	 */
	public Double getDividend() {
		return dividend;
	}

	/**
	 * @return the target_est_1Y
	 */
	public Double getTarget_est_1Y() {
		return target_est_1Y;
	}

	/**
	 * @return the dividend_yield
	 */
	public Double getDividend_yield() {
		return dividend_yield;
	}

	/**
	 * @return the ex_dividend_date
	 */
	public LocalDate getEx_dividend_date() {
		return ex_dividend_date;
	}

	/**
	 * @return the earnings_date_begin
	 */
	public LocalDate getEarnings_date_begin() {
		return earnings_date_begin;
	}

	/**
	 * @return the earnings_date_end
	 */
	public LocalDate getEarnings_date_end() {
		return earnings_date_end;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param instock_timestamp the instock_timestamp to set
	 */
	public void setInstock_timestamp(LocalDateTime instock_timestamp) {
		this.instock_timestamp = instock_timestamp;
	}

	/**
	 * @param name_long the name_long to set
	 */
	public void setName_long(String name_long) {
		this.name_long = name_long;
	}

	/**
	 * @param name_short the name_short to set
	 */
	public void setName_short(String name_short) {
		this.name_short = name_short;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		Symbol = symbol;
	}

	/**
	 * @param price_close the price_close to set
	 */
	public void setPrice_close(Double price_close) {
		this.price_close = price_close;
	}

	/**
	 * @param price_open the price_open to set
	 */
	public void setPrice_open(Double price_open) {
		this.price_open = price_open;
	}

	/**
	 * @param range_day_high the range_day_high to set
	 */
	public void setRange_day_high(Double range_day_high) {
		this.range_day_high = range_day_high;
	}

	/**
	 * @param range_day_low the range_day_low to set
	 */
	public void setRange_day_low(Double range_day_low) {
		this.range_day_low = range_day_low;
	}

	/**
	 * @param range_52w_high the range_52w_high to set
	 */
	public void setRange_52w_high(Double range_52w_high) {
		this.range_52w_high = range_52w_high;
	}

	/**
	 * @param range_52w_low the range_52w_low to set
	 */
	public void setRange_52w_low(Double range_52w_low) {
		this.range_52w_low = range_52w_low;
	}

	/**
	 * @param volume the volume to set
	 */
	public void setVolume(Double volume) {
		this.volume = volume;
	}

	/**
	 * @param volume_avg the volume_avg to set
	 */
	public void setVolume_avg(Double volume_avg) {
		this.volume_avg = volume_avg;
	}

	/**
	 * @param market_cap the market_cap to set
	 */
	public void setMarket_cap(Double market_cap) {
		this.market_cap = market_cap;
	}

	/**
	 * @param beta the beta to set
	 */
	public void setBeta(Double beta) {
		this.beta = beta;
	}

	/**
	 * @param pe_ratio the pe_ratio to set
	 */
	public void setPe_ratio(Double pe_ratio) {
		this.pe_ratio = pe_ratio;
	}

	/**
	 * @param eps the eps to set
	 */
	public void setEps(Double eps) {
		this.eps = eps;
	}

	/**
	 * @param dividend the dividend to set
	 */
	public void setDividend(Double dividend) {
		this.dividend = dividend;
	}

	/**
	 * @param target_est_1Y the target_est_1Y to set
	 */
	public void setTarget_est_1Y(Double target_est_1Y) {
		this.target_est_1Y = target_est_1Y;
	}

	/**
	 * @param dividend_yield the dividend_yield to set
	 */
	public void setDividend_yield(Double dividend_yield) {
		this.dividend_yield = dividend_yield;
	}

	/**
	 * @param ex_dividend_date the ex_dividend_date to set
	 */
	public void setEx_dividend_date(LocalDate ex_dividend_date) {
		this.ex_dividend_date = ex_dividend_date;
	}

	/**
	 * @param earnings_date_begin the earnings_date_begin to set
	 */
	public void setEarnings_date_begin(LocalDate earnings_date_begin) {
		this.earnings_date_begin = earnings_date_begin;
	}

	/**
	 * @param earnings_date_end the earnings_date_end to set
	 */
	public void setEarnings_date_end(LocalDate earnings_date_end) {
		this.earnings_date_end = earnings_date_end;
	}

	

}
