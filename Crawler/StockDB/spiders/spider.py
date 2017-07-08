# -*- coding:UTF-8 -*-
import scrapy
import hashlib
import re
import requests
from StockDB import *


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
    start_urls = url_generator(['AAPL', 'GOOG', 'CBG'])






    def parse(self, response):
        headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain'}
        name = response.xpath('//*[@id="quote-header-info"]/div[2]/div[1]/div/h1/text()').extract_first()
        oname = re.search('.+?(?=\()', name).group()
        print(oname)
        abbrev = re.search('(?<=\().+?(?=\))', name).group()
        print(abbrev)
        r = requests.post("http://127.0.0.1", data={u'name': oname.encode('utf-8'), u'abbrev' : abbrev.encode('utf-8')}, headers=headers)
