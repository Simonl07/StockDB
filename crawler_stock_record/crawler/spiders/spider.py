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
            item['market_cap'] = -1
        else:
            item['market_cap'] = summaryDetail['marketCap'].get('raw', -1)

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
            item['ex_dividend_date'] = summaryDetail['exDividendDate'].get('fmt', '1970-01-01')


        if 'quoteType' not in stores.keys():
            return

        item['name_short'] = stores['quoteType']['shortName']
        item['name_long'] = stores['quoteType']['longName']

        if 'price' not in stores.keys():
            return

        if 'regularMarketPrice' not in stores['price'].keys():
            item['price_open'] = -1
        else:
            item['price_open'] = stores['price']['regularMarketPrice'].get('raw', -1)

        if 'postMarketPrice' not in stores['price'].keys():
            item['price_close'] = -1
        else:
            item['price_close'] = stores['price']['postMarketPrice'].get('raw', -1)

        if 'defaultKeyStatistics' in stores.keys() and 'trailingEps' in stores['defaultKeyStatistics'].keys():
            item['eps'] = stores['defaultKeyStatistics']['trailingEps'].get('raw', -1)
        else:
            item['eps'] = -1

        if 'calendarEvents' in stores.keys() and 'earnings' in stores['calendarEvents'].keys() and 'earningsDate' in stores['calendarEvents']['earnings'].keys():
            if len(stores['calendarEvents']['earnings']['earningsDate']) == 2:
                item['earnings_date_begin'] = stores['calendarEvents']['earnings']['earningsDate'][0].get('fmt', '1970-01-01')
                item['earnings_date_end'] = stores['calendarEvents']['earnings']['earningsDate'][1].get('fmt', '1970-01-01')
            elif len(stores['calendarEvents']['earnings']['earningsDate']) == 1:
                item['earnings_date_begin'] = stores['calendarEvents']['earnings']['earningsDate'][0].get('fmt', '1970-01-01')
                item['earnings_date_end'] = stores['calendarEvents']['earnings']['earningsDate'][0].get('fmt', '1970-01-01')
            else:
                item['earnings_date_begin'] = '1970-01-01'
                item['earnings_date_end'] = '1970-01-01'
        else:
            item['earnings_date_begin'] = '1970-01-01'
            item['earnings_date_end'] = '1970-01-01'

        if 'financialData' in stores.keys() and 'targetMeanPrice' in stores['financialData'].keys():
            item['target_est_1Y'] = stores['financialData']['targetMeanPrice'].get('raw', -1)
        else:
            item['target_est_1Y'] = -1

        yield item
