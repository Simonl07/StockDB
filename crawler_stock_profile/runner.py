import requests
import crawler
import scrapy
import json
import sys
import hashlib
from scrapy.crawler import CrawlerProcess
from crawler import spiders
from crawler.spiders.spider import *


def url_generator(stockIDs):
    YAHOO_BASE_URL = 'https://finance.yahoo.com/quote/'
    lst = []
    for d in stockIDs:
        lst.append(YAHOO_BASE_URL + d["symbol"])
    return lst

def execute(HOST):
    ProfileSpider.HOST = HOST

    response = requests.get(HOST + '/task?type=profile')
    jsonObject = json.loads(response.text)

    crawl_id = jsonObject['id']
    crawl_start_time = jsonObject['assignment_begin']
    stocks = jsonObject['stocks']
    symbol2id = {}
    for d in stocks:
        symbol2id[d["symbol"]] =  d["id"]

    ProfileSpider.start_urls = url_generator(stocks)
    ProfileSpider.crawl_id = crawl_id
    ProfileSpider.symbol2id = symbol2id
    process = CrawlerProcess({'ITEM_PIPELINES':{'crawler.pipelines.Clean_name': 297,'crawler.pipelines.RequestDB': 300},'USER_AGENT': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36', 'CONCURRENT_REQUESTS': 5, 'DOWNLOAD_DELAY':2})
    process.crawl(ProfileSpider)
    process.start()

HOST = "http://" + sys.argv[1]
execute(HOST)
