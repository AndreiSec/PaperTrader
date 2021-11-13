from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy
import yfinance as yf
from models import *
import globals
from decimal import Decimal

# Create database and app sessions in global module
globals.initialize()
db = globals.db
app = globals.app

# Returns JSON of a specific stocks info
# in the following format:
#
# {
# "ticker" : ticker
# "company_desc" : long_desc
# "bid"
#
# }
#


@app.route('/api/stocks/stock_info/<ticker>')
def stock_info(ticker):
    try:
        stock = Stock.query.filter_by(ticker=ticker)
        output = stock.first().serialize

        info = yf.Ticker(ticker).info

        output['bid'] = info['bid']
        output['ask'] = info['ask']
        output['recommendationKey'] = info['recommendationKey']
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
        return jsonify({'success': 'true', 'stocks': output})

    except Exception as e:
        return {"success": 'false', "message": str(e)}


# Method to let a user purchase a certain amount of stock
# All the necessary data is contained in the request form as it is a POST
@app.route('/api/stocks/stocktransaction/buy', methods=['POST'])
def perform_transaction_buy():
    try:
        data = request.get_json()
        type = "buy"
        uid = data['uid']
        ticker = data['ticker']
        stock_amount = int(data['stock_amount'])

        price_per_stock = float(yf.Ticker(ticker).info['currentPrice'])
        total_cash = stock_amount * price_per_stock

        # First, check if user has enough funds to complete this transaction
        user = User.query.filter_by(uid=uid).first()
        # In this case, not enough cash to complete transaction
        if user.balance < total_cash:
            return_message = 'Insufficient funds to complete transaction'
            return {"success": 'false', "message": return_message}
        # In the case that user has enough funds, continue

        # First, create a past transaction to store in db
        past_transaction = PastTransaction(
            type, uid, ticker, stock_amount, price_per_stock, total_cash)

        db.session.add(past_transaction)

        # Next update users stocks owned
        new_owned_stock_entry = OwnedStock(
            uid, ticker, stock_amount, price_per_stock, total_cash)
        db.session.add(new_owned_stock_entry)

        # Finally update user cash balance
        user.balance = user.balance - Decimal(total_cash)
        db.session.merge(user)

        # Commit all changes simultaneously
        db.session.commit()
        return_message = type + " of " + str(stock_amount) + \
            ' ' + ticker + "at $" + str(price_per_stock) + \
            " totaling $" + str(total_cash) + " successful "
        return {"success": 'true', 'message': return_message}
    except Exception as e:
        return {"success": 'false', "message": str(e)}


# Method to create a user row in the database
# All the necessary data is contained in the request form as it is a POST
@app.route('/api/account/createuser', methods=['POST'])
def create_user():
    try:
        data = request.get_json()
        uid = data['uid']
        username = data['username']
        email = data['email']

        user_to_add = User(uid, username, email)
        db.session.add(user_to_add)
        db.session.commit()
        return {"success": 'true', "message": 'User ' + uid + ' successfully added'}
    except Exception as e:
        return {"success": 'false', "message": str(e)}


# Returns basic database information of all stocks
@app.route('/api/stocks/get_all_stock_info')
def get_all_stock_info():
    try:
        all_stocks = Stock.query
        output = [i.serialize for i in all_stocks.all()]
        return jsonify({'success': 'true', 'stocks': output})
    except Exception as e:
        return {"success": 'false', "message": str(e)}


if __name__ == "__main__":

    app.run()
