package roding.soconcepts.com.roding.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.model.DrawerMenu;


/**
 * Created by Soul on 11/3/2015.
 */
public class DrawerMenuAdapter extends RecyclerView.Adapter<DrawerMenuAdapter.DrawerMenuViewHolder> {

    public static int selected_item = 0;
    private LayoutInflater inflater;
    List<DrawerMenu> data = Collections.emptyList();
    private Context context;

    public DrawerMenuAdapter(Context context, List<DrawerMenu> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public DrawerMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.menu_row, parent, false);
        //FontHelper.applyFont(view.getContext(), view.findViewById(R.id.menuRow), "fonts/Montserrat-Regular.ttf");
        DrawerMenuViewHolder holder = new DrawerMenuViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DrawerMenuViewHolder holder, int position) {
        DrawerMenu current = data.get(position);

        holder.menuTitle.setText(current.title);
        if(position == selected_item)
        {
            holder.menuTitle.setTextColor(context.getResources().getColor(R.color.red));
        }
        else
        {
            holder.menuTitle.setTextColor(context.getResources().getColor(R.color.white));
        }
        holder.icon.setImageResource(current.iconId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DrawerMenuViewHolder extends RecyclerView.ViewHolder {

        TextView menuTitle;
        ImageView icon;

        public DrawerMenuViewHolder(View itemView) {
            super(itemView);

            menuTitle = (TextView) itemView.findViewById(R.id.menuText);
            icon = (ImageView) itemView.findViewById(R.id.menuIcon);
        }
    }
}
