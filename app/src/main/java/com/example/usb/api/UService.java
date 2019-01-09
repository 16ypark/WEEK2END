package com.example.usb.api;

import com.example.usb.models.UCatalog;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chuck on 2/18/16.
 */
public interface UService {
    /**
     * Every method must have an HTTP annotation that provides the request method and relative URL.
     * There are five built-in annotations: GET, POST, PUT, DELETE, and HEAD.
     * The relative URL of the resource is specified in the annotation.
     */
    @GET("contacts")
    Call<UCatalog> listCatalog();
}