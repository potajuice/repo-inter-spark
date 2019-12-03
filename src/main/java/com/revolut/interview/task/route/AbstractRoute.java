package com.revolut.interview.task.route;

import org.eclipse.jetty.http.HttpStatus;

import spark.Request;
import spark.Response;
import spark.Route;

public abstract class AbstractRoute implements Route {
	
	protected abstract Object execute(Request request, Response response) throws Exception;
	
	@Override
	public Object handle(Request request, Response response) throws Exception {
		try {
			return execute(request, response);
		} catch(Exception e) {
			response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
			response.body(e.getMessage());
			return response.body();
		}
	}

}
