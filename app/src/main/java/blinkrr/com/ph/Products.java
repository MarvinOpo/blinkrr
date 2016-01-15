package blinkrr.com.ph;

import android.os.Parcel;
import android.os.Parcelable;

public class Products implements Parcelable {
    public String prod_id, opt_id, type_id, model, description, brand, qty, reorder, discount,
        price, image, status;

    public Products(String prod_id, String opt_id, String type_id, String model,
                    String description, String brand, String qty, String reorder,
                    String discount, String price, String image, String status) {
        this.prod_id = prod_id;
        this.opt_id = opt_id;
        this.type_id = type_id;
        this.model = model;
        this.description = description;
        this.brand = brand;
        this.qty = qty;
        this.reorder = reorder;
        this.discount = discount;
        this.price = price;
        this.image = image;
        this.status = status;
    }

    protected Products(Parcel in) {
        prod_id = in.readString();
        opt_id = in.readString();
        type_id = in.readString();
        model = in.readString();
        description = in.readString();
        brand = in.readString();
        qty = in.readString();
        reorder = in.readString();
        discount = in.readString();
        price = in.readString();
        image = in.readString();
        status = in.readString();
    }

    public static final Creator<Products> CREATOR = new Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel in) {
            return new Products(in);
        }

        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(prod_id);
        dest.writeString(opt_id);
        dest.writeString(type_id);
        dest.writeString(model);
        dest.writeString(description);
        dest.writeString(brand);
        dest.writeString(qty);
        dest.writeString(reorder);
        dest.writeString(discount);
        dest.writeString(price);
        dest.writeString(image);
        dest.writeString(status);
    }
}
