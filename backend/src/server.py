from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy
import yfinance as yf
from models import *
import globals

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

    return jsonify({'stocks': output})


#         self.type = type
#         self.uid = uid
#         self.ticker = ticker
#         self.stock_amount = stock_amount
#         self.price_per_stock = price_per_stock
#         self.total_cash = total_cash


# Method to let a user purchase a certain amount of stock
# All the necessary data is contained in the request form as it is a POST
@app.route('/api/stocks/stocktransaction', methods=['POST'])
def perform_transaction():
    data = request.form
    type = data['type']

    uid = data['uid']
    ticker = data['ticker']
    stock_amount = data['stock_amount']
    price_per_stock = data['price_per_stock']
    total_cash = data['total_cash']

    return None


# Method to create a user row in the database
# All the necessary data is contained in the request form as it is a POST
@app.route('/api/account/createuser', methods=['POST'])
def create_user():
    data = request.get_json()
    uid = data['uid']
    username = data['username']
    email = data['email']

    user_to_add = User(uid, username, email)
    try:
        db.session.add(user_to_add)
        db.session.commit()
        return 'User ' + uid + ' successfully added'
    except Exception as e:
        return 'Error: ' + str(e)


# Returns basic database information of all stocks
@app.route('/api/stocks/get_all_stock_info')
def get_all_stock_info():
    all_stocks = Stock.query
    output = [i.serialize for i in all_stocks.all()]
    return jsonify({'stocks': output})


if __name__ == "__main__":

    app.run()
