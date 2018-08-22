package cn.com.dove.framework;


/**
 * listen request state,
 * you can use the class to write log ,
 * validate use login and so on
 */
public abstract class AbstractRequestListener {

	/**
	 * JDove will invoke this method while request before
	 * @throws Exception
	 */
	public abstract void onRequestBefore();

	/**
	 * Jdove will invoke this abstract method while request error
	 * @throws Exception
	 */
	public abstract void onRequestError();

	/**
	 * Jdove will invoke the abstract method while request finish
	 * @throws Exception
	 */
	public abstract void onRequestFinish();

}