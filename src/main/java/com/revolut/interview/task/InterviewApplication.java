package com.revolut.interview.task;


import com.revolut.interview.task.route.CreateAccountRoute;
import com.revolut.interview.task.route.GetAccountRoute;
import com.revolut.interview.task.route.TransferFundsRoute;
import com.revolut.interview.task.service.AccountDataService;
import com.revolut.interview.task.service.AccountDataServiceImpl;

import spark.Spark;

public class InterviewApplication {
    
	public static void main(String[] args){
		AccountDataService accountService = new AccountDataServiceImpl();

		Spark.get("/account/:accountNum", new GetAccountRoute(accountService));
    	Spark.post("/account/:from/to/:to/amount/:amount", new TransferFundsRoute(accountService));
    	Spark.put("account/:accountNum/balance/:balance", new CreateAccountRoute(accountService));
    }
}
