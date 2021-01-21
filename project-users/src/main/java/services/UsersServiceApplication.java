package services;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/app")
@ApplicationScoped
public class UsersServiceApplication extends Application {
}
