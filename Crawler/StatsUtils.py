

import requests


name_to_status = {}
drop_stocks = []
invalid_stock = []

def reportInvalid(stockName):
    invalid_stock.append(stockName)


def recordStatus(name, status):
    name_to_status[name] = status


def displayStats(stock_lst):
    print("There are ", len([name for name in name_to_status.keys() if name_to_status[name] == 200]), " HTTP200 request")
    print("There are ", len(drop_stocks), " dropped stocks")
    print("There are ", len(invalid_stock), " invalid stock")
    print(drop_stocks)
    print("Overall success rate: ", (len(stock_lst) - len(drop_stocks) - len(invalid_stock))/ len(stock_lst))


def updateServer():
    headers = {'charset': 'UTF-8', 'Content-Type': 'text/plain', 'Content-Encoding': 'utf-8', 'Accept-Encoding': 'utf-8'}
    for stock in invalid_stock:
        r = requests.post("http://127.0.0.1/delete", headers=headers, json={"stock" : stock})

    print(len(invalid_stock), " invalid stocks removed from the server.")
    print(invalid_stock)
    invalid_stock.clear()
    print(invalid_stock)





def dropStock(name):
    drop_stocks.append(name)

def url_generator(stockIDs):
    YAHOO_BASE_URL = 'https://finance.yahoo.com/quote/'
    lst = []
    for s in stockIDs:
        lst.append(YAHOO_BASE_URL + s + "?p=" + s)
    return lst
