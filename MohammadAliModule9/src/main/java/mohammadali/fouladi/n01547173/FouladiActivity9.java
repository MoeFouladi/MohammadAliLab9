package mohammadali.fouladi.n01547173;

import static android.content.res.Configuration.UI_MODE_NIGHT_NO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;

public class FouladiActivity9 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
// MohammadAli Fouladi N01547173

    private DrawerLayout drawerLayout;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_MODE = "dark_mode";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(PREF_DARK_MODE, false);
        setTheme(isDarkMode ? R.style.Theme_NavigationDrawerDark : R.style.Theme_NavigationDrawer);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Mo1e()).commit();
            navigationView.setCheckedItem(R.id.Mo1e);
        }


    }
    private void searchGoogle(String searchPhrase) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, searchPhrase);
        startActivity(intent);

        // Hide keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_toggle_mode) {
            // Handle toggle mode action

            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            boolean isDarkMode = sharedPreferences.getBoolean(PREF_DARK_MODE, false);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PREF_DARK_MODE, !isDarkMode);
            editor.apply();
            if (!isDarkMode) {
            setTheme(R.style.Theme_NavigationDrawerDark);}
            else {
setTheme(R.style.Theme_NavigationDrawer);}
recreate();



            return true;
        } else if (id == R.id.action_search) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.Search);
            final EditText input = new EditText(this);
            builder.setView(input);
            builder.setPositiveButton(R.string.Search, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String searchPhrase = input.getText().toString().trim();
                    if (!searchPhrase.isEmpty()) {
                        searchGoogle(searchPhrase);
                    }
                }
            });
            builder.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Mo1e){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Mo1e()).commit();}
        else if (item.getItemId() == R.id.Foula2i){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Foula2i()).commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }


}