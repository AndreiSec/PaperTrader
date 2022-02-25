from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy
import yfinance as yf
from models import *
import globals
from decimal import Decimal
from stock_module import *
import asyncio

# Create database and app sessions in global module
db = globals.db
app = globals.app
finnhub_client = globals.finnhub_client

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


def cleanup():
    print("Cleaning up...")
    db.session.remove()
    db.engine.dispose()


@app.route('/api/account/stocks/get_owned_stocks/<uid>', methods=['GET'])
def get_owned_stocks(uid):
    try:
        return jsonify({"stocks_owned": get_owned_stocks_by_uid(finnhub_client, uid)})
    except Exception as e:
        return {"success": 'false', "message": str(e)}
    finally:
        cleanup()


@app.route('/api/account/get_user_balance/<uid>', methods=['GET'])
def get_user_balance(uid):
    try:
        return jsonify({"user_balance": user_balance(uid)})
    except Exception as e:
        return {"success": 'false', "message": str(e)}
    finally:
        cleanup()


@app.route('/api/stocks/stock_info/<ticker>/<uid>', methods=['GET'])
def stock_info(ticker, uid):
    try:
        return jsonify({'stock_info': get_stock_info_by_ticker(ticker),
                        "owned_stock_info": get_owned_stock_by_uid_ticker(
            finnhub_client, uid, ticker)})

    except Exception as e:
        return {"success": 'false', "message": str(e)}
    finally:
        cleanup()


@app.route('/api/stocks/stocktransaction', methods=['POST'])
def perform_transaction():
    try:
        data = request.get_json()
        type = data['type']
        uid = data['uid']
        ticker = data['ticker']
        stock_amount = int(data['stock_amount'])
        if(type == 'buy'):
            return buy(finnhub_client, uid, ticker, stock_amount)
        else:
            return sell(finnhub_client, uid, ticker, stock_amount)
    except Exception as e:
        print(e)
        return {"success": 'false', "message": str(e)}
    finally:
        cleanup()

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
        print("Error " + e)
        return {"success": 'false', "message": str(e)}
    finally:
        cleanup()

# Returns basic database information of all stocks


@app.route('/api/stocks/get_all_stock_info', methods=['GET'])
def get_all_stock_info():
    try:
        all_stocks = Stock.query
        output = [i.serialize for i in all_stocks.all()]
        output = output
        # print("Success")
        return jsonify({'success': 'true', 'stocks': output})
    except Exception as e:
        return {"success": 'false', "message": str(e)}
    finally:
        cleanup()


@app.route('/api/account/pasttransactions/<uid>', methods=['GET'])
def get_past_transactions(uid):
    try:
        output = past_transactions(uid)
        return {"success": 'true', 'past_transactions': output}
    except Exception as e:
        print("Error " + e)
        return {"success": 'false', "message": str(e)}
    finally:
        cleanup()


if __name__ == "__main__":

    app.run()
