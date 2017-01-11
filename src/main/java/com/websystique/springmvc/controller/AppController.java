package com.websystique.springmvc.controller;
import java.sql.Date;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.websystique.springmvc.model.Account;
import com.websystique.springmvc.service.AccountService;
import com.websystique.springmvc.model.User;
import com.websystique.springmvc.model.UserProfile;
import com.websystique.springmvc.model.CreditCard;
import com.websystique.springmvc.model.Transaction;
import com.websystique.springmvc.service.OtpMailService;
import com.websystique.springmvc.service.UserProfileService;
import com.websystique.springmvc.service.UserService;
import com.websystique.springmvc.service.CreditCardService;
import com.websystique.springmvc.service.TransactionService;

import com.websystique.springmvc.model.Transc_mstr;
import com.websystique.springmvc.model.acc_mstr;
import com.websystique.springmvc.service.extuserservice;

@Controller
@RequestMapping("/")
@SessionAttributes("roles")
public class AppController {

	@Autowired
	private extuserservice extUserService;


	@Qualifier(value="extUserService")
	public void setUserService(extuserservice us){
		this.extUserService = us;
	}


	/*---------*/

	@Autowired
	UserService userService;
	
	@Autowired
	OtpMailService otpMailService;

	@Autowired
	AccountService accountService;

	@Autowired
	CreditCardService creditCardService;

	@Autowired
	TransactionService transactionService;

	@Autowired
	UserProfileService userProfileService;

	@Autowired
	MessageSource messageSource;

	@Autowired
	PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;

	@Autowired
	AuthenticationTrustResolver authenticationTrustResolver;

	@RequestMapping(value = {"/","/home" }, method = RequestMethod.GET)
	public String displayHome(ModelMap model) {
		System.out.println("INTO THE HOME CONTROLLER");
		//		List<User> users = userService.findAllUsers();
		//		model.addAttribute("users", users);
		model.addAttribute("loggedinuser", getPrincipal());
		return "home";
	}

