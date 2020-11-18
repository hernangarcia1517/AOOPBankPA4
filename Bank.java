import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.*;

/**
 * Programming Assignment 4
 * 
 * The purpose of this class is to simulate a bank account. The program
 * implements user interaction to ask for necessary information and what they
 * are using the program for.
 * 
 * @author Hernan Garcia and Alyssandra Cordero
 * @version 1.2.0
 * @since November 9, 2020
 * 
 *        CS 3331 - Adv. OOP, Fall 2020 Daniel Mejia A fundamental principle for
 *        any educational institution, academic integrity is highly valued and
 *        seriously regarded at The University of Texas at El Paso. More
 *        specifically, students are expected to maintain absolute integrity and
 *        a high standard of individual honor in scholastic work undertaken at
 *        the University. At a minimum, you should complete any assignments,
 *        exams, and other scholastic endeavors with the utmost honesty, which
 *        requires you to: Acknowledge the contributions of other sources to
 *        your scholastic efforts; Complete your assignments independently
 *        unless expressly authorized to seek or obtain assistance in preparing
 *        them; Follow instructions for assignments and exams, and observe the
 *        standards of your academic discipline; and Avoid engaging in any form
 *        of academic dishonesty on behalf of yourself or another student.
 *        Graded work, e.g., homework and tests, is to be completed
 *        independently and should be unmistakably your own work (or, in the
 *        case of group work, your team's work), although you may discuss your
 *        project with other students in a general way. You may not represent as
 *        your own work material that is transcribed or copied from another
 *        person, book, or any other source, e.g., a web page.
 */
public class Bank {
	private static int customerIdTracker = 0;
	private static int checkingAccountIdTracker = 0;
	private static int savingsAccountIdTracker = 0;
	private static int creditAccountIdTracker = 0;

	/**
	 * Main method This method holds the functionality of the Bank class.
	 * 
	 * @param args arguments during run
	 */
	public static void main(String[] args) {
		BufferedReader br = null;
		String currentUser = "";
		HashMap<String, Customer> data = new HashMap<String, Customer>();// 20, 0.75f);
		HashMap<String, String> customerIds = new HashMap<String, String>();
		BufferedReader inputReader = null;
		Writer transactionLog = null;
		readCVSFile(data, customerIds, br, currentUser); // Read the CVS File
		runBankingApp(data, customerIds, inputReader, transactionLog); // Run the Banking Process
	} // END OF main()

