package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理
 *
 * @author derekyi
 * @date 2020/12/5
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

	private final AdvisedSupport advised;

	public JdkDynamicAopProxy(AdvisedSupport advised) {
		this.advised = advised;
	}

	/**
	 * 返回代理对象
	 *
	 * @return
	 */
	@Override
	public Object getProxy() {
		return Proxy.newProxyInstance(getClass().getClassLoader(), advised.getTargetSource().getTargetClass(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
			//代理方法
			MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
			// 自定义方法拦截器#invoke -> AOP Alliance ReflectiveMethodInvocation#proceed
			return methodInterceptor.invoke(new ReflectiveMethodInvocation(advised.getTargetSource().getTarget(), method, args));
		}
		// 直接走原生反射调用
		return method.invoke(advised.getTargetSource().getTarget(), args);
	}
}
