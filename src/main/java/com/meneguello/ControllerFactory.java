package com.meneguello;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.util.Callback;

class ControllerFactory implements Callback<Class<?>, Object> {
	
	private static final Logger logger = LoggerFactory.getLogger(ControllerFactory.class);
	
	@Override
	public Object call(Class<?> clazz) {
		logger.debug("Requested controller " + clazz);
		try {
			Method factoryMethod = clazz.getDeclaredMethod("getInstance", new Class[] {});
			if (Modifier.isStatic(factoryMethod.getModifiers())) {
				return factoryInstantiate(factoryMethod);
			}
			return beanInstatiate(clazz);
		} catch(NoSuchMethodException e) {
			return beanInstatiate(clazz);
		}
	}

	private Object beanInstatiate(Class<?> clazz) {
		logger.debug("Instantiating controller by bean constructor");
		try {
			return clazz.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	private Object factoryInstantiate(Method factoryMethod) {
		logger.debug("Instantiating controller by factoryMethod");
		try {
			return factoryMethod.invoke(null, new Object[] {});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}