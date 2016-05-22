package blinkrr.com.ph;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class JSONParser {

    private static JSONParser parser;
    Context context;
    RequestQueue mRequestQueue;
    private ImageLoader imageLoader;
    public static int size=0;

    public JSONParser(Context context) {
        this.context = context;
        this.mRequestQueue = getRequestQueue();

        imageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized JSONParser getInstance(Context context) {
        parser = new JSONParser(context);
        return parser;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public void attemptLogin(JSONObject request, String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, request,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if(response.length() > 0) {
                                JSONObject obj = response.getJSONObject(0);
                                String status = obj.getString("status");

                                if (status.equalsIgnoreCase("active")) {
                                    String id = obj.getString("id");
                                    String name = obj.getString("name");
                                    String contact = obj.getString("contact");
                                    String gender = obj.getString("gender");
                                    String address = obj.getString("address");
                                    String birthday = obj.getString("birthday");
                                    String image = obj.getString("image");

                                    Intent intent = new Intent(context, DrawerActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("name", name);
                                    intent.putExtra("contact", contact);
                                    intent.putExtra("gender", gender);
                                    intent.putExtra("address", address);
                                    intent.putExtra("birthday", birthday);
                                    intent.putExtra("image", image);
                                    context.startActivity(intent);
                                    ((Activity)context).finish();
                                }
                            }else{
                                Toast.makeText(context, "Email and password does not match!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ATTEMPTLOGIN", error.getMessage());
            }
        });
        mRequestQueue.add(jsonArrayRequest);
    }

    public void appointmentRequest(final JSONObject request, final String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, request,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(context, "Successful request!", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("APPOINTMENTREQUEST", error.getMessage());
                appointmentRequest(request, url);
            }
        });
        mRequestQueue.add(jsonArrayRequest);
    }

    public void getProductsByShopId(final JSONObject request, String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, request,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i< response.length(); i++){
                                JSONObject obj = response.getJSONObject(i);

                                String prod_id = obj.getString("prod_id");
                                String type_id = obj.getString("type_id");
                                String model = obj.getString("model");
                                String description = obj.getString("description");
                                String brand = obj.getString("brand");
                                String qty = obj.getString("qty");
                                String reorder = obj.getString("reorder");
                                String discount = obj.getString("discount");
                                String price = obj.getString("price");
                                String image = obj.getString("optprod_img");
                                String status = obj.getString("status");

                                if(status.equalsIgnoreCase("Available")){
                                    OpticalDetailsFragment.mList.add(new Products(prod_id, request.getString("id"), type_id,
                                            model, description, brand, qty, reorder, discount, price, image, status));
                                }

                                if(i == response.length()-1){
                                    OpticalDetailsFragment.adapter = new RVadapter(context, "OpticalDetails", null, OpticalDetailsFragment.mList);
                                    OpticalDetailsFragment.rv.setAdapter(OpticalDetailsFragment.adapter);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("GETPRODUCTSBYID", error.getMessage());
            }
        });
        mRequestQueue.add(jsonArrayRequest);
    }

    public void getServicesByShopId(final JSONObject request, String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, request,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            for(int i = 0; i< response.length(); i++){
                                JSONObject obj = response.getJSONObject(i);

                                String name = obj.getString("name");
                                String status = obj.getString("status");

                                if(status.equalsIgnoreCase("active")){
                                    CheckBox tv = new CheckBox(context);
                                    //tv.setPadding(10,10,10,10);
                                    tv.setLayoutParams(params);
                                    tv.setText(name);
                                    tv.setTextSize(18);

                                    OpticalDetailsFragment.service_list.add(tv);
                                    OpticalDetailsFragment.services.addView(tv);

                                    tv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            for (int i = 0; i < OpticalDetailsFragment.service_list.size(); i++) {
                                                if (OpticalDetailsFragment.service_list.get(i).isChecked()) {
                                                    OpticalDetailsFragment.add_appointment.setVisibility(View.VISIBLE);
                                                    OpticalDetailsFragment.scroll.fullScroll(View.FOCUS_DOWN);
                                                    break;
                                                } else {
                                                    OpticalDetailsFragment.add_appointment.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("GETSERVICESBYSHOPID", error.getMessage());
            }
        });
        mRequestQueue.add(jsonArrayRequest);
    }

    public void getReserveItemsById(final JSONObject request, final String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, request,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i< response.length(); i++){
                                Log.e("response", response.toString());
                                JSONObject obj = response.getJSONObject(i);

                                String status = obj.getString("status");

                                if(status.equalsIgnoreCase("active")){
                                    String res_id = obj.getString("reserve_id");
                                    String optprod_id = obj.getString("optprod_id");
                                    String patient_id = obj.getString("patient_id");
                                    String start_date = obj.getString("start_date");
                                    String end_date = obj.getString("end_date");
                                    String res_qty = obj.getString("qty");

                                    JSONObject obj1 = obj.getJSONObject("product");
                                    String prod_id = obj1.getString("prod_id");
                                    String optshop_id = obj1.getString("opt_id");
                                    String type_id = obj1.getString("type_id");
                                    String model = obj1.getString("model");
                                    String description = obj1.getString("description");
                                    String brand = obj1.getString("brand");
                                    String qty = obj1.getString("qty");
                                    String reorder = obj1.getString("reorder");
                                    String discount = obj1.getString("discount");
                                    String price = obj1.getString("price");
                                    String image = obj1.getString("optprod_img");
                                    String prod_status = obj1.getString("status");

                                    Products prod = new Products(prod_id, optshop_id, type_id,
                                            model, description, brand, qty, reorder, discount, price, image, prod_status);

                                    JSONObject obj2 = obj.getJSONObject("opt_shop");
                                    String id = obj2.getString("id");
                                    String name = obj2.getString("name");
                                    String address = obj2.getString("address");
                                    String email = obj2.getString("email");
                                    String phone = obj2.getString("phone");
                                    String optshop_image = obj2.getString("image");
                                    String term = obj2.getString("term");

                                    OpticalShop opt_shop = new OpticalShop(id, name, address, email, phone, optshop_image, term);

                                    ReservedItemsFragment.mList.add(new ReservedItem(res_id, optprod_id, patient_id, start_date,
                                            end_date, res_qty, prod, opt_shop));
                                }

                                if(i == response.length()-1){
                                    ReservedItemsFragment.adapter = new RVadapter(context, "ReserveFragment", ReservedItemsFragment.mList);
                                    ReservedItemsFragment.rv.setAdapter(ReservedItemsFragment.adapter);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("GETRESERVEDITEMSBYID", error.getMessage());
                getReserveItemsById(request, url);
            }
        });
        mRequestQueue.add(jsonArrayRequest);
    }

    public void getAppointmentsById(final JSONObject request, final String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, request,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i< response.length(); i++){
                                Log.e("response", response.toString());
                                JSONObject obj = response.getJSONObject(i);

                                String status = obj.getString("status");

                                if(!status.equalsIgnoreCase("cancelled")){
                                    String app_id = obj.getString("app_id");
                                    String app_date = obj.getString("app_date");
                                    String app_time = obj.getString("app_time");
                                    String services = obj.getString("services");

                                    JSONObject obj2 = obj.getJSONObject("opt_shop");
                                    String id = obj2.getString("id");
                                    String name = obj2.getString("name");
                                    String address = obj2.getString("address");
                                    String email = obj2.getString("email");
                                    String phone = obj2.getString("phone");
                                    String optshop_image = obj2.getString("image");
                                    String term = obj2.getString("term");

                                    OpticalShop opt_shop = new OpticalShop(id, name, address, email, phone, optshop_image, term);

                                    AppointmentFragment.mList.add(new Appointment(app_id, services, app_date, app_time,
                                            status, opt_shop));
                                }

                                if(i == response.length()-1){
                                    AppointmentFragment.adapter = new RVadapter(context, "AppointmentFragment", null,
                                            AppointmentFragment.mList, null);
                                    AppointmentFragment.rv.setAdapter(AppointmentFragment.adapter);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("GETAPPOINTMENTSBYID", error.getMessage());
            }
        });
        mRequestQueue.add(jsonArrayRequest);
    }

    public void reserveProduct(final JSONObject request, String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, request,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length() == 0) Toast.makeText(context, "Unable to reserve! Please check your Reserved Items!",
                                Toast.LENGTH_LONG).show();
                        else{
                            Toast.makeText(context, "Successfully Reserved Item!",
                                    Toast.LENGTH_LONG).show();
                            DrawerActivity.fm.popBackStackImmediate();
                            OpticalDetailsFragment.refreshPage();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("RESERVEPRODUCT", error.getMessage());
            }
        });
        mRequestQueue.add(jsonArrayRequest);
    }

    public void getAllOpticalShops(String url, final Location location){
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, "",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i<response.length(); i++){
                            try{
                                JSONObject obj = response.getJSONObject(i);
                                double lat = Double.parseDouble(obj.getString("latitude"));
                                double lng = Double.parseDouble(obj.getString("longitude"));
                                String name = obj.getString("name");

                                if(context instanceof MapsActivity) {
                                    Location optLocation = new Location("");
                                    optLocation.setLatitude(lat);
                                    optLocation.setLongitude(lng);

                                    if (Math.round(location.distanceTo(optLocation)) <= 1000) {
                                        getImageUrl(obj.getString("image"), lat, lng, name);
                                    }

                                    Log.e("mapping damn it", i+"");

                                    if(i == response.length()-1) MapsActivity.pd.dismiss();
                                }else if(context instanceof DrawerActivity){
                                    String id = obj.getString("id");
                                    String address = obj.getString("address");
                                    String email = obj.getString("email");
                                    String phone = obj.getString("phone");
                                    String image = obj.getString("image");
                                    String term = obj.getString("term");

                                    HomeFragment.mList.add(new OpticalShop(id, name, address, email, phone, image, term));

                                    if(i == response.length()-1){
                                        HomeFragment.adapter = new RVadapter(context, "HomeFragment", HomeFragment.mList, null);
                                        HomeFragment.rv.setAdapter(HomeFragment.adapter);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if(response.length() == 0 && context instanceof MapsActivity) MapsActivity.pd.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("GETALLOPTICALSHOP", error.getMessage());
                getAllOpticalShops(Constants.getAllOpticalShops, location);
            }
        });
        mRequestQueue.add(jsonArrayRequest);
    }

    public void getImageUrl(final String url, final double lat, final double lng, final String name){
        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if(context instanceof MapsActivity && name.equalsIgnoreCase("MyLocationImage")){
                            MapsActivity.bitmap = bitmap;
                        }else if(context instanceof  MapsActivity){
                            View opticalMarker = MapsActivity.inflater.inflate(R.layout.custom_marker, null);
                            CircleImageView civ = (CircleImageView) opticalMarker.findViewById(R.id.marker_image);
                            ImageView iv = (ImageView) opticalMarker.findViewById(R.id.marker_body);
                            civ.setImageBitmap(bitmap);
                            iv.setImageResource(R.drawable.custom_marker2);

                            MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng)).title(name);
                            marker.icon(BitmapDescriptorFactory.fromBitmap(MapsActivity.createDrawableFromView(context, opticalMarker)));
                            MapsActivity.mMap.addMarker(marker);
                        }else if(context instanceof  DrawerActivity){
                             if(DrawerActivity.nav_image != null) DrawerActivity.nav_image.setImageBitmap(bitmap);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("GETIMAGEURL", error.getMessage());
                    }
                });
        mRequestQueue.add(imageRequest);
    }

    public void deleteReservation(JSONObject request, String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, request,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ReservedItemsFragment rif = new ReservedItemsFragment();
                        DrawerActivity.fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        DrawerActivity.ft = DrawerActivity.fm.beginTransaction();
                        DrawerActivity.ft.replace(R.id.fragment_container, rif).commit();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("DELETERESERVATION", error.getMessage());
            }
        });
        mRequestQueue.add(jsonArrayRequest);
    }

    public void cancelAppointment(JSONObject request, String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, request,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        AppointmentFragment af = new AppointmentFragment();
                        DrawerActivity.fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        DrawerActivity.ft = DrawerActivity.fm.beginTransaction();
                        DrawerActivity.ft.replace(R.id.fragment_container, af).commit();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("CANCELAPPOINTMENT", error.getMessage());
            }
        });
        mRequestQueue.add(jsonArrayRequest);
    }
}
