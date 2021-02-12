package listeners;

import engine.BCEngine;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import static constants.Constants.ENGINE_ATTRIBUTE_NAME;

@WebListener("WebApp Context Listener")
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        context.setAttribute(ENGINE_ATTRIBUTE_NAME, BCEngine.instance());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
