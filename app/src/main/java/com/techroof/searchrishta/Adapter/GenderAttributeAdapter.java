package com.techroof.searchrishta.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techroof.searchrishta.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GenderAttributeAdapter extends RecyclerView.Adapter<GenderAttributeAdapter.ViewHolder> {

    private Context context;
    private List<String> attributeNameArrayList;
    private int selectedItem;
    public String gender="null";

    public GenderAttributeAdapter(Context context, List<String> attributeNameArrayList) {
        this.context = context;
        this.attributeNameArrayList = attributeNameArrayList;

        selectedItem = -1;

    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reg_attribute_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GenderAttributeAdapter.ViewHolder holder, int position) {

        holder.attName.setText(attributeNameArrayList.get(position));

        holder.attName.setText(attributeNameArrayList.get(position));

        if (holder.getAdapterPosition()==selectedItem){

            holder.attName.setBackground(context.getResources().getDrawable(R.drawable.attribute_bg_selected));
            holder.attName.setTextColor(Color.WHITE);


        }else{

            holder.attName.setBackground(context.getResources().getDrawable(R.drawable.attribute_bg));
            holder.attName.setTextColor(context.getResources().getColor(R.color.lightBlack));

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.attName.setBackground(context.getResources().getDrawable(R.drawable.attribute_bg_selected));
                holder.attName.setTextColor(Color.WHITE);

                selectedItem=holder.getAdapterPosition();

                gender=attributeNameArrayList.get(selectedItem);

                notifyDataSetChanged();

            }
        });


    }

    @Override
    public int getItemCount() {
        return attributeNameArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView attName;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            attName = itemView.findViewById(R.id.attribute_text);

        }
    }
}
