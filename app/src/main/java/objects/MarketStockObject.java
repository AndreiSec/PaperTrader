package objects;

import org.json.JSONObject;

public class MarketStockObject {


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
}
