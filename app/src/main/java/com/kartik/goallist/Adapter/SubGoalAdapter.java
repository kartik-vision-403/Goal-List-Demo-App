package com.kartik.goallist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kartik.goallist.Model.AddGoalModel;
import com.kartik.goallist.R;

import java.util.ArrayList;

public class SubGoalAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<AddGoalModel> subGoalList;

    public SubGoalAdapter(Context context, ArrayList<AddGoalModel> subGoalList) {
        this.context = context;
        this.subGoalList = subGoalList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.sub_goal_list_raw, null);
        subGoalViewHolder container = new subGoalViewHolder(view);
        return container;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        subGoalViewHolder container = (subGoalViewHolder) holder;
        AddGoalModel model = subGoalList.get(position);
        container.subGoal.setText(model.getSubGoalTitle());
    }

    @Override
    public int getItemCount() {
        return subGoalList.size();
    }

    public class subGoalViewHolder extends RecyclerView.ViewHolder {
        public TextView subGoal;

        public subGoalViewHolder(@NonNull View itemView) {
            super(itemView);
            subGoal = itemView.findViewById(R.id.txtTitleSubGoalList);
        }
    }
}
