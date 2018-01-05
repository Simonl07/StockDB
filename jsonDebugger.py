
from bs4 import BeautifulSoup
import requests
import re
from json import loads

response = requests.get("https://finance.yahoo.com/quote/AAPL")

soup = BeautifulSoup(response.text)
script = soup.find("script",text=re.compile("root.App.main")).text
data = loads(re.search("root.App.main\s+=\s+(\{.*\})", script).group(1))
stores = data["context"]["dispatcher"]["stores"]
