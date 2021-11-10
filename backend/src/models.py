from flask import Flask
from flask_sqlalchemy import SQLAlchemy

# Don't store connection string on github as it includes auth details. Read from file
dbConnectionString = open("dbConnectionString.txt", "r").read()


app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = dbConnectionString


db = SQLAlchemy(app)


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
