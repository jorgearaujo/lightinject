package com.araujo.lightinject.di;

import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by jorge.araujo on 1/24/2016.
 */
public abstract class BindingConfiguration {

	private static List<Binding> bindings = new ArrayList<>();
	private static Map<Class, Object> instances = new HashMap<>();
	private static InjectStrategy injectStrategy = InjectStrategy.ONLY_INJECT_FIELDS;

	public abstract void configure();

	static void add(Binding binding) {
		bindings.add(binding);
	}

	protected Binding from(Class from) {
		Binding b = new Binding();
		return b.from(from);
	}

	public static <E> void inject(E object) {
		for (Field field : object.getClass().getDeclaredFields()) {
			for (Annotation annotation : field.getDeclaredAnnotations()) {
				if (annotation.annotationType().equals(Inject.class)) {
					tryToInstantiate(object, field);
				}
			}
		}
	}

	private static <E> void tryToInstantiate(E object, Field field) {
		try {
			boolean fieldIsAccesible = true;
			if (!field.isAccessible()) {
				fieldIsAccesible = false;
				field.setAccessible(true);
			}
			if (field.get(object) == null) {
				Object instance = get(field.getType());
				setObject(object, field, instance);
				Log.d("DependencyInjection", "Injected field " + field.getType() + " for the object " + object.getClass().getName());
				if (instance != null) {
					inject(instance);
				}
			}

			if (!fieldIsAccesible) {
				field.setAccessible(false);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private static <E> void setObject(E object, Field field, Object instance) throws IllegalAccessException {
		if (instance != null) {
			field.set(object, instance);
		} else {
			if (!field.getType().isInterface() && !Modifier.isAbstract(field.getType().getModifiers())) {
				try {
					instance = field.getType().newInstance();
					field.set(object, instance);
				} catch (InstantiationException e) {
				}
			}
		}
	}

	public static <E> E get(Class cl) {
		for (Binding binding : bindings) {
			if (binding.bind.equals(cl)) {
				try {
					E instance = (E)instances.get(binding.bind);
					if (instance != null) {
						return instance;
					} else {
						if (binding.provider != null) {
							instance = (E)((Provider)binding.provider.newInstance()).provide();
						} else {
							instance = (E)binding.to.newInstance();
						}
						if (binding.scope.equals(Scope.APP)) {
							instances.put(binding.bind, instance);
						}
						return instance;
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
