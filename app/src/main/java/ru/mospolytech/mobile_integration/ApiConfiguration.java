//Описание конфигурации Api

package ru.mospolytech.mobile_integration;

 // библиотека Gson, которая предназначена для преобразования Java-объектов
// в текстовый формат JSON (сериализация) и обратного преобразования (десереализация).
import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

// модуль для подключения к API
public class ApiConfiguration { //создаем класс ApiConfiguration

    public static final String BASE_URL = "https://api.themoviedb.org/"; //ссылка api, я выбрала сайт с фильмами https://www.themoviedb.org/

    private static ru.mospolytech.mobile_integration.ApiInterface api;
    private static ApiConfiguration mInstance;  // начала реализации паттерна "Одиночка"

    public static ApiConfiguration getInstance() { //функция, которая обрабатывает оюъект
        if (mInstance == null) { //если объекта нет, пусто
            mInstance = new ApiConfiguration(); //то создает новый
        }
        return mInstance; //иначе возращает уже существующий
    } // и завершается реализации паттерна "Одиночка"


    // Далее конструктор для работы с API https://api.themoviedb.org/
    private ApiConfiguration(){  // конструктур создаем приватным, чтоб невозможно было вызвать снаружи (реализации паттерна Одиночка)
        Gson gson = new GsonBuilder().setLenient().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create(); //далее создаем конвертер Gson Date в определенный формат
        OkHttpClient client = new OkHttpClient.Builder().build();  // И создаем экземпляр клиента OkHttp для http-вызовов

        Retrofit retrofit = new Retrofit.Builder() // Затем создаем экземляр интерфейса из библиотеки Retrofit для установки подключения и добавление конвертеров
                .client(client)  // клиент OkHttp
                .baseUrl(BASE_URL) // адрес сервера, к которому подключаемся
                .addConverterFactory(GsonConverterFactory.create(gson)) // затем добавляем конвертер даты Gson
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // для асинхронности
                .build();

        api = retrofit.create(ru.mospolytech.mobile_integration.ApiInterface.class); // А потом превращение интерфейса API в вызываемый объект с помощью retrofit
    }
    
    public static ApiInterface getApi(){ //метод, в котором пытаемся получить api https://www.themoviedb.org/ 
        if (api == null) { 
            new ApiConfiguration(); //создание нового подключения
        }
        return api;
    }
}


