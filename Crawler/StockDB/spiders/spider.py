# -*- coding:UTF-8 -*-
import scrapy
import hashlib
import re
from StockDB import *
from StockDB.items import Stock


class Spider(scrapy.Spider):

    handle_httpstatus_list = [200]


    def url_generator(stockIDs):
        YAHOO_BASE_URL = 'https://finance.yahoo.com/quote/'
        lst = []
        for s in stockIDs:
            lst.append(YAHOO_BASE_URL + s + "?p=" + s)
        return lst

    name = 'spider'
    allowed_domains = ['https://finance.yahoo.com']

    start_urls = url_generator(['AAPL', 'GOOG', 'NVDA'])






    def parse(self, response):
        item = Stock()

        item['name_full'] = re.search('.+?(?=\()', response.xpath('//*[@id="quote-header-info"]/div[2]/div[1]/div/h1/text()').extract_first()).group()
        item['name_short'] = re.search('(?<=\().+?(?=\))', response.xpath('//*[@id="quote-header-info"]/div[2]/div[1]/div/h1/text()').extract_first()).group()
        item['price_close'] = response.xpath('//*[@id="quote-summary"]/div[1]/table/tbody/tr[1]/td[2]/span/text()').extract_first()
        item['price_open'] = response.xpath('//*[@id="quote-summary"]/div[1]/table/tbody/tr[2]/td[2]/span/text()').extract_first()
        item['range_day'] = response.xpath('//*[@id="quote-summary"]/div[1]/table/tbody/tr[5]/td[2]/text()').extract_first()
        item['range_day_low'] = re.search('[\d.]+?(?=\s)', item['range_day']).group()
        item['range_day_high'] = re.search('(?<=\s)[\d.]+', item['range_day']).group()
        item['range_52w'] = response.xpath('//*[@id="quote-summary"]/div[1]/table/tbody/tr[6]/td[2]/text()').extract_first()
        item['range_52w_low'] = re.search('[\d.]+?(?=\s)', item['range_52w']).group()
        item['range_52w_high'] = re.search('(?<=\s)[\d.]+', item['range_52w']).group()
        item['volume'] = response.xpath('//*[@id="quote-summary"]/div[1]/table/tbody/tr[7]/td[2]/span/text()').extract_first()
        item['volume_avg'] = response.xpath('//*[@id="quote-summary"]/div[1]/table/tbody/tr[8]/td[2]/span/text()').extract_first()
        item['market_cap'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[1]/td[2]/span/text()').extract_first()
        item['beta'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[2]/td[2]/span/text()').extract_first()
        item['pe_ratio'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[3]/td[2]/span/text()').extract_first()
        item['eps'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[4]/td[2]/span/text()').extract_first()
        item['earnings_date'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[5]/td[2]/span/text()').extract_first()
        dividend_String = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[6]/td[2]/text()').extract_first()
        if 'N/A' in dividend_String:
            item['dividend'] = 'N/A'
            item['dividend_yield'] = 'N/A'
        else:
            item['dividend'] = re.search('.*(?=\s)', dividend_String).group()
            item['dividend_yield'] = str(round(float(str(re.search('(?<=\().*(?=\%)', dividend_String).group())) / 100, 4))
        item['ex_dividend_date'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[7]/td[2]/span/text()').extract_first()
        item['target_est_1Y'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[8]/td[2]/span/text()').extract_first()





        yield item