	/**
	 * This method read from a CSV File
	 * 
	 * @param data        the hashtable in which we are storing datat
	 * @param br          buffered reader
	 * @param currentUser the current user string
	 */
	public static void readCVSFile(HashMap<String, Customer> data, HashMap<String, String> customerIds,
			BufferedReader br, String currentUser) {
		try {
			br = new BufferedReader(new FileReader("CS 3331 - Bank Users 4.csv")); // Hardcoded File Give prompt
																					// instead
			int count = 0;
			HashMap<String, Integer> inputCorrector = new HashMap<String, Integer>();
			while ((currentUser = br.readLine()) != null) {
				if (count == 0) {
					String[] headers = currentUser.split(",");
					boolean seenAddress = false;
					boolean seenDateOfBirth = false;
					for (int i = 0; i < headers.length; i++) {
						if (seenAddress && seenDateOfBirth) {
							inputCorrector.put(headers[i], i + 3);
						} else if (seenAddress) {
							inputCorrector.put(headers[i], i + 2);
						} else if (seenDateOfBirth) {
							inputCorrector.put(headers[i], i + 1);
						} else {
							inputCorrector.put(headers[i], i);
						}
						if (headers[i].equals("Address")) {
							seenAddress = true;
						} else if (headers[i].equals("Date of Birth")) {
							seenDateOfBirth = true;
						}
					}
				}
				if (count > 0) { // Omitting the first row, which should just be the headers
					String[] VALS = currentUser.split(","); // This is going to create a String array, now we have to
															// store it
					// System.out.println(inputCorrector.get("Date of Birth"));
					String[] dateOfBirth = { VALS[inputCorrector.get("Date of Birth")],
							VALS[inputCorrector.get("Date of Birth") + 1] };
					// System.out.println(inputCorrector.get("Address"));
					String[] addressArray = { VALS[inputCorrector.get("Address")],
							VALS[inputCorrector.get("Address") + 1], VALS[inputCorrector.get("Address") + 2] };
					data.put(VALS[inputCorrector.get("Identification Number")], new Customer(
							VALS[inputCorrector.get("First Name")], // First name
							VALS[inputCorrector.get("Last Name")], // Last name
							VALS[inputCorrector.get("Identification Number")], // Customer ID
							VALS[inputCorrector.get("Password")], VALS[inputCorrector.get("Email")], true, // Has
																											// Checking
							true, // Has Savings
							dateOfBirth, // Date of birth
							VALS[inputCorrector.get("Phone Number")], // Phone number
							addressArray, // Address
							createCheckingAccount(Integer.parseInt(VALS[inputCorrector.get("Checking Account Number")]),
									Double.parseDouble(VALS[inputCorrector.get("Checking Starting Balance")]), 0.0), // Checking
							createSavingsAccount(Integer.parseInt(VALS[inputCorrector.get("Savings Account Number")]),
									Double.parseDouble(VALS[inputCorrector.get("Savings Starting Balance")]), 0.0), // Savings
							createCreditAccount(Integer.parseInt(VALS[inputCorrector.get("Credit Account Number")]),
									Double.parseDouble(VALS[inputCorrector.get("Credit Max")]),
									Double.parseDouble(VALS[inputCorrector.get("Credit Starting Balance")]), 0.0) // Credit
					));

					customerIds.put(data.get(VALS[inputCorrector.get("Identification Number")]).getName(),
							VALS[inputCorrector.get("Identification Number")]);
					if (Integer.parseInt(VALS[inputCorrector.get("Identification Number")]) > customerIdTracker)
						customerIdTracker = Integer.parseInt(VALS[inputCorrector.get("Identification Number")]);
					if (Integer
							.parseInt(VALS[inputCorrector.get("Checking Account Number")]) > checkingAccountIdTracker)
						checkingAccountIdTracker = Integer
								.parseInt(VALS[inputCorrector.get("Checking Account Number")]);
					if (Integer.parseInt(VALS[inputCorrector.get("Savings Account Number")]) > savingsAccountIdTracker)
						savingsAccountIdTracker = Integer.parseInt(VALS[inputCorrector.get("Savings Account Number")]);
					if (Integer.parseInt(VALS[inputCorrector.get("Credit Account Number")]) > creditAccountIdTracker)
						creditAccountIdTracker = Integer.parseInt(VALS[inputCorrector.get("Credit Account Number")]);
				}
				count++;
			}
		} catch (FileNotFoundException e) { // Handle File not found
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally { // Closing BufferedReader if it is not null
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	} // END OF readCVSFile()

	/**
	 * The run banking app handles all the banking functionality
	 * 
	 * @param data           the data hashmap
	 * @param inputReader    buffered reader that handles user input
	 * @param transactionLog logs user transaction
	 */
	public static void runBankingApp(HashMap<String, Customer> data, HashMap<String, String> customerIds,
			BufferedReader inputReader, Writer transactionLog) {
		try {
			inputReader = new BufferedReader(new InputStreamReader(System.in));
			transactionLog = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("transactionLog.txt", true), "utf-8")); // Appending the
																										// file since we
																										// might ant to
																										// keep track of
																										// all
																										// transactions
			boolean isDone = false;
			while (!isDone) {
				System.out.println(
						"1. Create Customer 2. Returning Customer 3. Bank Manager 4. Transaction Reader 5. Exit");
				String desiredFunctionality = inputReader.readLine();
				switch (Integer.parseInt(desiredFunctionality)) {
					case 1: // Create Customer
						runCreateCustomer(data, customerIds, inputReader, transactionLog);
						break;
					case 2: // Customer
						runCustomer(data, inputReader, transactionLog);
						break;
					case 3: // Bank Manager
						runBankManager(data, customerIds, inputReader, transactionLog);
						break;
					case 4:
						runTransactionReader(data, customerIds, transactionLog);
					case 5:
						isDone = !isDone;
						createCSVCopy(data);
						System.out.println("Ending Application");
						break;
					default:
						System.out.println("Input not recognized. Please try again.");
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally { // Closing BufferedReader and Writer
			try {
				if (inputReader != null) {
					inputReader.close();
				}
				if (transactionLog != null) {
					transactionLog.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} // END OF runBankingApp()

	/**
	 * This method handles the creation of a checking account for a customer
	 * 
	 * @param accountNumber  the account number
	 * @param currentBalance the opening balance
	 * @param interestRate   the interest rate
	 * @return returning the checking account
	 */
	public static Checking createCheckingAccount(int accountNumber, double currentBalance, double interestRate) {
		return new Checking(accountNumber, currentBalance, interestRate);
	}// END OF createCheckingAccount()

	/**
	 * This method handles the creation of a savings account for a customer
	 * 
	 * @param accountNumber  the account number
	 * @param currentBalance the opening balance
	 * @param interestRate   the interest rate
	 * @return returning the savings account
	 */
	public static Savings createSavingsAccount(int accountNumber, double currentBalance, double interestRate) {
		return new Savings(accountNumber, currentBalance, interestRate);
	}// END OF createSavingsAccount()

	/**
	 * This method handles the creation of a credit account for a customer
	 * 
	 * @param accountNumber  the account number
	 * @param currentBalance the opening balance
	 * @param interestRate   the interest rate
	 * @return returning the credit account
	 */
	public static Credit createCreditAccount(int accountNumber, double creditLine, double outstandingBalance,
			double interestRate) {
		return new Credit(accountNumber, creditLine, outstandingBalance, interestRate);
	}// END OF createCreditAccount()

	/**
	 * This method runs the create new customer logic
	 * 
	 * @param data           the hashmap of all the users
	 * @param inputReader    buffered reader that handles user input
	 * @param transactionLog logs user transaction
	 */
	public static void runCreateCustomer(HashMap<String, Customer> data, HashMap<String, String> customerIds, BufferedReader inputReader,
			Writer transactionLog) {
		customerIdTracker++;// increase the number of IDs we have
		try {
			// Ask the user for its basic information
			String customerFirstName = "";
			String customerLastName = "";
			String customerBirthMonth = "";
			String customerBirthDay = "";
			String customerBirthYear = "";
			String[] customerDOB = new String[2];
			String customerAddress = "";
			String[] customerAddressArray = new String[2];
			String email = "";
			String customerPhoneNumber = "";
			boolean isInputEmpty = false;
			while(!isInputEmpty){
				System.out.print("Enter First Name: ");
				customerFirstName = inputReader.readLine();
				System.out.println();
				System.out.print("Enter Last Name: ");
				customerLastName = inputReader.readLine();
				System.out.println();
				System.out.print("Enter Month of Birth: ");
				customerBirthMonth = inputReader.readLine();
				System.out.println();
				System.out.print("Enter Day of Birth: ");
				customerBirthDay = inputReader.readLine();
				System.out.println();
				System.out.print("Enter Year of Birth: ");
				customerBirthYear = inputReader.readLine();
				customerDOB[0] = customerBirthMonth + " " + customerBirthDay; 
				customerDOB[1] = customerBirthYear;
				System.out.println();
				System.out.print("Enter Address (Street, City, ZIP Code): ");
				customerAddress = inputReader.readLine(); // NEED TO VERIFY THE INPUT
				customerAddressArray = customerAddress.split(",");
				System.out.println();
				System.out.print("Enter Email Address: ");
				email = inputReader.readLine();
				System.out.println();
				System.out.print("Enter Phone Number: ");
				customerPhoneNumber = inputReader.readLine();
				System.out.println();
				if(
					customerFirstName.equals("") || customerLastName.equals("") ||
					customerBirthMonth.equals("") || customerBirthDay.equals("") ||
					customerBirthYear.equals("") || customerAddress.equals("") ||
					email.equals("") || customerPhoneNumber.equals("")){
						System.out.println("ERROR: One or more fields left empty, try again");
						System.out.println();
						System.out.println();
				}else{
					isInputEmpty = !isInputEmpty;
				}
			}
			System.out.print("Enter Savings Starting Balance: $ ");
			String savingsStartingBalance = inputReader.readLine();
			System.out.println();
			System.out.print("Enter new password:");
			String password = inputReader.readLine();
			// let the user know the new password and ask the user if they would like to
			// create an additional account.
			System.out.println("Your account has been created. Please save your new password to log in : " + password);
			System.out.println();
			System.out.print("Besides the savings account, would you like to create an additional account? [y/n]: ");
			String createAdditional = inputReader.readLine();
			System.out.println();
			if (createAdditional.equals("y")) {// User wants to create an additional account
				System.out.print(
						"1. To open new checking account 2. To open new credit account 3. To pen checking and credit account");
				System.out.println();
				String createAnother = inputReader.readLine();
				int createAnotherToInt = (Integer.valueOf(createAnother));
				boolean flagCustomer = true;
				do {
					switch (createAnotherToInt) {// based on the user input create the appropiate account
						case 1:// Creating an additional checking account
							System.out.print("Enter Checking Starting Balance: $");
							String checkingStartingBalance = inputReader.readLine();
							System.out.println();
							// create the customer
							// create savings account
							Savings savingsAccount = createSavingsProcedure(savingsStartingBalance);
							// Create Checking Account
							Checking checkingAccount = createCheckingProcedure(checkingStartingBalance);
							Credit creditAccount = ghostCredit();
							data.put(Integer.toString(customerIdTracker), new Customer(customerFirstName, // First name
									customerLastName, // Last name
									Integer.toString(customerIdTracker), // Customer ID
									password, // Has Password
									email, // Has Email
									true, // Has Checking
									true, // Has Savings
									customerDOB, // Date of birth
									customerPhoneNumber, // Phone number
									customerAddressArray, // Address
									checkingAccount, // Checking
									savingsAccount, // createSavingsAccount(Integer.parseInt(VALS[10]),
									creditAccount // Credit
							));
							customerIds.put(customerFirstName + " " + customerLastName, Integer.toString(customerIdTracker));
							break;
						case 2:// Creating an additional credit account
							System.out.print("Enter Credit Starting Balance: $");
							String creditStartingBalance = inputReader.readLine();
							System.out.println();
							// create the customer
							// create savings account
							Savings savingsAccount1 = createSavingsProcedure(savingsStartingBalance);
							// Create Credit Account
							Credit creditAccount1 = createCreditProcedure(creditStartingBalance);
							// initialize the checking "ghost" account
							Checking checkingAccount1 = ghostChecking();
							data.put(Integer.toString(customerIdTracker), new Customer(customerFirstName, // First name
									customerLastName, // Last name
									Integer.toString(customerIdTracker), // Customer ID
									password, // Has Password
									email, // Has Email
									true, // Has Checking
									true, // Has Savings
									customerDOB, // Date of birth
									customerPhoneNumber, // Phone number
									customerAddressArray, // Address
									checkingAccount1, // Checking
									savingsAccount1, // createSavingsAccount(Integer.parseInt(VALS[10]),
									creditAccount1 // Credit
							));
							customerIds.put(customerFirstName + " " + customerLastName, Integer.toString(customerIdTracker));
							break;
						case 3:// Create an additional checking and credit account
							System.out.print("Enter Checking Starting Balance: $");
							String checkingStartingBalance1 = inputReader.readLine();
							System.out.println();
							System.out.print("Enter Credit Starting Balance: $");
							String creditStartingBalance1 = inputReader.readLine();
							System.out.println();
							// create the customer

							// create savings account
							Savings savingsAccount3 = createSavingsProcedure(savingsStartingBalance);
							// Create Checking Account
							Checking checkingAccount3 = createCheckingProcedure(checkingStartingBalance1);
							// Create Credit Account
							Credit creditAccount3 = createCreditProcedure(creditStartingBalance1);

							data.put(Integer.toString(customerIdTracker), new Customer(customerFirstName, // First name
									customerLastName, // Last name
									Integer.toString(customerIdTracker), // Customer ID
									password, email, true, // Has Checking
									true, // Has Savings
									customerDOB, // Date of birth
									customerPhoneNumber, // Phone number
									customerAddressArray, // Address
									checkingAccount3, // Checking
									savingsAccount3, // createSavingsAccount(Integer.parseInt(VALS[10]),
									creditAccount3 // Credit
							));
							customerIds.put(customerFirstName + " " + customerLastName, Integer.toString(customerIdTracker));
							break;
						default:// if the user does not enter a valid selection, ask for a valid selection until
								// you get it
							flagCustomer = false;
							System.out.print("please enter 1, 2 or 3 to continue: ");
							System.out.println();
					}
				} while (!flagCustomer);// while the user inputs an invalid amount, keep asking until you get a valid
										// one
			} // finished creating the additional accounts
			else {// In case the user does not want to create an additional account, create a
					// savings account only
					// create the customer
					// create savings account
				Savings savingsAccount4 = createSavingsProcedure(savingsStartingBalance);
				// Create Checking Account
				Checking checkingAccount4 = ghostChecking();
				// Create a ghost credit account
				Credit creditAccount4 = ghostCredit();
				data.put(Integer.toString(customerIdTracker), new Customer(customerFirstName, // First name
						customerLastName, // Last name
						Integer.toString(customerIdTracker), // Customer ID
						password, // Has password
						email, // has email
						true, // Has Checking
						true, // Has Savings
						customerDOB, // Date of birth
						customerPhoneNumber, // Phone number
						customerAddressArray, // Address
						checkingAccount4, // Checking
						savingsAccount4, // createSavingsAccount(Integer.parseInt(VALS[10]),
						creditAccount4 // Credit
				));
				customerIds.put(customerFirstName + " " + customerLastName, Integer.toString(customerIdTracker));
			} // finished creating just the savings account
		} catch (IOException e) {
			System.out.println("Error:" + e);
		}
	}// END OF runCreateCustomer()

	/**
	 * This method acts like a helper method to the create a new user functionality
	 * by allowing the program to give a checking account with the starting balance
	 * provided to the user.
	 * 
	 * @param startingAmount value that holds the starting balance given by the
	 *                       user.
	 * @return returns the checking account created.
	 */
	public static Checking createCheckingProcedure(String startingAmount) {
		checkingAccountIdTracker++;
		Checking checkingAccount = createCheckingAccount(checkingAccountIdTracker, Double.parseDouble(startingAmount),
				0.0);
		System.out.println("Checking account created successfully.");
		System.out.println();
		return checkingAccount;
	} // END OF createCheckingProcedure()

	/**
	 * This method acts like a helper method to the create a new user functionality
	 * by allowing the program to give a credit account with the starting balance
	 * provided to the user.
	 * 
	 * @param startingAmount value that holds the starting balance given by the
	 *                       user.
	 * @return returns the credit account created.
	 */
	public static Credit createCreditProcedure(String startingAmount) {
		creditAccountIdTracker++;
		Credit creditAccount = createCreditAccount(creditAccountIdTracker, Double.parseDouble(startingAmount), 1000,
				0.0);
		System.out.println("Credit account created successfully.");
		System.out.println();
		return creditAccount;
	} // END OF createCreditProcedure()

	/**
	 * This method acts like a helper method to the create a new user functionality
	 * by allowing the program to give a savings account with the starting balance
	 * provided to the user.
	 * 
	 * @param startingAmount value that holds the starting balance given by the
	 *                       user.
	 * @return returns the savings account created.
	 */
	public static Savings createSavingsProcedure(String startingAmount) {
		System.out.println();
		savingsAccountIdTracker++;
		Savings savingsAccount = createSavingsAccount(savingsAccountIdTracker, Double.parseDouble(startingAmount), 0.0);
		System.out.println("Savings account created successfully.");
		System.out.println();
		return savingsAccount;
	}// END OF createSavingsProcedure()

	/**
	 * This method acts like a helper method to the create a new user functionality
	 * by allowing the program to give a checking account with no values for a
	 * customer that does not need a checking account.
	 * 
	 * @return a blank checking account
	 */
	public static Checking ghostChecking() {
		Checking ghost = createCheckingAccount(0, 0.0, 0.0);
		return ghost;
	} // END of ghostChecking()

	/**
	 * This method acts like a helper method to the create a new user functionality
	 * by allowing the program to give a checking account with no values for a
	 * customer that does not need a credit account.
	 * 
	 * @return a blank credit account
	 */
	public static Credit ghostCredit() {
		Credit ghost = createCreditAccount(0, 0.0, 0.0, 0.0);
		return ghost;
	} // END of ghostCredit()

	/**
	 * This method is going to handle the logic of runing the application as a
	 * customer
	 * 
	 * @param data           this is the data stored in a hashmap
	 * @param inputReader    input reader which handles user input
	 * @param transactionLog logs user transactions
	 */
	public static void runCustomer(HashMap<String, Customer> data, BufferedReader inputReader, Writer transactionLog) {
		try {
			String accountNumber = "";
			boolean isValidCustomer = false;
			while(!isValidCustomer){
				System.out.print("Enter customerID number: ");
				accountNumber = inputReader.readLine();
				Customer currentCustomer = data.get(accountNumber); // Getting checking object (accountNumber is the key)
				boolean verifyPassword = true; // boolean that checks if the password is correct, if false, it will ask the
												// user for
												// the password again.
				System.out.println();
				if(currentCustomer == null){
					System.out.println("ERROR: Customer does not exist, try again");
				}else{
					isValidCustomer = !isValidCustomer;
					System.out.print("Welcome " + currentCustomer.getName() + ". Please enter your password to continue: ");
					String userPassword = inputReader.readLine();
					System.out.println();
					do {
						if (passwordMatch(currentCustomer.getPassword(), userPassword) == true) {
							System.out.println("Password verified, thank you.");
							System.out.println();
							boolean transactionCompleted = false;
							while (!transactionCompleted) { // Keep looping until all transactions are completed
								System.out.println("Enter number of desired action:");
								System.out.println(
										"1. Inquire Balance 2. Deposit 3. Withdrawal 4. Transer Money 5. Send Money 6. Generate Log 7. Exit");
								String desiredAction = inputReader.readLine();
								String moneyAction = "";
								switch (desiredAction) {
									case "1": // Inquire Balance
										int desiredAccount = getDesiredAccount(true, inputReader);
										if (desiredAccount == 1)
											currentCustomer.getCheckingAccount().inquireBalance(); // Check checking balance
										if (desiredAccount == 2)
											currentCustomer.getSavingsAccount().inquireBalance(); // Check savings balance
										if (desiredAccount == 3)
											currentCustomer.getCreditAccount().inquireBalance(); // Check credit balance
										if (desiredAccount != 1 && desiredAccount != 2 && desiredAccount != 3)
											System.out.println("Error: No Balance inquired");
										break;
									case "2": // Deposit
										boolean flag2 = true;
										System.out.println("To which account would you like to perform this transaction?:");
										desiredAccount = getDesiredAccount(true, inputReader);
										System.out.print("Input amount you'd like to deposit: $");
										moneyAction = inputReader.readLine();
										do {
											if (desiredAccount == 1) { // Checking
												currentCustomer.getCheckingAccount()
														.receiveMoney(Double.parseDouble(moneyAction), "Deposit");
												transactionLog.write(currentCustomer.getName() + " deposited $" + moneyAction
														+ " in their checking.");
												transactionLog.write("\r\n");
												return;
											}
											if (desiredAccount == 2) { // Savings
												currentCustomer.getSavingsAccount()
														.receiveMoney(Double.parseDouble(moneyAction), "Deposit");
												transactionLog.write(currentCustomer.getName() + " deposited $" + moneyAction
														+ " in their savings.");
												transactionLog.write("\r\n");
												return;
											}
											if (desiredAccount == 3 && currentCustomer.getCreditAccount()
													.canReceive(Double.parseDouble(moneyAction))) { // Credit
												currentCustomer.getCreditAccount().receiveMoney(Double.parseDouble(moneyAction),
														"Deposit");
												transactionLog.write(currentCustomer.getName() + " deposited $" + moneyAction
														+ " in their credit.");
												transactionLog.write("\r\n");
												return;
											} else {
												System.out.print(
														"ERROR: Cannot deposit more than amount due. Please enter a valid amount:");
												moneyAction = inputReader.readLine();
												flag2 = false;
											}
										} while (!flag2);
										break;
									case "3": // Withdrawal
										boolean flag3 = true;
										System.out.print("To which account would you like to perform this transaction?:");
										desiredAccount = getDesiredAccount(false, inputReader);
										System.out.print("Input amount you'd like to withdraw: $");
										moneyAction = inputReader.readLine();
										do {
											if (Double.parseDouble(moneyAction) < currentCustomer.getCheckingAccount()
													.getCurrentBalance()
													&& Double.parseDouble(moneyAction) < currentCustomer.getSavingsAccount()
															.getCurrentBalance()) {
												if (desiredAccount == 1) { // Checking
													currentCustomer.getCheckingAccount()
															.sendMoney(Double.parseDouble(moneyAction), "Withdrawal");
													transactionLog.write(currentCustomer.getName() + " withdrew $" + moneyAction
															+ " in their checking.");
													transactionLog.write("\r\n");
													return;
												}
												if (desiredAccount == 2) { // Savings
													currentCustomer.getSavingsAccount()
															.sendMoney(Double.parseDouble(moneyAction), "Withdrawal");
													transactionLog.write(currentCustomer.getName() + " withdrew $" + moneyAction
															+ " in their savings.");
													transactionLog.write("\r\n");
													return;
												} else {
													System.out.println("Error: No Withdrawal made");
												}
											} else {
												System.out.print("Invalid input, please enter a valid amount to continue:");
												moneyAction = inputReader.readLine();
												flag3 = false;
											}
										} while (!flag3);
										break;
									case "4": // Transfer Money

										boolean checkAmountToTransfer = true;

										System.out.println("Which account do you want to transfer from?:");
										int transferFromAccount = getDesiredAccount(false, inputReader);
										System.out.println();
										System.out.print("How much would you like to transfer?: $");
										moneyAction = inputReader.readLine();
										System.out.println();
										do {
											if (Double.parseDouble(moneyAction) < currentCustomer.getCheckingAccount()
													.getCurrentBalance()
													&& Double.parseDouble(moneyAction) < currentCustomer.getSavingsAccount()
															.getCurrentBalance()) {
												if (transferFromAccount == 1)
													currentCustomer.getCheckingAccount()
															.sendMoney(Double.parseDouble(moneyAction), "Transfer");
												if (transferFromAccount == 2)
													currentCustomer.getSavingsAccount()
															.sendMoney(Double.parseDouble(moneyAction), "Transfer");

												System.out.print(
														"Which account do you want to transfer $" + moneyAction + " to?:");
												int transferToAccount = getDesiredAccount(true, inputReader);
												System.out.println();
												if (transferToAccount == 1) {
													currentCustomer.getCheckingAccount()
															.receiveMoney(Double.parseDouble(moneyAction), "Transfer");
													transactionLog.write(currentCustomer.getName() + " transfered $"
															+ moneyAction + " from savings to checking.");
													transactionLog.write("\r\n");
													return;
												}

												if (transferToAccount == 2) {
													currentCustomer.getSavingsAccount()
															.receiveMoney(Double.parseDouble(moneyAction), "Transfer");
													transactionLog.write(currentCustomer.getName() + " transfered $"
															+ moneyAction + " from checking to savings.");
													transactionLog.write("\r\n");
													return;
												}

												if (transferToAccount == 3 && currentCustomer.getCreditAccount()
														.canReceive(Double.parseDouble(moneyAction))) {
													currentCustomer.getCreditAccount()
															.receiveMoney(Double.parseDouble(moneyAction), "Transfer");
													if (transferFromAccount == 1) {
														transactionLog.write(currentCustomer.getName() + " transfered $"
																+ moneyAction + " from checking to credit.");
														transactionLog.write("\r\n");
													} else {
														transactionLog.write(currentCustomer.getName() + " transfered $"
																+ moneyAction + " from savings to credit.");
														transactionLog.write("\r\n");
													}
													return;
												}
											} else {
												System.out.print("Invalid input, please enter a valid amount to continue: ");
												System.out.println();
												moneyAction = inputReader.readLine();
												checkAmountToTransfer = false;
											}
										} while (!checkAmountToTransfer);
										break;
									case "5": // Send Money
										boolean flag5 = true;
										System.out.print("Which account would you like to send money from?: ");
										desiredAccount = getDesiredAccount(false, inputReader);
										System.out.println();
										System.out.print("Enter customer ID of receiver: ");
										String rC = inputReader.readLine(); //Returning customer
										do {
											if (Double.parseDouble(rC) < data.size() + 1 && Double.parseDouble(rC) >= 0) { //FIXME: Try to do another way
												Customer receivingCustomer = data.get(rC);
												System.out.print("Which account would you like to send money to?: ");
												int desiredReceiverAccount = getDesiredAccount(false, inputReader);
												System.out.println();
												System.out.print("Input amount you'd like to send: $");
												moneyAction = inputReader.readLine();
												System.out.println();
												// from checking
												if (desiredAccount == 1) {
													currentCustomer.getCheckingAccount()
															.sendMoney(Double.parseDouble(moneyAction), "Sent"); // Check
													// to their checking
													if (desiredReceiverAccount == 1) {
														receivingCustomer.getCheckingAccount()
																.receiveMoney(Double.parseDouble(moneyAction), "Deposit"); // Check
														transactionLog.write(currentCustomer.getName() + " sent $" + moneyAction
																+ " to " + receivingCustomer.getName());
														transactionLog.write("\r\n");
														return;
													}
													// to their savings
													if (desiredReceiverAccount == 2) {
														receivingCustomer.getSavingsAccount()
																.receiveMoney(Double.parseDouble(moneyAction), "Deposit"); // Check
														transactionLog.write(currentCustomer.getName() + " sent $" + moneyAction
																+ " to " + receivingCustomer.getName());
														transactionLog.write("\r\n");
														return;
													}
												}
												// from savings
												if (desiredAccount == 2) {
													currentCustomer.getSavingsAccount()
															.sendMoney(Double.parseDouble(moneyAction), "Sent"); // Check
													// to their checking
													// to their checking
													if (desiredReceiverAccount == 1) {
														receivingCustomer.getCheckingAccount()
																.receiveMoney(Double.parseDouble(moneyAction), "Deposit"); // Check
														transactionLog.write(currentCustomer.getName() + " sent $" + moneyAction
																+ " to " + receivingCustomer.getName());
														transactionLog.write("\r\n");
														return;
													}
													// to their savings
													if (desiredReceiverAccount == 2) {
														receivingCustomer.getSavingsAccount()
																.receiveMoney(Double.parseDouble(moneyAction), "Deposit"); // Check
														transactionLog.write(currentCustomer.getName() + " sent $" + moneyAction
																+ " to " + receivingCustomer.getName());
														transactionLog.write("\r\n");
														return;
													}
												}
											} else {
												System.out.print("Invalid input, please enter a valid ID: ");
												rC = inputReader.readLine();
												flag5 = false;
											}
										} while (!flag5);
										break;

										case "6": // Generate Log
										LogFile customerLogFile = new LogFile(currentCustomer);
										customerLogFile.createLogFile(currentCustomer.getFirstName() + currentCustomer.getLastName());
										break;

									case "7":
										System.out.println("Thank you, have a nice day!");
										return; // return, because we are ending all operations
									default: // Unrecognized character
										System.out.println("Action not recognized.");
										break;
								}
							}
						} else {
							System.out.print("Incorrect password. Please try again: ");
							verifyPassword = false;// set the verifyPassword to false
							userPassword = inputReader.readLine(); // ask for the password again.
							System.out.println();
						}
					} while (!verifyPassword);// keep asking for the password if user enters the wrong password.
				}//END Of while for valid customer
			}
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	}// END OF runCustomer()

	/**
	 * This method runs the bank manager functionality
	 *
	 * @param data           customer data stored in a hashmap
	 * @param inputReader    input reader handles user input
	 * @param transactionLog to read the user transactions
	 */
	public static void runBankManager(HashMap<String, Customer> data, HashMap<String, String> customerIds,
			BufferedReader inputReader, Writer transactionLog) {
		try {
			boolean transactionCompleted = false;
			while (!transactionCompleted) {
				System.out.print("1. Inquire customer by Name 2. Create Bank Statement 3. Print all accounts 4. Exit");
				System.out.println();
				String desiredAction = inputReader.readLine();
				switch (desiredAction) {

					case "1":
					boolean isValidIDC = false;
					while(!isValidIDC){
						System.out.print("Enter first name of desired customer: ");
						String customerFirstName = inputReader.readLine();
						System.out.println();
						System.out.print("Enter last name of desired customer: ");
						String customerLastName = inputReader.readLine();
						String desiredCustomerId = customerIds.get(customerFirstName + " " + customerLastName);
						Customer desiredCustomer = data.get(desiredCustomerId);
						if(desiredCustomer == null){
							System.out.println("ERROR: Customer does not exist.");
							System.out.println();
							System.out.println();
						}else{
							isValidIDC = !isValidIDC;
						System.out.println();
						System.out.println("Customer: " + desiredCustomer.getName());
						System.out.println();
						System.out.print("Checking ");
						desiredCustomer.getCheckingAccount().inquireBalance();
						System.out.print("Savings ");
						desiredCustomer.getSavingsAccount().inquireBalance();
						System.out.print("Credit ");
						desiredCustomer.getCreditAccount().inquireBalance();
						System.out.println();
					}
				}
						break;
					case "2":
					boolean isValidID = false;
					while(!isValidID){
					System.out.print("Enter first name of customer: ");
						String statementCustomerFirstName = inputReader.readLine();
						System.out.println();
						System.out.print("Enter last name of customer: ");
						String statementCustomerLastName = inputReader.readLine();
						System.out.println();
						String statementCustomerId = customerIds
								.get(statementCustomerFirstName + " " + statementCustomerLastName);

						Customer statementCustomer = data.get(statementCustomerId);
						if(statementCustomer == null){
							System.out.println("ERROR: Customer does not exist.");
							System.out.println();
							System.out.println();
						}else{
							isValidID = !isValidID;
						BankStatement customerBankStatement = new BankStatement(statementCustomer);
						customerBankStatement.createBankStatement(statementCustomerFirstName + statementCustomerLastName
								+ "statement" + (new SimpleDateFormat("yyyy_MM_dd")).format(new Date()));
						}
							}
								break;
					case "3":
						for (String key : data.keySet()) {
							Customer currentCustomer = data.get(key);
							System.out.print("Customer: " + currentCustomer.getName());
							System.out.print("Checking ");
							currentCustomer.getCheckingAccount().inquireBalance();
							System.out.print("Savings ");
							currentCustomer.getSavingsAccount().inquireBalance();
							System.out.print("Credit ");
							currentCustomer.getCreditAccount().inquireBalance();
							System.out.println();
						}
						break;
					case "4":
						transactionCompleted = !transactionCompleted;
						break;
					default:
						System.out.println("Action not recognized. Try again.");
						break;
				}
			}
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	}// END OF runBankManager()

	/**
	 * This method reads transaction from a csv file
	 * 
	 * @param data           this is the user data
	 * @param customerIds    this is a hashmap of the customer names and their ids
	 * @param transactionLog this is a log of the transactions
	 */
	public static void runTransactionReader(HashMap<String, Customer> data, HashMap<String, String> customerIds,
			Writer transactionLog) {
		BufferedReader brTransactions = null;
		String currentTransaction = "";
		try {
			brTransactions = new BufferedReader(new FileReader("Transaction Actions(3).csv"));
			int count = 0;
			while ((currentTransaction = brTransactions.readLine()) != null) {
				if (count > 0) {
					String sender = "";
					String receiver = "";
					String senderAccount = "";
					String receiverAccount = "";
					String transactionAction = "";
					String amount = "";
					String[] TRANSACTION = currentTransaction.split(",");
					for(int i = 0; i < TRANSACTION.length; i++){
						System.out.print(TRANSACTION[i] + " ");
					}
					System.out.println();
					// 0 first naem 1 last name 2 from where 3 action 4 reciever first 5 reciever
					// last 6 to where 7 amount
					if (TRANSACTION[3].equals("inquires")) {
						sender = TRANSACTION[0] + " " + TRANSACTION[1];
						receiver = TRANSACTION[0] + " " + TRANSACTION[1];
						transactionAction = TRANSACTION[3];
						senderAccount = TRANSACTION[2];
						receiverAccount = TRANSACTION[2];
					} else {
						sender = TRANSACTION[0] + " " + TRANSACTION[1];
						receiver = TRANSACTION[4] + " " + TRANSACTION[5];
						transactionAction = TRANSACTION[3];
						senderAccount = TRANSACTION[2];
						receiverAccount = TRANSACTION[6];
						amount = TRANSACTION[7];
					}
					switch (transactionAction) {
						case "pays":
							transactionReaderTwoCustomerHandler(data, customerIds, sender, receiver, transactionAction,
									senderAccount, receiverAccount, Double.parseDouble(amount));
							break;
						case "withdraws":
							transactionReaderOneCustomerHandler(data, customerIds, sender, transactionAction,
									senderAccount, Double.parseDouble(amount));
							break;
						case "inquires":
							transactionReaderOneCustomerHandler(data, customerIds, sender, transactionAction,
									senderAccount, 0.0);
							break;
						case "deposits":
							transactionReaderOneCustomerHandler(data, customerIds, receiver, transactionAction,
									receiverAccount, Double.parseDouble(amount));
							break;
						case "transfers":
							transactionReaderTwoCustomerHandler(data, customerIds, sender, receiver, transactionAction,
									senderAccount, receiverAccount, Double.parseDouble(amount));
							break;
						default:
							System.out.println("Action at row " + (count + 1) + " not recognized.");
							break;
					}
					if (sender.equals("")) {
						// run single person transaction
					} else {
						// run multiple person transaction
					}
				}
				count++;
			}
			System.out.println("Transactions Completed.");
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	} // END OF runTransactionReader()

	/**
	 * This method handls the logic of reading a single customer transction from a
	 * transaction reader
	 * 
	 * @param data              the hashmap of the users
	 * @param customerIds       hashmap of the users and their ids
	 * @param customer          string of customer name
	 * @param transactionAction string of the tansaction that is oing to be done
	 * @param account           the account that is receiving the action
	 * @param amount            the amount of money being moved
	 */
	public static void transactionReaderOneCustomerHandler(HashMap<String, Customer> data,
			HashMap<String, String> customerIds, String customer, String transactionAction, String account,
			double amount) {
		String customerId = customerIds.get(customer);
		Customer customerAccount = data.get(customerId);
		String caseAction = "Handler";
		switch (transactionAction) {
			case "inquires":
				if (account.equals("Checking"))
					customerAccount.getCheckingAccount().inquireBalance();
				if (account.equals("Savings"))
					customerAccount.getSavingsAccount().inquireBalance();
				if (account.equals("Credit"))
					customerAccount.getCreditAccount().inquireBalance();
				break;
			case "withdraws":
				if (account.equals("Checking"))
					customerAccount.getCheckingAccount().sendMoney(amount, caseAction);
				if (account.equals("Savings"))
					customerAccount.getSavingsAccount().sendMoney(amount, caseAction);
				if (account.equals("Credit"))
					customerAccount.getCreditAccount().sendMoney(amount, caseAction);
				break;
			case "deposits":
				if (account.equals("Checking"))
					customerAccount.getCheckingAccount().receiveMoney(amount, caseAction);
				if (account.equals("Savings"))
					customerAccount.getSavingsAccount().receiveMoney(amount, caseAction);
				if (account.equals("Credit"))
					customerAccount.getCreditAccount().receiveMoney(amount, caseAction);
				break;
			default:
				System.out.println("Action not recognized");
				break;
		}
	}

	/**
	 * This method handles the logic of a transaction between two people from the
	 * transaction reader
	 * 
	 * @param data              hashmap of the users and accounts
	 * @param customerIds       hashmap of users and their ids
	 * @param sender            name of the sender
	 * @param receiver          name of the receiver
	 * @param transactionAction name of the transction being done
	 * @param senderAccount     the account that is performing the action
	 * @param receiverAccount   the accoun that is receiving the action
	 * @param amount            the amount of money that is being moved
	 */
	public static void transactionReaderTwoCustomerHandler(HashMap<String, Customer> data,
			HashMap<String, String> customerIds, String sender, String receiver, String transactionAction,
			String senderAccount, String receiverAccount, double amount) {
		String senderId = customerIds.get(sender);
		String receiverId = customerIds.get(receiver);
		Customer senderCustomer = data.get(senderId);
		Customer receiverCustomer = data.get(receiverId);
		switch (transactionAction) {
			case "pays":
				twoCustomerHandlerActions(senderCustomer, receiverCustomer, senderAccount, receiverAccount, amount);
				break;
			case "transfers":
				twoCustomerHandlerActions(senderCustomer, receiverCustomer, senderAccount, receiverAccount, amount);
				break;
			default:
				System.out.println("Action not recognized");
				break;
		}
	} // END OF transactionReaderTwoCustomerHandler()

	/**
	 * This method is to facilitate the logic of two people transaction from the
	 * transaction reader
	 * 
	 * @param senderCustomer   This is a customer object the is sending money
	 * @param receiverCustomer this is a customer object that is receiving money
	 * @param senderAccount    this is the account of the sender that is being
	 *                         affected
	 * @param receiverAccount  this is the account of the receiver that is being
	 *                         affected
	 * @param amount           this is the amount of money tht is being moved
	 */
	public static void twoCustomerHandlerActions(Customer senderCustomer, Customer receiverCustomer,
			String senderAccount, String receiverAccount, double amount) {
		String caseAction = "Handler";
		switch (senderAccount) {
			case "Checking":
				senderCustomer.getCheckingAccount().sendMoney(amount, caseAction);
				if (receiverAccount.equals("Checking"))
					receiverCustomer.getCheckingAccount().receiveMoney(amount, caseAction);
				if (receiverAccount.equals("Savings"))
					receiverCustomer.getSavingsAccount().receiveMoney(amount, caseAction);
				if (receiverAccount.equals("Credit"))
					receiverCustomer.getCreditAccount().receiveMoney(amount, caseAction);
				break;
			case "Savings":
				senderCustomer.getSavingsAccount().sendMoney(amount, caseAction);
				if (receiverAccount.equals("Checking"))
					receiverCustomer.getCheckingAccount().receiveMoney(amount, caseAction);
				if (receiverAccount.equals("Savings"))
					receiverCustomer.getSavingsAccount().receiveMoney(amount, caseAction);
				if (receiverAccount.equals("Credit"))
					receiverCustomer.getCreditAccount().receiveMoney(amount, caseAction);
				break;
			default:
				System.out.println("ERROR: Account not valid");
				break;
		}
	} // END OF twoCustomerHandlerActions()

	/**
	 * This method handles the logic of retreiving a desired account(checking,
	 * savings, credit)
	 * 
	 * @param showCredit  we are checking where we want to display credit account
	 * @param inputReader handles user input
	 * @return integer depending on the desired account
	 */
	public static int getDesiredAccount(boolean showCredit, BufferedReader inputReader) { // 1. Checking 2. Savings 3.
																							// Credit
		try {
			String accountAction = "";
			boolean isDone = false;
			while (!isDone) {
				if (showCredit) {
					System.out.println("1. Checking 2. Savings 3. Credit");
					accountAction = inputReader.readLine();
					if (accountAction.equals("1"))
						return 1; // Checking
					if (accountAction.equals("2"))
						return 2; // Savings
					if (accountAction.equals("3"))
						return 3; // Credit
					else
						System.out.println("Input not recognized. Try again.");// Try again
				} else {
					System.out.println("1. Checking 2. Savings");
					accountAction = inputReader.readLine();
					if (accountAction.equals("1"))
						return 1; // Checking
					if (accountAction.equals("2"))
						return 2; // Savings
					else
						System.out.println("Input not recognized. Try again."); // Try again
				}
			}
			System.out.println("Error: No account defined");
			return -1;
		} catch (IOException e) {
			System.out.println("Error: " + e);
			return -1;
		}
	} // END OF getDesiredAccount()

	/**
	 * This method provides the functionality of creating a file with the updated
	 * information of the bank.
	 * 
	 * @param data the hashmap that holds the information of all the users of the
	 *             bank
	 */
	public static void createCSVCopy(HashMap<String, Customer> data) throws IOException {
		// create the ArrayList<String> of the new lines for the file
		// convert to objects again,
		BufferedReader br = new BufferedReader(new FileReader("CS 3331 - Bank Users 4.csv"));
		ArrayList<String> lines = new ArrayList<String>();

		data.entrySet().forEach(entry -> {
			lines.add(entry.getValue().getSavingsAccount().getAccountNumber() + "," + entry.getValue().getLastName()
					+ "," + entry.getValue().getCustomerID() + ","
					+ (entry.getValue().getDateOfBirth()[0] + entry.getValue().getDateOfBirth()[1]) + ","
					+ +entry.getValue().getCheckingAccount().getAccountNumber() + ","
					+ entry.getValue().getCreditAccount().getAccountNumber() + "," + entry.getValue().getPhoneNumber()
					+ "," + entry.getValue().getCheckingAccount().getCurrentBalance() + ","
					+ entry.getValue().getSavingsAccount().getCurrentBalance() + "," + entry.getValue().getPassword()
					+ "," + entry.getValue().getCreditAccount().getCurrentBalance() + ","
					+ (entry.getValue().getAddress()[0] + entry.getValue().getAddress()[1]
							+ entry.getValue().getAddress()[2])
					+ "," + entry.getValue().getFirstName() + "," + entry.getValue().getEmail() + ","
					+ entry.getValue().getCreditAccount().getCreditLimit());
		});

		try {
			// creating sorted copy / printing to file:

			Collections.sort(lines, new Comparator<String>() {
				@Override
				public int compare(String s1, String s2) {
					int s1i = Integer.parseInt(s1.substring(0, s1.indexOf(",")));
					int s2i = Integer.parseInt(s2.substring(0, s2.indexOf(",")));
					return s1i - s2i;
				}
			});

			// create a BufferedWriter
			// fileWriter set to false to overwrite the existing file
			BufferedWriter outputFile = new BufferedWriter(new FileWriter("CS 3331 - Bank Users 4 - Copy.csv", false));
			// read and print the first line of the last file
			outputFile.write(br.readLine());
			outputFile.newLine();// first jump to the next line;
			// print the rest of the file
			for (String line : lines) {
				outputFile.write(line);
				outputFile.newLine();
			}
			outputFile.close();

			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException es) {
			es.printStackTrace();
		}
	}// END OF THE CREATE COPY METHOD

	/**
	 * This method is a helper method used to generate a password with the required
	 * format for a new user when the program is creating a new customer.
	 * 
	 * @param firstName the first name of the customer.
	 * @param lastName  the last name of the customer.
	 * @return the new password for the user.
	 */
	public static String generatePassword(String firstName, String lastName) {
		String password = lastName + "*" + firstName + "!987";
		return password;
	} // END of generatePassword

	/**
	 * This method is a helper method used to verify that the password provided by
	 * the user matches the required password to access to the functionalities of
	 * the customers.
	 * 
	 * @param password  the password required by the user.
	 * @param userInput the password provided by the user.
	 * @return false if the passwords does not match, true if the passwords match
	 */
	public static boolean passwordMatch(String password, String userInput) {
		if (userInput.equals(password)) {
			return true;
		} else {
			return false;
		}
	} // END of passwordMatch
} // END OF class
	// Mickey Mouse 000-00-0001
	// Donald Duck 000-00-0002