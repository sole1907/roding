package roding.soconcepts.com.roding.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.util.FontHelper;

public class SocialsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socials);
        FontHelper.applyFont(this, findViewById(R.id.socials_activity), "fonts/TrebuchetMS.ttf");

        setup();

        Button fbButton = (Button) findViewById(R.id.facebookBtn);
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/RodingHealthcareLtd"));
                startActivity(browserIntent);
            }
        });

        Button tweetButton = (Button) findViewById(R.id.twitterBtn);
        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/rodinghc"));
                startActivity(browserIntent);
            }
        });

        Button linkedinButton = (Button) findViewById(R.id.linkedinBtn);
        linkedinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company/roding-healthcare-ltd"));
                startActivity(browserIntent);
            }
        });
    }
}
