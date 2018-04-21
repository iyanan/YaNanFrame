package com.YaNan.Demo.action;

import java.io.IOException;

import com.YaNan.frame.servlets.annotations.Action;
import com.YaNan.frame.servlets.annotations.RESPONSE_METHOD;
import com.YaNan.frame.servlets.annotations.Validate;
import com.YaNan.frame.servlets.servletSupport.DefaultServlet;
/**
 * 如果需要获取  HttpServletRequest HttpServletRespone等，请继承自各对应Servlet扩展类
 * 对应对象的命名  HttpServletRequest RequestContext，HttpServletResponse ResponseContext ，oken tokenContext
 * 如果只需要get方式请求  无需继承任何扩展类
 * 各扩展类型说明(下面的扩展类依次继承关系）
 * DefaultServlet 默认Servlet扩展，支持get方式传参，支持获取HttpServlet对象
 * MultiFormServlet 表单Servlet扩展 继承自DefalutServlet 支持post请求 
 * TokenServlet tokenServlet扩展，继承自MultiFormServlet 支持获取Token，以及支持@TokenObject注解 依赖组件  com.YaNan.frame.session
 * @author yanan
 */
public class ActionAnnotationsTest extends DefaultServlet{
	@Validate(RegExpression="[\\S]{2,}",Failed="请输入字少两个字符"/*,isNull="请输入中文  两个字符以上"*/)
	private String name;
	// 以下两个接口测试  action中method（返回内容的方式）
	@Action(method=RESPONSE_METHOD.FORWARD)
	public String testForward(){
		return "index.html";
	}
	@Action(method=RESPONSE_METHOD.REDIRCET)
	public String testRedirect(){
		return "index.html";
	}
	//以下  命名空间的使用 访问路径 项目路径/test/testNamespace.do
	// * 以下  命名空间的使用
	@Action(namespace="test")
	public String testNamespace(){
		return "你得到了内容";
	}
	// 以下   output属性的使用，某些情况下  我们可能不是返回字符类型数据
	@Action(output=true)
	public void testOutput(){
		try {
			this.ResponseContext.getWriter().write("你得到了你想要的内容".toCharArray());
			this.ResponseContext.getWriter().flush();
			this.ResponseContext.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//解析返回结果  其中 ${}中为该对象拥有的属性的值，没有则返回原内容
	@Action(decode=true)
	public String testDecode(){
		this.setName("解码内容");
		return "解码结果：${name}";
	}
	//跨域支持
	@Action(CorssOrgin=true)
	public String testCO(){
		return "跨域显示内容";
	}
	// args参数  为一个数组,需要验证的参数，必须配合使用注解@Validate。但@Validate可以独立使用，独立使用时仅在请求中含有该参数时有效
	@Action(args={"name"})
	public String testArgs(){
		return this.name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}