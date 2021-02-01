//файл для работы вывода одного фильма подробнее
package ru.mospolytech.mobile_integration;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MoviesActivity extends AppCompatActivity { //создаем класс, в котором будем выводить данные одного фильма
    TextView movieTitle; //текстовое поле для заголовка
    TextView movieOverview; //тектовое поле для описания фильма
    TextView movieDate; //дата премьеры фильма
    ImageView moviePoster; //картинка фильма
    ApiInterface api; //создаем api
    private CompositeDisposable disposables; //Одноразовые композитные материалы, которые облегчают утилизацию

    @Override
    // В методе onCreate() производится первоначальная настройка глобального состояния
    protected void onCreate(Bundle savedInstanceState) {  // Метод onCreate() принимает объект Bundle сохранённое в последнем вызове обработчика onSaveInstanceState (содержащий состояние пользовательского интерфейса)
        super.onCreate(savedInstanceState);  //инициализирует состояние
        setContentView(R.layout.activity_movies);
        movieTitle = findViewById(R.id.moviesHeader);
        movieOverview = findViewById(R.id.moviesBody);
        movieDate = findViewById(R.id.moviesDate);
        moviePoster = findViewById(R.id.moviesImageFull);
        api = ApiConfiguration.getApi();
        disposables = new CompositeDisposable(); // объект для создания одноразовых предметов путем упаковки других типов
        if (getIntent().getExtras() != null){
            disposables.add( // добавляем в disposables все Observable
                    api.movieInfo(getIntent().getStringExtra("movieid"))//вызываем api, чтоб получить информацию
                            .subscribeOn(Schedulers.io()) //обрабатываем работу с сервером в потоке io, который предназначен для ввода-вывода
                            .observeOn(AndroidSchedulers.mainThread()) //далее результат обрабатываем в основном потоке, в том месте где интерфейс распологается
                            .subscribe( // формируется полученные данные, если всё прошло успешно, то
                                    (movie) -> { //информация сохраняется в объект movie
                                        movieTitle.setText(movie.title); //название фильма
                                        movieOverview.setText(movie.overview); //описание фильма
                                        movieDate.setText("Дата выхода: " + movie.date); //дата премьеры
                                        Log.d(TAG, "onBindViewHolder: " + movie.poster); //постер фильма
                                        //с помощью библиотеки Glide ассинхронно подгружаем  постеры https://image.tmdb.org/t/p/w500 + имя картинки
                                        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + movie.poster).into(moviePoster);
                                    },
                                    (error) -> { //иначе, если ошибка
                                        error.printStackTrace();
                                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show(); //демонстрируем ошибку
                                    }));
        }
    }

    @Override
    public void onDestroy() { //после закрытия экрана с фильмом удаляем все в disposables
        super.onDestroy();
        if (disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}
