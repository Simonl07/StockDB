import requests
from StockDB import *
from StockDB.spiders.spider import spider


r = requests.get('http://127.0.0.1/list')
stock_lst = r.json().values()
print(Spider)
