package cn.com.dove.framework.orm;

import cn.com.dove.framework.orm.Api.Orm;
import cn.com.dove.framework.orm.impl.UpdateOrmImpl;

public class Ormer extends UpdateOrmImpl implements Orm{

    public Ormer(SessionFactory.SessionHolder sessionHolder) {
        super(sessionHolder);
    }
}