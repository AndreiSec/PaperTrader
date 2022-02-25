# Make getting the price of a stock a seperate, modular function so the stock price API provider can be swapped seamlessly. Did this originally because the api I was using (yahoo finance)
# is EXTREMELY slow. As in, a simple stock price query could take 7 seconds.

import time
import yfinance as yf
# import globals
from models import *
from flask import jsonify
import asyncio
from decimal import Decimal


def get_stock_price(client, ticker):
    # stock_price = float(yf.Ticker(ticker).info['currentPrice'])
    stock_price = client.quote(ticker)['c']

    return stock_price


def past_transactions(uid):
    past_transactions = PastTransaction.query.filter_by(
        uid=uid).order_by(PastTransaction.transaction_id.desc()).limit(15)
    output = [i.serialize for i in past_transactions.all()]

    return output


def user_balance(uid):
    user = User.query.filter_by(uid=uid).first()
    balance = float(user.balance)

    return balance


def buy(finnhub_client, uid, ticker, stock_amount):
    price_per_stock = get_stock_price(finnhub_client, ticker)
    total_cash = stock_amount * price_per_stock

    # First, check if user has enough funds to complete this transaction
    user = User.query.filter_by(uid=uid).first()
    # In this case, not enough cash to complete transaction
    if user.balance < total_cash:
        return_message = 'Insufficient funds to complete transaction'
        return {"success": 'false', "message": return_message}
    # In the case that user has enough funds, continue

    # Next, check if user already owns this stock. If not, create an entry into the table
    stock_entry = OwnedStock.query.filter_by(
        uid=uid, ticker=ticker).first()

    if stock_entry is None:  # In the case that we need to create the entry
        new_owned_stock_entry = OwnedStock(
            uid, ticker, stock_amount, price_per_stock, total_cash)
        db.session.add(new_owned_stock_entry)
    else:  # In this case must update existing average price per stock
        old_total_cash = Decimal(stock_entry.total_value)
        new_total_cash = Decimal(total_cash)
        total_units = stock_entry.amount_owned + stock_amount
        new_average_price = (
            old_total_cash + new_total_cash) / Decimal(total_units)
        print("Total amount of units: ", total_units)
        print("Total value of units: ", (old_total_cash + new_total_cash))
        print("New Average Price: ", new_average_price)
        stock_entry.average_price_per_stock = Decimal(new_average_price)
        stock_entry.amount_owned = total_units
        stock_entry.total_value = (old_total_cash + new_total_cash)
        db.session.merge(stock_entry)

        # Next, create a past transaction to store in db
    past_transaction = PastTransaction(
        "buy", uid, ticker, stock_amount, price_per_stock, total_value=total_cash)

    db.session.add(past_transaction)

    # Finally update user cash balance
    user.balance = user.balance - Decimal(total_cash)
    db.session.merge(user)

    # Commit all changes simultaneously
    db.session.commit()
    return_message = "Buy" + " of " + str(stock_amount) + \
        ' ' + ticker + " at $" + str(price_per_stock) + \
        " totaling $" + '{0:.2f}'.format(float(total_cash)) + " successful "
    return {"success": 'true', 'message': return_message}


def sell(finnhub_client, uid, ticker, stock_amount):
    user = User.query.filter_by(uid=uid).first()

    # First, check if this amount exists in the database

    stock_entry = OwnedStock.query.filter_by(
        uid=uid, ticker=ticker).first()

    if stock_entry is None:
        return_message = 'You do not currently own this stock.'
        return {"success": 'false', 'message': return_message}

    if stock_entry.amount_owned < stock_amount:
        return_message = 'You cannot sell more stock than you currently own.'
        return {"success": 'false', 'message': return_message}

    # Needed to determine which database entry to sell; which price entry
    stock_price = get_stock_price(finnhub_client, ticker)

    total_cash = stock_amount * stock_price

    # Next if possible to sell that amount of stock, facilitate the transaction

    value_sold = stock_entry.average_price_per_stock * \
        Decimal(stock_amount)
    stock_entry.amount_owned = stock_entry.amount_owned - stock_amount
    stock_entry.total_value = stock_entry.total_value - value_sold

    if(stock_entry.amount_owned == 0):
        db.session.delete(stock_entry)
        db.session.commit()
    else:
        db.session.merge(stock_entry)

    # First, create a past transaction to store in db
    past_transaction = PastTransaction(
        "sell", uid, ticker, stock_amount, stock_price, total_value=total_cash)

    db.session.add(past_transaction)

    # Finally update user cash balance

    user.balance = user.balance + Decimal(total_cash)

    db.session.merge(user)

    # Commit all changes simultaneously
    db.session.commit()
    return_message = "Sell" + " of " + str(stock_amount) + \
        ' ' + ticker + " at $" + str(stock_price) + \
        " totaling $" + '{0:.2f}'.format(float(total_cash)) + " successful "
    return {"success": 'true', 'message': return_message}


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
