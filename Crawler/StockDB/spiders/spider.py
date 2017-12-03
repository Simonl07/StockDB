# -*- coding:UTF-8 -*-
import scrapy
import hashlib
import re
import csv
from StockDB import *
import StatsUtils
from StatsUtils import *
from StockDB.items import Stock


class Spider1(scrapy.Spider):
    handle_httpstatus_list = [200, 404]

    name = 'spider'
    allowed_domains = ['https://finance.yahoo.com']
    stockList = []
    start_urls = url_generator(stockList)

    start_urls = ['https://finance.yahoo.com/quote/?p=AAPL']
    def parse(self, response):

        stockName = re.search('(?<=\=).+?$', response.url).group()
        recordStatus(stockName, response.status)

        if 'lookup' in response.url or response.status == 404:
            reportInvalid(stockName)
            return

        item = Stock()



        item['name_string'] = response.xpath('//*[@id="quote-header-info"]/div[2]/div[1]/div[1]/h1/text()').extract_first()
        item['price_close'] = response.xpath('//*[@id="quote-summary"]/div[1]/table/tbody/tr[1]/td[2]/span/text()').extract_first()
        item['price_open'] = response.xpath('//*[@id="quote-summary"]/div[1]/table/tbody/tr[2]/td[2]/span/text()').extract_first()
        item['range_day'] = response.xpath('//*[@id="quote-summary"]/div[1]/table/tbody/tr[5]/td[2]/text()').extract_first()
        item['range_52w'] = response.xpath('//*[@id="quote-summary"]/div[1]/table/tbody/tr[6]/td[2]/text()').extract_first()
        item["volume"] = response.xpath('//*[@id="quote-summary"]/div[1]/table/tbody/tr[7]/td[2]/span/text()').extract_first()
        item['volume_avg'] = response.xpath('//*[@id="quote-summary"]/div[1]/table/tbody/tr[8]/td[2]/span/text()').extract_first()
        item['market_cap'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[1]/td[2]/span/text()').extract_first()
        item['beta'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[2]/td[2]/span/text()').extract_first()
        item['pe_ratio'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[3]/td[2]/span/text()').extract_first()
        item['eps'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[4]/td[2]/span/text()').extract_first()
        item['earnings_date_begin'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[5]/td[2]/span/text()').extract_first()
        item['earnings_date_end'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[5]/td[2]/span[2]/text()').extract_first()
        item['dividend_String'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[6]/td[2]/text()').extract_first()
        item['ex_dividend_date'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[7]/td[2]/span/text()').extract_first()
        item['target_est_1Y'] = response.xpath('//*[@id="quote-summary"]/div[2]/table/tbody/tr[8]/td[2]/span/text()').extract_first()
        yield item
