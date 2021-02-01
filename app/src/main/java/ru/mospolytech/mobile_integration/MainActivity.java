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
    // В методе onCreate() производится первоначальная настройка глобального состояния
    protected void onCreate(Bundle savedInstanceState) {  // Метод onCreate() принимает объект Bundle сохранённое в последнем вызове обработчика onSaveInstanceState (содержащий состояние пользовательского интерфейса)
        super.onCreate(savedInstanceState); //инициализирует состояние
        setContentView(R.layout.activity_main); //главный экран приложения
        list = new ArrayList<>();
        adapter = new ListAdapter(this, list);
        recyclerView = findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        api = ApiConfiguration.getApi();
        disposables = new CompositeDisposable();
        this.onClick(this.recyclerView);

        Spinner aSpinner = findViewById(R.id.spinner);

       ArrayList<TimeWindow> time_list = new ArrayList<TimeWindow>(); //создаем список, в котором выпадающий список выбора параметра день или неделя
        time_list.add(new TimeWindow("day", "Трендовые фильмы за сегодня"));// есть name, которому добавляем id day
        time_list.add(new TimeWindow("week", "Трендовые фильмы за неделю")); // есть name, которому добавляем id week

        ArrayAdapter<TimeWindow> myAdapter = new ArrayAdapter<TimeWindow>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, time_list);

        aSpinner.setAdapter(myAdapter);
        aSpinner.setOnItemSelectedListener(this);
    }


    public void onClick(View view){
    }
//далее обрабатывается событие onItemSelected, после выбора одного из вариантов time_list
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

            findViewById(R.id.list).setVisibility(View.GONE);
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

            TimeWindow time = (TimeWindow) adapterView.getItemAtPosition(position); //нам передается position, которую выбрал пользователь
            disposables.add(api.trendingMovies(time.id) //указываю, что api дай фильмы trendingMovies за период времени time.id
                    .subscribeOn(Schedulers.io()) //обрабатываем работу с сервером в потоке io, который предназначен для ввода-вывода
                    .observeOn(AndroidSchedulers.mainThread()) //далее результат обрабатываем в основном потоке, в том месте где интерфейс распологается
                    .subscribe((results) -> { // формируется список из results, если всё прошло успешно, то

                        findViewById(R.id.progressBar).setVisibility(View.GONE); // скрываем progressBar загрузки
                        findViewById(R.id.list).setVisibility(View.VISIBLE); // показываем элемент со списков трендовых фильмов
                        list.clear(); //очищаем элемент list
                        list.addAll(results.movies); // добавляем все трендовые фильмы за неделю или сегодня в элемент списка
                        adapter.notifyDataSetChanged(); //затем мы говорим адаптеру, что данные обновились

                    }, (error) -> { //иначе, если ошибка
                        Toast.makeText(this, "Из-за леса, из-за гор, к нам пришел:\n" + error.getMessage(),
                                Toast.LENGTH_LONG).show();//демонстрируем ошибку
                        findViewById(R.id.progressBar).setVisibility(View.GONE); //закрываем progressBar загрузки
                        findViewById(R.id.list).setVisibility(View.VISIBLE); // делаем list видимым

                    }));
            Toast.makeText(this, "Показаны фильмы для: " + adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();// демонстрируем уведомление о результатах вывода
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
