package com.YaNan.Demo.action;

import com.YaNan.frame.core.servlet.annotations.Action;

public class HelloWord {
	@Action
	public String sayHello(){
		return "hello world!";
	}
}
