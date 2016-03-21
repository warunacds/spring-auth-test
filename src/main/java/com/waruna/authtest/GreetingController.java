package com.waruna.authtest;

/**
 * Created by waruna on 2/29/16.
 */
import java.util.concurrent.atomic.AtomicLong;

import com.waruna.authtest.dao.UserDAO;
import com.waruna.authtest.dao.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name, @AuthenticationPrincipal User user) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping("/user")
    public User getUser() {

        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
        UserDAO userDAO =  (UserDAO) context.getBean("userDAO");

        User user = userDAO.getUserByUserName("roy");

        return user;

    }

//    @PermitAll
//    @RequestMapping("login/{providerId}")
//    @POST
//    public Response socialLogin(@PathParam("providerId") String providerId, OAuth2Request request) {
//        OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) connectionFactoryLocator.getConnectionFactory(providerId);
//        Connection<?> connection = connectionFactory.createConnection(new AccessGrant(request.getAccessToken()));
//        AuthenticatedUserToken token = userService.socialLogin(connection);
//        return getLoginResponse(token);
//    }
}
