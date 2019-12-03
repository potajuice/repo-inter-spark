package com.revolut.interview.task.route;

import java.math.BigDecimal;

import com.revolut.interview.task.service.AccountDataService;

import spark.Request;
import spark.Response;

public class CreateAccountRoute extends AbstractRoute {
	
	private AccountDataService accountService;
	
	public CreateAccountRoute(AccountDataService accountService) {
		this.accountService = accountService;
	}

	@Override
	protected Object execute(Request request, Response response) {
		BigDecimal balance = new BigDecimal(request.params("balance"));
		
		accountService.createNewAccount(request.params("accountNum"), balance);

		return "";
	}
}
