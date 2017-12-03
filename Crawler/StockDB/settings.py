# -*- coding: utf-8 -*-

# Scrapy settings for StockDB project
#
# For simplicity, this file contains only settings considered important or
# commonly used. You can find more settings consulting the documentation:
#
#     http://doc.scrapy.org/en/latest/topics/settings.html
#     http://scrapy.readthedocs.org/en/latest/topics/downloader-middleware.html
#     http://scrapy.readthedocs.org/en/latest/topics/spider-middleware.html

BOT_NAME = 'StockDB'

SPIDER_MODULES = ['StockDB.spiders']
NEWSPIDER_MODULE = 'StockDB.spiders'
HTTPERROR_ALLOWED_CODES = [404]

# Crawl responsibly by identifying yourself (and your website) on the user-agent
USER_AGENT ='Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:41.0) Gecko/20100101 Firefox/41.0'
# Obey robots.txt rules
ROBOTSTXT_OBEY = False

# Configure maximum concurrent requests performed by Scrapy (default: 16)
#CONCURRENT_REQUESTS = 32

# Configure a delay for requests for the same website (default: 0)
# See http://scrapy.readthedocs.org/en/latest/topics/settings.html#download-delay
# See also autothrottle settings and docs
DOWNLOAD_DELAY = 0
# The download delay setting will honor only one of:
CONCURRENT_REQUESTS_PER_DOMAIN = 8

# Disable cookies (enabled by default)
COOKIES_ENABLED =True

# Disable Telnet Console (enabled by default)
#TELNETCONSOLE_ENABLED = False

# Override the default request headers:
DEFAULT_REQUEST_HEADERS = {
   'Accept': 'text/html, */*; q=0.01',
   'Accept-Language': 'zh-CN,zh;q=0.8',
   'Accept-Encoding': 'gzip, deflate, sdch',
   # 'Connection': 'keep-alive',
   # 'Referer': 'http://su.lianjia.com/xiaoqu/',
   # 'Upgrade-Insecure-Requests':1,
}

# Enable or disable spider middlewares
# See http://scrapy.readthedocs.org/en/latest/topics/spider-middleware.html
# SPIDER_MIDDLEWARES = {
#    'Middlewares_agent': 200,
# }

# Enable or disable downloader middlewares
# See http://scrapy.readthedocs.org/en/latest/topics/downloader-middleware.html
DOWNLOADER_MIDDLEWARES = {
   #'StockDB.middlewares.Middlewares_agent': 200,
   'scrapy.downloadermiddlewares.httpcompression.HttpCompressionMiddleware': 250
}

# Enable or disable extensions
# See http://scrapy.readthedocs.org/en/latest/topics/extensions.html
#EXTENSIONS = {
#    'scrapy.extensions.telnet.TelnetConsole': None,
#}

# Configure item pipelines
# See http://scrapy.readthedocs.org/en/latest/topics/item-pipeline.html
ITEM_PIPELINES = {
   'StockDB.pipelines.Clean_name': 287,
   'StockDB.pipelines.Clean_range_day': 288,
   'StockDB.pipelines.Clean_range_52w': 289,
   'StockDB.pipelines.Clean_market_cap': 290,
   'StockDB.pipelines.Clean_earnings_date': 291,
   'StockDB.pipelines.Clean_dividend': 292,
   'StockDB.pipelines.Clean_ex_dividend_date': 293,
   'StockDB.pipelines.Clean_target_est_1Y': 294,
   'StockDB.pipelines.Clean_eps': 295,
   'StockDB.pipelines.Clean_pe_ratio': 296,
   'StockDB.pipelines.Clean_volume': 297,
   'StockDB.pipelines.Clean_volume_avg': 298,
   'StockDB.pipelines.Clean_beta': 299,
   'StockDB.pipelines.RequestDB': 300,
}

# Enable and configure the AutoThrottle extension (disabled by default)
# See http://doc.scrapy.org/en/latest/topics/autothrottle.html
AUTOTHROTTLE_ENABLED = True
# The initial download delay
AUTOTHROTTLE_START_DELAY = 0
# The maximum download delay to be set in case of high latencies
#AUTOTHROTTLE_MAX_DELAY = 0.1
# The average number of requests Scrapy should be sending in parallel to
# each remote server
#AUTOTHROTTLE_TARGET_CONCURRENCY = 5
# Enable showing throttling stats for every response received:
#AUTOTHROTTLE_DEBUG = False

# Enable and configure HTTP caching (disabled by default)
# See http://scrapy.readthedocs.org/en/latest/topics/downloader-middleware.html#httpcache-middleware-settings
#HTTPCACHE_ENABLED = True
#HTTPCACHE_EXPIRATION_SECS = 0
#HTTPCACHE_DIR = 'httpcache'
#HTTPCACHE_IGNORE_HTTP_CODES = []
#HTTPCACHE_STORAGE = 'scrapy.extensions.httpcache.FilesystemCacheStorage'
