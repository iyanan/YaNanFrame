package com.YaNan.frame.plugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.YaNan.frame.logging.Log;
@WebListener
public class PluginAppincationContextInit implements ServletContextListener {
	private final Log log = PlugsFactory.getPlugsInstance(Log.class,PluginAppincationContextInit.class);
	private List<ServletContextListener> contextListernerList;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if(contextListernerList!=null)
			for(ServletContextListener contenxtInitListener :contextListernerList){
				contenxtInitListener.contextDestroyed(arg0);
			}
		log.debug("Plugin conetxt has destory at "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.gc();//通过gc清除无用的对象
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		long t = System.currentTimeMillis();
		log.debug("");
		log.debug("       PLUGINPLUGIN     PLUG         PLUG       PLUG      PLUGINPLUGIN    PLUGINPLUGIN   PLUG         PLUG");
		log.debug("      PLUG      PLUG   PLUG         PLUG       PLUG    PLUG                  PLUG       PLUGPLUG     PLUG");
		log.debug("     PLUG      PLUG   PLUG         PLUG       PLUG   PLUG                   PLUG       PLUG PLUG    PLUG");
		log.debug("    PLUG      PLUG   PLUG         PLUG       PLUG   PLUG                   PLUG       PLUG  PLUG   PLUG");
		log.debug("   PLUGPLUGPLUGI    PLUG         PLUG       PLUG   PLUG      PLUGINP      PLUG       PLUG   PLUG  PLUG");
		log.debug("  PLUG             PLUG         PLUG       PLUG   PLUG         PLUG      PLUG       PLUG    PLUG PLUG");
		log.debug(" PLUG             PLUG         PLUG       PLUG     PLUG       PLUG      PLUG       PLUG     PLUGINPL");
		log.debug("PLUG             PLUGINPLUGI   PLUGINPLUGINP        PLUGINPLUGIN    PLUGINPLUGIN  PLUG         PLUG");
		log.debug("");
		contextListernerList = PlugsFactory.getPlugsInstanceList(ServletContextListener.class);
		log.debug("Context Init Plug number:"+contextListernerList.size());
		for(ServletContextListener contenxtInitListener :contextListernerList){
			log.debug("Plug Instance:"+contenxtInitListener);
			contenxtInitListener.contextInitialized(servletContextEvent);
		}
		log.debug("Plugin conetxt init has completed in "+(System.currentTimeMillis()-t)+"ms");
		System.gc();//通过gc清除启动时创建的对象
	}

}
