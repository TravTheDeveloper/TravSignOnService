package org.onedata;

import io.vertx.core.Vertx;
import org.onedata.Repository.UserRepository;
import org.onedata.Service.UserService;

public class Main {

    public static void main(String[] args) {

        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);

        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new UserServiceVerticle(userService)).onComplete(res -> {
            if(res.succeeded()) {
                System.out.println("UserServiceVerticle Deployment Complete ");
            } else {
                System.err.println("Deloyment failed: " + res.cause());
            }
        });
    }
}

