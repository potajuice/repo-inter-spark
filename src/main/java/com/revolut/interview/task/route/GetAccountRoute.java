package com.revolut.interview.task.route;

import org.json.JSONObject;

import com.revolut.interview.task.exception.AccountDoesNotExistException;
import com.revolut.interview.task.service.AccountDataService;

import spark.Request;
import spark.Response;

public class GetAccountRoute extends AbstractRoute {
	
	private AccountDataService accountService;
	
	public GetAccountRoute(AccountDataService accountService) {
		this.accountService = accountService;
	}

	@Override
	protected Object execute(Request request, Response response) {
		String accountNum = request.params("accountNum");
		
		return accountService.getAccountBalance(accountNum)
				.map(balance -> new JSONObject()
						.put("accountNumber", accountNum)
						.put("balance", balance)
						.toString())
				.orElseThrow(() -> new AccountDoesNotExistException(accountNum));
	}
}
