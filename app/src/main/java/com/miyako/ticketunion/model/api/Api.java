package com.miyako.ticketunion.model.api;

import com.miyako.ticketunion.model.domain.Categories;
import com.miyako.ticketunion.model.domain.HomePagerContent;
import com.miyako.ticketunion.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> geCategoryContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParam param);
}
