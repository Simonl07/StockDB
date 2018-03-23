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





class Clean_name(object): 
	def process_item(self, item, spider):
        if item['name_short'] == 'N/A':
            item['name_short'] = 0;
        if item['name_long'] == 'N/A':
            item['name_long'] = 0;
        return item

class Clean_price(object):
	def process_item(self, item, spider):
        if item['price_close'] < 0 || item['price_close'] == null:
        	item['price_close'] == 0;
        if item['price_open'] < 0 || item['price_open'] == null:
        	item['price_open'] == 0;
        return item
class Clean_range_day_low(object):

    def process_item(self, item, spider):
        if item['range_day_low'] == 'N/A':
            item['range_day_low'] = 0;
        return item


class Clean_beta(object):
	def process_item(self, item, spider):
        if item['beta'] == 'N/A' || item['beta'] < 0:
            item['beta'] = 0;
        return  item


class Clean_range_day_high(object):

    def process_item(self, item, spider):
        if item['range_day_low'] == 'N/A':
            item['range_day_low'] = 0;
        return  item
class CLean_range_52w_high(object):

    def process_item(self, item, spider):
        if item['range_52w_high'] == 'N/A' || item['range_52w_high'] < 0:
            item['range_52w_high'] = 0;
        return item

class CLean_range_52w_low(object):

    def process_item(self, item, spider):
        if item['range_52w_low'] == 'N/A' || item['range_52w_low'] < 0:
            item['range_52w_low'] = 0;
        return item

class CLean_volume(object):

    def process_item(self, item, spider):
        if item['volume'] == 'N/A' || item['volume'] < 0:
            item['volume'] = 0;
        return item

class CLean_volume(object):

    def process_item(self, item, spider):
        if item['volume'] == 'N/A' || item['volume'] < 0:
            item['volume'] = 0;
        return item

class CLean_volume_avg(object):

    def process_item(self, item, spider):
        if item['volume_avg'] == 'N/A' || item['volume_avg'] < 0:
            item['volume_avg'] = 0;
        return item

class CLean_market_cap(object):

    def process_item(self, item, spider):
        if item['market_cap'] == 'N/A' || item['market_cap'] < 0:
            item['market_cap'] = 0;
        return item
class CLean_beta(object):

    def process_item(self, item, spider):
        if item['beta'] == 'N/A' || item['beta'] < 0:
            item['beta'] = 0;
        return item
class CLean_pe_ratio(object):

    def process_item(self, item, spider):
        if item['pe_ratio'] == 'N/A' || item['pe_ratio'] < 0:
            item['pe_ratio'] = 0;
        return item

class CLean_eps(object):

    def process_item(self, item, spider):
        if item['eps'] == 'N/A' || item['eps'] < 0:
            item['eps'] = 0;
        return item

class Clean_dividend(object):
	def process_item(self, item, spider):
        if item['dividend'] == 'N/A' || item['dividend'] < 0:
            item['dividend'] = 0;
        return item



class CLean_target_est_1Y(object):

    def process_item(self, item, spider):
        if item['target_est_1Y'] == 'N/A' || item['target_est_1Y'] < 0:
            item['target_est_1Y'] = 0;
        return item



class CLean_earnings_date_begin(object):

    def process_item(self, item, spider):
        if item['earnings_date_begin'] == 'N/A':
            item['earnings_date_begin'] = '1900-01-01';
        return item
class CLean_earnings_date_end(object):

    def process_item(self, item, spider):
        if item['earnings_date_end'] == 'N/A':
            item['earnings_date_end'] = '1900-01-01';
        return item

class CLean_dividend_yield(object):

    def process_item(self, item, spider):
        if item['dividend_yield'] == 'N/A':
            item['dividend_yield'] = 0;
        return item

class CLean_ex_dividend_date(object):

    def process_item(self, item, spider):
        if item['ex_dividend_date'] == 'N/A':
            item['ex_dividend_date'] = 0;
        return item




class RequestDB(object):
    # This funtion process the Stock data and compile a request to data base.
    def process_item(self, item, spider):
        final_keys = [
            'name_full',
            'name_short',
            'price_close',
            'price_open',
            'range_day',
            'range_day_low',
            'range_day_high',
            'range_52w',
            'range_52w_high',
            'range_52w_low',
            'volume',
            'volume_avg',
            'market_cap',
            'beta',
            'pe_ratio',
            'eps',
            'earnings_date',
            'earnings_date_begin',
            'earnings_date_end',
            'dividend',
            'dividend_yield',
            'ex_dividend_date',
            'target_est_1Y'
        ]
        headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
        payload = {}
        for k in final_keys:
            payload[k] = item[k]

        print(payload)
        r = requests.post('http://127.0.0.1/summary', headers=headers, json=payload)

