
from bs4 import BeautifulSoup
import requests
import re
import json
from json import loads

response = requests.get("https://finance.yahoo.com/quote/AAPL")

soup = BeautifulSoup(response.text, "lxml")
script = soup.find("script",text=re.compile("root.App.main")).text
data = loads(re.search("root.App.main\s+=\s+(\{.*\})", script).group(1))
print(json.dumps(data, sort_keys=False, indent=4))
