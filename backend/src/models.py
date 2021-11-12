from datetime import datetime
from flask import Flask
from flask_sqlalchemy import SQLAlchemy
import server
import globals
from sqlalchemy.sql import func

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
    total_cash = db.Column(db.Numeric, primary_key=False)

    def __init__(self, type, uid, ticker, stock_amount, price_per_stock, total_cash):
        self.type = type
        self.uid = uid
        self.ticker = ticker
        self.stock_amount = stock_amount
        self.price_per_stock = price_per_stock
        self.total_cash = total_cash

    def __repr__(self):
        return '<Transaction %r>' % self.transaction_id


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

    def __init__(self, ticker, name, exchange, sector, industry, country, currency):
        self.ticker = ticker
        self.name = name
        self.exchange = exchange
        self.sector = sector
        self.industry = industry
        self.country = country
        self.currency = currency

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
            'currency': self.currency}