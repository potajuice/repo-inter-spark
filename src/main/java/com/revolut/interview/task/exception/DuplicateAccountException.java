package com.revolut.interview.task.exception;

public class DuplicateAccountException extends RuntimeException {

	private static final long serialVersionUID = -8291096571028041652L;

	public DuplicateAccountException(String accountNum) {
		super(String.format("Account %s already exists", accountNum));
	}
}
