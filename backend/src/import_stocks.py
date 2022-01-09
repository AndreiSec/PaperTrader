import pandas as pd
from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from models import Stock
import yfinance as yf
import globals


globals.initialize()

db = globals.db


def get_all_stock_tickers():
    print("Loading input file...")
    xFile = pd.read_excel('./data/master_data.xlsx')
    print('Done.')
    return xFile


def add_stock_to_stocks_table(ticker, name, exchange, sector, industry, country, currency, logo_link):
    stock_to_add = Stock(ticker, name, exchange, sector,
                         industry, country, currency, logo_link)
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
            logo_link = info['logo_url']

            # Add stock to database
            print("Adding stock: " + ticker)
            add_stock_to_stocks_table(
                ticker, name, exchange, sector, industry, country, currency, logo_link)
            db.session.commit()
        except Exception as e:
            # Ticker not able to be processed by yFinance API
            # Will be useless to us, so continue to next
            print(e)
            continue


df = get_all_stock_tickers()

add_all_stocks(df)
