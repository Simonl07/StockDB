import sys
import requests

requests.get('http://' + sys.argv[1] + '/go?restore=True')
