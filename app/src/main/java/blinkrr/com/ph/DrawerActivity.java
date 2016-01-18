package blinkrr.com.ph;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static ImageView nav_image;
    public static FragmentManager fm;
    public static LayoutInflater inflater;
    public static Patient p;

    DrawerLayout drawer;
    NavigationView navigationView;
    TextView nav_name, nav_info;

    FragmentTransaction ft;
    HomeFragment hf = new HomeFragment();
    ReservedItemsFragment rif = new ReservedItemsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inflater = getLayoutInflater();
        try{
            Bundle b = this.getIntent().getExtras();
            String id = b.getString("id");
            String name = b.getString("name");
            String contact = b.getString("contact");
            String gender = b.getString("gender");
            String address = b.getString("address");
            String birthday = b.getString("birthday");
            String image = b.getString("image");

            p = new Patient(id, name, contact, gender, address, birthday, image);

            Log.e("Patient", p.name);
        }
        catch(Exception e){
            Log.e("Ex", e.toString());
        }

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, hf).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setUpHeader();
    }

    public void setUpHeader() {
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_drawer);

        nav_image = (ImageView) headerView.findViewById(R.id.nav_image);
        nav_name = (TextView) headerView.findViewById(R.id.nav_name);
        nav_info = (TextView) headerView.findViewById(R.id.nav_info);

        JSONParser parser = new JSONParser(this);
        parser.getImageUrl(p.image, 0, 0, "");

        nav_name.setText(p.name);
        nav_info.setText(p.address);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fm.popBackStackImmediate();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, hf).commit();
        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(this, MapsActivity.class);
            this.startActivity(intent);
        } else if(id == R.id.nav_reserve){
            fm.popBackStackImmediate();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, rif).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
