package com.zach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzExample {

//	private static final Log log = LogFactory.getLog(RecipeManage.class);
	private static final Logger bizDataLogger = LoggerFactory.getLogger("bizDataLogger");

	private JettyWebServer webServer;

	public static void main(String[] args) throws Exception {
		//因预发布环境服务器疑似显卡驱动问题导致无法正常生成图片
		//	设置Headless模式 -start
		/*if (BaseConstants.isProdMode()) {
			bizDataLogger.info("当前为生产环境模式");
	        System.setProperty("java.awt.headless","true");
		}*/
		//设置Headless模式 -end

		bizDataLogger.info("Server begin init");
		SpringFactory.getInstance();
		QuartzExample server = (QuartzExample) SpringFactory.getInstance().getBean("quartzExample");
		server.start();
//		SpringFactory.getInstance().startJmxHttpAdaptor();		暂不启用Jmx
		bizDataLogger.info("Server started. port=" + server.getWebServer().getPort());
	}
	
	public void start() throws Exception {
		this.webServer.start();
	}

	public JettyWebServer getWebServer() {
		return webServer;
	}

	public void setWebServer(JettyWebServer webServer) {
		this.webServer = webServer;
	}
}
