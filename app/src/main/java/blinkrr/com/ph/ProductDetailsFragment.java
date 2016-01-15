package blinkrr.com.ph;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailsFragment extends Fragment{

    Products p;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        p = getArguments().getParcelable("product");

        View view = inflater.inflate(R.layout.product_details, container, false);

        TextView desc, brand, price, qty;
        desc = (TextView) view.findViewById(R.id.product_desc);
        brand = (TextView) view.findViewById(R.id.product_brand);
        price = (TextView) view.findViewById(R.id.product_price);
        qty = (TextView) view.findViewById(R.id.product_qty);
        NetworkImageView niv = (NetworkImageView) view.findViewById(R.id.product_image);
        Button reserve = (Button) view.findViewById(R.id.product_reserve);

        ImageLoader imageLoader = JSONParser.getInstance(getContext()).getImageLoader();
        imageLoader.get(p.image, ImageLoader.getImageListener(niv,
                R.drawable.default_home_image, android.R.drawable.ic_dialog_alert));

        niv.setImageUrl(p.image, imageLoader);

        desc.setText(p.description);
        brand.setText(p.brand);
        price.setText(p.price);
        qty.setText(p.qty);

        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject request = new JSONObject();
                    request.accumulate("patient_id", DrawerActivity.p.id);
                    request.accumulate("optprod_id", p.prod_id);
                    request.accumulate("term", "+"+OpticalDetailsFragment.opticalShop.term+" days");

                    JSONParser.getInstance(getContext()).reserveProduct(request, Constants.reserveProduct);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
}
