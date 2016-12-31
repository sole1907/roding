package roding.soconcepts.com.roding.activity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.model.FAQ;
import roding.soconcepts.com.roding.util.CommonUtils;
import roding.soconcepts.com.roding.util.FontHelper;

public class AboutUsActivity extends BaseActivity {

    private static final String TAG = "AboutUsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        FontHelper.applyFont(this, findViewById(R.id.about_us_activity), "fonts/TrebuchetMS.ttf");

        setup();
    }

}
