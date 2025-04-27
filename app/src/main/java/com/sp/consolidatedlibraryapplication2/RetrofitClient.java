    package com.sp.consolidatedlibraryapplication2;

    import android.util.Log;

    import java.util.List;

    import retrofit2.Retrofit;
    import retrofit2.converter.gson.GsonConverterFactory;
    import okhttp3.OkHttpClient;
    import okhttp3.logging.HttpLoggingInterceptor;
    import retrofit2.Retrofit;
    import retrofit2.converter.gson.GsonConverterFactory;

    public class RetrofitClient {

        // Updated base URL with correct API version and db_library keyspace
        private static final String BASE_URL = "https://37f51461-a483-48ca-a40a-e900300d8a5e-us-east-2.apps.astra.datastax.com/";

        private static Retrofit retrofit;

        public static Retrofit getClient() {
            // Create logging interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // You can set it to BASIC, HEADERS, or BODY

            // Create OkHttpClient and add logging interceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client) // Add the custom OkHttpClient with logging
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }
