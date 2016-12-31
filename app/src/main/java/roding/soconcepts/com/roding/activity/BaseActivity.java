package roding.soconcepts.com.roding.activity;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.fragment.NavigationDrawerFragment;

public class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;


    protected void setup() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        String title = "";
        ActivityInfo activityInfo = null;
        try {
            activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (activityInfo != null) {
            title = activityInfo.loadLabel(getPackageManager())
                    .toString();
        }
        if (title != null && title.length() > 0) {
            ((TextView) toolbar.findViewById(R.id.textTitle)).setText(title);
            toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
        } else {
            toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        if (title != null && title.length() > 0) {
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.drawable.drawer_white);
        } else {
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        }
    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_search) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
