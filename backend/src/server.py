from flask import Flask
from flask_sqlalchemy import SQLAlchemy
import yfinance as yf

# Don't store connection string on github as it includes auth details. Read from file
dbConnectionString = open("dbConnectionString.txt", "r").read()


app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = dbConnectionString


db = SQLAlchemy(app)


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
@app.route('/stock_info/<ticker>')
def stock_info():

    return "<p>Hello, World!</p>"


@app.route('/test')
def test():
    return "Test"


if __name__ == "__main__":
    app.run()
