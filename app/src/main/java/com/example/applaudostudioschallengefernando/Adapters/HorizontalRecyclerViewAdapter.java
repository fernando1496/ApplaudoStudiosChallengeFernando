package com.example.applaudostudioschallengefernando.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.applaudostudioschallengefernando.Controller.DetailActivityController;
import com.example.applaudostudioschallengefernando.Data.QueryDB;
import com.example.applaudostudioschallengefernando.R;
import java.util.ArrayList;

public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewAdapter.ViewHolder> {

    //variables to display anime
    private ArrayList<Integer> mAnimeId = new ArrayList<>();
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mImageUrl = new ArrayList<>();
    private Context cContext;



    public HorizontalRecyclerViewAdapter( Context cContext, int iCategoryId) {

        try {
            this.cContext = cContext;
            QueryDB dbQrs = new QueryDB(cContext);
            dbQrs.open();

            Cursor cAnimes = dbQrs.GetAnimesByCategories(iCategoryId);
            if (cAnimes.moveToFirst()) {
                do {

                    int sAnimeId = cAnimes.getInt(0);
                    String sAnimteTitle = cAnimes.getString(1);
                    String sImageUrl = cAnimes.getString(2);

                    mAnimeId.add(sAnimeId);
                    mTitle.add(sAnimteTitle);
                    mImageUrl.add(sImageUrl);

                } while (cAnimes.moveToNext());
            }
            dbQrs.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vView = LayoutInflater.from(parent.getContext())
              .inflate(R.layout.cardview_item, parent, false);



        return new ViewHolder(vView);
    }

    @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // bind the anime name, id and image for each item

            Glide.with(cContext).asBitmap().load(mImageUrl.get(position)).into(holder.igImageHolder);

            holder.tvTextName.setText(mTitle.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(cContext, DetailActivityController.class);
                    intent.putExtra("AnimeId", mAnimeId.get(position));
                    cContext.startActivity(intent);
                }
            });


        }

    @Override
    public int getItemCount() {

        return mAnimeId.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView igImageHolder;
        TextView tvTextName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //set items from XML
            igImageHolder = itemView.findViewById(R.id.igImageHolder);
            tvTextName = itemView.findViewById(R.id.tvTextName);
        }

    }
}
