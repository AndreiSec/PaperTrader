# Python file to house any global variables (specifically the DB connection session to re-use between modules)
from flask import Flask
from flask_sqlalchemy import SQLAlchemy

# Don't store connection string on github as it includes auth details. Read from file
dbConnectionString = open("dbConnectionString.txt", "r").read()


def initialize():
    global db
    global app

    app = Flask(__name__)
    app.config['SQLALCHEMY_DATABASE_URI'] = dbConnectionString
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

    db = SQLAlchemy(app)
