from datetime import datetime
from flask import Flask
from flask_sqlalchemy import SQLAlchemy
import globals
from sqlalchemy.sql import func

globals.initialize()

db = globals.db

INITIAL_BALANCE = 100000


class PastTransaction(db.Model):
    __tablename__ = 'past_transactions'

    transaction_id = db.Column(db.Integer, primary_key=True, unique=True)
    type = db.Column(db.Text, primary_key=False, unique=False)
    uid = db.Column(db.Text, primary_key=False)
    ticker = db.Column(db.Text, primary_key=False)
    stock_amount = db.Column(db.BigInteger, primary_key=False)
    price_per_stock = db.Column(db.Numeric, primary_key=False)
    total_value = db.Column(db.Numeric, primary_key=False)

    def __init__(self, type, uid, ticker, stock_amount, price_per_stock, total_value):
        self.type = type
        self.uid = uid
        self.ticker = ticker
        self.stock_amount = stock_amount
        self.price_per_stock = price_per_stock
        self.total_value = total_value

    @property
    def serialize(self):
        """Return object data in easily serializable format"""
        return {
            'ticker': self.ticker,
            'type': self.type,
            'stock_amount': self.stock_amount,
            'price_per_stock': self.price_per_stock,
            'total_value': self.total_value
        }

    def __repr__(self):
        return '<Transaction %r>' % self.transaction_id


class OwnedStock(db.Model):
    __tablename__ = 'stocks_owned'

    owned_id = db.Column(db.Integer, primary_key=True, unique=True)
    uid = db.Column(db.Text, primary_key=False)
    ticker = db.Column(db.Text, primary_key=False)
    amount_owned = db.Column(db.BigInteger, primary_key=False)
    average_price_per_stock = db.Column(db.Numeric, primary_key=False)
    total_value = db.Column(db.Numeric, primary_key=False)

    def __init__(self, uid, ticker, amount_owned, price_per_stock, total_value):
        self.uid = uid
        self.ticker = ticker
        self.amount_owned = amount_owned
        self.average_price_per_stock = price_per_stock
        self.total_value = total_value

    def __repr__(self):
        return '<Stocks owned %r>' % self.owned_id

    @property
    def serialize(self):
        """Return object data in easily serializable format"""
        return {
            'ticker': self.ticker,
            'amount_owned': self.amount_owned,
            'average_price': self.average_price_per_stock,
            'total_value': self.total_value
        }


class User(db.Model):
    __tablename__ = 'users'

    uid = db.Column(db.Text, primary_key=True, unique=True)
    datecreated = db.Column(db.DateTime(timezone=True), server_default=func.now(),
                            primary_key=False, unique=False)
    balance = db.Column(db.Numeric, primary_key=False)
    username = db.Column(db.Text, primary_key=False)
    email = db.Column(db.Text, primary_key=False)

    def __init__(self,  uid, username, email):
        self.uid = uid
        self.datecreated = func.now()
        self.balance = INITIAL_BALANCE
        self.username = username
        self.email = email

    def __repr__(self):
        return '<User %r>' % self.uid


class Stock(db.Model):

    __tablename__ = 'stocks'

    ticker = db.Column(db.Text, primary_key=True, unique=True)
    name = db.Column(db.Text, primary_key=False)
    exchange = db.Column(db.Text, primary_key=False)
    sector = db.Column(db.Text, primary_key=False)
    industry = db.Column(db.Text, primary_key=False)
    country = db.Column(db.Text, primary_key=False)
    currency = db.Column(db.Text, primary_key=False)
    logo_link = db.Column(db.Text, primary_key=False)

    def __init__(self, ticker, name, exchange, sector, industry, country, currency, logo_link):
        self.ticker = ticker
        self.name = name
        self.exchange = exchange
        self.sector = sector
        self.industry = industry
        self.country = country
        self.currency = currency
        self.logo_link = logo_link

    def __repr__(self):
        return '<Stock %r>' % self.ticker

    @property
    def serialize(self):
        """Return object data in easily serializable format"""
        return {
            'ticker': self.ticker,
            'name': self.name,
            'exchange': self.exchange,
            'sector': self.sector,
            'industry': self.industry,
            'country': self.country,
            'currency': self.currency,
            'logo_link': self.logo_link}
