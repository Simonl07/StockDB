# -*- coding: utf-8 -*-
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



class PriceSpider1(scrapy.Spider):
    symbol2id = {}
    crawl_id = ""
    crawler_id = ""
    name = "price_spider"
    allowed_domains = ['https://finance.yahoo.com']
    TOTAL = 0

    def parse(self, response):
        if 'lookup' in response.url or response.status == 404:
            stock_name = re.search('(?<=\=).+$', response.url).group()
            url = self.HOST + '/update'
            url += '?crawl_task_id=' + self.crawl_id
            url += '&type=invalid'
            url += '&stock_id='+ self.symbol2id[stock_name]
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

        s = time.time()
        soup = BeautifulSoup(response.text)
        script = soup.find("script",text=re.compile("root.App.main")).text
        data = loads(re.search("root.App.main\s+=\s+(\{.*\})", script).group(1))
        stores = data["context"]["dispatcher"]["stores"]
        e = time.time()
        PriceSpider1.TOTAL = PriceSpider1.TOTAL + (e - s)

        item = Price()



        try:
            item['name_short'] = stores['QuoteSummaryStore']['symbol']
            item['name_full_long'] = stores['QuoteSummaryStore']['price']['longName']
            item['name_full_short'] = stores['QuoteSummaryStore']['price']['shortName']
            item['price'] = str(stores['QuoteSummaryStore']['financialData']['currentPrice']['raw'])
            item['volume'] = str(stores['QuoteSummaryStore']['summaryDetail']['volume']['raw'])
        except:
            return

        yield item

class PriceSpider2(scrapy.Spider):
    symbol2id = {}
    crawl_id = ""
    crawler_id = ""
    name = "price_spider"
    allowed_domains = ['https://finance.yahoo.com']

    def parse(self, response):
        if 'lookup' in response.url or response.status == 404:
            stock_name = re.search('(?<=\=).+$', response.url).group()
            url = self.HOST + '/update'
            url += '?crawl_task_id=' + self.crawl_id
            url += '&type=invalid'
            url += '&stock_id='+ self.symbol2id[stock_name]
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


        item = Price()


        try:
            item['name_short'] = stores['QuoteSummaryStore']['symbol']
            item['name_full_long'] = stores['QuoteSummaryStore']['price']['longName']
            item['name_full_short'] = stores['QuoteSummaryStore']['price']['shortName']
            item['price'] = str(stores['QuoteSummaryStore']['financialData']['currentPrice']['raw'])
            item['volume'] = str(stores['QuoteSummaryStore']['summaryDetail']['volume']['raw'])
        except:
            return

        yield item

class PriceSpider3(scrapy.Spider):
    symbol2id = {}
    crawl_id = ""
    crawler_id = ""
    name = "price_spider"
    allowed_domains = ['https://finance.yahoo.com']

    def parse(self, response):
        if 'lookup' in response.url or response.status == 404:
            stock_name = re.search('(?<=\=).+$', response.url).group()
            url = self.HOST + '/update'
            url += '?crawl_task_id=' + self.crawl_id
            url += '&type=invalid'
            url += '&stock_id='+ self.symbol2id[stock_name]
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


        item = Price()


        try:
            item['name_short'] = stores['QuoteSummaryStore']['symbol']
            item['name_full_long'] = stores['QuoteSummaryStore']['price']['longName']
            item['name_full_short'] = stores['QuoteSummaryStore']['price']['shortName']
            item['price'] = str(stores['QuoteSummaryStore']['financialData']['currentPrice']['raw'])
            item['volume'] = str(stores['QuoteSummaryStore']['summaryDetail']['volume']['raw'])
        except:
            return

        yield item

class PriceSpider4(scrapy.Spider):
    symbol2id = {}
    crawl_id = ""
    crawler_id = ""
    name = "price_spider"
    allowed_domains = ['https://finance.yahoo.com']

    def parse(self, response):
        if 'lookup' in response.url or response.status == 404:
            stock_name = re.search('(?<=\=).+$', response.url).group()
            url = self.HOST + '/update'
            url += '?crawl_task_id=' + self.crawl_id
            url += '&type=invalid'
            url += '&stock_id='+ self.symbol2id[stock_name]
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


        item = Price()


        try:
            item['name_short'] = stores['QuoteSummaryStore']['symbol']
            item['name_full_long'] = stores['QuoteSummaryStore']['price']['longName']
            item['name_full_short'] = stores['QuoteSummaryStore']['price']['shortName']
            item['price'] = str(stores['QuoteSummaryStore']['financialData']['currentPrice']['raw'])
            item['volume'] = str(stores['QuoteSummaryStore']['summaryDetail']['volume']['raw'])
        except:
            return

        yield item
