package me.li2.android.fiserv.smartmoney.webservice;

import me.li2.android.fiserv.smartmoney.model.Accounts;
import me.li2.android.fiserv.smartmoney.model.Offers;
import me.li2.android.fiserv.smartmoney.model.Transactions;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by weiyi on 03/07/2017.
 * https://github.com/li2
 */

public interface FiservService {
    // Fake data: http://li2.me/assets/file/fiserv_test_data_banking_account_list.json
    @GET("assets/file/fiserv_test_data_banking_account_list.json")
    Call<Accounts> getAccounts();

    // Fake data: http://li2.me/assets/file/fiserv_test_data_transaction_list.json
    @GET("assets/file/fiserv_test_data_transaction_list.json")
    Call<Transactions> getTransactions();

    // Fake data: http://li2.me/assets/file/fiserv_test_data_offer_list.json
    @GET("assets/file/fiserv_test_data_offer_list.json")
    Call<Offers> getOffers();
}
