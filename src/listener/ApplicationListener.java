package listener;

import service.TypeService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener {

    TypeService tsService = new TypeService();

    public ApplicationListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("typeList", tsService.GetAllType());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
