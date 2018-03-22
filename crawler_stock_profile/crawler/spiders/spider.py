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
    name = "profile"
    allowed_domains = ['https://finance.yahoo.com']

    def parse(self, response):

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
