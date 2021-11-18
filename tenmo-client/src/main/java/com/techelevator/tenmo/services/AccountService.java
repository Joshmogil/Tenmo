package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {

    private String API_BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    public AccountService(String API_BASE_URL, AuthenticatedUser currentUser) {
        this.API_BASE_URL = API_BASE_URL;
        this.currentUser = currentUser;
    }

    public Account getAccount(long accountId){
        Account currentAccount = null;

        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(API_BASE_URL + "account/" + accountId,
                            HttpMethod.GET, makeAuthEntity(), Account.class);
            currentAccount = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error finding transfer.");
            ;
        }
        return currentAccount;

    }

    public BigDecimal getAccountBalance() {
        BigDecimal accountBalance = new BigDecimal(0);
        try {
            accountBalance = restTemplate.exchange(API_BASE_URL + "balance/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
        } catch (RestClientException e) {
            System.out.println("Error getting balance");
        }
        return accountBalance;
    }

    public User[] findAllUsers(){
        // Method adds extra user in App
        User[] users = null;

        try {
            users = restTemplate.exchange(API_BASE_URL + "transfer/users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();

            for (int i = 0; i < users.length; i++) {
                System.out.println(users[i]);
            }

        } catch (RestClientResponseException e) {
            System.out.println("Error getting users");
        }
        return users;

    }

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(account, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

}
