package blinkrr.com.ph;

import android.os.Parcel;
import android.os.Parcelable;

public class OpticalShop implements Parcelable {
    public String id, name, address, email, phone, image, term;

    public OpticalShop(String id, String name, String address, String email, String phone, String image, String term) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.term = term;
    }

    protected OpticalShop(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        email = in.readString();
        phone = in.readString();
        image = in.readString();
        term = in.readString();
    }

    public static final Creator<OpticalShop> CREATOR = new Creator<OpticalShop>() {
        @Override
        public OpticalShop createFromParcel(Parcel in) {
            return new OpticalShop(in);
        }

        @Override
        public OpticalShop[] newArray(int size) {
            return new OpticalShop[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(image);
        dest.writeString(term);
    }
}
