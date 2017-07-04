# -*- coding:UTF-8 -*-
import scrapy
import hashlib
import re
from pyCrawler.items import MovieItem
from pyCrawler.mysql_connect.mysql_connect import MySQLConn


class Spider(scrapy.Spider):

    name = 'spider'
    allowed_domains = ['finance.yahoo.com']


    def parse(self, response):
