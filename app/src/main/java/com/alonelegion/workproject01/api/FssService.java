package com.alonelegion.workproject01.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by develop2 on 15.01.2018.
 */

public interface FssService {

    // Авторизация
    @GET("login_mob.asp")
    public Call<LoginResponse> login(
            @Query("login") String login,
            @Query("password") String password);

    // Оформленные заявки
    @GET("qwerynz.asp")
    public Call<WorkOrdersResponse> orders(
            @Query("token") String token,
            @Query("dtstart") String dtstart,
            @Query("dtend") String dtend);

    // Переданные заявки
    @GET("qwerynzpart.asp")
    public Call<SubmittedResponse> submitteds (
            @Query("token") String token,
            @Query("dtstart") String dtstart,
            @Query("dtend") String dtend);

    // Статистика по финансам
    @GET("qwerynzfin.asp")
    public Call<FinanceResponse> finances(
            @Query("token") String token,
            @Query("dtstart") String dtstart,
            @Query("dtend") String dtend);

    @GET("qwerynzmain.asp")
    public Call<ProcessedResponse> processeds(
            @Query("token") String token,
            @Query("dtstart") String dtstart,
            @Query("dtend") String dtend);

    @GET("qwerynzpartstat.asp")
    public Call<TransferredResponse> transferreds(
            @Query("token") String token,
            @Query("dtstart") String dtstart,
            @Query("dtend") String dtend);

    // Оформление заявки
    @GET("savenzttt.asp")
    public Call<OrderResponse> answerOrder(
            @Query("token") String token,
            @Query("region") String region,
            @Query("fio") String fio,
            @Query("data") String data,
            @Query("phone1") String phone1,
            @Query("phone2") String phone2,
            @Query("work") String work);

    // Частный мастер
    @GET("savenzttt.asp")
    public Call<OrderResponse> answerPrivate(
            @Query("token") String token,
            @Query("region") String region,
            @Query("lk") String lk,
            @Query("chm") String chm,
            @Query("fio") String fio,
            @Query("data") String data,
            @Query("phone1") String phone1,
            @Query("phone2") String phone2,
            @Query("work") String work,
            @Query("street") String street,
            @Query("dom") String dom,
            @Query("korp") String korp,
            @Query("kv") String kv,
            @Query("t_begin") String t_begin,
            @Query("t_end") String t_end,
            @Query("pr_int1") String pr_int1,
            @Query("pr_check1") String pr_check1,
            @Query("pr_check2") String pr_check2,
            @Query("zvon") String zvon,
            @Query("kod_tel") String kod_tel);

    // Новости
    @GET("qwerynews.asp")
    public Call<NewsResponse> newses (
            @Query("token") String token);

    // Регистрация
    @GET("addpart.asp")
    public Call<RegistrationResponse> registrations(
            @Query("phone")String phone,
            @Query("phone2")String phone2,
            @Query("fio")String fio,
            @Query("nbase")String nbase,
            @Query("email")String email);
}
