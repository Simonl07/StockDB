# Backend Server [![Build Status](https://travis-ci.org/Simonl07/stock-db.svg?branch=master)](https://travis-ci.org/Simonl07/stock-db)

### Resonsibilities:
* Inserting crawler data into database
* Serve fresh crawl task list to crawler
* Serve API data to user request

### Install:
* run ```mvn install``` for installation
* run ```mvn package``` for packaging
* run ```java -cp "target/stock-db-0.0.1-SNAPSHOT.jar:lib/*" src.TestServer -s``` to start server
   
### Test: 
   * run ```mvn test -B``` under StockDB directory
   
# Price Crawler
crawler for lastest price and volume data

### Run

On the crawler machine, clone this repository by running: 
```bash
git clone https://github.com/Simonl07/stock-db.git
```
change directory into stock-db
```bash
cd stock-db
```

setup anaconda and dependencies
```bash
curl https://repo.continuum.io/miniconda/Miniconda3-latest-Linux-x86_64.sh -o miniconda.sh
sh miniconda.sh -b -p $HOME/miniconda
export PATH="$HOME/miniconda/bin:$PATH"
conda env create -f environment.yml -n stock-crawler
. activate stock-crawler
```

 
 
