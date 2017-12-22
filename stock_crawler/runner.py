import requests
import crawler
import scrapy
import json
from scrapy.crawler import CrawlerProcess
from crawler import spiders
from crawler.spiders.spider import *
from StatsUtils import *


response = requests.get('http://127.0.0.1/list?type=summary')
jsonObject = json.loads(response.text)

crawl_id = jsonObject['id']
crawl_start_time = jsonObject['assignment_begin']
stocks = jsonObject['stocks']
Spider1.start_urls = url_generator(stocks)
Spider1.crawl_id = crawl_id

stock_lst = stocks

Spider1.start_urls = url_generator(stock_lst)


process = CrawlerProcess({'ITEM_PIPELINES': {
   'crawler.pipelines.Clean_name': 287,
   'crawler.pipelines.Clean_range_day': 288,
   'crawler.pipelines.Clean_range_52w': 289,
   'crawler.pipelines.Clean_market_cap': 290,
   'crawler.pipelines.Clean_earnings_date': 291,
   'crawler.pipelines.Clean_dividend': 292,
   'crawler.pipelines.Clean_ex_dividend_date': 293,
   'crawler.pipelines.Clean_target_est_1Y': 294,
   'crawler.pipelines.Clean_eps': 295,
   'crawler.pipelines.Clean_pe_ratio': 296,
   'crawler.pipelines.Clean_volume': 297,
   'crawler.pipelines.Clean_volume_avg': 298,
   'crawler.pipelines.Clean_beta': 299,
   'crawler.pipelines.RequestDB': 300,
}})

url = 'http://127.0.0.1/update'
url += '?id=' + Spider1.crawl_id
url += '&type=update'
url += '&status=1'
headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
r = requests.post(url, headers=headers)

process.crawl(Spider1)
process.start()

url = 'http://127.0.0.1/update'
url += '?id=' + PriceSpider.crawl_id
url += '&type=update'
url += '&status=2'
headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
r = requests.post(url, headers=headers)
