# Make getting the price of a stock a seperate, modular function so the stock price API provider can be swapped seamlessly. Did this originally because the api I was using (yahoo finance)
# is EXTREMELY slow. As in, a simple stock price query could take 7 seconds.

import time
import yfinance as yf
import globals


def get_stock_price(client, ticker):
    # stock_price = float(yf.Ticker(ticker).info['currentPrice'])
    stock_price = client.quote(ticker)['c']

    return stock_price


def get_stock_info(client, ticker):
    info = yf.Ticker(ticker).info
    # Basic financials
    # info = client.company_basic_financials(ticker, 'all')['metric']

    # output['bid'] = info['bid']
    # output['ask'] = info['ask']
    # output['recommendationKey'] = info['recommendationKey']
    # output['open'] = info['open']
    # output['high'] = info['dayHigh']
    # output['low'] = info['dayLow']
    # output['mkt_cap'] = info['marketCap']
    # output['price_earnings'] = info['bid']
    # output['52w_high'] = info['fiftyTwoWeekHigh']
    # output['52w_low'] = info['fiftyTwoWeekLow']
    # output['volume'] = info['volume']
    # output['avg_volume'] = info['averageVolume']
    # output['div_yield'] = info['dividendYield']

    return info


# start_time = time.time()
# globals.initialize()
# finnhub_client = globals.finnhub_client


# print(get_stock_info(finnhub_client, 'AAPL'))
# print("--- %s seconds ---" % (time.time() - start_time))
