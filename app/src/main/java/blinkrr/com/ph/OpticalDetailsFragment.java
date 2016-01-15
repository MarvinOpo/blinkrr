package blinkrr.com.ph;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OpticalDetailsFragment extends Fragment {

    public static LinearLayout services;
    public static RecyclerView rv;
    public static LinearLayoutManager llm;
    public static ArrayList<Products> mList = new ArrayList<Products>();
    public static RVadapter adapter;

    public static OpticalShop opticalShop;

    NetworkImageView opt_image;
    TextView opt_name, opt_add, opt_phone, opt_email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mList.clear();
            services.removeAllViews();
        }catch(Exception e){}

        opticalShop = getArguments().getParcelable("optical");

        try{
            JSONObject request = new JSONObject();
            request.accumulate("id", opticalShop.id);

            JSONParser.getInstance(getContext()).getProductsByShopId(request, Constants.getProductsByShopId);
            JSONParser.getInstance(getContext()).getServicesByShopId(request, Constants.getServicesByShopId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.optical_shop_details, container, false);

        opt_image = (NetworkImageView) view.findViewById(R.id.opt_info_image);
        opt_name = (TextView) view.findViewById(R.id.opt_info_name);
        opt_add = (TextView) view.findViewById(R.id.opt_info_address);
        opt_phone = (TextView) view.findViewById(R.id.opt_info_phone);
        opt_email = (TextView) view.findViewById(R.id.opt_info_email);
        services = (LinearLayout) view.findViewById(R.id.services_container);

        rv = (RecyclerView) view.findViewById(R.id.opt_info_recycler);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(llm);

        ImageLoader imageLoader = JSONParser.getInstance(getContext()).getImageLoader();
        imageLoader.get(opticalShop.image, ImageLoader.getImageListener(opt_image,
                R.drawable.default_home_image, android.R.drawable.ic_dialog_alert));

        opt_image.setImageUrl(opticalShop.image, imageLoader);

        opt_name.setText(opticalShop.name);
        opt_add.setText(opticalShop.address);
        opt_phone.setText(opticalShop.phone);
        opt_email.setText(opticalShop.email);
        return view;
    }
}
