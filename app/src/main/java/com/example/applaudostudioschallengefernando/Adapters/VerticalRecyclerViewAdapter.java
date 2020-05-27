package com.example.applaudostudioschallengefernando.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.applaudostudioschallengefernando.R;
import java.util.ArrayList;


public class VerticalRecyclerViewAdapter extends RecyclerView.Adapter<VerticalRecyclerViewAdapter.ViewHolder> {

    //variables to display categories
    private ArrayList<String> CategoryTitle = new ArrayList<>();
    private ArrayList<Integer> iCategoryId = new ArrayList<>();
    private Context cContext;

    public VerticalRecyclerViewAdapter(ArrayList<String> CategoryTitle,ArrayList<Integer> iCategoryId, Context cContext ) {
        this.CategoryTitle = CategoryTitle;
        this.iCategoryId = iCategoryId;
        this.cContext = cContext;
    }

    //This are the required method for recytcler view to work properly
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item , parent, false);
        return new ViewHolder(vView);
    }

    @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // bind the anime name, id and image for each item
        holder.tvCategory.setText(CategoryTitle.get(position));

        LinearLayoutManager layoutManager = new LinearLayoutManager(cContext, LinearLayoutManager.HORIZONTAL, false);
        holder.rvHorizontal.setLayoutManager(layoutManager);
        HorizontalRecyclerViewAdapter adapter = new HorizontalRecyclerViewAdapter( cContext, iCategoryId.get(position));
        holder.rvHorizontal.setAdapter(adapter);


        }

    @Override
    public int getItemCount() {
        return CategoryTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvCategory;
        RecyclerView rvHorizontal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //set items from XML
            tvCategory = itemView.findViewById(R.id.tvCategory);
            rvHorizontal = itemView.findViewById(R.id.rvHorizontal);


        }


    }
}
