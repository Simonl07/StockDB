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

process.crawl(Spider1)
process.start()

displayStats(stock_lst)
#updateServer()
