package com.kicsiroot.passwordstodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kicsiroot.passwordstodo.DTO.ToDo;

import java.util.ArrayList;

import static com.kicsiroot.passwordstodo.TodoConst.INTENT_TODO_ID;
import static com.kicsiroot.passwordstodo.TodoConst.INTENT_TODO_NAME;

public class TodoActivity extends AppCompatActivity {

    TodoDbHandler dbHandler;
    FloatingActionButton fab_dashboard;
    RecyclerView rv_dashboard;
    TodoActivity activity;
    Toolbar toolbar;
    ImageView imgV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        fab_dashboard = findViewById(R.id.fab_dashboard);
        rv_dashboard = findViewById(R.id.rv_dashboard);
        setSupportActionBar(toolbar);
        setTitle("To-Do");
        activity = this;
        dbHandler= new TodoDbHandler(activity);
        rv_dashboard.setLayoutManager(new LinearLayoutManager(activity));



        fab_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setTitle("Add To Do");
                View view = getLayoutInflater().inflate(R.layout.dialog_dashboard, null);
                final EditText toDoName = view.findViewById(R.id.ev_todo);
                dialog.setView(view);

                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (toDoName.getText().toString().length() > 0) {
                            ToDo toDo = new ToDo();
                            toDo.setName(toDoName.getText().toString());
                            //User cUser = new User();
                            dbHandler.addToDo(toDo);
                            refreshList();
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    protected void onResume() {
        refreshList();
        super.onResume();
    }

    public void refreshList(){
        rv_dashboard.setAdapter(new DashBoardAdapter(activity, dbHandler.getToDos()));
    }

    class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.ViewHolder> {
        ArrayList<ToDo> list;
        TodoActivity activity;

        DashBoardAdapter(TodoActivity activity, ArrayList<ToDo> list) {
            this.list = list;
            this.activity = activity;
            Log.d("TAG", "DashboardAdapter");
            Log.d("TAG", "list : " + list.size());
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_dashboard, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            holder.toDoName.setText(list.get(position).getName());

            holder.toDoName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, TodoItemActivity.class);
                    intent.putExtra(INTENT_TODO_ID, list.get(position).getId());
                    intent.putExtra(INTENT_TODO_NAME, list.get(position).getName());
                    activity.startActivity(intent);
                }
            });
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //final TextView tv = findViewById(R.id.tv_todo_name);
                    PopupMenu popup = new PopupMenu(activity, holder.menu);
                    popup.inflate(R.menu.dashboard_child);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu_edit: {
                                    dbHandler.updateToDo(list.get(position));
                                    break;
                                }
                                case R.id.menu_delete: {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                                    dialog.setTitle("Are you sure");
                                    dialog.setMessage("Do you want to delete this task?");
                                    dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            activity.dbHandler.deleteToDo(list.get(position).getId());
                                            activity.refreshList();
                                        }
                                    });
                                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    dialog.show();
                                }
                                case R.id.menu_mark: {
                                    activity.dbHandler.updateToDoItemCompletedStatus(list.get(position).getId(), true);
                                    //tv.setTextColor(Color.parseColor("#008000"));
                                    //tv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                    break;
                                }
                                case R.id.menu_reset: {
                                    activity.dbHandler.updateToDoItemCompletedStatus(list.get(position).getId(), false);
                                    //tv.setTextColor(Color.parseColor("#000000"));
                                    //tv.setPaintFlags(tv.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                                    break;
                                }
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView toDoName;
            ImageView menu;

            ViewHolder(View v) {
                super(v);
                toDoName = v.findViewById(R.id.tv_todo_name);
                menu = v.findViewById(R.id.iv_menu);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}