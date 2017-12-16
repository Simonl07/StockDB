# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html

import requests
import re
from crawler import *
from scrapy.exceptions import DropItem


class Clean_name(object):
    def process_item(self, item, spider):
        if '-' in item['name_string']:
            item['name_full'] = re.search('(?<=- ).+?$', item['name_string']).group()
            item['name_short'] = re.search('.+?(?= \-)', item['name_string']).group()
        else:
            item['name_full'] = re.search('.+?(?=\()', item['name_string']).group()
            item['name_short'] = re.search('(?<=\().+?(?=\))', item['name_string']).group()
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
        headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
        payload = {'crawl_task_id': spider.crawl_id}
        for k in item.keys():
            payload[k] = item[k]


        print("Sending:....\n", payload)
        r = requests.post('http://127.0.0.1/live', headers=headers, json=payload)
