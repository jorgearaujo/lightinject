
package com.araujo.lightinject.di;

/**
 * Created by jorge.araujo on 1/24/2016.
 */
public class Binding {

	Class bind;
	Class to;
	Class provider;
	Scope scope = Scope.CLASS;

	public static Binding create() {
		Binding binding = new Binding();
		binding.scope = Scope.CLASS;
		return binding;
	}

	public Binding from(Class cl) {
		this.bind = cl;
		return this;
	}

	public Binding to(Class cl) {
		this.to = cl;
		return this;
	}

	public Binding provider(Class cl) {
		this.provider = cl;
		return this;
	}

	public Binding singleton() {
		this.scope = Scope.APP;
		return this;
	}

	public void add() {
		if (bind != null && (to != null || provider != null)) {
			BindingConfiguration.add(this);
		}
	}
}
