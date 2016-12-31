package roding.soconcepts.com.roding.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.activity.AboutUsActivity;
import roding.soconcepts.com.roding.activity.ChangePasswordActivity;
import roding.soconcepts.com.roding.activity.CompanyMemberSearchActivity;
import roding.soconcepts.com.roding.activity.ContactUsActivity;
import roding.soconcepts.com.roding.activity.FaqsActivity;
import roding.soconcepts.com.roding.activity.HomeActivity;
import roding.soconcepts.com.roding.activity.HospitalLocatorActivity;
import roding.soconcepts.com.roding.activity.LoginActivity;
import roding.soconcepts.com.roding.activity.MemberSearchActivity;
import roding.soconcepts.com.roding.activity.MyDetailsActivity;
import roding.soconcepts.com.roding.activity.MyRodingIDActivity;
import roding.soconcepts.com.roding.activity.RequestUsageActivity;
import roding.soconcepts.com.roding.activity.SocialsActivity;
import roding.soconcepts.com.roding.activity.SubmitClaimActivity;
import roding.soconcepts.com.roding.adapter.DrawerMenuAdapter;
import roding.soconcepts.com.roding.listener.ClickListener;
import roding.soconcepts.com.roding.listener.RecyclerTouchListener;
import roding.soconcepts.com.roding.model.DrawerMenu;
import roding.soconcepts.com.roding.util.FontHelper;
import roding.soconcepts.com.roding.util.RodingPreferences;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NavigationDrawerFragment OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NavigationDrawerFragment newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationDrawerFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private RecyclerView recyclerView;
    private static final String PREF_FILE_NAME = "preffile";
    private static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private DrawerMenuAdapter drawerMenuAdapter;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    private View containerView;
    private TextView userNameText;
    private TextView pinText;

    public static List<DrawerMenu> drawerMenuList(String[] titles, int[] icons) {
        List<DrawerMenu> data = new ArrayList<>();
        System.out.println("Menu Size is :: " + titles.length);
        for (int i = 0; i < icons.length && i < titles.length; i++) {
            DrawerMenu drawerMenu = new DrawerMenu();
            drawerMenu.iconId = icons[i];
            drawerMenu.title = titles[i];

            data.add(drawerMenu);
        }

        return data;
    }

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        FontHelper.applyFont(getActivity(), layout.findViewById(R.id.navigationDrawer), "fonts/TrebuchetMS.ttf");
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        int[] icons = {R.drawable.homeicon, /*R.drawable.locationicon,*/ R.drawable.profileicon, R.drawable.hospitalid, R.drawable.socialicon, R.drawable.faqicon, R.drawable.abouticon, R.drawable.contacticon, R.drawable.padlockicon, R.drawable.logouticon};
        String[] titles = {"HOME", /*"HOSPITAL LOCATOR",*/ "MY DETAILS", "MY RODING ID", "SOCIALS", "FAQS", "ABOUT US", "CONTACT US", "CHANGE PASSWORD", "LOGOUT"};
        final String role = RodingPreferences.read(getActivity(), "role");
        if (role.equals("Provider")) {
            icons = new int[]{R.drawable.homeicon, R.drawable.submitclaimicon, R.drawable.profileicon, R.drawable.membersearch, R.drawable.socialicon, R.drawable.faqicon, R.drawable.abouticon, R.drawable.contacticon, R.drawable.padlockicon, R.drawable.logouticon};
            titles = new String[]{"HOME", "SUBMIT CLAIM", "MY DETAILS", "MEMBER SEARCH", "SOCIALS", "FAQS", "ABOUT US", "CONTACT US", "CHANGE PASSWORD", "LOGOUT"};
        }

        if (role.equals("Client")) {
            icons = new int[]{R.drawable.homeicon, /*R.drawable.locationicon,*/ R.drawable.profileicon, R.drawable.membersearch, R.drawable.requestusageicon, R.drawable.socialicon, R.drawable.faqicon, R.drawable.abouticon, R.drawable.contacticon, R.drawable.padlockicon, R.drawable.logouticon};
            titles = new String[]{"HOME", /*"HOSPITAL LOCATOR",*/ "MY DETAILS", "MEMBER SEARCH", "REQUEST USAGE", "SOCIALS", "FAQS", "ABOUT US", "CONTACT US", "CHANGE PASSWORD", "LOGOUT"};
        }
        drawerMenuAdapter = new DrawerMenuAdapter(getActivity(), drawerMenuList(titles, icons));
        recyclerView.setAdapter(drawerMenuAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View v, int position) {
                DrawerMenuAdapter.selected_item = position;
                if (position == 0) {
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                } else if (position == 1) {
                    if (role.equals("Provider")) {
                        startActivity(new Intent(getActivity(), SubmitClaimActivity.class));
                    } else {
                        //startActivity(new Intent(getActivity(), HospitalLocatorActivity.class));
                        startActivity(new Intent(getActivity(), MyDetailsActivity.class));
                    }
                } else if (position == 2) {
                    //startActivity(new Intent(getActivity(), MyDetailsActivity.class));
                    if (role.equals("Provider")) {
                        startActivity(new Intent(getActivity(), MemberSearchActivity.class));
                    } else if (role.equals("Client")) {
                        startActivity(new Intent(getActivity(), CompanyMemberSearchActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), MyRodingIDActivity.class));
                    }
                } else if (position == 3) {
                    /*if (role.equals("Provider")) {
                        startActivity(new Intent(getActivity(), MemberSearchActivity.class));
                    } else if (role.equals("Client")) {
                        startActivity(new Intent(getActivity(), CompanyMemberSearchActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), MyRodingIDActivity.class));
                    }*/
                    if (role.equals("Client")) {
                        startActivity(new Intent(getActivity(), RequestUsageActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), SocialsActivity.class));
                    }
                } else if (position == 4) {
                    /*if (role.equals("Client")) {
                        startActivity(new Intent(getActivity(), RequestUsageActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), SocialsActivity.class));
                    }*/
                    if (role.equals("Client")) {
                        startActivity(new Intent(getActivity(), SocialsActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), FaqsActivity.class));
                    }
                } else if (position == 5) {
                    /*if (role.equals("Client")) {
                        startActivity(new Intent(getActivity(), SocialsActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), FaqsActivity.class));
                    }*/
                    if (role.equals("Client")) {
                        startActivity(new Intent(getActivity(), FaqsActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), AboutUsActivity.class));
                    }
                } else if (position == 6) {
                    /*if (role.equals("Client")) {
                        startActivity(new Intent(getActivity(), FaqsActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), AboutUsActivity.class));
                    }*/
                    if (role.equals("Client")) {
                        startActivity(new Intent(getActivity(), AboutUsActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), ContactUsActivity.class));
                    }
                } else if (position == 7) {
                    /*if (role.equals("Client")) {
                        startActivity(new Intent(getActivity(), AboutUsActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), ContactUsActivity.class));
                    }*/
                    if (role.equals("Client")) {
                        startActivity(new Intent(getActivity(), ContactUsActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                    }
                } else if (position == 8) {
                    /*if (role.equals("Client")) {
                        startActivity(new Intent(getActivity(), ContactUsActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                    }*/
                    if (role.equals("Client")) {
                        startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                    } else {
                        RodingPreferences.write(getActivity(), "token", null);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else if (position == 9) {
                    /*if (role.equals("Client")) {
                        startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                    } else {
                        RodingPreferences.write(getActivity(), "token", null);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }*/
                    RodingPreferences.write(getActivity(), "token", null);
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (position == 10) {
                    RodingPreferences.write(getActivity(), "token", null);
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View v, int position) {
                //Toast.makeText(getActivity(), "onLongClick " + position, Toast.LENGTH_SHORT).show();
            }
        }));
        /*userNameText = (TextView) layout.findViewById(R.id.user_name_text);
        pinText = (TextView) layout.findViewById(R.id.pin_text);
        User user = UserDAL.getInstance(getActivity()).getUser();
        userNameText.setText(user.getFirstname() + " " + user.getLastname());
        pinText.setText(user.getUserPin());*/
        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        setUp(fragmentId, drawerLayout, toolbar, R.drawable.drawer);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar, int drawer) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(drawer);
        //mDrawerToggle.syncState();

        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("drawer clicked");
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }
}
