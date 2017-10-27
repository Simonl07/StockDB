


name_to_status = {}

def reportInvalid(stockName):
    pass


def recordStatus(name, status):
    name_to_status[name] = status


def displayStats():
    for name in name_to_status.keys():
        print(name, ":", name_to_status[name])
