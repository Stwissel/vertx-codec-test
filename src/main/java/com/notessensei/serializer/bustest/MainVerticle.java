package com.notessensei.serializer.bustest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

  public static final String PRIMARY_ADDRESS = "notInKansasAnymore";
  public static final String SECONDARY_ADDRESS = "somewhereOverTheRainbow";

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    final int actualPort = 8888;

    final Router router = Router.router(this.getVertx());
    final HttpServer server = vertx.createHttpServer();

    this.defineRoutes(router);
    OtherVerticle theOther = new OtherVerticle();
    this.getVertx().deployVerticle(theOther)
        .compose(id -> server.requestHandler(router).listen(actualPort))
        .onSuccess(v -> {
          startPromise.complete();
          System.out.printf("HTTP server started on port %s%n", actualPort);
        })
        .onFailure(startPromise::fail);
  }

  private void defineRoutes(Router router) {

    BodyHandler bh = BodyHandler.create();
    router.route().handler(bh);
    router.route("/body").method(HttpMethod.POST).handler(this::bodyEventBus);
    router.route("/sample").method(HttpMethod.POST).handler(this::sampleEventBus);
    router.route().handler(ctx -> ctx.end("Hello from vertx"));

  }

  void bodyEventBus(final RoutingContext ctx) {
    final JsonObject j = ctx.getBodyAsJson();
    this.getVertx().eventBus().request(SECONDARY_ADDRESS, j)
        .onSuccess(msg -> ctx.response().end(((JsonObject) msg.body()).encodePrettily()))
        .onFailure(err -> ctx.response().setStatusCode(500).end(err.getMessage()));
  }

  void sampleEventBus(final RoutingContext ctx) {
    final JsonObject j = ctx.getBodyAsJson();
    SampleCustomClass scc = new SampleCustomClass();

    scc.setAge(j.getInteger("age"));
    scc.setName(j.getString("name"));
    scc.setShape(j.getString("shape"));

    this.getVertx().eventBus().request(PRIMARY_ADDRESS, scc)
        .onSuccess(msg -> {
          System.out.println(scc.toString());
          System.out.println("---");
          System.out.println(msg.body().toString());
          ctx.response().end(msg.body().toString());
        })
        .onFailure(err -> ctx.response().setStatusCode(500).end(err.getMessage()));
  }


}
