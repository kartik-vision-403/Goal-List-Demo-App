package com.kartik.goallist.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.kartik.goallist.Adapter.AddGoalAdapter;
import com.kartik.goallist.DataBase.DataBaseHandler;
import com.kartik.goallist.Model.AddGoalModel;
import com.kartik.goallist.Model.MainGoalModel;
import com.kartik.goallist.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AddGoalActivity extends AppCompatActivity {
    public static int checkValidation = 0;
    public int checkFirstTimeActivity = 0;
    Bundle extras;
    private EditText mainGoalTitle;
    private EditText selectDate;
    private Context context = this;
    private Calendar calendar;
    private Map<String, String> mapList = new HashMap<String, String>();
    private ArrayList<AddGoalModel> subGoalList = new ArrayList<>();
    private RecyclerView rcvAddGoal;
    private AddGoalAdapter adapter;
    private LinearLayout llAddNewGoal;
    private DataBaseHandler dbHandler;
    private long id;
    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        getSupportActionBar().setTitle("Add Goal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainGoalTitle = findViewById(R.id.etAddMainGoalTitle);
        selectDate = findViewById(R.id.etDateAddGoal);
        calendar = Calendar.getInstance();
        rcvAddGoal = findViewById(R.id.rcvAddNewGoal);
        llAddNewGoal = findViewById(R.id.llAddNewGoal);
        dbHandler = new DataBaseHandler(context);

        selectDate.setOnClickListener(v -> pickDate());
        llAddNewGoal.setOnClickListener(v -> addNewGoal());

        extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong("mainGoal_id");
            mainGoalTitle.setText(extras.getString("main_goal_title"));
            selectDate.setText(extras.getString("main_goal_end_date"));
            subGoalList = dbHandler.getSubGoal(id);

        }
        adapter = new AddGoalAdapter(context, subGoalList);
        rcvAddGoal.setLayoutManager(new GridLayoutManager(context, 1));
        rcvAddGoal.setItemViewCacheSize(0);
        rcvAddGoal.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(addGoalCallBack);
        itemTouchHelper.attachToRecyclerView(rcvAddGoal);

    }

    //TODO: Item Drag and Drop
    ItemTouchHelper.SimpleCallback addGoalCallBack = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP |
                    ItemTouchHelper.DOWN |
                    ItemTouchHelper.START |
                    ItemTouchHelper.END, 0) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(subGoalList, fromPosition, toPosition);
            rcvAddGoal.getAdapter().notifyItemMoved(fromPosition, toPosition);

            dbHandler.deleteAllSubGoal(id);
            Log.e(TAG, "onMove: " + id);
            for (int i = 0; i < subGoalList.size(); i++) {
                dbHandler.insertSubGoal(subGoalList.get(i).getSubGoalTitle(), id);
            }
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    private void addNewGoal() {

        if (checkFirstTimeActivity == 0) {
            subGoalList.add(new AddGoalModel(""));
            checkValidation = 0;
        } else {
            if (checkValidation == 0)
                showAlert("Entry of Empty Sub Goal", "Empty", "Fill the Empty Record First");
            else
                subGoalList.add(new AddGoalModel(""));
        }

        setValues();
        checkFirstTimeActivity = 1;
    }

    private void setValues() {

        checkValidation = 0;
        adapter = new AddGoalAdapter(context, subGoalList);
        rcvAddGoal.setItemViewCacheSize(0);
        rcvAddGoal.setLayoutManager(new GridLayoutManager(context, 1));
        adapter.setHasStableIds(true);
        rcvAddGoal.setAdapter(adapter);

    }

    private void pickDate() {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        //date picker Dialog
        DatePickerDialog datePicker = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // it can convert your selected date to dd-MM-yyyy
                        // which can use in adapter for comparing dates
                        selectDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
        datePicker.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tool_bar_add_goal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_save_added_goal:
                if (mainGoalTitle.getText().length() == 0 || selectDate.getText().length() == 0) {
                    showAlert("Main Goal Entry ", "Empty", "Title or Date not be Blank");
                } else {
                    saveData();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlert(String action, String title, String message) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();

    }

    //TODO: Save Data to Database and move to main Activity
    private void saveData() {
        if (extras == null) {
            //TODO: Insert Main Goal

            id = dbHandler.insertMainGoal(new MainGoalModel(mainGoalTitle.getText().toString(),
                    selectDate.getText().toString(), dbHandler.getMaxPosition()));

            //TODO: Insert Sub Goal
            for (int i = 0; i < subGoalList.size(); i++) {
                dbHandler.insertSubGoal(subGoalList.get(i).getSubGoalTitle(), id);
            }

        } else {
            //TODO: Update Main Goal
            MainGoalModel model = new MainGoalModel(extras.getLong("mainGoal_id"),
                    mainGoalTitle.getText().toString(),
                    selectDate.getText().toString(),
                    extras.getLong("main_goal_position"));
            dbHandler.updateMainGoal(model);
            //TODO: Update Sub Goal
            dbHandler.deleteAllSubGoal(id);
            for (int i = 0; i < subGoalList.size(); i++) {
                dbHandler.insertSubGoal(subGoalList.get(i).getSubGoalTitle(), id);
            }

        }
        //TODO: Move to Main Activity
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}