# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy



class Stock(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()


    '''Summary Page'''
    name_full = scrapy.Field()
    name_short = scrapy.Field()
    price_close = scrapy.Field()
    price_open = scrapy.Field()
    range_day = scrapy.Field()
    range_day_high = scrapy.Field()
    range_day_low = scrapy.Field()
    range_52w = scrapy.Field()
    range_52w_high = scrapy.Field()
    range_52w_low = scrapy.Field()
    volume = scrapy.Field()
    volume_avg = scrapy.Field()
    market_cap = scrapy.Field()
    beta = scrapy.Field()
    pe_ratio = scrapy.Field()
    eps = scrapy.Field()
    earnings_date = scrapy.Field()
    dividend = scrapy.Field()
    dividend_yield = scrapy.Field()
    ex_dividend_date = scrapy.Field()
    target_est_1Y = scrapy.Field()

    '''Statistics'''


    '''Analysis Page'''
