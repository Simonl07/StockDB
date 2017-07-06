# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html
import requests

class Stock(object):


    # This funtion process the Stock data and compile a request to data base.
    def process_item(self, item, spider):
        print('item processed')
