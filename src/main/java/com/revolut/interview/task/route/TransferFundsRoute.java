package com.revolut.interview.task.route;

import java.math.BigDecimal;

import com.revolut.interview.task.service.AccountDataService;

import spark.Request;
import spark.Response;

public class TransferFundsRoute extends AbstractRoute {
	
	private AccountDataService accountService;
	
	public TransferFundsRoute(AccountDataService accountService) {
		this.accountService = accountService;
	}

	@Override
	protected Object execute(Request request, Response response) throws Exception {
		BigDecimal amount = new BigDecimal(request.params("amount"));
		
		accountService.transferFunds(request.params("from"), request.params("to"), amount);
		
		return "";
	}
}
