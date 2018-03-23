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

        if 'summaryDetail' not in stores.keys():
            return
        summaryDetail = stores['summaryDetail']

        if 'beta' not in summaryDetail.keys():
            item['beta'] = -1
        else:
            item['beta'] = summaryDetail['beta'].get('raw', -1)

        item['range_day_high'] = summaryDetail['dayHigh']['raw']
        item['range_day_low'] = summaryDetail['dayLow']['raw']
        item['range_52w_low'] = summaryDetail['fiftyTwoWeekLow']['raw']
        item['range_52w_high'] = summaryDetail['fiftyTwoWeekHigh']['raw']

        if 'volume' not in summaryDetail.keys():
            item['volume'] = -1
        else:
            item['volume'] = summaryDetail['volume'].get('raw', -1)

        if 'averageVolume' not in summaryDetail.keys():
            item['volume_avg'] = -1
        else:
            item['volume_avg'] = summaryDetail['averageVolume'].get('raw', -1)

        if 'marketCap' not in summaryDetail.keys():
            item['marketCap'] = -1
        else:
            item['marketCap'] = summaryDetail['marketCap'].get('raw', -1)

        if 'trailingPE' not in summaryDetail.keys():
            item['pe_ratio'] = -1
        else:
            item['pe_ratio'] = summaryDetail['trailingPE'].get('raw', -1)

        if 'dividendRate' not in summaryDetail.keys():
            item['dividend'] = -1
        else:
            item['dividend'] = summaryDetail['dividendRate'].get('raw', -1)

        if 'dividendYield' not in summaryDetail.keys():
            item['dividend_yield'] = -1
        else:
            item['dividend_yield'] = summaryDetail['dividendYield'].get('raw', -1)

        if 'exDividendDate' not in summaryDetail.keys():
            item['ex_dividend_date'] = '1970-01-01'
        else:
            item['ex_dividend_date'] = summaryDetail['exDividendDate'].get('raw', '1970-01-01')

        item['name_short'] = stores['quoteType']['shortName']
        item['name_long'] = stores['quoteType']['longName']
        item['price_close'] = stores['price'].get('regularMarketPrice', -1)['raw']
        item['price_open'] = stores['price'].get('postMarketPrice', -1)['raw']

        item['eps'] = stores['defaultKeyStatistics']['trailingEps']['raw']
        item['earnings_date_begin'] = stores['calendarEvents']['earnings']['earningsDate'][0].get('fmt', '1970-01-01')
        item['earnings_date_end'] = stores['calendarEvents']['earnings']['earningsDate'][1].get('fmt', '1970-01-01')

        item['target_est_1Y'] = stores['financialData']['targetMeanPrice']['raw']

        yield item
