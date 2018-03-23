# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html
import requests
import re
from crawler import *
import StatsUtils
from StatsUtils import *
from scrapy.exceptions import DropItem



class Clean_symbol(object):
	def process_item(self, item, spider):
		if item['symbol'] == 'N/A':
			raise DropItem("Illegal format %s" % item)
		return item

class Clean_name(object):
	def process_item(self, item, spider):
		if	item['name_short'] == 'N/A':
			item['name_short'] = 0
		if item['name_long'] == 'N/A':
			item['name_long'] = 0
		return item

class Clean_price(object):
	def process_item(self, item, spider):
		if item['price_close'] < 0 or item['price_close'] == None:
			item['price_close'] == 0
		if item['price_open'] < 0 or item['price_open'] == None:
			item['price_open'] == 0
		return item
class Clean_range_day_low(object):
	def process_item(self, item, spider):
		if item['range_day_low'] == 'N/A':
			item['range_day_low'] = 0
		return item


class Clean_beta(object):
	def process_item(self, item, spider):
		if item['beta'] == 'N/A' or item['beta'] < 0:
			item['beta'] = 0
		return item


class Clean_range_day_high(object):

    def process_item(self, item, spider):
        if item['range_day_low'] == 'N/A':
            item['range_day_low'] = 0
        return  item
class CLean_range_52w_high(object):

    def process_item(self, item, spider):
        if item['range_52w_high'] == 'N/A' or item['range_52w_high'] < 0:
            item['range_52w_high'] = 0
        return item

class CLean_range_52w_low(object):

    def process_item(self, item, spider):
        if item['range_52w_low'] == 'N/A' or item['range_52w_low'] < 0:
            item['range_52w_low'] = 0
        return item

class CLean_volume(object):

    def process_item(self, item, spider):
        if item['volume'] == 'N/A' or item['volume'] < 0:
            item['volume'] = 0
        return item

class CLean_volume(object):

    def process_item(self, item, spider):
        if item['volume'] == 'N/A' or item['volume'] < 0:
            item['volume'] = 0
        return item

class CLean_volume_avg(object):

    def process_item(self, item, spider):
        if item['volume_avg'] == 'N/A' or item['volume_avg'] < 0:
            item['volume_avg'] = 0
        return item

class CLean_market_cap(object):

    def process_item(self, item, spider):
        if item['market_cap'] == 'N/A' or item['market_cap'] < 0:
            item['market_cap'] = 0
        return item
class CLean_beta(object):

    def process_item(self, item, spider):
        if item['beta'] == 'N/A' or item['beta'] < 0:
            item['beta'] = 0
        return item
class CLean_pe_ratio(object):

    def process_item(self, item, spider):
        if item['pe_ratio'] == 'N/A' or item['pe_ratio'] < 0:
            item['pe_ratio'] = 0
        return item

class CLean_eps(object):

    def process_item(self, item, spider):
        if item['eps'] == 'N/A' or item['eps'] < 0:
            item['eps'] = 0
        return item

class Clean_dividend(object):
	def process_item(self, item, spider):
		if item['dividend'] == 'N/A' or item['dividend'] < 0:
			item['dividend'] = 0
		return item



class CLean_target_est_1Y(object):

    def process_item(self, item, spider):
        if item['target_est_1Y'] == 'N/A' or item['target_est_1Y'] < 0:
            item['target_est_1Y'] = 0
        return item



class CLean_earnings_date_begin(object):

    def process_item(self, item, spider):
        if item['earnings_date_begin'] == 'N/A':
            item['earnings_date_begin'] = '1900-01-01'
        return item
class CLean_earnings_date_end(object):

    def process_item(self, item, spider):
        if item['earnings_date_end'] == 'N/A':
            item['earnings_date_end'] = '1900-01-01'
        return item

class CLean_dividend_yield(object):

    def process_item(self, item, spider):
        if item['dividend_yield'] == 'N/A':
            item['dividend_yield'] = 0
        return item

class CLean_ex_dividend_date(object):

    def process_item(self, item, spider):
        if item['ex_dividend_date'] == 'N/A':
            item['ex_dividend_date'] = 0
        return item




class RequestDB(object):
    # This funtion process the Stock data and compile a request to data base.
    def process_item(self, item, spider):
        headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
        payload = {'stock_id': spider.symbol2id[item['symbol']]}
        for k in item:
            payload[k] = item[k]

        print(payload)
        r = requests.post(spider.HOST + '/record', headers=headers, json=payload)
