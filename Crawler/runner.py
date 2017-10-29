import requests
import StockDB
import scrapy
from scrapy.crawler import CrawlerProcess
from StockDB import spiders
from StockDB.spiders.spider import Spider
from StatsUtils import *


r = requests.get('http://127.0.0.1/list')
stock_lst = list(r.json().values())
targets = stock_lst[50:400]
Spider.start_urls = url_generator(targets)

process = CrawlerProcess({'ITEM_PIPELINES': {'StockDB.pipelines.Stock': 300}})

process.crawl(Spider)
process.start()

displayStats(targets)
#updateServer()
