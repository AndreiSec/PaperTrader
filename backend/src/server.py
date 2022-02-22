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
            type, uid, ticker, stock_amount, price_per_stock, total_value=total_cash)

        db.session.add(past_transaction)

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
        print(e)
        return {"success": 'false', "message": str(e)}
    finally:
        cleanup()
# Method to let a user sell a certain amount of stock
# All the necessary data is contained in the request form as it is a POST


@app.route('/api/stocks/stocktransaction/sell', methods=['POST'])
def perform_transaction_sell():
    try:
        data = request.get_json()
        type = "sell"
        uid = data['uid']
        ticker = data['ticker']
        stock_amount = int(data['stock_amount'])

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

        db.session.merge(stock_entry)

        # First, create a past transaction to store in db
        past_transaction = PastTransaction(
            type, uid, ticker, stock_amount, stock_price, total_value=total_cash)

        db.session.add(past_transaction)

        # Finally update user cash balance

        user.balance = user.balance + Decimal(total_cash)

        db.session.merge(user)

        # Commit all changes simultaneously
        db.session.commit()
        return_message = type + " of " + str(stock_amount) + \
            ' ' + ticker + "at $" + str(stock_price) + \
            " totaling $" + str(total_cash) + " successful "
        return {"success": 'true', 'message': return_message}
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
        print("HERE 0")
        output = past_transactions(uid)
        return {"success": 'true', 'past_transactions': output}
    except Exception as e:
        print("Error " + e)
        return {"success": 'false', "message": str(e)}
    finally:
        cleanup()


if __name__ == "__main__":

    app.run()
