import sys
import time
import random
import json
import subprocess
import requests
import crawler_stock_price
from crawler_stock_price import *



HOST = sys.argv[1]
INTERVAL_MIN = 5

while True:

    response = requests.get('http://' + HOST + '/go')
    j = json.loads(response.text)
    if j['go'] == 1:
        p = subprocess.Popen(['python', 'crawler_stock_price/runner.py'] + [HOST])
        time.sleep(5)
    else:
        time.sleep(5)
