package com.codingwithmitch.googlemaps2018.Fragment;

import com.codingwithmitch.googlemaps2018.Notifications.MyResponse;
import com.codingwithmitch.googlemaps2018.Notifications.Sender;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA0xDUD8A:APA91bHpznmmg6Ezg-lrMbH7ZT_SlBycy867dNzE8TfbJgjkP2P5x8DYtRjgUt1mqFEZFXiHfcclwrEhqAlW_e18BSS8_U7l477f6T11vL5jU22cFOYwYmJvy2zgRe6S2B3b_ULzbrFf"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
