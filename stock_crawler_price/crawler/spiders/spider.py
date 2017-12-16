# -*- coding: utf-8 -*-
import scrapy
import re
import datetime
import requests
import time
from crawler import *
from crawler.items import Price

class PriceSpider(scrapy.Spider):
    crawl_id = ""
    name = "price_spider"
    allowed_domains = ['https://finance.yahoo.com']

    def parse(self, response):

        if 'lookup' in response.url or response.status == 404:
            stock_name = re.search('(?<=\=).+$', response.url).group()
            url = 'http://127.0.0.1/update'
            url += '?id=' + PriceSpider.crawl_id
            url += '&type=invalid'
            url += '&stock='+ stock_name
            headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
            r = requests.post(url, headers=headers)
            return

        self.crawler.stats.inc_value('spiders_crawled')

        if datetime.datetime.now().time().microsecond > 950000:
            url = 'http://127.0.0.1/update'
            url += '?id=' + PriceSpider.crawl_id
            url += '&type=progress'
            url += '&value='+ str(self.crawler.stats.get_value('spiders_crawled'))
            headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
            r = requests.post(url, headers=headers)


        item = Price()

        item['name_string'] = response.xpath("//*[@id=\"quote-header-info\"]/div[2]/div[1]/div[1]/h1/text()").extract_first()
        item['price'] = response.xpath("//*[@id=\"quote-header-info\"]/div[3]/div[1]/div/span[1]/text()").extract_first()
        item['volume'] = response.xpath('//*[@id="quote-summary"]/div[1]/table/tbody/tr[7]/td[2]/span/text()').extract_first()

        yield item
