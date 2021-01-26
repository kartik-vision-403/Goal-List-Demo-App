package com.kartik.goallist.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kartik.goallist.Activity.AddGoalActivity;
import com.kartik.goallist.DataBase.DataBaseHandler;
import com.kartik.goallist.Model.AddGoalModel;
import com.kartik.goallist.R;

import java.util.ArrayList;

public class AddGoalAdapter extends RecyclerView.Adapter  {
    private Context context;
    public static ArrayList<AddGoalModel> addGoalList;
    public DataBaseHandler dbHandler;

    public AddGoalAdapter(Context context, ArrayList<AddGoalModel> addGoalList) {
        this.context = context;
        this.addGoalList = addGoalList;
        dbHandler = new DataBaseHandler(context);
        setHasStableIds(true);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_goal_row, parent, false);
        return new addGoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        addGoalViewHolder container = (addGoalViewHolder) holder;
        AddGoalModel model = addGoalList.get(position);
        container.textGoalTitle.setText(model.getSubGoalTitle());

        container.textGoalTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                model.setSubGoalTitle(((addGoalViewHolder) holder).textGoalTitle.getText().toString());

                if (container.textGoalTitle.getText().length() > 0) {
                    AddGoalActivity.checkValidation = 1;
                } else {
                    AddGoalActivity.checkValidation = 0;
                }
            }
        });



        container.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoalList.remove(addGoalList.get(position));
                dbHandler.deleteAnySubGoal(model.getId());
                AddGoalActivity.checkValidation = 1;
                notifyDataSetChanged();
            }
        });

        /*This two lines are use for set focus to newly added record and
         not recyclable old record*/
        container.textGoalTitle.requestFocus();
        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return addGoalList.size();
    }


    public static class addGoalViewHolder extends RecyclerView.ViewHolder {
        public EditText textGoalTitle;
        public ImageView imgRemove;

        public addGoalViewHolder(@NonNull View itemView) {
            super(itemView);
            textGoalTitle = itemView.findViewById(R.id.etSubGoalTitle);
            imgRemove = itemView.findViewById(R.id.imgRemoveSubGoal);
        }
    }
}
