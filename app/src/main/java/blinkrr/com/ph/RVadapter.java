package blinkrr.com.ph;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RVadapter extends RecyclerView.Adapter<RVadapter.CardViewHolder> {

    Context context;
    static List<Products> prod;
    static List<OpticalShop> opt_shop;
    static List<ReservedItem> reserve;
    static List<Appointment> app;
    private ImageLoader imageLoader;
    static String activity;

    public RVadapter(Context context, String activity, List<OpticalShop> opt_shop, List<Products> prod) {
        this.context = context;
        this.activity = activity;
        if(opt_shop != null) this.opt_shop = opt_shop;
        this.prod = prod;
    }

    public RVadapter(Context context, String activity, List<ReservedItem> reserve) {
        this.context = context;
        this.activity = activity;
        this.reserve = reserve;
    }

    public RVadapter(Context context, String activity, List<ReservedItem> reserve, List<Appointment> app,
                     List<OpticalShop> opt_shop) {
        this.context = context;
        this.activity = activity;
        this.app = app;
    }
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(opt_shop != null && activity.equalsIgnoreCase("HomeFragment")) {
            view = inflater.inflate(R.layout.optical_shop, parent, false);
        }else if(prod != null && activity.equalsIgnoreCase("OpticalDetails")){
            view = inflater.inflate(R.layout.optical_shop_details_product, parent, false);
        }else if(reserve != null && activity.equalsIgnoreCase("ReserveFragment")){
            view = inflater.inflate(R.layout.reserve_item, parent, false);
        }else if(app != null && activity.equalsIgnoreCase("AppointmentFragment")){
            view = inflater.inflate(R.layout.appointment_item, parent, false);
        }

        CardViewHolder cvh = new CardViewHolder(view);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        final int index = position;

        if(opt_shop != null && activity.equalsIgnoreCase("HomeFragment")){
            imageLoader = JSONParser.getInstance(context).getImageLoader();
            imageLoader.get(opt_shop.get(position).image, ImageLoader.getImageListener(holder.opt_image1,
                    R.drawable.default_home_image, android.R.drawable.ic_dialog_alert));

            holder.opt_image1.setImageUrl(opt_shop.get(position).image, imageLoader);
            holder.opt_name1.setText(opt_shop.get(position).name);

            holder.cv.setOnClickListener(new View.OnClickListener() {
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
        }else if(reserve != null && activity.equalsIgnoreCase("ReserveFragment")){
            imageLoader = JSONParser.getInstance(context).getImageLoader();
            imageLoader.get(reserve.get(position).prod.image, ImageLoader.getImageListener(holder.reserve_image,
                    R.drawable.default_home_image, android.R.drawable.ic_dialog_alert));

            holder.reserve_image.setImageUrl(reserve.get(position).prod.image, imageLoader);
            holder.reserve_optName.setText(reserve.get(position).opt_shop.name);
            holder.reserve_brand.setText("Brand: "+reserve.get(position).prod.brand);
            holder.reserve_qty.setText("Qty Reserved: "+reserve.get(position).qty);
            holder.reserve_date.setText(reserve.get(position).end_date);
            holder.reserve_price.setText(String.valueOf(Double.parseDouble(reserve.get(position).prod.price) * Integer.parseInt(reserve.get(position).qty)));
            holder.reserve_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setTitle("Notice");
                    builder.setMessage("Are you sure you want to cancel this reservation?");
                    builder.setNegativeButton("No", null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                JSONObject request = new JSONObject();
                                request.accumulate("res_id", reserve.get(index).res_id);

                                JSONParser.getInstance(context).deleteReservation(request, Constants.deleteReservation);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.show();
                }
            });
        }else if(app != null && activity.equalsIgnoreCase("AppointmentFragment")){
            imageLoader = JSONParser.getInstance(context).getImageLoader();
            imageLoader.get(app.get(position).opt_shop.image, ImageLoader.getImageListener(holder.appointment_image,
                    R.drawable.default_home_image, android.R.drawable.ic_dialog_alert));

            holder.appointment_image.setImageUrl(app.get(position).opt_shop.image, imageLoader);
            holder.appointment_optName.setText(app.get(position).opt_shop.name);
            holder.appointment_services.setText("Services: "+app.get(position).services);
            holder.appointment_date.setText(app.get(position).app_date+", "+app.get(position).app_time);
            holder.appointment_status.setText(app.get(position).status);
            holder.appointment_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setTitle("Notice");
                    builder.setMessage("Are you sure you want to cancel this appointment?");
                    builder.setNegativeButton("No", null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                JSONObject request = new JSONObject();
                                request.accumulate("appointment_id", app.get(index).id);

                                JSONParser.getInstance(context).cancelAppointment(request, Constants.cancelAppointment);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;

        if(opt_shop != null && activity.equalsIgnoreCase("HomeFragment")) size = opt_shop.size()-(opt_shop.size()/2);
        else if(prod != null && activity.equalsIgnoreCase("OpticalDetails")) size = prod.size();
        else if(reserve != null && activity.equalsIgnoreCase("ReserveFragment")) size = reserve.size();
        else if(app != null && activity.equalsIgnoreCase("AppointmentFragment")) size = app.size();

        return size;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        NetworkImageView opt_image1, opt_image2, prod_image, reserve_image, appointment_image;
        TextView opt_name1, opt_name2, reserve_optName, reserve_brand, reserve_qty,
                reserve_date, reserve_price, appointment_optName, appointment_services, appointment_date,
                appointment_status;
        Button prod_button;
        ImageView reserve_cancel, appointment_cancel;

        public CardViewHolder(View itemView) {
            super(itemView);

            if(opt_shop != null && activity.equalsIgnoreCase("HomeFragment")){
                cv = (CardView) itemView.findViewById(R.id.card_view);
                opt_image1 = (NetworkImageView) itemView.findViewById(R.id.opt_shop_image);
                opt_name1 = (TextView) itemView.findViewById(R.id.opt_shop_name);
            }else if(prod != null && activity.equalsIgnoreCase("OpticalDetails")){
                prod_image = (NetworkImageView) itemView.findViewById(R.id.opt_info_prod_image);
                prod_button = (Button) itemView.findViewById(R.id.opt_info_prod_btn);
            }else if(reserve != null && activity.equalsIgnoreCase("ReserveFragment")){
                reserve_cancel = (ImageView) itemView.findViewById(R.id.reserve_decline_btn);
                reserve_image = (NetworkImageView) itemView.findViewById(R.id.reserve_image);
                reserve_optName = (TextView) itemView.findViewById(R.id.reserve_optName);
                reserve_brand = (TextView) itemView.findViewById(R.id.reserve_brand);
                reserve_qty = (TextView) itemView.findViewById(R.id.reserve_qty);
                reserve_date = (TextView) itemView.findViewById(R.id.reserve_date);
                reserve_price = (TextView) itemView.findViewById(R.id.reserve_price);
            }else if(app != null && activity.equalsIgnoreCase("AppointmentFragment")){
                appointment_cancel = (ImageView) itemView.findViewById(R.id.appointment_cancel_btn);
                appointment_image = (NetworkImageView) itemView.findViewById(R.id.appointment_image);
                appointment_optName = (TextView) itemView.findViewById(R.id.appointment_optName);
                appointment_services= (TextView) itemView.findViewById(R.id.appointment_services);
                appointment_date = (TextView) itemView.findViewById(R.id.appointment_date);
                appointment_status = (TextView) itemView.findViewById(R.id.appointment_status);
            }
        }
    }
}
