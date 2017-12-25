import sys
import time
import random
import json
import subprocess
import requests
import stock_crawler_price
from stock_crawler_price import *



HOST = sys.argv[1]
INTERVAL_MIN = 5

while True:

    response = requests.get('http://' + HOST + '/go')
    j = json.loads(response.text)
    if j['go'] == 1:
        p = subprocess.Popen(['python', 'stock_crawler_price/runner.py'] + [HOST])
        time.sleep(INTERVAL_MIN * 60)
    else:
        print("Waiting go signal from server")
        time.sleep(INTERVAL_MIN * 60)
