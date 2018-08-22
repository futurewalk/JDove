package cn.com.dove.framework;

public abstract class AbstractLoginState {

    /**
     * you will visit mapping url when you are not login
     */
    public abstract void allowNotLogin();

    /**
     * you won't visit mapping url when you are login
     */
    public abstract void forbidLogin();

    /**
     * you can visit mapping url,
     * when you must not login
     */
    public abstract void mustNotLogin();

    /**
     * you can visit mapping url when you must login
     */
    public abstract void mustLogin();
}