	/**
	 * This method will list all existing users.
	 */
	@RequestMapping(value = {"/list" }, method = RequestMethod.GET)
	public String listUsers(ModelMap model) {

		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);
		model.addAttribute("loggedinuser", getPrincipal());
		return "userslist";
	}



	@RequestMapping("/pending_trans")
	public String PendingTransactions(Model model){
		List<Transaction> t = transactionService.getPendingTransactions();
		model.addAttribute("transactions", t);
		return "pending_trans";
	}
	/**
	 * This method will list all existing users.
	 */


	/**
	 * This method will provide the medium to add a new user.
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.GET)
	public String newUser(ModelMap model) {
		User user = new User();
		Account acc = new Account();
		model.addAttribute("account",acc);
		model.addAttribute("user", user);
		model.addAttribute("edit", false);
		model.addAttribute("loggedinuser", getPrincipal());
		return "registration";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * saving user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.POST)
	public String saveUser(@Valid User user, BindingResult result,
			ModelMap model) {

		if (result.hasErrors()) {
			return "registration";
		}

		/*
		 * Preferred way to achieve uniqueness of field [sso] should be implementing custom @Unique annotation 
		 * and applying it on field [sso] of Model class [User].
		 * 
		 * Below mentioned peace of code [if block] is to demonstrate that you can fill custom errors outside the validation
		 * framework as well while still using internationalized messages.
		 * 
		 */
		if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
			result.addError(ssoError);
			return "registration";
		}

		userService.saveUser(user);
		
		int ids = user.getTypeId();
		
		if(ids == 4 || ids == 5){
			Account defaultAccount = new Account();
			int max_acc = accountService.getMaxTransaction();
			defaultAccount.setAccount_no(max_acc+1);
			defaultAccount.setAccount_type_id(1);
			defaultAccount.setBalance(1000);
			int max_uid = userService.getMaxId();
			defaultAccount.setStatus("Active");
			defaultAccount.setUid(max_uid);
			accountService.saveAccount(defaultAccount);		
		}
		
		
		
		
		model.addAttribute("success", "User " + user.getFirstName() + " "+ user.getLastName() + " registered successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		//return "success";
		return "registrationsuccess";
	}


	/**
	 * This method will provide the medium to update an existing user.
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.GET)
	public String editUser(@PathVariable String ssoId, ModelMap model) {
		User user = userService.findBySSO(ssoId);
		model.addAttribute("user", user);
		model.addAttribute("edit", true);
		model.addAttribute("loggedinuser", getPrincipal());
		return "registration";
	}
	@RequestMapping(value = {"/approve-{transactionId}" }, method = RequestMethod.GET)
	public String ApproveTransactions(@PathVariable String transactionId, ModelMap model) {		

		List<Transaction> tx=transactionService.updateTransaction(Integer.parseInt(transactionId));
		accountService.transferFundAfterAuthorize(getPrincipal(), Integer.parseInt(tx.get(0).getTrxMerchant()), tx.get(0).getTrxAmount());

		//		List<User> users = userService.findAllUsers();
		//		model.addAttribute("users", users);
		//		model.addAttribute("loggedinuser", getPrincipal());
		return "home";
	}
	


	/**
	 * This method will be called on form submission, handling POST request for
	 * updating user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.POST)
	public String updateUser(@Valid User user, BindingResult result,
			ModelMap model, @PathVariable String ssoId) {

		if (result.hasErrors()) {
			return "registration";
		}

		/*//Uncomment below 'if block' if you WANT TO ALLOW UPDATING SSO_ID in UI which is a unique key to a User.
		if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
		    result.addError(ssoError);
			return "registration";
		}*/


		userService.updateUser(user);

		model.addAttribute("success", "User " + user.getFirstName() + " "+ user.getLastName() + " updated successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		return "registrationsuccess";
	}


	/**
	 * This method will delete an user by it's SSOID value.
	 */
	@RequestMapping(value = { "/delete-user-{ssoId}" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable String ssoId) {
		userService.deleteUserBySSO(ssoId);
		return "redirect:/list";
	}


	/**
	 * This method will provide UserProfile list to views
	 */
	@ModelAttribute("roles")
	public List<UserProfile> initializeProfiles() {
		return userProfileService.findAll();
	}

	/**
	 * This method handles Access-Denied redirect.
	 */
	@RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
		model.addAttribute("loggedinuser", getPrincipal());
		return "accessDenied";
	}

	/**
	 * This method handles Access-Denied redirect.
	 */
	@RequestMapping(value = "/viewAccounts", method = RequestMethod.GET)
	public String viewAccountsPage(ModelMap model) {
		model.addAttribute("loggedinuser", getPrincipal());
		return "viewAccounts";
	}

	/**
	 * This method handles login GET requests.
	 * If users is already logged-in and tries to goto login page again, will be redirected to list page.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		if (isCurrentAuthenticationAnonymous()) {
			return "login";
		} else {
			return "redirect:/home";  
		}
	}

	/**
	 * This method handles logout requests.
	 * Toggle the handlers if you are RememberMe functionality is useless in your app.
	 */
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){    
			//new SecurityContextLogoutHandler().logout(request, response, auth);
			persistentTokenBasedRememberMeServices.logout(request, response, auth);
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:/login?logout";
	}

	/**
	 * This method returns the principal[user-name] of logged-in user.
	 */
	private String getPrincipal(){
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails)principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

	/**
	 * This method returns true if users is already authenticated [logged-in], else false.
	 */
	private boolean isCurrentAuthenticationAnonymous() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authenticationTrustResolver.isAnonymous(authentication);
	}


	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
	public String listAccounts(Model model) {
		String ssoid=getPrincipal();
		model.addAttribute("account", new Account());
		//model.addAttribute("listPersons", this.personService.listPersons());
		if(accountService.listAccounts(ssoid) == null) {
			System.out.println("NUllllll");
		}
		model.addAttribute("listAccounts", accountService.listAccounts(ssoid));
		return "account";
	}

	@RequestMapping(value = "/creditcard", method = RequestMethod.GET)
	public String creditCardDetails(Model model) {
		String user = getPrincipal();
		model.addAttribute("card", new CreditCard());
		if(creditCardService.listCards(user) == null) {
			System.out.println("NUllllll");
		}
		model.addAttribute("listCards", creditCardService.listCards(user));
		return "creditcard";
	}

	@RequestMapping("/debitCC/{amt}")
	public String debit(CreditCard card, @PathVariable("amt") double amt)
	{
		this.creditCardService.debit(card,amt);
		return "into debit function";
	}

	@RequestMapping("/creditCC/{amt}")
	public String credit(CreditCard card, @PathVariable("amt") double amt)
	{
		this.creditCardService.credit(card,amt);
		return "";
	}

	@RequestMapping(value ="makePayment", method = RequestMethod.POST)
	public String makePayment(Model model, HttpServletRequest req, RedirectAttributes attr)
	{
		model.addAttribute("creditcard", new CreditCard());
		model.addAttribute("loggedinuser", getPrincipal());
		String user = getPrincipal();
		if(creditCardService.listCards(user) == null) {
			System.out.println("NUllllll");
		}
		model.addAttribute("listCards", creditCardService.listCards(user));
		this.creditCardService.makePayment(user, Integer.parseInt(req.getParameter("toAcc")),Double.parseDouble(req.getParameter("amount")));
		return "creditcard";
	}

	@RequestMapping(value ="cctransaction", method = RequestMethod.POST)
	public String ccTransactions(Model model, HttpServletRequest req, RedirectAttributes attr) {
		model.addAttribute("loggedinuser", getPrincipal());
		String user = getPrincipal();
		model.addAttribute("card", new CreditCard());
		if(creditCardService.listCards(user) == null) {
			System.out.println("NUllllll");
		}
		model.addAttribute("listCards", creditCardService.listCards(user));
		if(req.getParameter("display")!=null){
			model.addAttribute("listcctransactions",this.creditCardService.getTransaction(user));
		}
		return "creditcard";
	}

	@RequestMapping("/interestGenerationCC/{amt}")
	public String interestGeneration(CreditCard card)
	{
		this.creditCardService.interestGeneration(card);
		return "";
	}

	@RequestMapping("/latePaymentCC/{amt}")
	public String latePaymentFee(CreditCard card)
	{
		this.creditCardService.latePaymentFee(card);
		return "";
	}

	@RequestMapping("/newCreditLimitCC/{amt}")
	public String newCreditLimit(CreditCard card)
	{
		this.creditCardService.newCreditLimit(card);
		return "";
	}	

	@RequestMapping("/edit/{id}")
	public String editAccount(@PathVariable("id") int id, Model model){
		model.addAttribute("account", this.accountService.getAccountById(id));
		model.addAttribute("listAccounts", this.accountService.listAccounts(getPrincipal()));
		return "account";
	}


	@RequestMapping(value= "/account/add", method = RequestMethod.POST)
	public String addPerson(@ModelAttribute("account") Account c){

		if(c.getAccount_no() == 0){
			//new person, add it
			this.accountService.addAccount(c);
		}else{
			//existing person, call update
			this.accountService.updateAccount(c);
		}

		return "redirect:/accounts";

	}


	@RequestMapping("/debit/{amt}")
	public String debit(Account acc, @PathVariable("amt") double amt)

	//public String debit(Account acc, @PathVariable("amt") double amt,Model model)
	{
		this.accountService.debit(acc,amt);
		return "into debit function";

		//model.addAttribute("debit", this.accountService.debit(acc,amt));
	}

	@RequestMapping("/credit/{amt}")
	public String credit(Account acc, @PathVariable("amt") double amt)
	{
		this.accountService.credit(acc,amt);
		return "";
	}

	@RequestMapping(value ="payCreditCard", method = RequestMethod.POST)
	public String payCreditCard(Model model, HttpServletRequest req, RedirectAttributes attrt)
	{
		model.addAttribute("account", new Account());
		model.addAttribute("loggedinuser", getPrincipal());
		String user = getPrincipal();
		if(creditCardService.listCards(user) == null) {
			System.out.println("NUllllll");
		}
		model.addAttribute("listCards", creditCardService.listCards(user));
		this.accountService.payCreditCard(user, Double.parseDouble(req.getParameter("amount")));
		return "creditcard";
	}

	@RequestMapping(value = "/transactions", method = RequestMethod.GET)
	public String transactionDetails(Model model) {
		model.addAttribute("transactions", new Transaction());
		return "transactions";
	}

	@RequestMapping(value = "/transactions/latestTransactions", method = RequestMethod.GET)
	public String listTransactions(int accountNum) {
		this.transactionService.getLatestTrnsactions(accountNum);
		return "";
	}

	@RequestMapping(value = "/viewtransactions", method = RequestMethod.GET)
	public String viewTransactionsPage(Model model) {
		return "viewtransactions";
	}

	@RequestMapping("/downloadPDF")
	public String downloadPDF(Model model){
		model.addAttribute("account", new Account());
		//model.addAttribute("users", users);
		String user = getPrincipal();
		model.addAttribute("loggedinuser", getPrincipal());
		this.transactionService.getTransactionsPDF(user);
		return "viewtransactions";

	}


	@RequestMapping(value = "/managefunds", method = RequestMethod.GET)
	public String viewManageFundsPage(Model model) {
		return "managefunds";
	}
	
	@RequestMapping(value ="validateOtp", method = RequestMethod.POST)
	public String checkOtp(Model model, HttpServletRequest req, RedirectAttributes attr) {
		this.otpMailService.checkOtp(req.getParameter("otp"), getPrincipal());
		return "managefunds";
	}
	
	
	
	@RequestMapping(value ="transferFund", method = RequestMethod.POST)
	public String transferFunds(Model model, HttpServletRequest req, RedirectAttributes attr) {
		model.addAttribute("account", new Account());
		model.addAttribute("loggedinuser", getPrincipal());
		String user = getPrincipal();
	
		boolean emailFlag = false;
		if(!req.getParameter("toEmail").isEmpty())
			emailFlag = this.accountService.transferFundUsingEmail(user, req.getParameter("toEmail"), Double.parseDouble(req.getParameter("amount")));
		
		boolean phoneFlag = false;
		if(!req.getParameter("toPhone").isEmpty())
			phoneFlag = this.accountService.transferFundUsingPhone(user, Long.parseLong(req.getParameter("toPhone")), Double.parseDouble(req.getParameter("amount")));
		
		boolean flag = false;
		if(!req.getParameter("toAcc").isEmpty())
		    flag = this.accountService.transferFund(user,Integer.parseInt(req.getParameter("toAcc")),Double.parseDouble(req.getParameter("amount")));
		return "managefunds";
	}


	/*------------------------------------------------------------EXT USER--------------------------------------------------*/
	@RequestMapping(value ="transaction", method = RequestMethod.POST)
	public String creditAndDebit(Model model, HttpServletRequest req, RedirectAttributes attr) {
		model.addAttribute("loggedinuser", getPrincipal());
		String user = getPrincipal();
		int extUserId = this.extUserService.getUserId(user);
		int accNo =  Integer.parseInt(this.extUserService.getuseraccountnumbers(extUserId).get(0));
		if(req.getParameter("display")!=null){
			model.addAttribute("listtransactions",this.extUserService.getTransaction(accNo));
			return "viewtransactions";
		}
		if(req.getParameter("debitandcredit")!=null){
			Integer amount= Integer.parseInt(req.getParameter("amount")) ;
			String status = this.extUserService.Updatetransactions(accNo, amount, req.getParameter("debitandcredit")) ;
			if(!status.equals("insufficient balance"))
				this.extUserService.UpdateBalance(accNo, amount,  req.getParameter("debitandcredit")) ;
			model.addAttribute("status",status);
			return "managefunds";
		}
		return "account";
	}
	
	@RequestMapping(value = "/pendingapprovalsbyuser")
	public String pendingapprovalsbyuser(Model model,HttpServletRequest req, RedirectAttributes attr) {
		return "pendingapprovalsbyuser";
	}

	@RequestMapping(value ="/approve-{transc_id}")
	public String approvemerchantrequest(@PathVariable int transc_id , Model model){
		model.addAttribute("approved",this.extUserService.userapproval(transc_id)) ;
		return "pendingapprovalsbyuser" ;
	}

	@RequestMapping(value ="approvals")
	public String getpendingapprovals(Model model,HttpServletRequest req, RedirectAttributes attr) {
		model.addAttribute("loggedinuser", getPrincipal());
		String user = getPrincipal();
		int extUserId = this.extUserService.getUserId(user);
		int accNo =  Integer.parseInt(this.extUserService.getuseraccountnumbers(extUserId).get(0));
		model.addAttribute("usermerchantrequest",this.extUserService.userauthorization(accNo)) ;
		return "pendingapprovalsbyuser";
	}

	@RequestMapping(value = "/submitpaymentbymerchant")
	public String submitpaymentbymerchant(Model model,HttpServletRequest req, RedirectAttributes attr) {
		return "submitpaymentbymerchant";
	}
	
	@RequestMapping(value ="submitpayment")
	public String submitpayment(Model model,HttpServletRequest req, RedirectAttributes attr) {
		model.addAttribute("loggedinuser", getPrincipal());
		String user = getPrincipal();
		Integer amount= Integer.parseInt(req.getParameter("amount")) ;
		Integer accountno = Integer.parseInt(req.getParameter("accountno")) ;
		model.addAttribute("submitpayment",this.extUserService.merchantpaymentforuser(user, accountno, amount)) ;
		//String balstatus = this.extUserService.UpdateBalance(accountno, amount,  "debit") ;
		return "submitpaymentbymerchant";
	}
}