# Neo4J Overview - an IT-Tec Project

## Preface

My goal is to create a simple [Neo4J](https://neo4j.com/) setup that contains the Neo4J database system within a
[docker](https://hub.docker.com/_/neo4j/) container.  
For this, I created a simple
[docker-compose file](https://github.com/Shahondin1624/IT_Tec_Neo4J/blob/master/neo4j.yml) that defines the Neo4J server
and this simple Java-Application as 'services.'  
Neo4J exposes a simple web interface can be accessed at `http://localhost:7687` and used to execute queries against
the database.  
Additionally, the Java-App allows connecting to Neo4J and then initially runs an
['init-schema'-script](https://github.com/Shahondin1624/IT_Tec_Neo4J/blob/master/src/main/resources/creation.cypher)
and then accepts (unchecked) queries directly typed into the console. This obviously should be used with caution.

## General Information about the used technologies

### Neo4J

1. A fast Graph-Database written in Java that is well known and offers many Libraries for a multitude of programming
   languages like Java, Python and Go. [Bolt](https://en.wikipedia.org/wiki/Bolt_(network_protocol)) is used to access
   Neo4J that way.
2. It has its own Query-Language [Cypher](https://neo4j.com/docs/cypher-cheat-sheet/5/auradb-enterprise/) that visually
   represents the structure Graph-Databases are in
3. Has a visualization tool [Bloom](https://neo4j.com/docs/bloom-user-guide/current/) that can show the data represented
   as Edges and Nodes in Clusters
4. Is open source, No-SQL and [ACID](https://en.wikipedia.org/wiki/ACID) compliant
5. Developed by Neo4J Inc.

### [Graph-Databases](https://en.wikipedia.org/wiki/Graph_database)

Graph Databases use a different approach in comparison to classic relational or even document-based databases.
They are designed to handle complex relationships between data points, making them ideal for applications that require
fast traversal of connected data. In relational or document-based databases extracting those relationships and the
information describing it can become quite performance intensive, whereas graph databases are optimized for this. Use
cases where graph databases excel would, for example, be social networks where modeling the different connections
between people would be quite difficult in other database types. Another example would be maps, these are an excellent
field to use graphs for.

### [Docker](https://en.wikipedia.org/wiki/Docker_(software))

1. Uses a Container-Model-approach to offer cheap and fast virtualization
2. Containers are based on images that in turn are composed of layers stacked on top of each other
3. [Dockerfile](https://docs.docker.com/engine/reference/builder/)s are a common way to define new images. These are
   always based on other images
4. [docker-compose](https://docs.docker.com/compose/) is an easy way to define multiple containers working together in a
   shared environment that can be controlled as a single unit

## Steps in this project

### Data used in this project

The file [vbb_neo4j.csv](https://github.com/Shahondin1624/IT_Tec_Neo4J/blob/master/Project/vbb_neo4j.csv) contains all
data about the nodes.  
The file [vbb_neo4j_edge.csv](https://github.com/Shahondin1624/IT_Tec_Neo4J/blob/master/Project/vbb_neo4j_edge.csv)
contains all data about the edges.  
In general this data represents data about the [VBB](https://www.vbb.de/).  
Nodes are stations for the subway, bus and tram. Their name, district and precise location is provided.  
Edges are the connections between two stations. The type of the connection and their cost (as in cost for routing) is
also provided.

### About my simple Java-App

I use the official [Neo4J-java-driver](https://neo4j.com/docs/java-manual/current/) to connect to the Neo4J database.
The way a connection url string should look like is `bolt://<ip-address>:<port>`, additionally an authentication
token is required (this could be empty for no authentication or a username-password pair).  
For this, I used the [Fluent-Interface-Design-Pattern](https://en.wikipedia.org/wiki/Fluent_interface) to guide a
potential adaptor through the process of initializing the connection.  
Afterward, I read the
['init-schema'-script-file](https://github.com/Shahondin1624/IT_Tec_Neo4J/blob/master/src/main/resources/creation.cypher)
and read its contents, using a
[simple parser](https://github.com/Shahondin1624/IT_Tec_Neo4J/blob/master/src/main/java/queryfilehandling/QueryFileReader.java)
to turn its lines into the two queries. Then I run those against the database isolated into their own transaction.  
After that, the init-schema step is finished and the app accepts queries via the console. Additionally,
'[:STOP](https://github.com/Shahondin1624/IT_Tec_Neo4J/blob/master/src/main/java/console/ConsoleInterface.java)'
can be requested, and then the app will shut down. This is done by attempting to analyze the line typed into the console
and then determining an appropriate
[action](https://github.com/Shahondin1624/IT_Tec_Neo4J/blob/master/src/main/java/console/Action.java).
This can be easily extended to support more or more fine-grained operations.  
This application is built with [Gradle](https://gradle.org/). I use a
[Gradle-Plugin](https://plugins.gradle.org/plugin/com.bmuschko.docker-java-application) to automatically generate a
Dockerfile for the app and then bind it into my docker-compose file. I also created
an [IntelliJ-Run-Configuration](https://github.com/Shahondin1624/IT_Tec_Neo4J/blob/master/runconfigurations/Full-Build.run.xml)
that executes the following steps:

1. compile the java project/app (compileJava)
2. create/update the Dockerfile for this app (dockerCreateDockerfile)
3. build Docker image (dockerBuildImage)
4. spin up both containers with docker-compose

Alternatively (in Linux) this could be done by running  
`./gradlew dockerBuildImage && docker-compose -f neo4j.yml up -d`

### Some queries I defined

In [queries.cypher](https://github.com/Shahondin1624/IT_Tec_Neo4J/blob/master/Project/queries.cypher) I defined some
queries to show how Cypher works:

1. In this query, I ask for the direct-neighbor-count of each station. What's interesting is, while `Alias`es
   and `ORDER BY`    is the same as you would expect it from a relational database, the explicit use of `RETURN`
   is unusual. Also, the relatively unique way Cipher works is shown in a short, concise way: You define your
   starting-point, in fact so quite literally, as you start from a node (in this case `Station s`) where `s`
   allows us to access the properties of the station later on. Then from there we "go", symbolized by the `->`
   operator, to another node, defining their relationship in between the `[]` brackets. As the relationship also has
   attributes, we can access those by naming the relationship. In this example however we don't care about the
   attributes, so we don't name it `[:Type]`. Instead, we "collect" them into the variable `n` and later use the
   `COUNT()` operator to get the total number of relationships (which we remember being connections to other
   stations) for this station.
2. In the next query we extend the previous one and thus encounter a new operator `COLLECT`, which aggregates the
   data stored in n into a list. This allows us to show more detailed information than just counting the number of
   relationships.
3. Here we further specify the second query, by limiting the data we gather through the relationship. For this we use
   `*1..5` or `*n..m` where `n` is the lower limit and `m` the upper one. That means that we now allow
   the reach of the relationship to extend, or in other words when we go from Node `A` to `B` our count of the
   relationship hopping goes to 1. Then we hop from `B` to `C` and now our count is 2 and so on. This stops,
   once we hopped five times from our initial node, and thus we have gathered all stations that are reachable within the
   distance of a maximum of five hops.
4. Now we want to show the cost (which could mean distance or time to travel) for all connections between two neighbors,
   so we need to explicitly name both stations `s1: Station`, `s2: Station` and our relationship `r: Type`.
5. Here we use the `WHERE` operator, which should be familiar to users of other database-types. In Cypher, conditions
   have to be placed within `()` but can be combined using the usual `AND` or `OR` operators. As you can see the
   `COLLECT` function used before is also able to collect into tuples which we do here.
6. In this query we use `SUM` to accumulate the total cost of the whole connection-line.