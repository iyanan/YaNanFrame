package com.YaNan.frame.servlets.session;

import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.YaNan.frame.logging.Log;
import com.YaNan.frame.plugin.PlugsFactory;
import com.YaNan.frame.plugin.annotations.Register;
import com.YaNan.frame.servlets.session.entity.TokenEntity;

@Register
public class PluginInit  implements ServletContextListener{
	private final Log log = PlugsFactory.getPlugsInstance(Log.class, TokenManager.class);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		log.debug("start init token plugin");
		TokenManager.init();
		Iterator<Entry<String, TokenEntity>> ei = TokenManager.getInstance().getTokenMap().entrySet().iterator();
		while(ei.hasNext()){
			Entry<String, TokenEntity> e = ei.next();
		log.debug("urlpattern:"+e.getKey()+" entity:"+e.getValue());	
		}
		}
}
