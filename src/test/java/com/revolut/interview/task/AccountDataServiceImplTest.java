package com.revolut.interview.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.Test;

import com.revolut.interview.task.exception.InsufficientFundsException;
import com.revolut.interview.task.service.AccountDataService;
import com.revolut.interview.task.service.AccountDataServiceImpl;

public class AccountDataServiceImplTest {

	private static final String ACCOUNT_NUM1 = "1";
	private static final String ACCOUNT_NUM2 = "2";
	private static final String ACCOUNT_NUM3 = "3";
	private static final String ACCOUNT_NUM4 = "4";
	private static final String ACCOUNT_NUM5 = "5";
	
	private AccountDataService accountService;
	
	@Before
	public void setUp() throws Exception {
		accountService = new AccountDataServiceImpl();
		accountService.createNewAccount(ACCOUNT_NUM1, new BigDecimal(100.54));
		accountService.createNewAccount(ACCOUNT_NUM2, new BigDecimal(187.54));
		accountService.createNewAccount(ACCOUNT_NUM3, new BigDecimal(187.54));
		accountService.createNewAccount(ACCOUNT_NUM4, new BigDecimal(187.54));
		accountService.createNewAccount(ACCOUNT_NUM5, new BigDecimal(187.54));
	}
	
	@Test
	public void shouldCreateAccount() {
		assertTrue(accountService.getAccountBalance(ACCOUNT_NUM1).isPresent());
		assertTrue(accountService.getAccountBalance(ACCOUNT_NUM2).isPresent());
	}

	@Test
	public void shouldTransferFundsToAnotherAccount() throws InterruptedException {
		accountService.transferFunds(ACCOUNT_NUM1, ACCOUNT_NUM2, new BigDecimal(10.01));
		assertEquals(90.53, accountService.getAccountBalance(ACCOUNT_NUM1).get().doubleValue(), 0);
		assertEquals(197.55, accountService.getAccountBalance(ACCOUNT_NUM2).get().doubleValue(), 0);
	}

	
	@Test
	public void shouldNotLockUp() {
		ExecutorService threadPool = Executors.newFixedThreadPool(5);
		try {
			threadPool.submit(getTransferRunnable(ACCOUNT_NUM1, ACCOUNT_NUM2, 5.48));
			threadPool.submit(getTransferRunnable(ACCOUNT_NUM2, ACCOUNT_NUM1, 6.49));
			threadPool.submit(getTransferRunnable(ACCOUNT_NUM3, ACCOUNT_NUM4, 7.21));
			threadPool.submit(getTransferRunnable(ACCOUNT_NUM5, ACCOUNT_NUM3, 9.54));
			threadPool.submit(getTransferRunnable(ACCOUNT_NUM4, ACCOUNT_NUM5, 6.84));
		} finally {
			threadPool.shutdown();
		}
		Awaitility.await().atMost(20, TimeUnit.SECONDS).until(threadPool::isTerminated);
		
		BigDecimal totalAmount = accountService.getAccountBalance(ACCOUNT_NUM1).get()
				.add(accountService.getAccountBalance(ACCOUNT_NUM2).get()
						.add(accountService.getAccountBalance(ACCOUNT_NUM3).get()
								.add(accountService.getAccountBalance(ACCOUNT_NUM4).get())
									.add(accountService.getAccountBalance(ACCOUNT_NUM5).get())));
		
		assertEquals(850.7, totalAmount.doubleValue(), 0);
	}

	private Runnable getTransferRunnable(String from, String to, double amount) {
		return () -> 
			IntStream.range(0, 500).forEach(i -> {
				try {
					accountService.transferFunds(from, to, BigDecimal.valueOf(amount));
				} catch (InsufficientFundsException | InterruptedException e) {}
			});
	}
}
