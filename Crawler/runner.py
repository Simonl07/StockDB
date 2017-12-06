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
   'StockDB.pipelines.Clean_name': 287,
   'StockDB.pipelines.Clean_range_day': 288,
   'StockDB.pipelines.Clean_range_52w': 289,
   'StockDB.pipelines.Clean_market_cap': 290,
   'StockDB.pipelines.Clean_earnings_date': 291,
   'StockDB.pipelines.Clean_dividend': 292,
   'StockDB.pipelines.Clean_ex_dividend_date': 293,
   'StockDB.pipelines.Clean_target_est_1Y': 294,
   'StockDB.pipelines.Clean_eps': 295,
   'StockDB.pipelines.Clean_pe_ratio': 296,
   'StockDB.pipelines.Clean_volume': 297,
   'StockDB.pipelines.Clean_volume_avg': 298,
   'StockDB.pipelines.Clean_beta': 299,
   'StockDB.pipelines.RequestDB': 300,
}})

process.crawl(Spider1)
process.start()

displayStats(stock_lst)
#updateServer()
