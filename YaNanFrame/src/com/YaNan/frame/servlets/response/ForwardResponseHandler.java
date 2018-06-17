package com.YaNan.frame.servlets.response;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.YaNan.frame.plugin.annotations.Register;
import com.YaNan.frame.servlets.ServletBean;
import com.YaNan.frame.servlets.response.annotations.Forward;

@Register(attribute="com.YaNan.frame.servlets.response.annotations.Forward")
public class ForwardResponseHandler implements ResponseHandler{

	@Override
	public void render(HttpServletRequest request, HttpServletResponse response, Object handlerResult,Annotation annotation,
			ServletBean servletBean) throws ServletException, IOException {
		StringBuilder sb = new StringBuilder(handlerResult.toString());
		if(annotation!=null)
			sb.insert(0, ((Forward)annotation).prefix()).append(((Forward)annotation).suffix());
		request.getRequestDispatcher(sb.toString()).forward(request, response);
	}

}
