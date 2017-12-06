
# -*- coding: utf-8 -*-

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


def formatDate(date):
    if date == "N/A":
        return "1900-01-01", "1900-01-01"
    if '-' in date:
        first = re.search('.*(?= -) ', date).group().strip()
        second = re.search('(?<=- ).*', date).group().strip()
        return formatHelper1(first), formatHelper1(second)
    return formatHelper1(date), formatHelper1(date)


def formatHelper1(date):
    print("DATE: ", date)
    year = re.search('\d{4}', date).group()
    day = re.search('\d+(?=,)', date).group()
    month = re.search('[a-zA-Z]+', date).group()
    return formatHelper2(year, month, day)

def formatHelper2(year, month, day):
    months = {
    'Jan': '01',
    'Feb': '02',
    'Mar': '03',
    'Apr': '04',
    'May': '05',
    'Jun': '06',
    'Jul': '07',
    'Aug': '08',
    'Sep': '09',
    'Oct': '10',
    'Nov': '11',
    'Dec': '12',
    }
    return year + "-" + months[month] + "-" + day





class Clean_name(object):

    def process_item(self, item, spider):
        if '-' in item['name_string']:
            item['name_full'] = re.search('(?<=- ).+?$', item['name_string']).group()
            item['name_short'] = re.search('.+?(?= \-)', item['name_string']).group()
        else:
            item['name_full'] = re.search('.+?(?=\()', item['name_string']).group()
            item['name_short'] = re.search('(?<=\().+?(?=\))', item['name_string']).group()
        return item

class Clean_range_day(object):

    def process_item(self, item, spider):
        if not item['range_day'] or item['range_day'] == 'N/A':
            dropStock(item['name_short'])
            raise DropItem("Illegal format %s" % item)
        item['range_day_low'] = re.search('[\d.]+?(?=\s)', item['range_day']).group()
        item['range_day_high'] = re.search('(?<=\s)[\d.]+', item['range_day']).group()
        return item

class Clean_range_52w(object):

    def process_item(self, item, spider):

        if(item['range_52w'] == None or item['range_52w'] == 'N/A'):
            dropStock(item['name_short'])
            raise DropItem("Illegal format %s" % item)
        item['range_52w_low'] = re.search('[\d.]+?(?=\s)', item['range_52w']).group()
        item['range_52w_high'] = re.search('(?<=\s)[\d.]+', item['range_52w']).group()
        return item


class Clean_market_cap(object):

    def convertValue(capital):
        value = 0
        if (capital.endswith("B")):
            value = int(float(capital[:-1])*1000000000)
        else:
            value = int(float(capital[:-1])*100000)
        return value

    def process_item(self, item, spider):
        if item['market_cap'] == "N/A":
            item['market_cap'] = "-1"
        else:
            item['market_cap'] = str(Clean_market_cap.convertValue(item['market_cap']))

        return item



class Clean_earnings_date(object):

    def process_item(self, item, spider):
        final = ""
        if item['earnings_date_begin'] is None and item['earnings_date_end'] is None:
            final = "N/A"
        elif item['earnings_date_begin'] is None:
            final = item['earnings_date_end']
        elif item['earnings_date_end'] is None:
            final = item['earnings_date_begin']
        else:
            final = item['earnings_date_begin'] + " - " + item['earnings_date_end']

        item['earnings_date'] = final
        if "%" in item['earnings_date']:
            dropStock(item['name_short'])
            raise DropItem("Corrupted earnings_date %s" % item)

        item['earnings_date_begin'], item['earnings_date_end'] = formatDate(item['earnings_date'])
        return item

class Clean_dividend(object):

    def process_item(self, item, spider):
        if 'N/A' in item['dividend_String']:
            item['dividend'] = '-1'
            item['dividend_yield'] = '-1'
        else:
            item['dividend'] = re.search('.*(?=\s)', item['dividend_String']).group()
            item['dividend_yield'] = str(round(float(str(re.search('(?<=\().*(?=\%)', item['dividend_String']).group())) / 100, 4))

        return item


class Clean_ex_dividend_date(object):
    def process_item(self, item, spider):
        if item['ex_dividend_date']  == "N/A":
            item['ex_dividend_date']  = "1900-01-01"
        return item

class Clean_target_est_1Y(object):
    def process_item(self, item, spider):
        if item['target_est_1Y'] == None:
            item['target_est_1Y'] = "-1"

        item['target_est_1Y'].replace(",", "")
        if item['target_est_1Y'] == "N/A":
            item['target_est_1Y'] = "-1"
        return item

class Clean_eps(object):
    def process_item(self, item, spider):
        if item['eps'] == "N/A":
            item['eps'] = "-1"
        return item

class Clean_pe_ratio(object):
    def process_item(self, item, spider):
        if item['pe_ratio'] == None:
            item['pe_ratio'] = "-1"

        item['pe_ratio'] = item['pe_ratio'].replace(",", "")
        if item["pe_ratio"] == "N/A":
            item["pe_ratio"] = "-1"
        return item

class Clean_volume(object):
    def process_item(self, item, spider):
        if item['volume'] == None:
            item['volume'] = "-1"

        item['volume'] = item['volume'].replace(",", "")

        if item["volume"] == "N/A":
            item["volume"] = "-1"
        return item

class Clean_volume_avg(object):
    def process_item(self, item, spider):
        if item['volume_avg'] == None:
            item['volume_avg'] = "-1"

        item['volume_avg'] = item['volume_avg'].replace(",", "")

        if item["volume_avg"] == "N/A":
            item["volume_avg"] = "-1"
        return item

class Clean_beta(object):
    def process_item(self, item, spider):
        if item['beta'] == "N/A":
            item['beta'] = "-1"
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
        r = requests.post('http://127.0.0.1', headers=headers, json=payload)
