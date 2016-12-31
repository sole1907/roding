package roding.soconcepts.com.roding.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.adapter.MemberAdapter;
import roding.soconcepts.com.roding.dal.MemberDAL;
import roding.soconcepts.com.roding.model.Member;
import roding.soconcepts.com.roding.util.FontHelper;

public class CompanyMemberSearchActivity extends BaseActivity {

    private List<Member> members = new ArrayList<>();
    private static final String TAG = "CompanyMemberSearchActivity";
    private Spinner spinner = null;
    private HashMap<String, String> spinnerMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_member_search);
        FontHelper.applyFont(this, findViewById(R.id.company_member_search_activity), "fonts/TrebuchetMS.ttf");

        setup();

        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        String[] spinnerArray = new String[]{"ACTIVE", "INACTIVE"};

        final EditText surnameText = (EditText) findViewById(R.id.surname_text);
        spinner = (Spinner) findViewById(R.id.status_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.view_spinner_item,
                spinnerArray
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                String status = spinner.getItemAtPosition(arg2).toString();
                if (status.equals("ACTIVE")) {
                    status = "TRUE";
                }
                String surname = surnameText.getText().toString();
                if (surname.isEmpty()) {
                    surname = null;
                }

                members = MemberDAL.getInstance(getBaseContext()).filterHospital(surname, status.equals("TRUE"));

                MemberAdapter recyclerAdapter = new MemberAdapter(members);
                mRecyclerView.setAdapter(recyclerAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        surnameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String status = spinner.getSelectedItem().toString();
                String surname = s.toString();
                if (surname.isEmpty()) {
                    surname = null;
                }
                members = MemberDAL.getInstance(getBaseContext()).filterHospital(surname, status.equals("TRUE"));

                MemberAdapter recyclerAdapter = new MemberAdapter(members);
                mRecyclerView.setAdapter(recyclerAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            }
        });

    }
}
