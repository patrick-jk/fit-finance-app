package com.fitfinance.app.data.remote

import com.fitfinance.app.domain.request.AuthenticationRequest
import com.fitfinance.app.domain.request.ChangePasswordRequest
import com.fitfinance.app.domain.request.FinancePostRequest
import com.fitfinance.app.domain.request.FinancePutRequest
import com.fitfinance.app.domain.request.InvestmentPostRequest
import com.fitfinance.app.domain.request.InvestmentPutRequest
import com.fitfinance.app.domain.request.RegisterRequest
import com.fitfinance.app.domain.request.UserPutRequest
import com.fitfinance.app.domain.response.AuthenticationResponse
import com.fitfinance.app.domain.response.FinanceGetResponse
import com.fitfinance.app.domain.response.FinancePostResponse
import com.fitfinance.app.domain.response.HomeSummaryResponse
import com.fitfinance.app.domain.response.InvestmentGetResponse
import com.fitfinance.app.domain.response.InvestmentPostResponse
import com.fitfinance.app.domain.response.InvestmentSummaryResponse
import com.fitfinance.app.domain.response.UserGetResponse
import com.fitfinance.app.domain.response.UserPostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    companion object {
        private const val BASE_AUTH_PATH = "auth"
        private const val BASE_USERS_PATH = "users"
        private const val BASE_FINANCES_PATH = "finances"
        private const val BASE_INVESTMENTS_PATH = "investments"
    }

    //Auth Endpoints - No token required
    @POST("$BASE_AUTH_PATH/register")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<UserPostResponse>

    @POST("$BASE_AUTH_PATH/authenticate")
    fun authenticateUser(@Body authenticationRequest: AuthenticationRequest): Call<AuthenticationResponse>

    @POST("$BASE_AUTH_PATH/refresh-token")
    fun refreshToken(@Header("Authorization") token: String): Call<AuthenticationResponse>

    //Users Endpoints
    @GET("$BASE_USERS_PATH/me")
    fun getUserInfo(@Header("Authorization") token: String): Call<UserGetResponse>

    @PUT(BASE_USERS_PATH)
    fun updateUser(@Body userPutRequest: UserPutRequest, @Header("Authorization") token: String): Call<Unit>

    @PATCH(BASE_USERS_PATH)
    fun updatePassword(@Body changePasswordRequest: ChangePasswordRequest, @Header("Authorization") token: String): Call<Unit>

    //Finances Endpoints
    @GET("$BASE_FINANCES_PATH/by-user-id")
    fun getFinancesByUserId(@Header("Authorization") token: String): Call<List<FinanceGetResponse>>

    @GET("$BASE_FINANCES_PATH/user-summary")
    fun getUserSummary(@Header("Authorization") token: String): Call<HomeSummaryResponse>

    @POST(BASE_FINANCES_PATH)
    fun createFinance(@Body financePostRequest: FinancePostRequest, @Header("Authorization") token: String): Call<FinancePostResponse>

    @PUT(BASE_FINANCES_PATH)
    fun updateFinance(@Body financePutRequest: FinancePutRequest, @Header("Authorization") token: String): Call<Unit>

    @DELETE("$BASE_FINANCES_PATH/{id}")
    fun deleteFinance(@Path("id") id: Long, @Header("Authorization") token: String): Call<Unit>

    //Investments Endpoints
    @GET("$BASE_INVESTMENTS_PATH/by-user-id")
    fun getInvestmentsByUserId(@Header("Authorization") token: String): Call<List<InvestmentGetResponse>>

    @GET("$BASE_INVESTMENTS_PATH/total-summary")
    fun getInvestmentSummary(@Header("Authorization") token: String): Call<InvestmentSummaryResponse>

    @POST(BASE_INVESTMENTS_PATH)
    fun createInvestment(@Body investmentPostRequest: InvestmentPostRequest, @Header("Authorization") token: String): Call<InvestmentPostResponse>

    @PUT(BASE_INVESTMENTS_PATH)
    fun updateInvestment(@Body investmentPutRequest: InvestmentPutRequest, @Header("Authorization") token: String): Call<Unit>

    @DELETE("$BASE_INVESTMENTS_PATH/{id}")
    fun deleteInvestment(@Path("id") id: Long, @Header("Authorization") token: String): Call<Unit>
}