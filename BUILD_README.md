# Building Vert.x

### Prerequisites

Maven 3.2.x

## To build a distro

The github.com/clipse/vert.x project contains the core Vert.x implementation. Building it produces a vertx-core.jar, and not a full vert.x distribution with run scripts, and services like Apex - a web development framework, or asynchronous MySql / PostgreSQL service for db operations ...

For building a complete distribution see the Vert.x stack project: https://github.com/vert-x3/vertx-stack/README.md

One thing to note is that building from master means building a SNAPSHOT version with SNAPSHOT dependencies, where it is not guaranteed that your build is reproducible. For example, if you clone https://github.com/vert-x3/vertx-stack and try to build it without first cloning and building all the dependent vert-x3 projects, those dependencies will be pulled from public Maven repository, and may not in fact be the latest bug fixed versions of those component. In case of problems during a SNAPSHOT version build it is advised to also build the SNAPSHOT version dependencies yourself. 

## Building all SNAPSHOT versions of all dependencies

### Clone all the projects

    git clone git@github.com:vert-x3/vertx-parent.git
    git clone git@github.com:vert-x3/vertx-docgen.git
    git clone git@github.com:vert-x3/vertx-dependencies.git
    git clone git@github.com:eclipse/vert.x.git
    git clone git@github.com:vert-x3/vertx-codegen.git
    git clone git@github.com:vert-x3/vertx-codetrans.git
    git clone git@github.com:vert-x3/vertx-service-factory.git
    git clone git@github.com:vert-x3/vertx-maven-service-factory.git
    git clone git@github.com:vert-x3/vertx-lang-js.git
    git clone git@github.com:vert-x3/vertx-lang-groovy.git
    
    # the next three don't build ATM
    #git clone git@github.com:vert-x3/vertx-lang-scala.git
    #git clone git@github.com:vert-x3/vertx-lang-python.git
    #git clone git@github.com:vert-x3/vertx-lang-ceylon.git
    
    git clone git@github.com:vert-x3/vertx-service-proxy.git
    
    # doesn't build
    #git clone git@github.com:vert-x3/vertx-ext-parent.git
    
    git clone git@github.com:vert-x3/vertx-metrics.git
    git clone git@github.com:vert-x3/vertx-rx.git
    git clone git@github.com:vert-x3/vertx-reactive-streams.git
    git clone git@github.com:vert-x3/vertx-hazelcast.git
    git clone git@github.com:vert-x3/vertx-apex.git
    git clone git@github.com:vert-x3/vertx-sql-common.git
    git clone git@github.com:vert-x3/vertx-mysql-postgresql-service.git
    git clone git@github.com:vert-x3/vertx-jdbc-service.git
    git clone git@github.com:vert-x3/vertx-mongo-service.git
    
    git clone git@github.com:vert-x3/vertx-stack.git
    
Don't forget vert.x core of course:
    
    git clone git@github.com:eclipse/vert.x.git


## To run tests

mvn test

## To run a specific test

mvn test -Dtest=<test_name/pattern>



