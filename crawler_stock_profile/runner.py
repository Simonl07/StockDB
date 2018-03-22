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
        lst.append(YAHOO_BASE_URL + d["symbol"] + "?p=" + d["symbol"])
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
    process = CrawlerProcess({'ITEM_PIPELINES':{'crawler.pipelines.Clean_name': 297,'crawler.pipelines.RequestDB': 300},'USER_AGENT': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:41.0) Gecko/20100101 Firefox/41.0', 'CONCURRENT_REQUESTS': 1, 'DOWNLOAD_DELAY':1})
    process.crawl(ProfileSpider)
    process.start()

HOST = "http://" + sys.argv[1]
execute(HOST)
