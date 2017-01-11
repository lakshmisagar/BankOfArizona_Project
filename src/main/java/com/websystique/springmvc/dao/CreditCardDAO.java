package com.websystique.springmvc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.websystique.springmvc.model.Account;
import com.websystique.springmvc.model.CreditCard;
import com.websystique.springmvc.model.CreditCardTransc;

public interface CreditCardDAO {
	public boolean debit(CreditCard card, double amt);
	public void credit(CreditCard card, double amt);
	public boolean makePayment(String user, int toAcc, double amt);
	public double interestGeneration(CreditCard card);
	public double latePaymentFee(CreditCard card);
	public double newCreditLimit(CreditCard crd);
	public void saveTransaction(CreditCardTransc tx);
	public List<CreditCard> listCards(String user);
	public List<CreditCardTransc> getTransaction(String user); 
}
