package com.example.papertrader.api;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ApiConnection {

    private static ApiConnection single_instance = null;

    private ApiConnection() {

    }

    public static ApiConnection getInstance()
    {
        if (single_instance == null)
            single_instance = new ApiConnection();

        return single_instance;
    }


    final String BASE_API_ADDRESS = "http:10.0.2.2:5000/";


    private final OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public void create_user_in_database(String uid, String username, String email) {
        String url = BASE_API_ADDRESS + "/api/account/createuser";
        JSONObject json = new JSONObject();
        try {
            json.put("uid", uid);
            json.put("email", email);
            json.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(json.toString(), JSON);

//        RequestBody body = RequestBody.create(
//                MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();

//        RequestBody formBody = new FormBody.Builder()
//                .add("uid", uid)
//                .add("email", email)
//                .add("username", username)
//                .build();
//
//
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("Content-Type", "application/json")
//                .post(formBody)
//                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println(responseBody.string());
                }
            }
        });



    }

    public void get_all_stock_info(ResponseCallBack responseCallBack){
        String url = BASE_API_ADDRESS + "api/stocks/get_all_stock_info";
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//                    System.out.println(responseBody);

                    StringBuffer sb = new StringBuffer();
                    int c;
                    while ((c = responseBody.byteStream().read()) != -1) {
                        sb.append((char) c);
                    }
                    String jsonString = sb.toString();

                    JSONObject json = new JSONObject(jsonString);

                    json.put("Success", "True");
                    responseCallBack.getJsonResponse(json);
//                    System.out.println(jsonString);
//                    Headers responseHeaders = response.headers();

//                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }
//                        JSONObject jsonObject = new JSONObject(responseBody.string());
//                    System.out.println(responseBody.string());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void get_owned_stocks(ResponseCallBack responseCallBack, String authToken){
        String url = BASE_API_ADDRESS + "/api/account/stocks/get_owned_stocks/" + authToken;
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//                    System.out.println(responseBody);

                    StringBuffer sb = new StringBuffer();
                    int c;
                    while ((c = responseBody.byteStream().read()) != -1) {
                        sb.append((char) c);
                    }
                    String jsonString = sb.toString();

                    JSONObject json = new JSONObject(jsonString);

                    json.put("Success", "True");
                    responseCallBack.getJsonResponse(json);
//                    System.out.println(jsonString);
//                    Headers responseHeaders = response.headers();

//                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }
//                        JSONObject jsonObject = new JSONObject(responseBody.string());
//                    System.out.println(responseBody.string());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void get_past_transactions(ResponseCallBack responseCallBack, String authToken){
        String url = BASE_API_ADDRESS + "/api/account/pasttransactions/" + authToken;
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//                    System.out.println(responseBody);

                    StringBuffer sb = new StringBuffer();
                    int c;
                    while ((c = responseBody.byteStream().read()) != -1) {
                        sb.append((char) c);
                    }
                    String jsonString = sb.toString();

                    JSONObject json = new JSONObject(jsonString);

                    json.put("Success", "True");
                    responseCallBack.getJsonResponse(json);
//                    System.out.println(jsonString);
//                    Headers responseHeaders = response.headers();

//                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }
//                        JSONObject jsonObject = new JSONObject(responseBody.string());
//                    System.out.println(responseBody.string());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void get_stock_stats(ResponseCallBack responseCallBack, String authToken, String ticker){
        String url = BASE_API_ADDRESS + "/api/stocks/stock_info/" + ticker + "/" + authToken;
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//                    System.out.println(responseBody);

                    StringBuffer sb = new StringBuffer();
                    int c;
                    while ((c = responseBody.byteStream().read()) != -1) {
                        sb.append((char) c);
                    }
                    String jsonString = sb.toString();

                    JSONObject json = new JSONObject(jsonString);

                    json.put("Success", "True");
                    responseCallBack.getJsonResponse(json);
//                    System.out.println(jsonString);
//                    Headers responseHeaders = response.headers();

//                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }
//                        JSONObject jsonObject = new JSONObject(responseBody.string());
//                    System.out.println(responseBody.string());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
