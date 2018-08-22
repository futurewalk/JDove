package cn.com.dove.framework;

public abstract class Router {

	protected void confRouter(){

	}

	protected abstract <T> void initRouter(String url,Class<T> clazz,String method);
}