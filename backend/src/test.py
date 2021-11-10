import yfinance as yf

msft = yf.Ticker("ADBE")

# get stock info
print(msft.info)
