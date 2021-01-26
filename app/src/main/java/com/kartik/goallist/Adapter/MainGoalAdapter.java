package com.kartik.goallist.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kartik.goallist.Activity.AddGoalActivity;
import com.kartik.goallist.DataBase.DataBaseHandler;
import com.kartik.goallist.Model.AddGoalModel;
import com.kartik.goallist.Model.MainGoalModel;
import com.kartik.goallist.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainGoalAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<MainGoalModel> mainGoalList;
    private ArrayList<AddGoalModel> subGoalList;
    private DataBaseHandler dbHandler;

    public MainGoalAdapter(Context context, ArrayList<MainGoalModel> mainGoalList) {
        this.context = context;
        this.mainGoalList = mainGoalList;
        dbHandler = new DataBaseHandler(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_goal_row, parent, false);
        return new mainGoalViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        mainGoalViewHolder container = (mainGoalViewHolder) holder;
        MainGoalModel model = mainGoalList.get(position);
        container.goalTitle.setText(model.getMaingoalTitle());
        container.endDate.setText(model.getEndDate());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {

            Date selectedDate = sdFormat.parse(container.endDate.getText().toString());
            Date currentDate = Calendar.getInstance().getTime();


            String finalSelectedDate = sdFormat.format(selectedDate);
            String finalCurrentDate = sdFormat.format(currentDate);

            String[] SSDate = finalSelectedDate.split("/");
            int selectedYear = Integer.parseInt(SSDate[2]);
            int selectedMonth = Integer.parseInt(SSDate[1]);

            String[] SCDate = finalCurrentDate.split("/");
            int currentYear = Integer.parseInt(SCDate[2]);
            int currentMonth = Integer.parseInt(SCDate[1]);

            if (currentYear > selectedYear) {
                container.endDate.setTextColor(ContextCompat.getColor(context, R.color.red));

            } else if (currentYear < selectedYear) {
                int tempCurrentMonth = 12 - currentMonth;
                if ((tempCurrentMonth + selectedMonth) > 6) {
                    container.endDate.setTextColor(ContextCompat.getColor(context, R.color.green));
                } else if ((tempCurrentMonth + selectedMonth) >= 3 && (tempCurrentMonth + selectedMonth) <= 6) {
                    container.endDate.setTextColor(ContextCompat.getColor(context, R.color.yello));
                } else {
                    container.endDate.setTextColor(ContextCompat.getColor(context, R.color.red));
                }
            } else {
                if ((selectedMonth - currentMonth) > 6) {
                    container.endDate.setTextColor(ContextCompat.getColor(context, R.color.green));
                } else if (selectedMonth - currentMonth >= 3 && selectedMonth - currentMonth <= 6) {
                    container.endDate.setTextColor(ContextCompat.getColor(context, R.color.yello));
                } else {
                    container.endDate.setTextColor(ContextCompat.getColor(context, R.color.red));
                }
            }
        } catch (Exception e) {
            Log.d("TAG", "Catch Message: " + e.getMessage());
        }

        container.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMainGoal = new Intent(context, AddGoalActivity.class);
                intentMainGoal.putExtra("mainGoal_id", model.getId());
                intentMainGoal.putExtra("main_goal_title", model.getMaingoalTitle());
                intentMainGoal.putExtra("main_goal_end_date", model.getEndDate());
                intentMainGoal.putExtra("main_goal_position", model.getPosition());
                context.startActivity(intentMainGoal);
            }
        });

        container.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete");
                alert.setMessage("Are You Sure! You want to delete Record");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHandler.deleteMainGoal(model.getId());
                        dbHandler.deleteAllSubGoal(model.getId());
                        mainGoalList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

        container.imgExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.imgCollepse.setVisibility(View.VISIBLE);
                container.imgExpand.setVisibility(View.GONE);
                container.cardExpanded.setVisibility(View.VISIBLE);
                container.rcvsubGoal.setVisibility(View.VISIBLE);

                subGoalList = dbHandler.getSubGoal(model.getId());

                if (subGoalList.size() > 0) {
                    SubGoalAdapter subGoalAdapter = new SubGoalAdapter(context, subGoalList);
                    container.rcvsubGoal.setLayoutManager(new LinearLayoutManager(context));
                    container.rcvsubGoal.setAdapter(subGoalAdapter);
                } else {
                    Toast.makeText(context, "No Records Available in Database.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        container.imgCollepse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.imgExpand.setVisibility(View.VISIBLE);
                container.imgCollepse.setVisibility(View.GONE);
                container.cardExpanded.setVisibility(View.GONE);
                container.rcvsubGoal.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mainGoalList.size();
    }

    static class mainGoalViewHolder extends RecyclerView.ViewHolder {
        public TextView goalTitle, endDate;
        public ImageButton imgEdit, imgDelete, imgExpand, imgCollepse;
        public RecyclerView rcvsubGoal;
        public CardView cardExpanded;

        public mainGoalViewHolder(@NonNull View itemView) {
            super(itemView);
            goalTitle = itemView.findViewById(R.id.txtTitleGoalList);
            endDate = itemView.findViewById(R.id.txtEndDateGoalList);
            imgEdit = itemView.findViewById(R.id.imgEditGoalLIst);
            imgDelete = itemView.findViewById(R.id.imgDeleteGoalLIst);
            imgExpand = itemView.findViewById(R.id.imgExpandeGoalList);
            imgCollepse = itemView.findViewById(R.id.imgCollepseGoalList);
            rcvsubGoal = itemView.findViewById(R.id.rcvSubGoalList);
            cardExpanded = itemView.findViewById(R.id.cardSubGoalList);
        }
    }
}
