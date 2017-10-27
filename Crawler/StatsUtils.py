


name_to_status = {}

def reportInvalid(stockName):
    pass



def recordStatus(name, status):
    name_to_status[name] = status


def displayStats():
    for name in name_to_status.keys():
        print(name, ":", name_to_status[name])

def url_generator(stockIDs):
    YAHOO_BASE_URL = 'https://finance.yahoo.com/quote/'
    lst = []
    for s in stockIDs:
        lst.append(YAHOO_BASE_URL + s + "?p=" + s)
    return lst
