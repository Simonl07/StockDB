import requests
import crawler
import scrapy
from scrapy.crawler import CrawlerProcess
from crawler import spiders
from crawler.spiders.spider import *


def url_generator(stockIDs):
    YAHOO_BASE_URL = 'https://finance.yahoo.com/quote/'
    lst = []
    for s in stockIDs:
        lst.append(YAHOO_BASE_URL + s + "?p=" + s)
    return lst

while True:
    # r = requests.get('http://127.0.0.1/list')
    # stock_lst = list(r.json().values())
    #
    # stock_lst = stock_lst[1:30]

    PriceSpider.start_urls = url_generator(['NVDA', 'AAPL', 'GOOG'])



    process = CrawlerProcess({'ITEM_PIPELINES': {
       'crawler.pipelines.Clean_name': 297,
       'crawler.pipelines.Clean_price': 298,
       'crawler.pipelines.Clean_volume': 299,
       'crawler.pipelines.RequestDB': 300
    }})

    process.crawl(PriceSpider)
    process.start()

#updateServer()
