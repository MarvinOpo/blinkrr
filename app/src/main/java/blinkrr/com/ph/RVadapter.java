package blinkrr.com.ph;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class RVadapter extends RecyclerView.Adapter<RVadapter.CardViewHolder> {

    Context context;
    static List<Products> prod;
    static List<OpticalShop> opt_shop;
    private ImageLoader imageLoader;
    static String activity;

    public RVadapter(Context context, String activity, List<OpticalShop> opt_shop, List<Products> prod) {
        this.context = context;
        this.activity = activity;
        if(opt_shop != null) this.opt_shop = opt_shop;
        this.prod = prod;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(opt_shop != null && activity.equalsIgnoreCase("HomeFragment")) {
            view = inflater.inflate(R.layout.optical_shop, parent, false);
        }else if(prod != null && activity.equalsIgnoreCase("OpticalDetails")){
            view = inflater.inflate(R.layout.optical_shop_details_product, parent, false);
        }

        CardViewHolder cvh = new CardViewHolder(view);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        final int index = position;

        if(opt_shop != null && activity.equalsIgnoreCase("HomeFragment")){
            if(position % 2 == 0) {
                imageLoader = JSONParser.getInstance(context).getImageLoader();
                imageLoader.get(opt_shop.get(position).image, ImageLoader.getImageListener(holder.opt_image1,
                        R.drawable.default_home_image, android.R.drawable.ic_dialog_alert));

                holder.opt_image1.setImageUrl(opt_shop.get(position).image, imageLoader);
                holder.opt_name1.setText(opt_shop.get(position).name);

                holder.opt_image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("optical", opt_shop.get(index));

                        OpticalDetailsFragment odf = new OpticalDetailsFragment();
                        odf.setArguments(bundle);

                        FragmentTransaction ft = DrawerActivity.fm.beginTransaction();
                        ft.add(R.id.fragment_container, odf).addToBackStack(null).commit();
                    }
                });

                if(position < opt_shop.size()-1){
                    imageLoader = JSONParser.getInstance(context).getImageLoader();
                    imageLoader.get(opt_shop.get(position+1).image, ImageLoader.getImageListener(holder.opt_image2,
                            R.drawable.default_home_image, android.R.drawable.ic_dialog_alert));

                    holder.opt_image2.setImageUrl(opt_shop.get(position + 1).image, imageLoader);
                    holder.opt_name2.setText(opt_shop.get(position + 1).name);

                    holder.opt_image2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("optical", opt_shop.get(index+1));

                            OpticalDetailsFragment odf = new OpticalDetailsFragment();
                            odf.setArguments(bundle);

                            FragmentTransaction ft = DrawerActivity.fm.beginTransaction();
                            ft.add(R.id.fragment_container, odf).addToBackStack(null).commit();
                        }
                    });
                }
            }
        }else if(prod != null && activity.equalsIgnoreCase("OpticalDetails")){
            imageLoader = JSONParser.getInstance(context).getImageLoader();
            imageLoader.get(prod.get(position).image, ImageLoader.getImageListener(holder.prod_image,
                    R.drawable.default_home_image, android.R.drawable.ic_dialog_alert));

            holder.prod_image.setImageUrl(prod.get(position).image, imageLoader);

            holder.prod_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("product", prod.get(index));

                    ProductDetailsFragment pdf = new ProductDetailsFragment();
                    pdf.setArguments(bundle);

                    FragmentTransaction ft = DrawerActivity.fm.beginTransaction();
                    ft.add(R.id.fragment_container, pdf).addToBackStack(null).commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;

        if(opt_shop != null && activity.equalsIgnoreCase("HomeFragment")) size = opt_shop.size()-(opt_shop.size()/2);
        if(opt_shop != null && activity.equalsIgnoreCase("OpticalDetails")) size = prod.size();

        return size;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder{

        NetworkImageView opt_image1, opt_image2, prod_image;
        TextView opt_name1, opt_name2;
        Button prod_button;

        public CardViewHolder(View itemView) {
            super(itemView);

            if(opt_shop != null && activity.equalsIgnoreCase("HomeFragment")){
                opt_image1 = (NetworkImageView) itemView.findViewById(R.id.opt_shop_image);
                opt_image2 = (NetworkImageView) itemView.findViewById(R.id.opt_shop_image2);
                opt_name1 = (TextView) itemView.findViewById(R.id.opt_shop_name);
                opt_name2 = (TextView) itemView.findViewById(R.id.opt_shop_name2);
            }else if(prod != null && activity.equalsIgnoreCase("OpticalDetails")){
                prod_image = (NetworkImageView) itemView.findViewById(R.id.opt_info_prod_image);
                prod_button = (Button) itemView.findViewById(R.id.opt_info_prod_btn);
            }
        }
    }
}
