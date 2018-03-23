import scrapy
import re
import random
import datetime
import requests
import time
from crawler import *
from bs4 import BeautifulSoup
from crawler.items import Price
from scrapy.selector import Selector
from json import loads

REPORT_RATE = 0.01


class marketCapSpider2(scrapy.Spider):
    crawl_id = ""
    crawler_id = ""
    name = "price_spider"
    allowed_domains = ['https://finance.yahoo.com']

    def parse(self, response):
        if 'lookup' in response.url or response.status == 404:
            stock_name = re.search('(?<=\=).+$', response.url).group()
            url = self.HOST + '/update'
            url += '?id=' + self.crawl_id
            url += '&type=invalid'
            url += '&stock='+ stock_name
            headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
            r = requests.post(url, headers=headers)
            return

        self.crawler.stats.inc_value('spiders_crawled')

        if random.random() < 0.02:
            url = self.HOST + '/update'
            url += '?task_id=' + self.crawl_id
            url += '&crawler_id=' + str(self.crawler_id)
            url += '&type=progress'
            url += '&value='+ str(self.crawler.stats.get_value('spiders_crawled'))
            headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
            r = requests.post(url, headers=headers)

        soup = BeautifulSoup(response.text)
        script = soup.find("script",text=re.compile("root.App.main")).text
        data = loads(re.search("root.App.main\s+=\s+(\{.*\})", script).group(1))
        stores = data["context"]["dispatcher"]["stores"]


        item = Stock()


        try:
        	item['beta'] = stores['summaryDetail']['beta']['raw']
            item['name_short'] = stores['quoteType']['shortName']
           	item['name_long'] = stores['quoteType']['longName']
            item['price_close'] = stores['price']['regularMarketPrice']
            item['price_open'] = stores['price']['postMarketPrice']
            item['range_day_high'] = stores['summarydetail']['dayHigh']['raw']
            item['range_day_low'] = stores['summarydetail']['dayLow']['raw']
            item['range_52w_low'] =  stores['summaryDetail']['fiftyTwoWeekLow']['raw']   
            item['range_52w_high'] =  stores['summaryDetail']['fiftyTwoWeekHigh']['raw'] 
            item['volume'] = stores['summaryDetail']['volume']['raw']		 
            item['volume_avg'] = stores['summaryDetail']['averageVolume']['raw']
            item['market_cap'] = stores['summaryDetail']['marketCap']['raw']	  
            item['beta'] = stores['summaryDetail']['beta']['raw']	
            item['pe_ratio'] = stores['summaryDetail']['trailingPE']['raw']	  
            item['eps'] = stores['defaultKeyStatistics']['trailingEps']['raw']
            item['earnings_date_begin'] = stores['calendarEvents']['earnings']['earningsDate'][0]['raw']
            item['earnings_date_end'] = stores['calendarEvents']['earnings']['earningsDate'][1]['raw']
            item['dividend'] = stores['summaryDetail']['dividendRate']['raw']
            item['dividend_yield'] =  stores['summaryDetail']['dividendYield']['raw'] 
            item['ex_dividend_date'] = stores['summaryDetail']['exDividendDate']
            item['target_est_1Y'] = stores['financialData']['targetMeanPrice']['raw']
            		

           
        except:
            return

        yield item
