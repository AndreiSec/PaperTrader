package objects;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class HoldingsStockObject implements Parcelable {

    private String ticker;
    private String companyName;
    private String sector;
    private String exchange;
    private String country;
    private String currency;
    private String industry;
    private String logoUrl;
    private Integer amount_owned;
    private Double average_price_per_stock;
    private Double total_value;
    private Double current_value;



    public HoldingsStockObject(JSONObject stockJson) {
        try{
            System.out.println("STOCK JSON: ");
            System.out.println(stockJson);
            this.ticker = (String) stockJson.get("ticker");
            this.companyName = (String) stockJson.get("name");
            this.amount_owned = (Integer) stockJson.get("amount_owned");
            this.total_value = (Double) (stockJson.get("total_value"));
            this.average_price_per_stock = (Double) stockJson.get("average_price");
            this.current_value = (Double) stockJson.get("current_value");
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public String getTicker() { return ticker;}

    public String getCompanyName() { return companyName; }

    public String getSector() { return sector; }

    public String getExchange() { return exchange; }

    public String getCountry() { return country; }

    public String getCurrency() { return currency; }

    public String getIndustry() { return industry; }

    public String getLogoUrl() { return logoUrl; }

    public Integer getAmount_owned() { return amount_owned; }

    public float getAverage_price_per_stock() { return average_price_per_stock.floatValue(); }

    public float getTotal_value() { return total_value.floatValue(); }

    public float getCurrent_value() { return current_value.floatValue(); }

    public HoldingsStockObject(String ticker, String companyName, String sector, String industry, String exchange, String country, String currency, String logoUrl,
                               Integer amount_owned, Double total_value, Double average_price_per_stock, Double current_value) {
        this.ticker = ticker;
        this.companyName = companyName;
        this.sector = sector;
        this.exchange = exchange;
        this.country = country;
        this.currency = currency;
        this.industry = industry;
        this.logoUrl = logoUrl;
        this.amount_owned = amount_owned;
        this.total_value = total_value;
        this.average_price_per_stock = average_price_per_stock;
        this.current_value = current_value;
    }

    protected HoldingsStockObject(Parcel in) {
        ticker = in.readString();
        companyName = in.readString();
        sector = in.readString();
        exchange = in.readString();
        country = in.readString();
        currency = in.readString();
        industry = in.readString();
        logoUrl = in.readString();
        amount_owned = in.readInt();
        total_value = in.readDouble();
        average_price_per_stock = in.readDouble();
        current_value = in.readDouble();
    }

    public static final Creator<MarketStockObject> CREATOR = new Creator<MarketStockObject>() {
        @Override
        public MarketStockObject createFromParcel(Parcel in) {
            return new MarketStockObject(in);
        }

        @Override
        public MarketStockObject[] newArray(int size) {
            return new MarketStockObject[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ticker);
        dest.writeString(companyName);
        dest.writeString(sector);
        dest.writeString(exchange);
        dest.writeString(country);
        dest.writeString(currency);
        dest.writeString(industry);
        dest.writeString(logoUrl);
        dest.writeInt(amount_owned);
        dest.writeDouble(total_value);
        dest.writeDouble(average_price_per_stock);
        dest.writeDouble(current_value);
    }
}
