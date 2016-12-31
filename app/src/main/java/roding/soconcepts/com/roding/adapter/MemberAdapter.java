package roding.soconcepts.com.roding.adapter;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import roding.soconcepts.com.roding.dal.StateDAL;
import roding.soconcepts.com.roding.model.Hospital;
import roding.soconcepts.com.roding.model.Member;
import roding.soconcepts.com.roding.util.FontHelper;
import roding.soconcepts.com.roding.util.GPSTracker;

/**
 * Created by mac on 9/3/16.
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.CustomViewHolder>
{
    List<Member> memberList = new ArrayList<>();
    ViewGroup parent = null;

    public MemberAdapter(List<Member> myList)
    {
        memberList = myList;
    }
    @Override
    public int getItemCount()
    {
        return memberList.size();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, final int viewType)
    {
        this.parent = parent;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.memberview_item, null);
        FontHelper.applyFont(parent.getContext(), v, "fonts/TrebuchetMS.ttf");

        CustomViewHolder customViewHolder = new CustomViewHolder(v);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(MemberAdapter.CustomViewHolder holder, final int position)
    {
        final Member member = memberList.get(position);
        holder.tvStatus.setText(String.valueOf(member.isActive() ? "ACTIVE" : "INACTIVE"));
        holder.tvMemberId.setText(member.getMemberId());
        holder.tvOtherNames.setText(member.getOtherNames());
        holder.tvSurname.setText(member.getSurname());
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView tvSurname;
        protected TextView tvOtherNames;
        protected TextView tvMemberId;
        protected TextView tvStatus;
        public CustomViewHolder(View view)
        {
            super(view);
            this.tvSurname = (TextView) view.findViewById(R.id.tv_surname);
            this.tvOtherNames = (TextView) view.findViewById(R.id.tv_othername);
            this.tvMemberId = (TextView) view.findViewById(R.id.tv_idno);
            this.tvStatus = (TextView) view.findViewById(R.id.tv_status);
        }
    }
}
