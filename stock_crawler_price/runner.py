import requests
import crawler
import scrapy
import json
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
    # r = requests.get('http://127.0.0.1/list')
    # stock_lst = list(r.json().values())
    #
    # stock_lst = stock_lst[1:30]


def execute():

    PROCESS_SIZE = 6

    response = requests.get('http://127.0.0.1:/list?type=price')
    jsonObject = json.loads(response.text)

    crawl_id = jsonObject['id']
    crawl_start_time = jsonObject['assignment_begin']
    stocks = jsonObject['stocks']

    part = len(stocks)// PROCESS_SIZE

    PriceSpider1.start_urls = url_generator(stocks[:part])
    PriceSpider1.crawl_id = crawl_id
    PriceSpider1.crawler_id = int('0x' + hashlib.md5(str.encode(crawl_id + "PriceSpider1")).hexdigest(), 16)
    reportCrawler(crawl_id, PriceSpider1.crawler_id)

    PriceSpider2.start_urls = url_generator(stocks[part:part * 2])
    PriceSpider2.crawl_id = crawl_id
    PriceSpider2.crawler_id = int('0x' + hashlib.md5(str.encode(crawl_id + "PriceSpider2")).hexdigest(), 16)
    reportCrawler(crawl_id, PriceSpider2.crawler_id)

    PriceSpider3.start_urls = url_generator(stocks[part * 2: part * 3])
    PriceSpider3.crawl_id = crawl_id
    PriceSpider3.crawler_id = int('0x' + hashlib.md5(str.encode(crawl_id + "PriceSpider3")).hexdigest(), 16)
    reportCrawler(crawl_id, PriceSpider3.crawler_id)

    PriceSpider4.start_urls = url_generator(stocks[part * 3:part * 4])
    PriceSpider4.crawl_id = crawl_id
    PriceSpider4.crawler_id = int('0x' + hashlib.md5(str.encode(crawl_id + "PriceSpider4")).hexdigest(), 16)
    reportCrawler(crawl_id, PriceSpider4.crawler_id)

    PriceSpider5.start_urls = url_generator(stocks[part * 4:part * 5])
    PriceSpider5.crawl_id = crawl_id
    PriceSpider5.crawler_id = int('0x' + hashlib.md5(str.encode(crawl_id + "PriceSpider5")).hexdigest(), 16)
    reportCrawler(crawl_id, PriceSpider5.crawler_id)

    PriceSpider6.start_urls = url_generator(stocks[part * 5:])
    PriceSpider6.crawl_id = crawl_id
    PriceSpider6.crawler_id = int('0x' + hashlib.md5(str.encode(crawl_id + "PriceSpider6")).hexdigest(), 16)
    reportCrawler(crawl_id, PriceSpider6.crawler_id)


    setStatusBegin(crawl_id)

    process = CrawlerProcess({'ITEM_PIPELINES': {
       'crawler.pipelines.Clean_name': 297,
       'crawler.pipelines.Clean_price': 298,
       'crawler.pipelines.Clean_volume': 299,
       'crawler.pipelines.RequestDB': 300
    }})

    process.crawl(PriceSpider1)
    process.crawl(PriceSpider2)
    process.crawl(PriceSpider3)
    process.crawl(PriceSpider4)
    process.crawl(PriceSpider5)
    process.crawl(PriceSpider6)
    process.start()

    print("TOTAL: ", PriceSpider1.TOTAL)
    print("AVG: ", PriceSpider1.TOTAL/part)

    setStatusComplete(crawl_id)


def setStatusBegin(crawl_id):
    url = 'http://127.0.0.1/update'
    url += '?id=' + crawl_id
    url += '&type=update'
    url += '&status=1'
    headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
    r = requests.post(url, headers=headers)

def setStatusComplete(crawl_id):
    url = 'http://127.0.0.1/update'
    url += '?id=' + crawl_id
    url += '&type=update'
    url += '&status=2'
    headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
    r = requests.post(url, headers=headers)

def reportCrawler(crawl_id, crawler_id):
    url = 'http://127.0.0.1/update'
    url += '?id=' + crawl_id
    url += '&type=new_crawler'
    url += '&task_id=' + crawl_id
    url += '&crawler_id=' + str(crawler_id)
    headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
    r = requests.post(url, headers=headers)

execute()
