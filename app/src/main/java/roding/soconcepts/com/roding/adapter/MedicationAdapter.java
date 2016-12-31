package roding.soconcepts.com.roding.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.StreamHandler;

import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.dal.StateDAL;
import roding.soconcepts.com.roding.model.Medication;
import roding.soconcepts.com.roding.util.FontHelper;

/**
 * Created by mac on 9/3/16.
 */
public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.CustomViewHolder> {
    List<Medication> detailList = new ArrayList<>();
    ViewGroup parent = null;
    int resource = R.layout.medication_row;
    int type;

    public MedicationAdapter(List<Medication> myList, int type) {
        detailList = myList;
        this.type = type;
        if (type == 2) {
            resource = R.layout.medication_row_2;
        }
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        this.parent = parent;
        View v = LayoutInflater.from(parent.getContext()).inflate(resource, null, false);
        FontHelper.applyFont(parent.getContext(), v, "fonts/TrebuchetMS.ttf");

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        CustomViewHolder customViewHolder = new CustomViewHolder(v);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(MedicationAdapter.CustomViewHolder holder, final int position) {
        final Medication medication = detailList.get(position);
        holder.tvTotalPrice.setText(String.valueOf(medication.getTotalPrice()));
        holder.tvQuantity.setText(String.valueOf(medication.getQuantity()));
        holder.tvUnitPrice.setText(String.valueOf(medication.getUnitPrice()));
        holder.tvMedication.setText(medication.getMedication());
        if (holder.ivRemove != null) {
            holder.ivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Removing position :: " + position);
                    detailList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        if (position == detailList.size()-1 && type == 2) {
            holder.tvMedication.setBackgroundResource(R.drawable.leftbottom);
            holder.tvQuantity.setBackgroundResource(R.drawable.leftrightbottom);
            holder.tvTotalPrice.setBackgroundResource(R.drawable.rightbottomborder);
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView ivRemove;
        protected TextView tvMedication;
        protected TextView tvUnitPrice;
        protected TextView tvQuantity;
        protected TextView tvTotalPrice;

        public CustomViewHolder(View view) {
            super(view);
            this.ivRemove = (ImageView) view.findViewById(R.id.removeIcon);
            this.tvMedication = (TextView) view.findViewById(R.id.medicationText);
            this.tvUnitPrice = (TextView) view.findViewById(R.id.unitPriceText);
            this.tvQuantity = (TextView) view.findViewById(R.id.quantityText);
            this.tvTotalPrice = (TextView) view.findViewById(R.id.totalPriceText);
        }
    }
}
