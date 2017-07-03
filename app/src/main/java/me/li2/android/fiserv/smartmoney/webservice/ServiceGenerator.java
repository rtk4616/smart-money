package me.li2.android.fiserv.smartmoney.webservice;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Centralize Retrofit client.
 *
 * It's common practice to just use one OkHttpClient instance to reuse open socket connections.
 * We chose to use the static field, and because the httpClient is used throughout this class,
 * we need to make all fields and methods static.
 *
 * Make it rock & enjoy coding!
 *
 * Created by weiyi on 26/03/2017.
 * https://github.com/li2
 */

public class ServiceGenerator {

    private static final String BASE_URL = "http://li2.me/";
    private static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .setDateFormat(DATE_FORMAT)
            .create();

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor);

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
            ;

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    public static <S> S createService(Class<S> serviceClass, String username, String password) {
        String authToken = null;
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            authToken = Credentials.basic(username, password);
        }
        return createService(serviceClass, authToken);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            // add query parameters to every request
            // add authentication to every requests
        }

        builder.client(httpClient.build());
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
