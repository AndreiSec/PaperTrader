package objects;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class PastTransactionObject implements Parcelable {
    private String ticker;
    private String type;
    private Integer stock_amount;
    private String price_per_stock;
    private String total_value;



    public PastTransactionObject(JSONObject transactionJSON) {
        try{
            System.out.println("TRANSACTION JSON: ");
            this.ticker = (String) transactionJSON.get("ticker");
            this.type = (String) transactionJSON.get("type");
            this.stock_amount = (Integer) transactionJSON.get("stock_amount");
            this.price_per_stock = (String) transactionJSON.get("price_per_stock");
            this.total_value = (String) transactionJSON.get("total_value");
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public String getTicker() {
        return ticker;
    }

    public String getType() {
        return type;
    }

    public Integer getStock_amount() {
        return stock_amount;
    }

    public String getPrice_per_stock() {
        return price_per_stock;
    }

    public String getTotal_value() {
        return total_value;
    }

    protected PastTransactionObject(Parcel in) {
        ticker = in.readString();
        type = in.readString();
        if (in.readByte() == 0) {
            stock_amount = null;
        } else {
            stock_amount = in.readInt();
        }
        if (in.readByte() == 0) {
            price_per_stock = null;
        } else {
            price_per_stock = in.readString();
        }
        if (in.readByte() == 0) {
            total_value = null;
        } else {
            total_value = in.readString();
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Creator<PastTransactionObject> CREATOR = new Creator<PastTransactionObject>() {
        @Override
        public PastTransactionObject createFromParcel(Parcel in) {
            return new PastTransactionObject(in);
        }

        @Override
        public PastTransactionObject[] newArray(int size) {
            return new PastTransactionObject[size];
        }
    };
}

