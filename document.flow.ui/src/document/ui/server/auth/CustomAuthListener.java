package document.ui.server.auth;

import java.util.logging.Logger;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

public class CustomAuthListener implements ApplicationListener<AbstractAuthenticationEvent> {
	
	
	private static final Logger _logger = Logger
			.getLogger(CustomAuthListener.class.getName());

	@Override
	public void onApplicationEvent(AbstractAuthenticationEvent event) {		
		
		  
		final StringBuilder builder = new StringBuilder();
        builder.append("Authentication event ");
        builder.append(event.getClass().getSimpleName());
        builder.append(": ");
        builder.append(event.getAuthentication().getName());
        builder.append("; details: ");               		
	    builder.append(event.getAuthentication().getDetails());	
		
        //event.getAuthentication().getAuthorities()
        if (event instanceof AbstractAuthenticationFailureEvent) {
            builder.append("; exception: ");
            builder.append(((AbstractAuthenticationFailureEvent) event).getException().getMessage());
        }

        _logger.info(builder.toString());

	}

}
