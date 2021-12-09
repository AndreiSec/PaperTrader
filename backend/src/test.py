# import yfinance as yf
from models import *
import yfinance as yf
# msft = yf.Ticker("ADBE")

# # get stock info
# print(msft.info)

stock_price = float(yf.Ticker('MSFT').info['currentPrice'])
print(stock_price)

# Create database and app sessions in global module
# globals.initialize()
# db = globals.db
# app = globals.app


# user = User.query.filter_by(uid='testuid').first()
# user.balance = 100000
# db.session.merge(user)
# db.session.commit()

# print(user.balance)
