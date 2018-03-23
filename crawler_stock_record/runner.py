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

   'crawler.pipelines.Clean_name': 286,
   'crawler.pipelines.Clean_price': 287,
   'crawler.pipelines.Clean_range_day_low': 288,
   'crawler.pipelines.Clean_range_day_high': 289,
   'crawler.pipelines.Clean_beta': 290,
   'crawler.pipelines.CLean_range_52w_high': 291,
   'crawler.pipelines.CLean_range_52w_low': 292,
   'crawler.pipelines.CLean_volume': 293,
   'crawler.pipelines.CLean_volume_avg': 294,
   'crawler.pipelines.CLean_market_cap': 295,
   'crawler.pipelines.CLean_beta': 296,
   'crawler.pipelines.CLean_pe_ratio': 297,
   'crawler.pipelines.CLean_eps': 298,
   'crawler.pipelines.Clean_dividend': 299,
   'crawler.pipelines.CLean_target_est_1Y': 300,
   'crawler.pipelines.CLean_earnings_date_begin': 301,
   'crawler.pipelines.CLean_earnings_date_end': 302,
   'crawler.pipelines.CLean_dividend_yield': 303,
   'crawler.pipelines.CLean_ex_dividend_date': 304,
   'crawler.pipelines.RequestDB': 305,
   
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
