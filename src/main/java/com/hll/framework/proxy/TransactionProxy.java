package com.hll.framework.proxy;

import com.hll.framework.annotation.Transaction;
import com.hll.framework.helper.DBHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by hll on 2016/4/16.
 * 事务代理
 */
public class TransactionProxy implements Proxy {

  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

  private static final ThreadLocal<Boolean> FLAG_HOLDER=new ThreadLocal<Boolean>(){
    @Override
    protected Boolean initialValue() {
      return false;
    }
  };

  @Override
  public Object doProxy(ProxyChain proxyChain) throws Throwable {//赞
    Object result;
    Boolean flag = FLAG_HOLDER.get();
    Method method = proxyChain.getTargetMethod();
    if (!flag&&method.isAnnotationPresent(Transaction.class)){
      FLAG_HOLDER.set(true);
      try {
        DBHelper.beginTransaction();
        LOGGER.debug("begin transaction");
        result = proxyChain.doProxyChain();
        DBHelper.commitTransaction();
        LOGGER.debug("commit transaction");
      }catch (Exception e){
        DBHelper.rollbackTransaction();
        LOGGER.debug("rollback transaction");
        throw e;
      }finally {
        FLAG_HOLDER.remove();
      }
    }else {
      result=proxyChain.doProxyChain();
    }
    return result;
  }
}
