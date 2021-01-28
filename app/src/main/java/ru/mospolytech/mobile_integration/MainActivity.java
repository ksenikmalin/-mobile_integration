package ru.mospolytech.mobile_integration;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    ListAdapter adapter;
    List<MovieDetails> list;
    ApiInterface api;
    private CompositeDisposable disposables;
    private String geo_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        adapter = new ListAdapter(this, list);
        recyclerView = findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        api = ApiConfiguration.getApi();
        disposables = new CompositeDisposable();
        this.onClick(this.recyclerView);

        Spinner aSpinner = findViewById(R.id.spinner);

       ArrayList<TimeWindow> time_list = new ArrayList<TimeWindow>();
        time_list.add(new TimeWindow("day", "Трендовые фильмы за сегодня"));
        time_list.add(new TimeWindow("week", "Трендовые фильмы за неделю"));

        ArrayAdapter<TimeWindow> myAdapter = new ArrayAdapter<TimeWindow>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, time_list);


        aSpinner.setAdapter(myAdapter);
        aSpinner.setOnItemSelectedListener(this);
    }


    public void onClick(View view){
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

            findViewById(R.id.list).setVisibility(View.GONE);
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

            TimeWindow time = (TimeWindow) adapterView.getItemAtPosition(position);
            disposables.add(api.trendingMovies(time.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((results) -> {

                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                        findViewById(R.id.list).setVisibility(View.VISIBLE);
                        list.clear();
                        list.addAll(results.movies);
                        adapter.notifyDataSetChanged();
                    }, (error) -> {
                        Toast.makeText(this, "Из-за леса, из-за гор, к нам пришел:\n" + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                        findViewById(R.id.list).setVisibility(View.VISIBLE);

                    }));
            Toast.makeText(this, "Показаны фильмы для: " + adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
