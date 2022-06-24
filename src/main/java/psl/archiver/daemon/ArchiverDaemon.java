package psl.archiver.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import psl.archiver.repository.FileLogRepository;
import psl.archiver.repository.FileRepository;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class ArchiverDaemon implements ServletContextListener {
	
	private final static Logger logger = LoggerFactory.getLogger(ArchiverDaemon.class);

	@Autowired
	private Archiver archiver;

	private Thread archiverThread;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("Starting daemon");
		archiverThread = new Thread(archiver::run);
		archiverThread.start();
		logger.info("Daemon started");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("Shutting down daemon");
		logger.info("Daemon shut down");
	}


}
