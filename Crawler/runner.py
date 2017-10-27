import requests
import StockDB
import scrapy
from scrapy.crawler import CrawlerProcess
from StockDB import spiders
from StockDB.spiders.spider import Spider
from StatsUtils import *


r = requests.get('http://127.0.0.1/list')
stock_lst = list(r.json().values())
Spider.start_urls = url_generator(stock_lst[:3])

process = CrawlerProcess()

process.crawl(Spider)
process.start()

displayStats()
