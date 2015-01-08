/*
 * Copyright (c) 2011-2014 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

/**
 * = Vert.x Core Manual
 * :toc: right
 *
 * At the heart of Vert.x is a set of Java APIs that we call *Vert.x Core*
 *
 * https://github.com/eclipse/vert.x[Repository].
 *
 * Vert.x core provides functionality for things like:
 *
 * * Writing TCP clients and servers
 * * Writing HTTP clients and servers including support for WebSockets
 * * The Event bus
 * * Shared data - local maps and clustered distributed maps
 * * Periodic and delayed actions
 * * Deploying and undeploying Verticles
 * * Datagram Sockets
 * * DNS client
 * * File system access
 * * High availability
 * * Clustering
 *
 * The functionality in core is fairly low level - you won't find stuff like database access, authorisation or high level
 * web functionality here - that kind of stuff you'll find in *Vert.x ext* (extensions).
 *
 * Vert.x core is small and lightweight. You just use the parts you want. It's also entirely embeddable in your
 * existing applications - we don't force you to structure your applications in a special way just so you can use
 * Vert.x.
 *
 * You can use core from any of the other languages that Vert.x supports. But here'a a cool bit - we don't force
 * you to use the Java API directly from, say, JavaScript or Ruby - after all, different languages have different conventions
 * and idioms, and it would be odd to force Java idioms on Ruby developers (for example).
 * Instead, we automatically generate an *idiomatic* equivalent of the core Java APIs for each language.
 *
 * From now on we'll just use the word *core* to refer to Vert.x core.
 *
 * Let's discuss the different concepts and features in core.
 *
 * == In the beginning there was Vert.x
 *
 * NOTE: Much of this is Java specific - need someway of swapping in language specific parts
 *
 * You can't do much in Vert.x-land unless you can commune with a {@link io.vertx.core.Vertx} object!
 *
 * It's the control centre of Vert.x and is how you do pretty much everything, including creating clients and servers,
 * getting a reference to the event bus, setting timers, as well as many other things.
 *
 * So how do you get an instance?
 *
 * If you're embedding Vert.x then you simply create an instance as follows:
 *
 *  Vertx vertx = Vertx.vertx();
 *
 * If you're using Verticles
 *
 * NOTE: Most applications will only need a single Vert.x instance, but it's possible to create multiple Vert.x instances if you
 * require, for example, isolation between the event bus or different groups of servers and clients.
 *
 * === Specifying options when creating a Vertx object
 *
 * When creating a Vertx object you can also specify options if the defaults aren't right for you:
 *
 *  Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
 *
 * The {@link io.vertx.core.VertxOptions} object has many settings and allows you to configure things like clustering,
 * high availability, pool sizes and various other settings. The Javadoc describes all the settings in detail.
 *
 * === Creating a clustered Vert.x object
 *
 * If you're creating a *clustered Vert.x* (See the section on the <<event_bus, event bus>> for more information
 * on clustering the event bus), then you will normally use the asynchronous variant to create the Vertx object.
 *
 * This is because it usually takes some time (maybe a few seconds) for the different Vert.x instances in a cluster to
 * group together. During that time, we don't want to block the calling thread, so we give the result to you asynchronously.
 *
 * == Are you fluent?
 *
 * You may have noticed that in the previous examples a *fluent* API was used.
 *
 * A fluent API is where multiple methods calls can be chained together. For example:
 *
 *  new VertxOptions().setWorkerPoolSize(40).setEventLoopPoolSize(4).setClustered(true);
 *
 * This is a common pattern throughout Vert.x APIs, so get used to it.
 *
 * Chaining calls like this allows you to write code that's a little bit less verbose. Of course, if you don't
 * like the fluent approach *we don't force you* to do it that way, you can happily ignore it if you prefer and write
 * your code like this:
 *
 * ----
 * VertxOptions options = new VertxOptions();
 * options.setWorkerPoolSize(40);
 * options.setEventLoopPoolSize(4);
 * options.setClustered(true);
 * ----
 *
 * == Don't call us, we'll call you.
 *
 * The Vert.x APIs are largely _event driven_. This means that when things happen in Vert.x that you are interested in,
 * Vert.x will call you by sending you events.
 *
 * Some example events are:
 *
 * * a timer has fired
 * * some data has arrived on a socket,
 * * some data has been read from disk
 * * an exception has occurred
 * * an HTTP server has received a request
 *
 * You handle events by providing _handlers_ to the Vert.x APIs. For example to receive a timer event every second you
 * would do:
 *
 *  vertx.setTimer(1000, id -> System.out.println("This will be called every second"));
 *
 * Or to receive an HTTP request:
 *
 *  // Respond to each http request with "Hello World"
 *  server.requestHandler(request -> request.response().end("Hello World"));
 *
 * Some time later when Vert.x has an event to pass to your handler Vert.x will call it *asynchronously*.
 *
 * This leads us to some important concepts in Vert.x:
 *
 * == Don't block me!
 *
 * With very few exceptions (i.e. some file system operations ending in 'Sync'), none of the APIs in Vert.x block the calling thread.
 *
 * If a result can be provided immediately, it will be returned immediately, otherwise you will usually provide a handler
 * to receive events some time later.
 *
 * Because none of the Vert.x APIs block threads that means you can use Vert.x to handle a lot of concurrency using
 * just a small number of threads.
 *
 * With a conventional blocking API the calling thread might block when:
 *
 * * Reading data from a socket
 * * Writing data to disk
 * * Sending a message to a recipient and waiting for a reply.
 * * ... Many other situations
 *
 * In all the above cases, when your thread is waiting for a result it can't do anything else - it's effectively useless.
 *
 * This means that if you want a lot of concurrency using blocking APIs then you need a lot of threads to prevent your
 * application grinding to a halt.
 *
 * Threads have overhead in terms of the memory they require (e.g. for their stack) and in context switching.
 *
 * For the levels of concurrency required in many modern applications, a blocking approach just doesn't scale.
 *
 * == Reactor and Multi-Reactor
 *
 * We mentioned before that Vert.x APIs are event driven - Vert.x passes events to handlers when they are available.
 *
 * In most cases Vert.x calls your handlers using a thread called an *event loop*.
 *
 * As nothing in Vert.x or your application blocks, the event loop can merrily run around delivering events to different handlers in succession
 * as they arrive.
 *
 * Because nothing blocks, an event loop can potentially deliver huge amounts of events in a short amount of time.
 * For example a single event loop can handle many thousands of HTTP requests very quickly.
 *
 * We call this the http://en.wikipedia.org/wiki/Reactor_pattern[Reactor Pattern].
 *
 * You may have heard of this before - for example Node.js implements this pattern.
 *
 * In a standard reactor implementation there is a *single event loop* thread which runs around in a loop delivering all
 * events to all handlers as they arrive.
 *
 * The trouble with a single thread is it can only run on a single core at any one time, so if you want your single threaded
 * reactor application (e.g. your Node.js application) to scale over your multi-core server you have to start up and
 * manage many different processes.
 *
 * Vert.x works differently here. Instead of a single event loop, each Vertx instance maintains *several event loops*.
 * By default we choose the number based on the number of available cores on the machine, but this can be overridden.
 *
 * This means a single Vertx process can scale across your server, unlike Node.js.
 *
 * We call this pattern the *Multi-Reactor Pattern* to distinguish it from the single threaded reactor pattern.
 *
 * NOTE: Even though a Vertx instance maintains multiple event loops, any particular handler will never be executed
 * concurrently, and in most cases (with the exception of <<worker_verticles, worker verticles>>) will always be called
 * using the *exact same event loop*.
 *
 * == The Golden Rule - *Don't Block an Event Loop*
 *
 * We already know that the Vert.x APIs are non blocking and won't block the event loop, but that's not much help if
 * you block the event loop *yourself* in a handler.
 *
 * If you do that, then that event loop will not be able to do anything else while it's blocked. If you block all of the
 * event loops in Vertx instance then your application will grind to a complete halt!
 *
 * So don't do it! *You have been warned*.
 *
 * Examples of blocking include:
 *
 * * +Thread.sleep()+
 * * Waiting on a lock
 * * Waiting on a mutex or monitor (e.g. synchronized section)
 * * Doing a long lived database operation and waiting for a result
 * * Doing a complex calculation that takes some significant time.
 * * Spinning in a loop
 *
 * If any of the above stop the event loop from doing anything else for a *significant amount of time* then you should
 * go immediately to the naughty step, and await further instructions.
 *
 * So... what is a *significant amount of time*?
 *
 * How long is a piece of string? It really depends on your application and the amount of concurrency you require.
 *
 * If you have a single event loop, and you want to handle 10000 http requests per second, then it's clear that each request
 * can't take more than 0.1 ms to process, so you can't block for any more time than that.
 *
 * *The maths is not hard and shall be left as an exercise for the reader.*
 *
 * If your application is not responsive it might be a sign that you are blocking an event loop somewhere. To help
 * you diagnose such issues, Vert.x will automatically log warnings if it detects an event loop hasn't returned for
 * some time. If you see warnings like these in your logs, then you should investigate.
 *
 *  Thread vertx-eventloop-thread-3 has been blocked for 20458 ms
 *
 * Vert.x will also provide stack traces to pinpoint exactly where the blocking is occurring.
 *
 * If you want to turn of these warnings or change the settings, you can do that in the
 * {@link io.vertx.core.VertxOptions} object before creating the Vertx object.
 *
 * == Running blocking code
 *
 * In a perfect world, there will be no war or hunger, all APIs will be written asynchronously and bunny rabbits will
 * skip hand-in-hand with baby lambs across sunny green meadows.
 *
 * *But.. the real world is not like that. (Have you watched the news lately?)*
 *
 * Fact is, many, if not most libraries, especially in the JVM ecosystem have synchronous APIs and many of the methods are
 * likely to block. A good example is the JDBC API - it's inherently asynchronous, and no matter how hard it tries, Vert.x
 * cannot sprinkle magic pixie dust on it to make it asynchronous.
 *
 * We're not going to rewrite everything to be asynchronous overnight so we need to provide you a way to use "traditional"
 * blocking APIs safely within a Vert.x application.
 *
 * As discussed before, you can't call blocking operations directly from an event loop, as that would prevent it
 * from doing any other useful work. So how can you do this?
 *
 * It's done by calling {@link io.vertx.core.Vertx#executeBlocking} specifying both the blocking code to execute and a
 * result handler to be called back asynchronous when the blocking code has been executed.
 *
 * ----
 * vertx.executeBlocking(future -> {
 *   // Call some blocking API that takes a significant amount of time to return
 *   String result = someAPI.blockingMethod("hello");
 *   future.setResult(result);
 * }, res -> {
 *   System.out.println("The result is: " + res.result());
 * });
 * ----
 *
 * An alternative way to run blocking code is to use a <<worker_verticles, worker verticle>>
 *
 * == Verticles
 *
 * Vert.x can be used as a library by instantiating Vertx instances and using the core APIs to create servers, clients,
 * use the event bus and many other things.
 *
 * This is often the best route if you're embedding Vert.x in an existing
 * application that already has its own threading or deployment model. Or maybe you'd just prefer to handle all
 * that stuff yourself in your application for your own good reasons.
 *
 * However, Vert.x also comes with simple _actor-like_ deployment and concurrency model that you can use to structure your
 * application if you wish.
 *
 * *This model is entirely optional and Vert.x does not force you to create your applications in this way if you don't
 * want to*.
 *
 * NOTE: This model does not claim to be a strict actor-model implementation, but it does share similarities especially
 * with respect to concurrency, scaling and deployment.
 *
 * To use this model, you write your code as set of *verticles*.
 *
 * You can think of a verticle as a bit like an actor in the http://en.wikipedia.org/wiki/Actor_model[Actor Model].
 * A real application would typically be composed of many verticle instances communicating with each other by sending messages
 * over the <<event_bus, Event Bus>>.
 *
 * WARNING: Java specific
 *
 * Verticles can be written in any of the languages that Vert.x supports. Here's an example in Java:
 *
 * ----
 * public class MyVerticle extends AbstractVerticle {
 *
 *   public void start() {
 *     System.out.println("Hello World");
 *   }
 * }
 * ----
 *
 * The verticle must have a +start+ method, and can optionally have a +stop+ method. The +start+ method is called when the
 * verticle is deployed, and the +stop+ method is called (if it exists) when the verticle is undeployed.
 *
 * === Deploying verticles programmatically
 *
 * You can deploy a verticle using one of the {@link io.vertx.core.Vertx#deployVerticle} methods, specifying the verticle
 * name, or you can pass in a verticle instance.
 *
 * === Deploying using a Verticle instance
 *
 * === Deploying specifying a Verticle name
 *
 * === Deploying verticles at the command line
 *
 * === Verticle asynchronous start
 *
 * === Undeploying verticles
 *
 * === Verticle threading and concurrency
 *
 * Standard verticles are assigned an event loop thread when they are created and the +start+ method is called with that
 * event loop. When you call any other methods that takes a handler on a core API from an event loop then Vert.x
 * will guarantee that those handlers, when called, will be executed on the same event loop.
 *
 * This means we can guarantee that all the code in your verticle instance is always executed on the same event loop (as
 * long as you don't create your own threads and call it!).
 *
 * This means you can write all the code in your application as single threaded and let Vert.x worrying about the threading
 * and scaling. No more worrying about +synchronized+ and +volatile+ any more, and you also avoid many other cases of race conditions
 * and deadlock so prevalent when doing hand-rolled 'traditional' multi-threaded application development.
 *
 * Here's an example Java verticle showing how different handlers will be called on the same event loop:
 *
 * ----
 * public class MyVerticle extends AbstractVerticle {
 *
 *   public void start() {
 *
 *     // Start called on event loop here
 *
 *     // Start a timer
 *     vertx.setTimer(1000, id -> {
 *       // This handler will also be called on same event loop
 *
 *       // Send a message to another verticle
 *       vertx.eventBus().send("foo", "hello");
 *     });
 *
 *     // Create an HTTP server
 *     vertx.createHttpServer(new HttpServerOptions().setPort(8080).requestHandler(request -> {
 *       // This handler will also called on same event loop
 *     }).listen();
 *
 *   }
 * }
 * ----
 *
 * === Passing configuration to a verticle
 *
 * You can pass configuration
 *
 * === Accessing environment variables in a Verticle
 *
 * === Causing Vert.x to exit
 *
 * === The Context object
 *
 * === Specifying number of verticle instances
 *
 * [[worker_verticles]]
 * === Worker verticles
 *
 * ==== Multi-threaded worker verticles
 *
 * === Verticle Isolation Groups
 *
 * === High Availability
 *
 * Verticles can be deployed with High Availability (HA) enabled.
 *
 * === Verticle factories
 *
 * Configuring programmatically and via classpath.
 *
 * How are verticle identifiers used to find factories?
 *
 * [[event_bus]]
 * == The Event Bus
 *
 * == TCP Clients and Servers
 *
 * == HTTP Clients and Servers
 *
 * == Shared Data
 *
 * === Local Shared Data
 *
 * === Clustered Distributed Maps
 *
 * == Buffers
 *
 * == JSON
 *
 * == Timers - one shot and periodic
 *
 * == UDP
 *
 * == File System
 *
 * == DNS
 *
 *
 *
 *
 *
 * == Thread safety
 *
 * Notes on thread safety of Vert.x objects
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
@Document(fileName = "index.adoc")
@io.vertx.codegen.annotations.GenModule(name = "vertx")
package io.vertx.core;

import io.vertx.docgen.Document;
