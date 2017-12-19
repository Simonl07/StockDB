# -*- coding: utf-8 -*-
import scrapy
import re
import datetime
import requests
import time
from crawler import *
from bs4 import BeautifulSoup
from crawler.items import Price
from scrapy.selector import Selector
from scrapy_splash import SplashRequest
from json import loads

class PriceSpider1(scrapy.Spider):
    crawl_id = ""
    crawler_id = ""
    name = "price_spider"
    allowed_domains = ['https://finance.yahoo.com']

    def parse(self, response):
        if 'lookup' in response.url or response.status == 404:
            stock_name = re.search('(?<=\=).+$', response.url).group()
            url = 'http://127.0.0.1/update'
            url += '?id=' + self.crawl_id
            url += '&type=invalid'
            url += '&stock='+ stock_name
            headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
            r = requests.post(url, headers=headers)
            return

        self.crawler.stats.inc_value('spiders_crawled')

        if datetime.datetime.now().time().microsecond > 950000:
            url = 'http://127.0.0.1/update'
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


        item['name_short'] = stores['QuoteSummaryStore']['symbol']
        item['name_full'] = stores['QuoteSummaryStore']['price']['longName']
        item['price'] = str(stores['QuoteSummaryStore']['financialData']['currentPrice']['raw'])
        item['volume'] = str(stores['QuoteSummaryStore']['summaryDetail']['volume']['raw'])

        yield item

class PriceSpider2(scrapy.Spider):
    crawl_id = ""
    crawler_id = ""
    name = "price_spider"
    allowed_domains = ['https://finance.yahoo.com']

    def parse(self, response):
        if 'lookup' in response.url or response.status == 404:
            stock_name = re.search('(?<=\=).+$', response.url).group()
            url = 'http://127.0.0.1/update'
            url += '?id=' + self.crawl_id
            url += '&type=invalid'
            url += '&stock='+ stock_name
            headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
            r = requests.post(url, headers=headers)
            return

        self.crawler.stats.inc_value('spiders_crawled')

        if datetime.datetime.now().time().microsecond > 950000:
            url = 'http://127.0.0.1/update'
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


        item['name_short'] = stores['QuoteSummaryStore']['symbol']
        item['name_full'] = stores['QuoteSummaryStore']['price']['longName']
        item['price'] = str(stores['QuoteSummaryStore']['financialData']['currentPrice']['raw'])
        item['volume'] = str(stores['QuoteSummaryStore']['summaryDetail']['volume']['raw'])

        yield item

class PriceSpider3(scrapy.Spider):
    crawl_id = ""
    crawler_id = ""
    name = "price_spider"
    allowed_domains = ['https://finance.yahoo.com']

    def parse(self, response):
        if 'lookup' in response.url or response.status == 404:
            stock_name = re.search('(?<=\=).+$', response.url).group()
            url = 'http://127.0.0.1/update'
            url += '?id=' + self.crawl_id
            url += '&type=invalid'
            url += '&stock='+ stock_name
            headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
            r = requests.post(url, headers=headers)
            return

        self.crawler.stats.inc_value('spiders_crawled')

        if datetime.datetime.now().time().microsecond > 950000:
            url = 'http://127.0.0.1/update'
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


        item['name_short'] = stores['QuoteSummaryStore']['symbol']
        item['name_full'] = stores['QuoteSummaryStore']['price']['longName']
        item['price'] = str(stores['QuoteSummaryStore']['financialData']['currentPrice']['raw'])
        item['volume'] = str(stores['QuoteSummaryStore']['summaryDetail']['volume']['raw'])

        yield item

class PriceSpider4(scrapy.Spider):
    crawl_id = ""
    crawler_id = ""
    name = "price_spider"
    allowed_domains = ['https://finance.yahoo.com']

    def parse(self, response):
        if 'lookup' in response.url or response.status == 404:
            stock_name = re.search('(?<=\=).+$', response.url).group()
            url = 'http://127.0.0.1/update'
            url += '?id=' + self.crawl_id
            url += '&type=invalid'
            url += '&stock='+ stock_name
            headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
            r = requests.post(url, headers=headers)
            return

        self.crawler.stats.inc_value('spiders_crawled')

        if datetime.datetime.now().time().microsecond > 950000:
            url = 'http://127.0.0.1/update'
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


        item['name_short'] = stores['QuoteSummaryStore']['symbol']
        item['name_full'] = stores['QuoteSummaryStore']['price']['longName']
        item['price'] = str(stores['QuoteSummaryStore']['financialData']['currentPrice']['raw'])
        item['volume'] = str(stores['QuoteSummaryStore']['summaryDetail']['volume']['raw'])

        yield item

class PriceSpider5(scrapy.Spider):
    crawl_id = ""
    crawler_id = ""
    name = "price_spider"
    allowed_domains = ['https://finance.yahoo.com']

    def parse(self, response):
        if 'lookup' in response.url or response.status == 404:
            stock_name = re.search('(?<=\=).+$', response.url).group()
            url = 'http://127.0.0.1/update'
            url += '?id=' + self.crawl_id
            url += '&type=invalid'
            url += '&stock='+ stock_name
            headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
            r = requests.post(url, headers=headers)
            return

        self.crawler.stats.inc_value('spiders_crawled')

        if datetime.datetime.now().time().microsecond > 950000:
            url = 'http://127.0.0.1/update'
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


        item['name_short'] = stores['QuoteSummaryStore']['symbol']
        item['name_full'] = stores['QuoteSummaryStore']['price']['longName']
        item['price'] = str(stores['QuoteSummaryStore']['financialData']['currentPrice']['raw'])
        item['volume'] = str(stores['QuoteSummaryStore']['summaryDetail']['volume']['raw'])

        yield item


class PriceSpider6(scrapy.Spider):
    crawl_id = ""
    crawler_id = ""
    name = "price_spider"
    allowed_domains = ['https://finance.yahoo.com']

    def parse(self, response):
        if 'lookup' in response.url or response.status == 404:
            stock_name = re.search('(?<=\=).+$', response.url).group()
            url = 'http://127.0.0.1/update'
            url += '?id=' + self.crawl_id
            url += '&type=invalid'
            url += '&stock='+ stock_name
            headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
            r = requests.post(url, headers=headers)
            return

        self.crawler.stats.inc_value('spiders_crawled')

        if datetime.datetime.now().time().microsecond > 950000:
            url = 'http://127.0.0.1/update'
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


        item['name_short'] = stores['QuoteSummaryStore']['symbol']
        item['name_full'] = stores['QuoteSummaryStore']['price']['longName']
        item['price'] = str(stores['QuoteSummaryStore']['financialData']['currentPrice']['raw'])
        item['volume'] = str(stores['QuoteSummaryStore']['summaryDetail']['volume']['raw'])

        yield item
