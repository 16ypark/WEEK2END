package com.example.usb.util;

import com.example.usb.api.UService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chuck on 2/19/16.
 */
public class ServiceManager {
    private UService service;

    public UService getUService(){

        /**
         * The Retrofit class generates an implementation of the UService interface.
         */
        if(service == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(UService.class);
        }
        return service;
    }
}