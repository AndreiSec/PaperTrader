from flask import Flask
from flask_sqlalchemy import SQLAlchemy

# Don't store connection string on github as it includes auth details. Read from file
dbConnectionString = open("dbConnectionString.txt", "r").read()


app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://postgres:%Papertrader%123'

db = SQLAlchemy(app)


@app.route('/')
def index():
    return "Hello world"


if __name__ == "__main__":
    app.run()
