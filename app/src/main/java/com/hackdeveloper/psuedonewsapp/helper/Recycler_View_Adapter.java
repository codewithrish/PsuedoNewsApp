package com.hackdeveloper.psuedonewsapp.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hackdeveloper.psuedonewsapp.R;
import com.hackdeveloper.psuedonewsapp.activity.BrowserActivity;
import com.hackdeveloper.psuedonewsapp.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.hackdeveloper.psuedonewsapp.activity.MainActivity.favouriteItems;

public class Recycler_View_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<NewsItem> list = Collections.emptyList();
    public ArrayList<NewsItem> arraylist;
    Context context;

    public Recycler_View_Adapter(List<NewsItem> list, Context context) {
        this.list = list;
        this.context = context;

        this.arraylist = new ArrayList<NewsItem>();
        this.arraylist.addAll(list);
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder {


        private TextView ID;
        private TextView TITLE;
        private TextView URL;
        private TextView PUBLISHER;
        private TextView CATEGORY;
        private TextView HOSTNAME;
        private TextView TIMESTAMP;
        private ImageView FAVOURITE;

        MenuItemViewHolder(View view) {
            super(view);
            ID = view.findViewById(R.id.id);
            TITLE = view.findViewById(R.id.title);
            URL = view.findViewById(R.id.url);
            PUBLISHER = view.findViewById(R.id.publisher);
            CATEGORY = view.findViewById(R.id.category);
            HOSTNAME = view.findViewById(R.id.hostname);
            TIMESTAMP = view.findViewById(R.id.timestamp);
            FAVOURITE = view.findViewById(R.id.favourites);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View menuItemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_layout, parent, false
        );
        return new MenuItemViewHolder(menuItemLayoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final MenuItemViewHolder menuItemHolder = (MenuItemViewHolder) holder;
        final NewsItem data = (NewsItem) list.get(position);

        menuItemHolder.ID.setText(data.getID());
        menuItemHolder.TITLE.setText(data.getTITLE());
        menuItemHolder.URL.setText(data.getURL());
        menuItemHolder.PUBLISHER.setText(data.getPUBLISHER());
        menuItemHolder.CATEGORY.setText("category: "+data.getCATEGORY());
        menuItemHolder.HOSTNAME.setText("source: "+data.getHOSTNAME()+" ");

        menuItemHolder.URL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postUrl = list.get(position).getURL();

                Intent i = new Intent(menuItemHolder.URL.getContext(), BrowserActivity.class);
                i.putExtra("postUrl", postUrl);
                menuItemHolder.URL.getContext().startActivity(i);
            }
        });

        menuItemHolder.FAVOURITE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.isFAVOURITE()) {
                    menuItemHolder.FAVOURITE.setImageResource(R.drawable.unlike);
                    data.setFAVOURITE(false);
                    Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();
                } else {
                    favouriteItems.add(data);
                    menuItemHolder.FAVOURITE.setImageResource(R.drawable.like);
                    data.setFAVOURITE(true);
                    Toast.makeText(context, "Added To Favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy' 'HH:mm:ss");
        String time = simpleDateFormat.format(data.getTIMESTAMP());
        menuItemHolder.TIMESTAMP.setText(time + "   .");

        animate(menuItemHolder);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.bounce_from_bottom);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

    // Filter Class
    public void filter(String charText, String type) {

        Log.i("charText", charText);

        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        else
        {
            for (NewsItem ni : arraylist)
            {
                if (type.equals("p")) {
                    if (ni.getPUBLISHER().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        list.add(ni);
                    }
                } else if (type.equals("c")) {
                    if (ni.getCATEGORY().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        list.add(ni);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void old_to_new() {
        Collections.sort(list);
        Collections.reverse(list);
        notifyDataSetChanged();
    }
    public void new_to_old() {
        Collections.sort(list);
        notifyDataSetChanged();
    }


}