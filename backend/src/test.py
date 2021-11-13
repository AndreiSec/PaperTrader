# import yfinance as yf
from models import *
# msft = yf.Ticker("ADBE")

# # get stock info
# print(msft.info)


# Create database and app sessions in global module
globals.initialize()
db = globals.db
app = globals.app


user = User.query.filter_by(uid='testuid').first()
user.balance = 100000
db.session.merge(user)
db.session.commit()

print(user.balance)
