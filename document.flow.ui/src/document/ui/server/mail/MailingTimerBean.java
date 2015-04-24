/**
 * 
 */
package document.ui.server.mail;

import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.interceptor.InvocationContext;

import mdb.core.shared.configuration.PropertyLoader;

/**
 * @author azhuk
 * Creation date: Oct 21, 2014
 *
 */

@Singleton
@Startup
public class MailingTimerBean {
	private static final Logger _logger = Logger.getLogger(MailingTimerBean.class
			.getName());
	
	private static final String PROPERTIES_FILE = "sheduling.properties";
	
	Properties _shedulProp;
	
	@Resource
    private TimerService timerService;
    
    @Timeout
    public void scheduler(Timer timer) {
    	//TODO 
        //System.out.println("MailingTimerBean: EJB Timer: Info=" + timer.getInfo());
        _logger.info("MailingTimerBean: EJB Timer: Info=" + timer.getInfo());
        _logger.info("NextTimeout: " + timer.getNextTimeout());
        
    }
    
    
	public void initProperty()  {
		_logger.info("Try load Properties file "); 
		_shedulProp = PropertyLoader.loadProperties(PROPERTIES_FILE, this.getClass());
		if ( _shedulProp != null) {  
			_logger.info("Properties file loaded");
		}
	}
	
    @PostConstruct
    public void initialize( InvocationContext ctx ) {
    	initProperty();
        ScheduleExpression se = new ScheduleExpression();
        // Set schedule to every 3 seconds (starting at second 0 of every minute).
        //se.hour("*").minute("*").second("0/10");
        _logger.info("Init MailingTimerBean");
        int h = Integer.parseInt(_shedulProp.getProperty("hour"));
        int m = Integer.parseInt(_shedulProp.getProperty("minute"));
        se.hour(h).minute(m);
        Timer timer = timerService.createCalendarTimer( se, new TimerConfig("Hi from MailingBean!", false) );
        _logger.info("NextTimeout: " + timer.getNextTimeout());
        
    }
    
    @PreDestroy
    public void stop() {    
        System.out.println("Stop all existing timers");
        for (Timer timer : timerService.getTimers()) {
            System.out.println("Stopping timer: " + timer.getInfo());
            timer.cancel();
        }
    }
	
}
