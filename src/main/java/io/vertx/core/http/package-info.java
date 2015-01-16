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

/**
 * == Writing HTTP servers and clients
 *
 * Vert.x allows you to easily write full featured, highly performant, scalable HTTP servers.
 *
 * === Creating an HTTP Server
 *
 * The simplest way to create an HTTP server, using all default options is as follows:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example1}
 * ----
 *
 * === Configuring an HTTP server
 *
 * If you don't want the default, a server can be configured by passing in a {@link io.vertx.core.http.HttpServerOptions}
 * instance when creating it:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example2}
 * ----
 *
 * === Start the Server Listening
 *
 * To tell the server to listen for incoming requests you use one of the {@link io.vertx.core.http.HttpServer#listen}
 * alternatives.
 *
 * To tell the server to listen at the host and port as specified in the options:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example3}
 * ----
 *
 * Or to specify the host and port in the call to listen, ignoring what is configured in the options:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example4}
 * ----
 *
 * The default host is `0.0.0.0` which means 'listen on all available addresses' and the default port is `80`.
 *
 * The actual bind is asynchronous so the server might not actually be listening until some time *after* the call to
 * listen has returned.
 *
 * If you want to be notified when the server is actually listening you can provide a handler to the `listen` call.
 * For example:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example5}
 * ----
 *
 * === Getting notified of incoming requests
 *
 * To be notified when a request arrives you need to set a {@link io.vertx.core.http.HttpServer#requestHandler}:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example6}
 * ----
 *
 * === Handling requests
 *
 * When a request arrives, the request handler is called passing in an instance of {@link io.vertx.core.http.HttpServerRequest}.
 * This object represents the server side HTTP request.
 *
 * The handler is called when the headers of the request have been fully read.
 *
 * If the request contains a body, that body will arrive at the server some time after the request handler has been called.
 *
 * The server request object allows you to retrieve the {@link io.vertx.core.http.HttpServerRequest#uri},
 * {@link io.vertx.core.http.HttpServerRequest#path}, {@link io.vertx.core.http.HttpServerRequest#params} and
 * {@link io.vertx.core.http.HttpServerRequest#headers}, amongst other things.
 *
 * Each server request object is associated with one server response object. You use
 * {@link io.vertx.core.http.HttpServerRequest#response} to get a reference to the {@link io.vertx.core.http.HttpServerResponse}
 * object.
 *
 * Here's a simple example of a server handling a request and replying with "hello world" to it.
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example7_1}
 * ----
 *
 * ==== Request version
 *
 * The version of HTTP specified in the request can be retrieved with {@link io.vertx.core.http.HttpServerRequest#version}
 *
 * ==== Request method
 *
 * Use {@link io.vertx.core.http.HttpServerRequest#method} to retrieve the HTTP method of the request.
 * (i.e. whether it's GET, POST, PUT, DELETE, HEAD, OPTIONS, etc).
 *
 * ==== Request URI
 *
 * Use {@link io.vertx.core.http.HttpServerRequest#uri} to retrieve the URI of the request.
 *
 * Note that this is the actual URI as passed in the HTTP request, and it's almost always a relative URI.
 *
 * The URI is as defined in http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html[Section 5.1.2 of the HTTP specification - Request-URI]
 *
 * ==== Request path
 *
 * Use {@link io.vertx.core.http.HttpServerRequest#path} to return the path part of the URI
 *
 * For example, if the request URI was:
 *
 *  a/b/c/page.html?param1=abc&param2=xyz
 *
 * Then the path would be
 *
 *  /a/b/c/page.html
 *
 * ==== Request query
 *
 * Use {@link io.vertx.core.http.HttpServerRequest#query} to return the query part of the URI
 *
 * For example, if the request URI was:
 *
 *  a/b/c/page.html?param1=abc&param2=xyz
 *
 * Then the query would be
 *
 *  param1=abc&param2=xyz
 *
 * ==== Request headers
 *
 * Use {@link io.vertx.core.http.HttpServerRequest#headers} to return the headers of the HTTP request.
 *
 * This returns an instance of {@link io.vertx.core.MultiMap} - which is like a normal Map or Hash but allows multiple
 * values for the same key - this is because HTTP allows multiple header values with the same key.
 *
 * It also has case-insensitive keys, that means you can do the following:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example8}
 * ----
 *
 * ==== Request parameters
 *
 * Use {@link io.vertx.core.http.HttpServerRequest#params} to return the parameters of the HTTP request.
 *
 * Just like {@link io.vertx.core.http.HttpServerRequest#headers} this returns an instance of {@link io.vertx.core.MultiMap}
 * as there can be more than one parameter with the same name.
 *
 * Request parameters are sent on the request URI, after the path. For example if the URI was:
 *
 *  /page.html?param1=abc&param2=xyz
 *
 * Then the parameters would contain the following:
 *
 * ----
 * param1: 'abc'
 * param2: 'xyz
 * ----
 *
 * Note that these request parameters are retrieved from the URL of the request. If you have form attributes that
 * have been sent as part of the submission of an HTML form submitted in the body of a `multi-part/form-data` request
 * then they will not appear in the params here.
 *
 * ==== Remote address
 *
 * The address of the sender of the request can be retrieved with {@link io.vertx.core.http.HttpServerRequest#remoteAddress}.
 *
 * ==== Absolute URI
 *
 * The URI passed in an HTTP request is usually relative. If you wish to retrieve the absolute URI corresponding
 * to the request, you can get it with {@link io.vertx.core.http.HttpServerRequest#absoluteURI}
 *
 * ==== End handler
 *
 * The {@link io.vertx.core.http.HttpServerRequest#endHandler} of the request is invoked when the entire request,
 * including any body has been fully read.
 *
 * ==== Reading Data from the Request Body
 *
 * Often an HTTP request contains a body that we want to read. As previously mentioned the request handler is called
 * when just the headers of the request have arrived so the request object does not have a body at that point.
 *
 * This is because the body may be very large (e.g. a file upload) and we don't generally want to buffer the entire
 * body in memory before handing it to you, as that could cause the server to exhaust available memory.
 *
 * To receive the body, you can use the {@link io.vertx.core.http.HttpServerRequest#handler}  on the request,
 * this will get called every time a chunk of the request body arrives. Here's an example:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example9}
 * ----
 *
 * The object passed into the handler is a {@link io.vertx.core.buffer.Buffer}, and the handler can be called
 * multiple times as data arrives from the network, depending on the size of the body.
 *
 * In some cases (e.g. if the body is small) you will want to aggregate the entire body in memory, so you could do
 * the aggregation yourself as follows:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example10}
 * ----
 *
 * This is such a common case, that Vert.x provides a {@link io.vertx.core.http.HttpServerRequest#bodyHandler} to do this
 * for you. The body handler is called once when all the body has been received:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example11}
 * ----
 *
 * ==== Pumping requests
 *
 * The request object is a {@link io.vertx.core.streams.ReadStream} so you can pump the request body to any
 * {@link io.vertx.core.streams.WriteStream} instance.
 *
 * See the chapter on <<streams, streams and pumps>> for a detailed explanation.
 *
 * ==== Handling HTML forms
 *
 * HTML forms can be submitted with either a content type of `application/x-www-form-urlencoded` or `multipart/form-data`.
 *
 * For url encoded forms, the form attributes are encoded in the url, just like normal query parameters.
 *
 * For multi-part forms they are encoded in the request body, and as such are not available until the entire body
 * has been read from the wire.
 *
 * Multi-part forms can also contain file uploads.
 *
 * If you want to retrieve the attributes of a multi-part form you should tell Vert.x that you expect to receive
 * such a form *before* any of the body is read by calling {@link io.vertx.core.http.HttpServerRequest#setExpectMultipart}
 * with true, and then you should retrieve the actual attributes using {@link io.vertx.core.http.HttpServerRequest#formAttributes}
 * once the entire body has been read:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example12}
 * ----
 *
 * ==== Handling form file uploads
 *
 * Vert.x can also handle file uploads which are encoded in a multi-part request body.
 *
 * To receive file uploads you tell Vert.x to expect a multi-part form and set an
 * {@link io.vertx.core.http.HttpServerRequest#uploadHandler} on the request.
 *
 * This handler will be called once for every
 * upload that arrives on the server.
 *
 * The object passed into the handler is a {@link io.vertx.core.http.HttpServerFileUpload} instance.
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example13}
 * ----
 *
 * File uploads can be large we don't provide the entire upload in a single buffer as that might result in memory
 * exhaustion, instead, the upload data is received in chunks:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example14}
 * ----
 *
 * The upload object is a {@link io.vertx.core.streams.ReadStream} so you can pump the request body to any
 * {@link io.vertx.core.streams.WriteStream} instance. See the chapter on <<streams, streams and pumps>> for a
 * detailed explanation.
 *
 * If you just want to upload the file to disk somewhere you can use {@link io.vertx.core.http.HttpServerFileUpload#streamToFileSystem}:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example15}
 * ----
 *
 * WARNING: Make sure you check the filename in a production system to avoid malicious clients uploading files
 * to arbitrary places on your filesystem. See <<security_notes, security notes>> for more information.
 *
 * === Sending back responses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
@Document(fileName = "http.adoc")
package io.vertx.core.http;

import io.vertx.docgen.Document;

