package blinkrr.com.ph;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class OpticalDetailsFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static LinearLayout services;
    public static RecyclerView rv;
    public static LinearLayoutManager llm;
    public static ArrayList<Products> mList = new ArrayList<Products>();
    public static ArrayList<CheckBox> service_list = new ArrayList<CheckBox>();
    public static RVadapter adapter;
    public static Button add_appointment;
    public static ScrollView scroll;

    public static OpticalShop opticalShop;

    static Context context;
    NetworkImageView opt_image;
    TextView opt_name, opt_add, opt_phone, opt_email, app_message, app_date, app_time;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
        try {
            mList.clear();
            services.removeAllViews();
        }catch(Exception e){}

        opticalShop = getArguments().getParcelable("optical");

        try{
            JSONObject request = new JSONObject();
            request.accumulate("id", opticalShop.id);

            JSONParser.getInstance(context).getProductsByShopId(request, Constants.getProductsByShopId);
            JSONParser.getInstance(context).getServicesByShopId(request, Constants.getServicesByShopId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.optical_shop_details, container, false);

        opt_image = (NetworkImageView) view.findViewById(R.id.opt_info_image);
        opt_name = (TextView) view.findViewById(R.id.opt_info_name);
        opt_add = (TextView) view.findViewById(R.id.opt_info_address);
        opt_phone = (TextView) view.findViewById(R.id.opt_info_phone);
        opt_email = (TextView) view.findViewById(R.id.opt_info_email);
        services = (LinearLayout) view.findViewById(R.id.services_container);
        add_appointment = (Button) view.findViewById(R.id.add_appointment);
        scroll = (ScrollView) view.findViewById(R.id.scroll);

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

        final View view2 = inflater.inflate(R.layout.appointment_dialog, null, false);

        add_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                String s = "";
                for (int i = 0; i < service_list.size(); i++) {
                    if(service_list.get(i).isChecked()){
                        message += service_list.get(i).getText().toString() + "\n";
                        s += service_list.get(i).getText().toString();
                        if(i != service_list.size()) s += ", ";
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setView(view2);
                builder.setTitle("Appointment");
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Request", null);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                app_message = (TextView) view2.findViewById(R.id.app_message);
                app_date = (TextView) view2.findViewById(R.id.app_date);
                app_time = (TextView) view2.findViewById(R.id.app_time);

                app_message.setText(message);

                app_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar now = Calendar.getInstance();
                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                OpticalDetailsFragment.this,
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH)
                        );
                        dpd.setMinDate(now);
                        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                    }
                });

                app_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar now = Calendar.getInstance();
                        TimePickerDialog tpd = TimePickerDialog.newInstance(
                                OpticalDetailsFragment.this,
                                now.get(Calendar.HOUR),
                                now.get(Calendar.MINUTE),
                                true
                        );
                        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
                    }
                });

                final String finalS = s;
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // if EditText is empty disable closing on possitive button
                        if (app_date.getText().toString().equalsIgnoreCase("Pick Date")) {
                            Toast.makeText(getContext(), "Please pick date!", Toast.LENGTH_SHORT).show();
                        } else if (app_time.getText().toString().equalsIgnoreCase("Pick Time")) {
                            Toast.makeText(getContext(), "Please pick date!", Toast.LENGTH_SHORT).show();
                        } else {
                                try {
                                JSONObject request = new JSONObject();
                                request.accumulate("patient_id", DrawerActivity.p.id);
                                request.accumulate("optshop_id", opticalShop.id);
                                request.accumulate("services", finalS);
                                request.accumulate("date", app_date.getText().toString());
                                request.accumulate("time", app_time.getText().toString());

                                JSONParser parser = new JSONParser(getContext());
                                parser.appointmentRequest(request, Constants.appointmentRequest);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
        add_appointment.setVisibility(View.INVISIBLE);
        return view;
    }

    public static void refreshPage(){
        try{
            mList.clear();
            services.removeAllViews();
            JSONObject request = new JSONObject();
            request.accumulate("id", opticalShop.id);

            JSONParser.getInstance(context).getProductsByShopId(request, Constants.getProductsByShopId);
            JSONParser.getInstance(context).getServicesByShopId(request, Constants.getServicesByShopId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        app_date.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        app_time.setText(hourOfDay+":"+minute+":"+second);
    }
}
