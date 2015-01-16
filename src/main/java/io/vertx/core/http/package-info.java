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
 * The server response object is an instance of {@link io.vertx.core.http.HttpServerResponse} and is obtained from the
 * request with {@link io.vertx.core.http.HttpServerRequest#response}.
 *
 * You use the response object to write a response back to the HTTP client.
 *
 * ==== Setting status code and message
 *
 * The default HTTP status code for a response is `200`, representing `OK`.
 *
 * Use {@link io.vertx.core.http.HttpServerResponse#setStatusCode} to set a different code.
 *
 * You can also specify a custom status message with {@link io.vertx.core.http.HttpServerResponse#setStatusMessage}.
 *
 * If you don't specify a status message, the default one corresponding to the status code will be used.
 *
 * ==== Writing HTTP responses
 *
 * To write data to an HTTP response, you use one the {@link io.vertx.core.http.HttpServerResponse#write} operations.
 *
 * These can be invoked multiple times before the response is ended. They can be invoked in a few ways:
 *
 * With a single buffer:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example16}
 * ----
 *
 * With a string. In this case the string will encoded using UTF-8 and the result written to the wire.
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example17}
 * ----
 *
 * With a string and an encoding. In this case the string will encoded using the specified encoding and the
 * result written to the wire.
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example18}
 * ----
 *
 * Writing to a response is asynchronous and always returns immediately after the write has been queued.
 *
 * If you are just writing a single string or buffer to the HTTP response you can write it and end the response in a
 * single call to the {@link io.vertx.core.http.HttpServerResponse#end(String)}
 *
 * The first call to write results in the response header being being written to the response. Consequently, if you are
 * not using HTTP chunking then you must set the `Content-Length` header before writing to the response, since it will
 * be too late otherwise. If you are using HTTP chunking you do not have to worry.
 *
 * ==== Ending HTTP responses
 *
 * Once you have finished with the HTTP response you should {@link io.vertx.core.http.HttpServerResponse#end} it.
 *
 * This can be done in several ways:
 *
 * With no arguments, the response is simply ended.
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example19}
 * ----
 *
 * It can also be called with a string or buffer in the same way `write` is called. In this case it's just the same as
 * calling write with a string or buffer followed by calling end with no arguments. For example:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example20}
 * ----
 *
 * ==== Closing the underlying connection
 *
 * You can close the underlying TCP connection with {@link io.vertx.core.http.HttpServerResponse#close}.
 *
 * Non keep-alive connections will be automatically closed by Vert.x when the response is ended.
 *
 * Keep-alive connections are not automatically closed by Vert.x by default. If you want keep-alive connections to be
 * closed after an idle time, then you configure {@link io.vertx.core.http.HttpServerOptions#setIdleTimeout}.
 *
 * ==== Setting response headers
 *
 * HTTP response headers can be added to the response by adding them directly to the
 * {@link io.vertx.core.http.HttpServerResponse#headers}:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example21}
 * ----
 *
 * Or you can use {@link io.vertx.core.http.HttpServerResponse#putHeader}
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example22}
 * ----
 *
 * Headers must all be added before any parts of the response body are written.
 *
 * ==== Chunked HTTP responses and trailers
 *
 * Vert.x supports http://en.wikipedia.org/wiki/Chunked_transfer_encoding[HTTP Chunked Transfer Encoding].
 *
 * This allows the HTTP response body to be written in chunks, and is normally used when a large response body is
 * being streamed to a client and the total size is not known in advance.
 *
 * You put the HTTP response into chunked mode as follows:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example23}
 * ----
 *
 * Default is non-chunked. When in chunked mode, each call to one of the {@link io.vertx.core.http.HttpServerResponse#write}
 * methods will result in a new HTTP chunk being written out.
 *
 * When in chunked mode you can also write HTTP response trailers to the response. These are actually written in
 * the final chunk of the response.
 *
 * To add trailers to the response, add them directly to the {@link io.vertx.core.http.HttpServerResponse#trailers}.
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example24}
 * ----
 *
 * Or use {@link io.vertx.core.http.HttpServerResponse#putTrailer}.
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example25}
 * ----
 *
 * ==== Serving files directly from disk
 *
 * If you were writing a web server, one way to serve a file from disk would be to open it as an {@link io.vertx.core.file.AsyncFile}
 * and pump it to the HTTP response.
 *
 * Or you could load it it one go using {@link io.vertx.core.file.FileSystem#readFile} and write it straight to the response.
 *
 * Alternatively, Vert.x provides a method which allows you to serve a file from disk to an HTTP response in one operation.
 * Where supported by the underlying operating system this may result in the OS directly transferring bytes from the
 * file to the socket without being copied through user-space at all.
 *
 * This is done by using {@link io.vertx.core.http.HttpServerResponse#sendFile}, and is usually more efficient for large
 * files, but may be slower for small files.
 *
 * Here's a very simple web server that serves files from the file system using sendFile:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example26}
 * ----
 *
 * Sending a file is asynchronous and may not complete until some time after the call has returned. If you want to
 * be notified when the file has been writen you can use {@link io.vertx.core.http.HttpServerResponse#sendFile(String, io.vertx.core.Handler)}
 *
 * NOTE: If you use `sendFile` while using HTTPS it will copy through user-space, since if the kernel is copying data
 * directly from disk to socket it doesn't give us an opportunity to apply any encryption.
 *
 * WARNING: If you're going to write web servers directly using Vert.x be careful that users cannot exploit the
 * path to access files outside the directory from which you want to serve them. It may be safer instead to use
 * Vert.x Apex.
 *
 * ==== Pumping responses
 *
 * The server response is a {@link io.vertx.core.streams.WriteStream} instance so you can pump to it from any
 * {@link io.vertx.core.streams.ReadStream}, e.g. {@link io.vertx.core.file.AsyncFile}, {@link io.vertx.core.net.NetSocket},
 * {@link io.vertx.core.http.WebSocket} or {@link io.vertx.core.http.HttpServerRequest}.
 *
 * Here's an example which echoes the request body back in the response for any PUT methods.
 * It uses a pump for the body, so it will work even if the HTTP request body is much larger than can fit in memory
 * at any one time:
 *
 * [source,java]
 * ----
 * {@link examples.HTTPExamples#example27}
 * ----
 *
 * === HTTP Compression
 *
 * Vert.x comes with support for HTTP Compression out of the box.
 *
 * This means you are able to automatically compress the body of the responses before they are sent back to the client.
 *
 * If the client does not support HTTP compression the responses are sent back without compressing the body.
 *
 * This allows to handle Client that support HTTP Compression and those that not support it at the same time.
 *
 * To enable compression use can configure it with {@link io.vertx.core.http.HttpServerOptions#setCompressionSupported}.
 *
 * By default compression is not enabled.
 *
 * When HTTP compression is enabled the server will check if the client incldes an `Accept-Encoding` header which
 * includes the supported compressions. Commonly used are deflate and gzip. Both are supported by Vert.x.
 *
 * If such a header is found the server will automatically compress the body of the response with one of the supported
 * compressions and send it back to the client.
 *
 * Be aware that compression may be able to reduce network traffic but is more CPU-intensive.
 *
 * === Creating an HTTP client
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

