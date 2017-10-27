import requests
import StockDB
from StockDB import spiders
from StockDB.spiders.spider import Spider


r = requests.get('http://127.0.0.1/list')
stock_lst = r.json().values()
print(Spider)
