




name_to_status = {}
drop_stocks = []
invalid_stock = []

def reportInvalid(stockName):
    invalid_stock.append(stockName)


def recordStatus(name, status):
    name_to_status[name] = status


def displayStats(stock_lst):
    print("There are ", len([name for name in name_to_status.keys() if name_to_status[name] == 200]), " 200 request")
    print("There are ", len(drop_stocks), " none stocks")
    print(drop_stocks)
    print("Overall success rate: ", (len(stock_lst) - len(drop_stocks))/ len(stock_lst))





def add_None(name):
    drop_stocks.append(name)

def url_generator(stockIDs):
    YAHOO_BASE_URL = 'https://finance.yahoo.com/quote/'
    lst = []
    for s in stockIDs:
        lst.append(YAHOO_BASE_URL + s + "?p=" + s)
    return lst
