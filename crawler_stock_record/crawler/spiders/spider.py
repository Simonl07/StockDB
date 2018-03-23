import scrapy
import re
import random
import datetime
import requests
import time
from crawler import *
from bs4 import BeautifulSoup
from crawler.items import Stock
from scrapy.selector import Selector
from json import loads

REPORT_RATE = 0.01


class Spider1(scrapy.Spider):
    crawl_id = ""
    crawler_id = ""
    name = "price_spider"

    def parse(self, response):
        if 'lookup' in response.url or response.status == 404:
            stock_name = re.search('(?<=\=).+$', response.url).group()
            url = self.HOST + '/update'
            url += '?task_id=' + str(self.crawl_id)
            url += '&type=invalid'
            url += '&stock_id='+ str(self.symbol2id[stock_name])
            headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
            r = requests.post(url, headers=headers)
            return

        self.crawler.stats.inc_value('spiders_crawled')

        if random.random() < 0.02:
            url = self.HOST + '/update'
            url += '?task_id=' + str(self.crawl_id)
            url += '&crawler_id=' + str(self.crawler_id)
            url += '&type=progress'
            url += '&value='+ str(self.crawler.stats.get_value('spiders_crawled'))
            headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
            r = requests.post(url, headers=headers)

        soup = BeautifulSoup(response.text)
        script = soup.find("script",text=re.compile("root.App.main")).text
        data = loads(re.search("root.App.main\s+=\s+(\{.*\})", script).group(1))
        stores = data["context"]["dispatcher"]["stores"]['QuoteSummaryStore']

        item = Stock()
        item['symbol'] = stores['symbol']
        summaryDetail = stores['summaryDetail']
        if summaryDetail is None:
            return
        item['beta'] = summaryDetail['beta'].get('raw', '1970-01-01')
        item['range_day_high'] = summaryDetail['dayHigh']['raw']
        item['range_day_low'] = summaryDetail['dayLow']['raw']
        item['range_52w_low'] = summaryDetail['fiftyTwoWeekLow']['raw']
        item['range_52w_high'] = summaryDetail['fiftyTwoWeekHigh']['raw']
        item['volume'] = summaryDetail['volume']['raw']
        item['volume_avg'] = summaryDetail['averageVolume']['raw']
        item['market_cap'] = summaryDetail['marketCap']['raw']
        item['beta'] = summaryDetail['beta']['raw']
        item['pe_ratio'] = summaryDetail.get('trailingPE', -1)['raw']
        item['dividend'] = summaryDetail['dividendRate'].get('raw', -1)
        item['dividend_yield'] =  summaryDetail['dividendYield'].get('raw', -1)
        item['ex_dividend_date'] = summaryDetail['exDividendDate'].get('fmt', '1970-01-01')

        item['name_short'] = stores['quoteType']['shortName']
        item['name_long'] = stores['quoteType']['longName']
        item['price_close'] = stores['price'].get('regularMarketPrice', -1)['raw']
        item['price_open'] = stores['price'].get('postMarketPrice', -1)['raw']

        item['eps'] = stores['defaultKeyStatistics']['trailingEps']['raw']
        item['earnings_date_begin'] = stores['calendarEvents']['earnings']['earningsDate'][0].get('fmt', '1970-01-01')
        item['earnings_date_end'] = stores['calendarEvents']['earnings']['earningsDate'][1].get('fmt', '1970-01-01')

        item['target_est_1Y'] = stores['financialData']['targetMeanPrice']['raw']

        yield item
