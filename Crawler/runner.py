import requests
import StockDB
import scrapy
from scrapy.crawler import CrawlerProcess
from StockDB import spiders
from StockDB.spiders.spider import *
from StatsUtils import *


r = requests.get('http://127.0.0.1/list')
stock_lst = list(r.json().values())


quarter = len(stock_lst) // 4

Spider1.start_urls = url_generator(stock_lst[0:1 * quarter])
Spider2.start_urls = url_generator(stock_lst[1 * quarter: 2 * quarter])
Spider3.start_urls = url_generator(stock_lst[2 * quarter: 3 * quarter])
Spider4.start_urls = url_generator(stock_lst[3 * quarter: len(stock_lst)])


process = CrawlerProcess({'ITEM_PIPELINES': {'StockDB.pipelines.Stock': 300}})

process.crawl(Spider1)
process.crawl(Spider2)
process.crawl(Spider3)
process.crawl(Spider4)
process.start()

displayStats(stock_lst)
#updateServer()
