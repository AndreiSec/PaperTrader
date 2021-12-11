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

    db = SQLAlchemy(app)

    # Setup client
    finnhub_client = finnhub.Client(api_key=finnHubToken)
