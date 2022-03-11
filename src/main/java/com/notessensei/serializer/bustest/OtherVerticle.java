package com.notessensei.serializer.bustest;

import java.time.Instant;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class OtherVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    final EventBus bus = this.getVertx().eventBus();
    bus.consumer(MainVerticle.PRIMARY_ADDRESS, this::sampleHandler);
    bus.consumer(MainVerticle.SECONDARY_ADDRESS, this::rawJsonHandler);
    bus.registerDefaultCodec(SampleCustomClass.class,
        new MessageCodecSerializable<SampleCustomClass>());

    startPromise.complete();
  }

  void sampleHandler(Message<SampleCustomClass> msg) {

    final SampleCustomClass scc = msg.body();
    final String name = scc.getName();
    scc.setName("ALtered: " + name);
    // Wait a second to reply
    vertx.setTimer(1000, h -> msg.reply(scc));
  }

  void rawJsonHandler(Message<JsonObject> msg) {
    final JsonObject j = msg.body();
    j.put("replyDate", Instant.now().toString());
    msg.reply(j);

  }

}
