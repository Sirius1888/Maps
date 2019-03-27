package com.example.angelina.mapsinobi.ui.activity.history.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angelina.mapsinobi.AppDatabase;
import com.example.angelina.mapsinobi.R;
import com.example.angelina.mapsinobi.adapter.HistoryDBAdapter;
import com.example.angelina.mapsinobi.model.LocationEntity;
import com.example.angelina.mapsinobi.ui.activity.history.presenter.HistoryPresenter;
import com.example.angelina.mapsinobi.ui.activity.history.presenter.HistoryPresenterImpl;
import com.example.angelina.mapsinobi.utils.PrefUtil;

import java.util.List;

import butterknife.BindView;

public class HistoryActivity extends AppCompatActivity implements HistoryContract.View, HistoryContract.OnItemClickListener {

//    @BindView(R.id.history_route_tv)TextView historyEmptyTeVi;
    private Toolbar toolbar;
    private HistoryPresenter presenter;
    private HistoryDBAdapter adapter;
    private TextView emptyTv;


    @BindView(R.id.history_recycler_view) RecyclerView recyclerView;
//    @BindView(R.id.empty_history_activity_text_view) TextView emptyTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        emptyTv = (TextView)findViewById(R.id.empty_history_activity_text_view);
        adapter = new HistoryDBAdapter(this);
        AppDatabase db = AppDatabase.getDatabase(getApplication());
        presenter = new HistoryPresenterImpl(this, db.locationModel());


        initializeToolbar();
        initializeRecyclerView();

        getData();


    }

    private void getData() {
        presenter.showAllHistory();
    }


    private void initializeRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public void clickItem(LocationEntity locationEntity) {
        Intent intent = new Intent();
        long id = locationEntity.getId();
        PrefUtil.setIdRoute(id);
        intent.putExtra("intentId", id);
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public void clickLongItem(LocationEntity locationEntity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Удаление");
        alertDialog.setMessage("Вы хотите удалить этот маршрут?");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);


        alertDialog.setPositiveButton("Ок",
                (dialog, which) -> {



                });

        alertDialog.setNegativeButton("Отмена",
                (dialog, which) -> dialog.cancel());

        alertDialog.show();
    }

    @Override
    public void showToast(String message) {
        new Handler().postDelayed(() -> Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(),200);

    }

    @Override
    public void setPresenter(HistoryPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showEmptyMessage() {
//        emptyTv.setVisibility(View.VISIBLE);
//        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showRecyclerView() {
//        emptyTv.setVisibility(View.GONE);
//        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showHistory(List<LocationEntity> historyModel) {
//        historyEmptyTeVi.setVisibility(View.GONE);
        adapter.setValues(historyModel);
    }
}
