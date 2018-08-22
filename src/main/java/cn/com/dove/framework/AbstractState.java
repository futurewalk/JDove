package cn.com.dove.framework;

import cn.com.dove.framework.orm.Api.Orm;
import cn.com.dove.framework.orm.SessionFactory;

public abstract class AbstractState {

	protected Orm orm(){
		return SessionFactory.newOrm();
	}
	protected void realease(){
		SessionFactory.release();
	}

	/**
	 * after register context
	 */
	public abstract void afterCreate();


	/**
	 * before create context
	 */
	public abstract void beforeCreate();
}