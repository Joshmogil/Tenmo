package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.view.ConsoleService;

import javax.lang.model.type.IntersectionType;
import java.math.BigDecimal;
import java.text.NumberFormat;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private TransferService transferService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService(API_BASE_URL), new TransferService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService,AccountService accountService,TransferService transferService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.transferService = transferService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();

	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	/* ViewCurrentBalance method --Notes
	--- client side ---
	* >calls getAccountBalance from account service
	* >getAccountBalance takes the user id from Authenticated User, uses it to make auth entity
	* >getAccountBalance makes an http request using user id and the auth entity to make a GET request to the server-side API
	--- Server side ---
	* accountService.getBalance makes get request on path "http://localhost:8080/balance/{id}" to account controller,
	  with id passed in as a path variable, the auth token is passed in the headers
	* the account controller uses the account specific data access object (accountDao) to grab an account object, then grabs the account balance
	* account controller sends back the account balance
	* */
	private void viewCurrentBalance() {

		try {
			System.out.println("Your current account balance is: $" + accountService.getAccountBalance(currentUser));
		} catch (NullPointerException e) {
			System.out.println("Account empty.");
		}
	}

	private void viewTransferHistory() {
		//shows user's past transfers pulls from the database

		try {
			transferService.getAllTransfers(currentUser);
		} catch (NullPointerException e) {
			System.out.println("No transfers found");
		}

		Integer enteredTransferIDorZero = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel): ");

		try {
			if (enteredTransferIDorZero == 0) {
				mainMenu(); //"0" is the input to go back to the main menu, it is never used as a transfer ID
			}else {
				transferService.getSingleTransfer(enteredTransferIDorZero, currentUser);
			}
		} catch (NullPointerException e) {
			System.out.println("Transfer ID Invalid.");
		}
		catch (NumberFormatException e) {
			System.out.println("Transfer ID Invalid");
		}

	}
	/* SendBucks method --Notes
	--- client side ---
	* >calls findAllUsers() from accountService to display all users in system
	* >calls createTransfer() from transferService, creates transfer instance using user input
	* >uses getSingleTransfer() to check and see if the transfer was successful or not
	* */

	private void sendBucks() {
    	//show a list of users
		//-> user selects user to transfer to from list of users
		//-> user enters amount to be sent in transfer


		try {
			accountService.findAllUsers(currentUser);
			//shows the list of all users
		} catch (NullPointerException e) {
			System.out.println("Account error.");
		}

		Integer enteredUserID = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel): ");
		if (enteredUserID == 0) {
			mainMenu();
		}
		BigDecimal enteredAmount = console.getUserInputBigD("Enter amount: ");

		try {

			if(enteredAmount.compareTo(accountService.getAccountBalance(currentUser)) == 1) { //if the user's balance is too low, transaction is not even initiated
				System.out.println("Not enough balance.");
			}else {

				Transfer newTransfer = transferService.createTransfer(currentUser.getUser().getId(), enteredUserID, enteredAmount, currentUser);

				Transfer newTransferCheck = transferService.getSingleTransfer(newTransfer.getTransfer_id(), currentUser);

				if (!newTransferCheck.equals(null)) {
					System.out.println("Transfer successfully processed");
				}

				if (newTransferCheck.equals(null)) {
					System.out.println("Transfer failed.");
				}
			}

		}catch(NullPointerException f){

		}catch(Exception e){

			System.out.println();

		}
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
