import requests
import crawler
import scrapy
from scrapy.crawler import CrawlerProcess
from crawler import spiders
from crawler.spiders.spider import *
from StatsUtils import *


r = requests.get('http://127.0.0.1/list')
stock_lst = list(r.json().values())

stock_lst = stock_lst[1:100]

Spider1.start_urls = url_generator(stock_lst)


process = CrawlerProcess()

process.crawl(Spider1)
process.start()

displayStats(stock_lst)
#updateServer()
