# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class StockProfile(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    name_full_long = scrapy.Field()
    name_full_short = scrapy.Field()
    name_full = scrapy.Field()
    symbol = scrapy.Field()
    address = scrapy.Field()
    zipcode = scrapy.Field()
    city = scrapy.Field()
    country = scrapy.Field()
    description = scrapy.Field()
    employee = scrapy.Field()
    industry = scrapy.Field()
    sector = scrapy.Field()
    state = scrapy.Field()
    website = scrapy.Field()
