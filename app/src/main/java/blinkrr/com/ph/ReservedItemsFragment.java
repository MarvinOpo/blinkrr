package blinkrr.com.ph;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReservedItemsFragment extends Fragment {
    public static RecyclerView rv;
    public static LinearLayoutManager llm;
    public static ArrayList<ReservedItem> mList = new ArrayList<ReservedItem>();
    public static RVadapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mList.clear();
        try {
            JSONObject request = new JSONObject();
            request.accumulate("id", DrawerActivity.p.id);

            JSONParser.getInstance(getContext()).getReserveItemsById(request, Constants.getReserveItemsById);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        rv = (RecyclerView) view.findViewById(R.id.products_recycler);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        return view;
    }
}
