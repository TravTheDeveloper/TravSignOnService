package org.onedata.TravSignOnService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.EncodeException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.onedata.TravSignOnService.exceptions.UserExistException;
import org.onedata.TravSignOnService.exceptions.UserNotFoundException;
import org.onedata.TravSignOnService.model.User;
import org.onedata.TravSignOnService.service.UserService;

import static org.onedata.TravSignOnService.Constants.EMAIL_REGEX;

public class UserServiceVerticle extends AbstractVerticle {

    private final UserService userService;

    public UserServiceVerticle(UserService userService) {
        this.userService = userService;
    }

    /**
     *
     * @param startFuture a promise which should be called when verticle start-up is complete.
     *                    Create the routers using vertx instance and map them to their respective handlers
     */

    @Override
    public void start(Promise<Void> startFuture) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.post("/users").handler(this::createUserHandler);
        router.get("/users/:id").handler(this::findUserHandler);
        router.put("/users/:id").handler(this::updateUserHandler);
        router.delete("/users/:id").handler(this::deleteUserHandler);

        vertx.createHttpServer().requestHandler(router).listen(8080);
    }


    /**
     * Handler for POST Request
     *
     * @param ctx context for handling HTTP request
     */
    private void createUserHandler(RoutingContext ctx) {

        try {
            final JsonObject userInfo = ctx.body().asJsonObject();
            if (userInfo == null || !userInfo.containsKey("name") || userInfo.getString("name").isEmpty()
                    || !userInfo.containsKey("email")) {
                ctx.response().setStatusCode(400).end("Missing required fields: Name and/or Email");
                return;
            } else if (!userInfo.getString("email").matches(EMAIL_REGEX)) {
                ctx.response().setStatusCode(400).end("Invalid email format");
                return;
            }
            User user = userService.createUser(userInfo.getString("name"),
                    userInfo.getString("email").toLowerCase());
            ctx.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end("User Created Successfully\n" + Json.encodePrettily(user));

        } catch (UserExistException | EncodeException ue) {
            ctx.response()
                    .setStatusCode(409)
                    .end(ue.getMessage());
        }
    }

    /**
     * Handler for GET Request
     *
     * @param ctx context for handling HTTP request
     */
    private void findUserHandler(RoutingContext ctx) {
        long id;
        try {
            id = Long.parseLong(ctx.pathParam("id"));
            User user = userService.readUser(id);
            ctx.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(user));
        } catch (NumberFormatException npe) {
            ctx.response()
                    .setStatusCode(400)
                    .end("Invalid parameter " + npe.getMessage());
        } catch (UserNotFoundException unfe) {
            ctx.response()
                    .setStatusCode(404)
                    .end(unfe.getMessage());
        } catch (EncodeException e) {
            ctx.response()
                    .setStatusCode(409)
                    .end(e.getMessage());
        }
    }

    /**
     * Handler for PUT Request
     *
     * @param ctx context for handling HTTP request
     */
    private void updateUserHandler(RoutingContext ctx) {
        long id;
        try {
            id = Long.parseLong(ctx.pathParam("id"));
            final JsonObject userInfo = ctx.body().asJsonObject();
            if (userInfo == null || !userInfo.containsKey("email")) {
                ctx.response().setStatusCode(400).end("Missing required field: email");
                return;
            } else if (!userInfo.getString("email").matches(EMAIL_REGEX)) {
                ctx.response().setStatusCode(400).end("Invalid email format");
                return;
            }
            User user = userService.updateUser(id, userInfo.getString("email").toLowerCase());
            ctx.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(user));

        } catch (NumberFormatException npe) {
            ctx.response()
                    .setStatusCode(400)
                    .end("Invalid parameter " + npe.getMessage());
        } catch (UserNotFoundException unfe) {
            ctx.response()
                    .setStatusCode(404)
                    .end(unfe.getMessage());
        } catch (EncodeException e) {
            ctx.response()
                    .setStatusCode(409)
                    .end(e.getMessage());
        }
    }

    /**
     * Handler for Delete Request
     *
     * @param ctx context for handling HTTP request
     */
    private void deleteUserHandler(RoutingContext ctx) {
        long id;
        try {
            id = Long.parseLong(ctx.pathParam("id"));
            userService.deleteUser(id);
            ctx.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end("User ID: " + id + " successfully deleted");

        } catch (NumberFormatException npe) {
            ctx.response()
                    .setStatusCode(400)
                    .end("Invalid parameter " + npe.getMessage());
        } catch (UserNotFoundException unfe) {
            ctx.response()
                    .setStatusCode(404)
                    .end(unfe.getMessage());
        }
    }

}
