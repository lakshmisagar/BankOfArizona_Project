package com.websystique.springmvc.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.websystique.springmvc.model.Account;

import com.websystique.springmvc.model.CreditCard;
import com.websystique.springmvc.model.CreditCardTransc;
import com.websystique.springmvc.model.Transaction;
import com.websystique.springmvc.model.Transc_mstr;
import com.websystique.springmvc.model.User;
import com.websystique.springmvc.service.CreditCardService;
import com.websystique.springmvc.service.TransactionService;

@Repository("creditCardDao")
public class CreditCardDAOImpl extends AbstractDao<Integer, CreditCardTransc> implements CreditCardDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private CreditCardService creditCardService;

	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}

	@Override
	public boolean debit(CreditCard card, double amt) {
		Session session = this.sessionFactory.getCurrentSession();
		double balance = card.getCreditLimit() - card.getAmountSpent();
		if(amt <= balance){
			card.setAmountSpent(card.getAmountSpent() + amt);
			session.update(card);
			return true;
		}  
		return false;
	}

	@Override
	public void credit(CreditCard card, double amt) {
		Session session = this.sessionFactory.getCurrentSession();
		card.setAmountSpent(card.getAmountSpent() - amt);
		session.update(card);
	}


	@Override
	public boolean makePayment(String user, int toAcc, double amt) {
		Session session = this.sessionFactory.getCurrentSession();
		List<User> usersList = session.createQuery("from User where ssoId = '"+user+"'").list();
		int id = 0;
		if(usersList.size()>0)
			id = usersList.get(0).getId();
		List<CreditCard> cardsList = session.createQuery("from CreditCard where uid="+id).list();
		List<Account> toAccList = session.createQuery("from Account where account_no="+toAcc).list();
		double balance = (cardsList.get(0).getCreditLimit() - cardsList.get(0).getAmountSpent());
		if(amt<=balance && toAccList.size()>0){
			Transaction tx = new Transaction();
			tx.setTrxAccNum(toAcc);
			tx.setTrxAmount(amt);
			tx.setId(1);
			tx.setTrxDate("25th Oct");
			tx.setTrxTypeId(1);
			tx.setTrxMerchant("Making Payment");
			tx.setTrxFee(0);
			tx.setTrxDesc("CC Payment");
			tx.setTrxStatusId(3);
			tx.setBalance(0);
			cardsList.get(0).setAmountSpent(cardsList.get(0).getAmountSpent() + amt);
			toAccList.get(0).setBalance(toAccList.get(0).getBalance() + amt);
			session.update(cardsList.get(0));
			session.update(toAccList.get(0));
			transactionService.saveAccount(tx);
			CreditCardTransc ccTransc = new CreditCardTransc();
			ccTransc.setCcId(cardsList.get(0).getCardId());
			ccTransc.setAmount(amt);
			ccTransc.setId(cardsList.get(0).getUid());
			ccTransc.setAcc_id(cardsList.get(0).getAccountId());
			ccTransc.setDate("testing");
			ccTransc.setTrxDesc("cctrx testing");
			ccTransc.setMerchant(toAcc);
			creditCardService.saveTransaction(ccTransc);
			return true;	
		}
		return false;
	}

	@Override
	public double interestGeneration(CreditCard card) {
		Session session = this.sessionFactory.getCurrentSession();
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		Date curDate = new Date();
		Date dueDate;
		double interestRate = 0;
		try {
			dueDate = format.parse(card.getDueDate());
			int numOfDays = (int)(dueDate.getTime() - curDate.getTime());
			if(numOfDays > 0) {
				interestRate = (card.getCreditLimit() - card.getAmountSpent())*0.1;		
			}
			card.setInterestAmount(interestRate);
			session.update(card);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public double latePaymentFee(CreditCard card) {
		Session session = this.sessionFactory.getCurrentSession();
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		Date curDate = new Date();
		Date dueDate;
		double latePayment = 0;
		try {
			dueDate = format.parse(card.getDueDate());
			int numOfDays = (int)(dueDate.getTime() - curDate.getTime());
			if(numOfDays > 0) {
				latePayment = (card.getCreditLimit() - card.getAmountSpent())*0.1;		
			}
			card.setLateFee(latePayment);
			session.update(card);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public double newCreditLimit(CreditCard card) {
		Session session = this.sessionFactory.getCurrentSession();
//		DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
//		Date curDate = new Date();
//		Date limitDate;
//		int creditLimit = card.getCreditLimit();
//		try {
//			limitDate = format.parse(card.getLastLimitUpdate());
//			int numOfDays = (int)(limitDate.getTime() - curDate.getTime());
//			if(numOfDays > 60) {
//				creditLimit += 300;
//			}
//			card.setCreditLimit(creditLimit);
//			session.update(card);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}		
		return 0;
	}
	
	@Override
	public void saveTransaction(CreditCardTransc tx) {
		persist(tx);
	}

	@Override
	public List<CreditCard> listCards(String user){
		Session session = this.sessionFactory.getCurrentSession();
		int id=0;
		List<User> usersList = session.createQuery("from User where ssoId = '"+user+"'").list();
		if(usersList.size()>0)
			id = usersList.get(0).getId();
		
		List<CreditCard> cardsList = session.createQuery("from CreditCard where uid="+id).list();
		for(CreditCard card : cardsList) {
			//System.out.println("Account no retrieved is "+account.getAccount_no());
		}
		return cardsList;
	}
	
	@Override	
	@Transactional
	public List<CreditCardTransc> getTransaction(String user){
		Session session = this.sessionFactory.getCurrentSession();
		List<User> usersList = session.createQuery("from User where ssoId = '"+user+"'").list();
		int id = 0;
		if(usersList.size()>0)
			id = usersList.get(0).getId();
		List<CreditCardTransc> ccTrxList = session.createQuery("from CreditCardTransc where id="+id).list();
		return ccTrxList ;
	}
}
