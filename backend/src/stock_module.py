# Make getting the price of a stock a seperate, modular function so the stock price API provider can be swapped seamlessly. Did this originally because the api I was using (yahoo finance)
# is EXTREMELY slow. As in, a simple stock price query could take 7 seconds.

import time
import yfinance as yf
# import globals
from models import *
from flask import jsonify
import asyncio


def get_stock_price(client, ticker):
    # stock_price = float(yf.Ticker(ticker).info['currentPrice'])
    stock_price = client.quote(ticker)['c']

    return stock_price


def past_transactions(uid):
    past_transactions = PastTransaction.query.filter_by(uid=uid).limit(15)
    output = [i.serialize for i in past_transactions.all()]
    print("OUTPUT: ", output)

    return output


def get_stock_info_by_ticker(ticker):
    try:
        ticker = ticker.upper()
        info = yf.Ticker(ticker).info
        stock = Stock.query.filter_by(ticker=ticker)
        output = stock.first().serialize

        output['recommendationKey'] = info['recommendationKey']
        output['bid'] = info['bid']
        output['ask'] = info['ask']
        output['open'] = info['open']
        output['high'] = info['dayHigh']
        output['low'] = info['dayLow']
        output['mkt_cap'] = info['marketCap']
        output['price_earnings'] = info['bid']
        output['52w_high'] = info['fiftyTwoWeekHigh']
        output['52w_low'] = info['fiftyTwoWeekLow']
        output['volume'] = info['volume']
        output['avg_volume'] = info['averageVolume']
        output['div_yield'] = info['dividendYield']

        print("OUTPUT: ", output)
        return output

    except Exception as e:
        return {"success": False, "message": str(e)}


def get_owned_stocks_by_uid(finnhub_client, uid):
    try:
        user = User.query.filter_by(uid=uid).first()
        if user is None:
            return_message = 'User does not exist'
            return {"success": False, "message": return_message}

        user_owned_stocks = OwnedStock.query.filter_by(
            uid=uid)

        output = [i.serialize for i in user_owned_stocks.all()]

        # Add stock prices to output
        for index, stock in enumerate(output):
            name = Stock.query.filter_by(
                ticker=stock['ticker']).first().serialize['name']
            price = get_stock_price(finnhub_client, stock['ticker'])
            output[index]['average_price'] = round(
                float(output[index]['average_price']), 2)
            output[index]['total_value'] = round(
                float(output[index]['total_value']), 2)
            output[index]['name'] = name
            output[index]['price'] = float(price)
            output[index]['current_value'] = float(price *
                                                   output[index]['amount_owned'])
        print("Output: ", output)
        return output

    except Exception as e:
        print(e)
        return {"success": False, "message": str(e)}


def get_owned_stock_by_uid_ticker(finnhub_client, uid, ticker):
    try:
        ticker = ticker.upper()
        user = User.query.filter_by(uid=uid).first()
        if user is None:
            return_message = 'User does not exist'
            return {"success": False, "message": return_message}

        user_owned_stocks = OwnedStock.query.filter_by(
            uid=uid, ticker=ticker)

        output = [i.serialize for i in user_owned_stocks.all()]

        # Add stock prices to output
        for index, stock in enumerate(output):
            name = Stock.query.filter_by(
                ticker=stock['ticker']).first().serialize['name']
            price = get_stock_price(finnhub_client, stock['ticker'])
            output[index]['average_price'] = round(
                float(output[index]['average_price']), 2)
            output[index]['total_value'] = round(
                float(output[index]['total_value']), 2)
            output[index]['name'] = name
            output[index]['price'] = float(price)
            output[index]['current_value'] = float(price *
                                                   output[index]['amount_owned'])
        print("Output: ", output)
        return {"owned_stock": output[0], "success": True}

    except Exception as e:
        print(e)
        return {"success": False, "message": str(e)}
# start_time = time.time()
# globals.initialize()
# finnhub_client = globals.finnhub_client


# print(get_stock_info(finnhub_client, 'AAPL'))
# print("--- %s seconds ---" % (time.time() - start_time))
