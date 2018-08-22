package cn.com.dove.exception;

public abstract class LoginStatus extends Throwable {
	public abstract void mustLogin(String msg);

	public abstract void allowNotLogin(String msg);

	public abstract void mustNotLogin(String msg);

	public abstract void forBidLogin(String msg);
}