package com.miyako.ticketunion.model.api;

import com.miyako.ticketunion.model.domain.Categories;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();
}
