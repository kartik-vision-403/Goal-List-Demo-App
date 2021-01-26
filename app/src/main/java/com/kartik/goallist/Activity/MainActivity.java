package com.kartik.goallist.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.kartik.goallist.Adapter.MainGoalAdapter;
import com.kartik.goallist.DataBase.DataBaseHandler;
import com.kartik.goallist.Model.AddGoalModel;
import com.kartik.goallist.Model.MainGoalModel;
import com.kartik.goallist.R;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private Context context = this;
    private ArrayList<MainGoalModel> mainGoalList = new ArrayList<>();
    private ArrayList<AddGoalModel> subGoalList = new ArrayList<>();
    private RecyclerView rcvGoalList;
    //TODO: Item Drag and Drop
    ItemTouchHelper.SimpleCallback mainGoalCallBack = new ItemTouchHelper.SimpleCallback(
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
            Collections.swap(mainGoalList, fromPosition, toPosition);
            rcvGoalList.getAdapter().notifyItemMoved(fromPosition, toPosition);
           /* Log.d("TAG", "onMoveFROM: "+ fromPosition);
            Log.d("TAG", "onMoveTO: "+ toPosition);*/
            int fromId = dbHandler.getPositionofRecord(fromPosition+1);
            int toId = dbHandler.getPositionofRecord(toPosition+1);
            dbHandler.updatePositionRecord(fromId,toPosition+1);
            dbHandler.updatePositionRecord(toId,fromPosition+1);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
    private MainGoalAdapter adapter;
    private DataBaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DataBaseHandler(context);
        rcvGoalList = findViewById(R.id.rcvMainGoalList);
        setMainGoalValues();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mainGoalCallBack);
        itemTouchHelper.attachToRecyclerView(rcvGoalList);

    }

    //TODO: Set a Main Goal Value to recyclerView
    private void setMainGoalValues() {
        mainGoalList.clear();
        mainGoalList = dbHandler.getAllMainGoal();

        if (mainGoalList.size() > 0) {
            adapter = new MainGoalAdapter(context, mainGoalList);
            rcvGoalList.setLayoutManager(new GridLayoutManager(context, 1));
            rcvGoalList.setAdapter(adapter);

        } else {
            Toast.makeText(context, "No Records Available in Database.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tool_bar_main_goal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_add_new_goal:
                Intent intent = new Intent(this, AddGoalActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}