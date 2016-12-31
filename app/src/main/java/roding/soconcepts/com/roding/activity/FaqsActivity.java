package roding.soconcepts.com.roding.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.model.FAQ;
import roding.soconcepts.com.roding.model.Hospital;
import roding.soconcepts.com.roding.util.CommonUtils;
import roding.soconcepts.com.roding.util.FontHelper;
import roding.soconcepts.com.roding.util.GPSTracker;

public class FaqsActivity extends BaseActivity {

    private static final String TAG = "FaqsActivity";
    private LinearLayout faqContainer;
    private List<FAQ> faqs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);
        FontHelper.applyFont(this, findViewById(R.id.faqs_activity), "fonts/TrebuchetMS.ttf");

        setup();

        initFaqs();
        faqContainer = (LinearLayout) findViewById(R.id.faqContainer);

        int i = 0;

        View view = new View(this);
        view.setBackgroundColor(getResources().getColor(R.color.red));
        LinearLayout.LayoutParams vLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        view.setLayoutParams(vLayoutParams);
        faqContainer.addView(view);

        for (FAQ faq : faqs) {
            LinearLayout faqItemLayout = new LinearLayout(this);
            //LinearLayout.LayoutParams faqItemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            faqItemLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout faqIconLayout = new LinearLayout(this);
            LinearLayout.LayoutParams faqIconParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            faqIconLayout.setLayoutParams(faqIconParams);
            faqIconLayout.setOrientation(LinearLayout.VERTICAL);

            ImageView imageView = new ImageView(this);
            imageView.setPadding(CommonUtils.dpToPx(this, 1), CommonUtils.dpToPx(this, 8), CommonUtils.dpToPx(this, 1), CommonUtils.dpToPx(this, 1));
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.faq_icon));

            faqIconLayout.addView(imageView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            faqItemLayout.addView(faqIconLayout, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            final LinearLayout faqTextLayout = new LinearLayout(this);
            LinearLayout.LayoutParams faqTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            faqTextLayout.setOrientation(LinearLayout.VERTICAL);

            TextView tv = new TextView(this);
            LinearLayout.LayoutParams tvLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tvLayoutParams.setMargins(CommonUtils.dpToPx(this, 0), CommonUtils.dpToPx(this, 5), CommonUtils.dpToPx(this, 0), CommonUtils.dpToPx(this, 5));
            tv.setLayoutParams(tvLayoutParams);
            tv.setPadding(CommonUtils.dpToPx(this, 8), CommonUtils.dpToPx(this, 8), CommonUtils.dpToPx(this, 8), CommonUtils.dpToPx(this, 8));
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setBackgroundColor(getResources().getColor(R.color.white));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tv.setText(faq.getQuestion());
            final int j = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentVisibility = faqTextLayout.findViewById(j).getVisibility();
                    hideAllAccordionViews();
                    faqTextLayout.findViewById(j).setVisibility(currentVisibility == View.GONE ? View.VISIBLE : View.GONE);
                }
            });
            FontHelper.applyFont(this, tv, "fonts/TrebuchetMS.ttf");
            faqTextLayout.addView(tv);

            final LinearLayout faqLayout = new LinearLayout(this);
            LinearLayout.LayoutParams faqLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            faqLayout.setLayoutParams(faqLayoutParams);
            faqLayout.setOrientation(LinearLayout.VERTICAL);
            if (i == 0) {
                faqLayout.setVisibility(View.VISIBLE);
            } else {
                faqLayout.setVisibility(View.GONE);
            }
            faqLayout.setId(i);

            TextView ftv = new TextView(this);
            LinearLayout.LayoutParams ftvLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ftvLayoutParams.setMargins(CommonUtils.dpToPx(this, 0), CommonUtils.dpToPx(this, 7), CommonUtils.dpToPx(this, 0), CommonUtils.dpToPx(this, 7));
            ftv.setLayoutParams(ftvLayoutParams);
            ftv.setTextAppearance(this, android.R.style.TextAppearance_Small);
            ftv.setTextColor(getResources().getColor(R.color.black));
            ftv.setText(faq.getAnswer());
            ftv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            FontHelper.applyFont(this, ftv, "fonts/TrebuchetMS.ttf");
            faqLayout.addView(ftv);


            faqTextLayout.addView(faqLayout);

            faqItemLayout.addView(faqTextLayout, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 9f));
            faqContainer.addView(faqItemLayout);

            view = new View(this);
            view.setBackgroundColor(getResources().getColor(R.color.red));
            view.setLayoutParams(vLayoutParams);
            faqContainer.addView(view);
            i++;
        }
    }

    private void hideAllAccordionViews() {
        int i = 0;
        for (FAQ faq : faqs) {
            faqContainer.findViewById(i).setVisibility(View.GONE);
            i++;
        }
    }

    private void initFaqs() {
        faqs = new ArrayList<>();

        FAQ faq = new FAQ();
        faq.setQuestion("How do I subscribe to the Roding Healthcare package?");
        faq.setAnswer("Simply obtain the RODING HEALTHCARE subscription form from your HR department, download from our website at www.rodinghealthcareltd.com; or request for the subscription form by email to\n\ninfo@rodinghealthcareltd.com, rodinghealthcare@gmail.com ;\n\n" +
                "call the following phone numbers:\n\n+234-1- 8449821\n+234-1- 2952577\n+234 807 420 6599\n+234 807 420 6515\n+2347080601040(Airtel Tollfree)\n+2348031230273(MTN Tollfree)\n\n" +
                "Visit our office at 4, Makoko Road, yaba, Lagos.\n\n" +
                "Or\n\n" +
                "The Roding Medical Centre at 29B Olabode George Street,Victoria Island,Lagos.\n\n" +
                "Or\n\n" +
                "The Roding Reproductive Centre,18b Ogidi Crescent ,Off Alhaji Sule Onabiyi Street Off Christ Avenue off Admiralty Road Off Admiralty Way, Lekki Phase 1, Lagos State, Nigeria.");
        faqs.add(faq);

        faq = new FAQ();
        faq.setQuestion("What information do I need to complete the subscription form?");
        faq.setAnswer("The subscription form must be completed with all the essential data and information requested. Basic personal and medical information (e.g. biodata, contact, next of kin, pre-existing ailment/past medical history) are required for records, production of I.D cards and also contact (s ) in case of emergencies.\n\n" +
                "The form is expected to be filled out, passport photographs of members (with name of members written on the backside) should be attached and submitted to RODING HEALTHCARE via your HR, email, postage, our website or RODING HEALTHCARE office nearest to you.\n\n" +
                "Please note that you only need to complete the first provider column if you and your family are to use the same provider. Please fill in all the requested information carefully to avoid a delay in the production of your ID card.");
        faqs.add(faq);

        faq = new FAQ();
        faq.setQuestion("What do I do if I have registered but my dependents are yet to register e.g spouse, child etc?");
        faq.setAnswer("Simply obtain another subscription form, tick the additional membership column, fill in the other necessary details, attach the passport photograph of the dependant(s ) (with name of dependant(s) written on the backside) and send the form through your HR, by post, email or in person to RODING HEALTHCARE.");
        faqs.add(faq);

        faq = new FAQ();
        faq.setQuestion("What do I do If I registered a long time ago and I have not received my ID card?");
        faq.setAnswer("Kindly contact RODING HEALTHCARE on the numbers stated above , send us an email, complete the enquiry page on our website or visit RODING HEALTHCARE office. The issue would be sorted out immediately.");
        faqs.add(faq);

        faq = new FAQ();
        faq.setQuestion("What do I do if I was not attended to in the hospital despite presenting an ID card?");
        faq.setAnswer("Please do not leave the hospital without calling our helplines, toll free lines\n\n" +
                "+2347080601040(Airtel Tollfree);+2348031230273(MTN Tollfree);\n\n" +
                "or sending a text message to the cell phone numbers above. You may also send us an email or a tweet.");
        faqs.add(faq);

        faq = new FAQ();
        faq.setQuestion("What do I do if I was told to pay for services received or other things by the provider?");
        faq.setAnswer("Please be informed that you are not to make any payment for a benefit covered under your scheme. Please call or send text to the cell phone numbers stated above. You may also send us an email, a tweet or visit our office.");
        faqs.add(faq);

        faq = new FAQ();
        faq.setQuestion("If I want to change my provider, what do I do?");
        faq.setAnswer("Simply send us an email or a letter with your details or download the change of provider form from our website and fill it ; the change would be effected immediately as long as the provider is on our network.\n\nFor providers not on our network you would need to give us at least a month to accredit the provider and get back to you.");
        faqs.add(faq);

        faq = new FAQ();
        faq.setQuestion("If I am living apart from my family, how would my family access care?");
        faq.setAnswer("You do not need to worry. There are provisions for family leaving apart, simply complete the principal and dependants column on your subscription form. You do not need to complete more than one provider column if you and your family are to use the same provider.");
        faqs.add(faq);

        faq = new FAQ();
        faq.setQuestion("If I am not satisfied with the services of a provider, how do I lodge my complaints?");
        faq.setAnswer("Please call the numbers above, send us an email, download and fill the Member's complaints form or write a letter and send to our office.");
        faqs.add(faq);

        faq = new FAQ();
        faq.setQuestion("Can I continue to use my old healthcare provider after my subscription?");
        faq.setAnswer("Yes, only if the hospital is registered in our network of providers.");
        faqs.add(faq);

        faq = new FAQ();
        faq.setQuestion("What happens in an emergency where I cannot reach a provider on the network?");
        faq.setAnswer("You may get treated in the nearest hospital and notify RODING HEALTHCARE within 48hrs of occurrence.");
        faqs.add(faq);

        faq = new FAQ();
        faq.setQuestion("If I am a traveller, can I access care anywhere in the country apart from my chosen provider?");
        faq.setAnswer("Yes. Call the RODING HEALTHCARE numbers above and your access will be pre-authorized.");
        faqs.add(faq);
    }

}
