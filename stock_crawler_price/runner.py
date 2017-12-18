import requests
import crawler
import scrapy
import json
from scrapy.crawler import CrawlerProcess
from crawler import spiders
from crawler.spiders.spider import *


def url_generator(stockIDs):
    YAHOO_BASE_URL = 'https://finance.yahoo.com/quote/'
    lst = []
    for s in stockIDs:
        lst.append(YAHOO_BASE_URL + s + "?p=" + s)
    return lst
    # r = requests.get('http://127.0.0.1/list')
    # stock_lst = list(r.json().values())
    #
    # stock_lst = stock_lst[1:30]


response = requests.get('http://127.0.0.1:/list')
jsonObject = json.loads(response.text)

crawl_id = jsonObject['id']
crawl_start_time = jsonObject['assignment_begin']
stocks = jsonObject['stocks']
PriceSpider.start_urls = url_generator(stocks)
PriceSpider.crawl_id = crawl_id


url = 'http://127.0.0.1/update'
url += '?id=' + PriceSpider.crawl_id
url += '&type=update'
url += '&status=1'
headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
r = requests.post(url, headers=headers)

process = CrawlerProcess({'ITEM_PIPELINES': {
   'crawler.pipelines.Clean_name': 297,
   'crawler.pipelines.Clean_price': 298,
   'crawler.pipelines.Clean_volume': 299,
   'crawler.pipelines.RequestDB': 300
}})

process.crawl(PriceSpider)
process.start()


url = 'http://127.0.0.1/update'
url += '?id=' + PriceSpider.crawl_id
url += '&type=update'
url += '&status=2'
headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
r = requests.post(url, headers=headers)
