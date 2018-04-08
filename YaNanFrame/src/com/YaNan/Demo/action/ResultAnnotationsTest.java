package com.YaNan.Demo.action;

import com.YaNan.frame.core.servlet.annotations.Action;
import com.YaNan.frame.core.servlet.annotations.ActionResults.Result;
import com.YaNan.frame.core.servlet.annotations.RESPONSE_METHOD;

/**
 * TokenServlet简单案例
 * @author yanan
 *
 */
public class ResultAnnotationsTest{
	private int result;
	@Result(name = "success", value = "index.html",method=RESPONSE_METHOD.FORWARD)
	@Result(name = "failed", value = "index.html",method=RESPONSE_METHOD.REDIRCET)
	@Action
	public String getResult(){
		if(result<10)
			return "你输入的数字是"+this.result;
		if(result>1000)
			return "failed";
		return "success";
	}
	public void setResult(int result) {
		this.result = result;
	}
}