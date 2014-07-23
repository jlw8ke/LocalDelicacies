package com.mobiquity.LocalDelicacies;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import com.mobiquity.LocalDelicacies.delicacies.DelicacyClickedEvent;
import com.mobiquity.LocalDelicacies.delicacies.DelicacyDetailActivity;
import com.mobiquity.LocalDelicacies.delicacies.DelicacyListFragment;
import com.mobiquity.LocalDelicacies.location.LocationClickedEvent;
import com.mobiquity.LocalDelicacies.location.LocationDetailActivity;
import com.mobiquity.LocalDelicacies.location.LocationListFragment;
import com.mobiquity.LocalDelicacies.navdrawer.NavigationDrawerClickEvent;
import com.squareup.otto.Subscribe;

public class MainActivity extends Activity
{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence drawerTitle;
    private CharSequence title;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main);
        title = getTitle();
        drawerTitle = getTitle();

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout );
        initDrawer();
        if(savedInstanceState == null)
        {
            title = getString(R.string.locations);
            getActionBar().setTitle(title);
            switchFragment(LocationListFragment.TAG);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate( savedInstanceState );
        drawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationBus.getInstance().register( this );
    }

    @Override
    protected void onPause() {
        super.onPause();
        ApplicationBus.getInstance().unregister( this );
    }

    private void initDrawer()
    {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.open_drawer,
                R.string.close_drawer)
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActionBar().setTitle(title);
                invalidateOptionsMenu();

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Checks if home icon was selected and lets nav drawer handle
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }



    @Subscribe
    public void onNavigationDrawerItemSelected(NavigationDrawerClickEvent event)
    {
        title = event.getTitle();
        drawerLayout.closeDrawer(Gravity.START);
        switchFragment(event.getFragmentTag());

    }

    @Subscribe
    public void onLocationClicked(LocationClickedEvent event)
    {
        Intent intent = LocationDetailActivity.createIntent(this, event.getLocation());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
       startActivity(intent);
    }

    @Subscribe
    public void onDelicacyClicked(DelicacyClickedEvent event)
    {
        Intent intent = DelicacyDetailActivity.createIntent(this, event.getDelicacy());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void switchFragment(String fragmentTag)
    {
        Fragment fragment = null;
        if (fragmentTag.equals(LocationListFragment.TAG))
        {
            fragment = new LocationListFragment();
        } else if(fragmentTag.equals(DelicacyListFragment.TAG))
        {
            fragment = new DelicacyListFragment();
        }

        FragmentManager fragmentManager = getFragmentManager();
        if(fragment != null)
        {
            fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        }

    }
}