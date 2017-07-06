# -*- coding:UTF-8 -*-
import scrapy
import hashlib
import re
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
        print(response.xpath('//*[@id="quote-header-info"]/div[2]/div[1]/div/h1/text()').extract_first() ,response.xpath('//*[@id="quote-header-info"]/div[3]/div[1]/div/span[1]/text()').extract_first())
