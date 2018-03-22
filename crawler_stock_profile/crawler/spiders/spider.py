# -*- coding: utf-8 -*-
import scrapy
import re
import random
import datetime
import requests
import time
from crawler import *
from bs4 import BeautifulSoup
from crawler.items import StockProfile
from scrapy.selector import Selector
from json import loads




class ProfileSpider(scrapy.Spider):
    symbol2id = {}
    handle_httpstatus_list = [404, 200, 301, 303]
    name = "profile"
    allowed_domains = ['https://finance.yahoo.com']

    def parse(self, response):

        if 'lookup' in response.url:
            stock_name = re.search('(?<=\=).+$', response.url).group()
            url = self.HOST + '/update'
            url += '?crawl_task_id=' + self.crawl_id
            url += '&type=invalid'
            url += '&stock_id='+ str(self.symbol2id[stock_name])
            headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
            r = requests.post(url, headers=headers)
            return

        soup = BeautifulSoup(response.text)
        script = soup.find("script",text=re.compile("root.App.main")).text
        data = loads(re.search("root.App.main\s+=\s+(\{.*\})", script).group(1))
        stores = data["context"]["dispatcher"]["stores"]
        quoteSummaryStore = stores['QuoteSummaryStore']
        profile = quoteSummaryStore['summaryProfile']

        item = StockProfile()

        try:
            item['symbol'] = quoteSummaryStore['symbol']
            item['name_full_long'] = quoteSummaryStore['price']['longName']
            item['name_full_short'] = quoteSummaryStore['price']['shortName']
            item['address'] = profile['address1']
            item['zipcode'] = profile['zip']
            item['city'] = profile['city']
            item['state'] = profile['state']
            item['country'] = profile['country']
            item['description'] = profile['longBusinessSummary']
            item['employee'] = profile['fullTimeEmployees']
            item['industry'] = profile['industry']
            item['sector'] = profile['sector']
            item['website'] = profile['website']
        except:
            return

        yield item
