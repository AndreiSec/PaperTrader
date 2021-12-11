# Python file to house any global variables (specifically the DB connection session to re-use between modules)
import finnhub
from flask import Flask
from flask_sqlalchemy import SQLAlchemy

# Don't store connection string on github as it includes auth details. Read from file
dbConnectionString = open("dbConnectionString.txt", "r").read()

finnHubToken = open('FinnHubToken.txt', 'r').read()


def initialize():
    global db
    global app
    global finnhub_client

    app = Flask(__name__)
    app.config['SQLALCHEMY_DATABASE_URI'] = dbConnectionString
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
    app.config['SQLALCHEMY_ENGINE_OPTIONS'] = {
        'pool_size': 10,
        'max_overflow': 0,
        # looks like postgres likes this more than rollback
        'pool_reset_on_return': 'commit',
        'pool_timeout': 5  # try a low value here maybe
    }

    db = SQLAlchemy(app)

    # Setup client
    finnhub_client = finnhub.Client(api_key=finnHubToken)
