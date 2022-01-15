package objects;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class MarketStockObject implements Parcelable {


    private String ticker;
    private String companyName;
    private String sector;
    private String exchange;
    private String country;
    private String currency;
    private String industry;
    private String logoUrl;


    public MarketStockObject(JSONObject stockJson) {
        try{
            this.ticker = (String) stockJson.get("ticker");
            this.companyName = (String) stockJson.get("name");
            this.sector = (String) stockJson.get("sector");
            this.exchange = (String) stockJson.get("exchange");
            this.industry = (String) stockJson.get("industry");
            this.country = (String) stockJson.get("country");
            this.currency = (String) stockJson.get("currency");
            this.logoUrl = (String) stockJson.get("logo_link");
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public MarketStockObject(String ticker, String companyName, String sector, String industry, String exchange, String country, String currency, String logoUrl) {
        this.ticker = ticker;
        this.companyName = companyName;
        this.sector = sector;
        this.exchange = exchange;
        this.country = country;
        this.currency = currency;
        this.industry = industry;
        this.logoUrl = logoUrl;
    }

    protected MarketStockObject(Parcel in) {
        ticker = in.readString();
        companyName = in.readString();
        sector = in.readString();
        exchange = in.readString();
        country = in.readString();
        currency = in.readString();
        industry = in.readString();
        logoUrl = in.readString();
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

    public String getTicker() {
        return ticker;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSector() {
        return sector;
    }

    public String getExchange() {
        return exchange;
    }

    public String getCountry() {
        return country;
    }

    public String getCurrency() {
        return currency;
    }

    public String getIndustry() {
        return industry;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

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
    }
}
