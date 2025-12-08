package org.onedata.TravSignOnService;

import io.vertx.core.Vertx;
import org.onedata.TravSignOnService.repository.UserRepository;
import org.onedata.TravSignOnService.service.UserService;

public class Main {

    public static void main(String[] args) {

        // Initialize Dependencies
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);

        Vertx vertx = Vertx.vertx();

        // assign context to Verticle and run the start method in the Verticle
        vertx.deployVerticle(new UserServiceVerticle(userService)).onComplete(res -> {
            if (res.succeeded()) {
                System.out.println("UserServiceVerticle Deployment Successful");
            } else {
                System.err.println("Deployment Failed: " + res.cause());
            }
        });
    }
}

