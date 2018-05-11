package com.YaNan.frame.RTDT;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.YaNan.frame.RTDT.context.ActionDispatcher;
import com.YaNan.frame.RTDT.context.NotifyManager;
import com.YaNan.frame.RTDT.context.SocketConfigurator;
import com.YaNan.frame.RTDT.entity.RequestAction;
import com.YaNan.frame.RTDT.entity.ResponseAction;
import com.YaNan.frame.RTDT.entity.interfacer.RESPONSE_STATUS;
import com.YaNan.frame.servlets.session.Token;
import com.google.gson.Gson;

@ServerEndpoint(value = "/RTDT", configurator = SocketConfigurator.class)
public class WebSocketListener{

	protected Session session;
	protected Token token;
	protected String TUID;
	/**
	 * @param session
	 */
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		this.session = session;
		String tokenid = (String) config.getUserProperties().get("TOKENID");
		token = Token.getToken(tokenid);
		this.TUID=tokenid;
	}

	@OnClose
	public void onClose() {
		NotifyManager.getManager().unbindNotify(this.session.getId());
	}

	/**
	 * 
	 * @param message
	 * @param session
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
	try{
		RequestAction action = new Gson().fromJson(message,RequestAction.class);
		if(action.getAction()==null||action.getAction().equals("")){
			this.ActionException("action is null",action);
			return;
		}
		this.createParametersMap(action);
		this.buildContext(action);
		action.setSessionId(this.session.getId());
		ActionDispatcher dispatcher = new ActionDispatcher();
		dispatcher.doDipatcher(action,this);
	}catch(Exception e){
		e.printStackTrace();
		this.ActionException(e.getMessage());
	}
	
	}

	private void buildContext(RequestAction action) {
		action.setToken(this.token);
		action.setSessionId(this.session.getId());
	}

	private void createParametersMap(RequestAction action) {
		if(action.getData()==null)return;
		String[] strs = action.getData().split("&");
		Map<String,String> parameters = new HashMap<String,String>();
		try {
			for (String block : strs) {
				String[] lines = block.split("=");
				if (lines.length > 1) {
					parameters.put(URLDecoder.decode(lines[0], "UTF-8"),URLDecoder.decode(lines[1], "UTF-8"));
				} else {
					parameters.put(URLDecoder.decode(lines[0], "UTF-8"),null);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		action.setParameterMap(parameters);
	}

	private void ActionException(String message) {
		ResponseAction action = new ResponseAction();
		action.setStatus(RESPONSE_STATUS.PROTOCOL_EXCEPTION);
		action.setData(message);
		try {
			this.write(this.getJosn(action));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	private void ActionException(String message,RequestAction action) {
		ResponseAction response = new ResponseAction();
		response.setType(action.getType());
		response.setAUID(action.getAUID());
		response.setStatus(RESPONSE_STATUS.PROTOCOL_EXCEPTION);
		response.setData(message);
		try {
			this.write(this.getJosn(response));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	@OnError
	public void onError(Session session, Throwable throwable) {
		throwable.printStackTrace();
	}
	public String getJosn(Object response){
		return new Gson().toJson(response);
	}
	private void write(String content) throws IOException{
		this.session.getBasicRemote().sendText(content);
	}

	public void write(ResponseAction raction) {
		try {
			this.write(this.getJosn(raction));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
