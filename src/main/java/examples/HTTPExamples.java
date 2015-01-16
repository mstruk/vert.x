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

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.net.NetSocket;

/**
 * Created by tim on 09/01/15.
 */
public class HTTPExamples {

  public void example1(Vertx vertx) {

    HttpServer server = vertx.createHttpServer();
  }

  public void example2(Vertx vertx) {

    HttpServerOptions options = new HttpServerOptions().setMaxWebsocketFrameSize(1000000);

    HttpServer server = vertx.createHttpServer(options);
  }

  public void example3(Vertx vertx) {

    HttpServer server = vertx.createHttpServer();
    server.listen();
  }

  public void example4(Vertx vertx) {

    HttpServer server = vertx.createHttpServer();
    server.listen(8080, "myhost.com");
  }

  public void example5(Vertx vertx) {

    HttpServer server = vertx.createHttpServer();
    server.listen(8080, "myhost.com", res -> {
      if (res.succeeded()) {
        System.out.println("Server is now listening!");
      } else {
        System.out.println("Failed to bind!");
      }
    });
  }

  public void example6(Vertx vertx) {

    HttpServer server = vertx.createHttpServer();
    server.requestHandler(request -> {
      // Handle the request in here
    });
  }

  public void example7(Vertx vertx) {

    HttpServer server = vertx.createHttpServer();
    server.requestHandler(request -> {
      // Handle the request in here
      HttpMethod method = request.method();
    });
  }

  public void example7_1(Vertx vertx) {

    vertx.createHttpServer().requestHandler(request -> {
      request.response().end("Hello world");
    }).listen(8080);

  }



  public void example8(HttpServerRequest request) {

    MultiMap headers = request.headers();

    // Get the User-Agent:
    System.out.println("User agent is " + headers.get("user-agent"));

    // You can also do this and get the same result:
    System.out.println("User agent is " + headers.get("User-Agent"));
  }

  public void example9(HttpServerRequest request) {

   request.handler(buffer -> {
     System.out.println("I have received a chunk of the body of length " + buffer.length());
   });
  }

  public void example10(HttpServerRequest request) {

    // Create an empty buffer
    Buffer totalBuffer = Buffer.buffer();

    request.handler(buffer -> {
      System.out.println("I have received a chunk of the body of length " + buffer.length());
      totalBuffer.appendBuffer(buffer);
    });

    request.endHandler(v -> {
      System.out.println("Full body received, length = " + totalBuffer.length());
    });
  }

  public void example11(HttpServerRequest request) {

    request.bodyHandler(totalBuffer -> {
      System.out.println("Full body received, length = " + totalBuffer.length());
    });
  }

  public void example12(HttpServer server) {

    server.requestHandler(request -> {
      request.setExpectMultipart(true);
      request.endHandler(v -> {
        // The body has now been fully read, so retrieve the form attributes
        MultiMap formAttributes = request.formAttributes();
      });
    });
  }

  public void example13(HttpServer server) {

    server.requestHandler(request -> {
      request.setExpectMultipart(true);
      request.uploadHandler(upload -> {
        System.out.println("Got a file upload " +  upload.name());
      });
    });
  }

  public void example14(HttpServerRequest request) {

    request.uploadHandler(upload -> {
      upload.handler(chunk -> {
        System.out.println("Received a chunk of the upload of length " + chunk.length());
      });
    });
  }

  public void example15(HttpServerRequest request) {

    request.uploadHandler(upload -> {
      upload.streamToFileSystem("myuploads_directory/" + upload.filename());
    });
  }

}
