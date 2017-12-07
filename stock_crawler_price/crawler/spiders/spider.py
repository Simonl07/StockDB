# -*- coding: utf-8 -*-
import scrapy
from crawler import *
from crawler.items import Price

class PriceSpider(scrapy.Spider):
    name = "price_spider"
    allowed_domains = ['https://finance.yahoo.com']
    start_urls = ['https://finance.yahoo.com/quote/NVDA?p=NVDA','https://finance.yahoo.com/quote/AAPL','https://finance.yahoo.com/quote/GOOG','https://finance.yahoo.com/quote/AMD','https://finance.yahoo.com/quote/TSLA']

    def parse(self, response):

        if 'lookup' in response.url or response.status == 404:
            return

        item = Price()

        item['name_string'] = response.xpath("//*[@id=\"quote-header-info\"]/div[2]/div[1]/div[1]/h1/text()").extract_first()
        item['price'] = response.xpath("//*[@id=\"quote-header-info\"]/div[3]/div[1]/div/span[1]/text()").extract_first()
        item['volume'] = response.xpath('//*[@id="quote-summary"]/div[1]/table/tbody/tr[7]/td[2]/span/text()').extract_first()

        yield item
