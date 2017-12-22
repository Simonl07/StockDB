# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html

import requests
import re
from crawler import *
from scrapy.exceptions import DropItem

s = requests.Session()

class Clean_name(object):
    def process_item(self, item, spider):
        if item['name_full_long'] is None and item['name_full_short'] is not None:
            item['name_full'] = item['name_full_short']
        else:
            item['name_full'] = item['name_full_long']

        return item

class Clean_price(object):
    def process_item(self, item, spider):

        if "+" in item['price']:
            raise DropItem("Illegal format %s" % item)

        if '(' in item['price'] and ')' in item['price']:
            item['price'] = re.search('.+?(?= \()', item['price']).group()

        return item



class Clean_volume(object):
    def process_item(self, item, spider):
        if item['volume'] == None:
            item['volume'] = "-1"

        item['volume'] = item['volume'].replace(",", "")

        if item["volume"] == "N/A":
            item["volume"] = "-1"
        return item


class RequestDB(object):
    # This funtion process the Stock data and compile a request to data base.
    def process_item(self, item, spider):
        headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Connection': 'keep-alive', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
        payload = {'crawl_task_id': spider.crawl_id}
        for k in item.keys():
            payload[k] = item[k]


        s.post(spider.HOST + '/live', headers=headers, json=payload)
