/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package examples;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

/**
 * Created by tim on 08/01/15.
 */
public class CoreExamples {

  public void example1() {
    Vertx vertx = Vertx.vertx();
  }

  public void example2() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
  }

  public void example3(HttpServerRequest request) {
    request.response().putHeader("Content-Type", "text/plain").write("some text").end();
  }

  public void example4(HttpServerRequest request) {
    HttpServerResponse response = request.response();
    response.putHeader("Content-Type", "text/plain");
    response.write("some text");
    response.end();
  }

  public void example5(Vertx vertx) {
    vertx.setTimer(1000, id -> {
      // This handler will get called every second
      System.out.println("timer fired!");
    });
  }

  public void example6(HttpServer server) {
    // Respond to each http request with "Hello World"
    server.requestHandler(request -> {
      // This handler will be called every time an HTTP request is received at the server
      request.response().end("hello world!");
    });
  }

  public void example7(Vertx vertx) {
    vertx.executeBlocking(future -> {
      // Call some blocking API that takes a significant amount of time to return
      String result = someAPI.blockingMethod("hello");
      future.complete(result);
    }, res -> {
      System.out.println("The result is: " + res.result());
    });
  }

  BlockingAPI someAPI = new BlockingAPI();

  class BlockingAPI {
    String blockingMethod(String str) {
      return str;
    }
  }

}
