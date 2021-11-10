import pandas as pd
from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from models import Stock
import yfinance as yf


# Don't store connection string on github as it includes auth details. Read from file
dbConnectionString = open("dbConnectionString.txt", "r").read()


app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = dbConnectionString


db = SQLAlchemy(app)


def get_all_stock_tickers():
    print("Loading input file...")
    xFile = pd.read_excel('./data/master_data.xlsx')
    print('Done.')
    return xFile


def add_stock_to_stocks_table(ticker, name, exchange, sector, industry, country, currency):
    stock_to_add = Stock(ticker, name, exchange, sector,
                         industry, country, currency)
    db.session.add(stock_to_add)
    return


def add_all_stocks(df):
    for index, row in df.iterrows():
        ticker = row['Ticker']
        name = row['Name']
        exchange = row['Exchange']
        try:
            stock_obj = yf.Ticker(ticker)
            info = stock_obj.info

            industry = info['industry']
            currency = info['financialCurrency']
            country = info['country']
            sector = info['sector']

            # Add stock to database
            print("Adding stock: " + ticker)
            add_stock_to_stocks_table(
                ticker, name, exchange, sector, industry, country, currency)
            db.session.commit()
        except Exception as e:
            # Ticker not able to be processed by yFinance API
            # Will be useless to us, so continue to next
            print(e)
            continue


df = get_all_stock_tickers()

add_all_stocks(df)
