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
    for s in stockIDs:
        lst.append(YAHOO_BASE_URL + s + "?p=" + s)
    return lst

def execute(HOST):
    ProfileSpider.HOST = HOST

    response = requests.get(HOST + '/list?type=price')
    jsonObject = json.loads(response.text)

    crawl_id = jsonObject['id']
    crawl_start_time = jsonObject['assignment_begin']
    stocks = jsonObject['stocks']


    ProfileSpider.start_urls = url_generator(stocks)
    process = CrawlerProcess({'ITEM_PIPELINES': {
       'crawler.pipelines.Clean_name': 297,
       'crawler.pipelines.RequestDB': 300
    }})

    process.crawl(ProfileSpider)
    process.start()

HOST = "http://" + sys.argv[1]
execute(HOST)
