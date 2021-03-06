Meteor.js Processing(Java) DDP Client
=========================

This is a [Processing](https://processing.org/) library to connect to a web application using the [Meteor framework](https://www.meteor.com/). Obviously, you will need a working meter app to connect it to your processing sketch. A Sample Meteor Web App is inside the examples repository, you can clone it from [here](https://github.com/yasushisakai/processing-ddp-client-examples) or
`git clone --recursive https://github.com/yasushisakai/processing-ddp-client.git`
to get the whole repository.

This library was forked from [Ken Yee's Java-DDP implementation](https://github.com/kenyee/java-ddp-client).

[Download latest **ddpclient.zip**](https://github.com/yasushisakai/processing-ddp-client/releases) *build instructions below*

1. Installation
-----
Two libraries are necessary prior to this one. One is [Nathan Rajlich's Java-WebSocket](https://github.com/TooTallNate/Java-WebSocket)
and another is [Google's Gson](https://github.com/google/gson). There are two ways to obtain this. More readings on installing libraries on Processing can be seen [here](https://github.com/processing/processing/wiki/How-to-Install-a-Contributed-Library).

### 1-1. Fast and Dirty
Get the jar files ([websocket](http://central.maven.org/maven2/org/java-websocket/Java-WebSocket/1.3.0/Java-WebSocket-1.3.0.jar),[gson](http://central.maven.org/maven2/com/google/code/gson/gson/2.5/gson-2.5.jar)) from each library and drag them into your PDE sketch. Processing will create a directory called "code" inside your sketch and this will make it work. However, it will only work on a single sketch.

### 1-2. Clean and Preferred??
also get each of the jar files ([websocket](http://central.maven.org/maven2/org/java-websocket/Java-WebSocket/1.3.0/Java-WebSocket-1.3.0.jar),[gson](http://central.maven.org/maven2/com/google/code/gson/gson/2.5/gson-2.5.jar)) and rename them only using letters and numbers. Save them in a directory that Processing likes to read as a library. (*your_sketchbook_directory/libraries/{{library_name}}/library/{{library_name}}.jar*)

after having those two, extract the [ddpclient.zip file in Releases](https://github.com/yasushisakai/processing-ddp-client/releases) and put it inside your sketchbook libraries directory.

2. (Example)Usage
-------
### 2-1. Server side (Meteor)
Assume we have a Mongo.Collection and a Meteor.method like this. Like mentioned above there is a [Sample App inside the examples repository](https://github.com/yasushisakai/processing-ddp-client-examples) for testing.

```javascript
DBCollection = new Mongo.Collection("data");

Meteor.methods({
	addData: function(text){
		DBCollection.insert({
			text:text,
			createdAt:new Data()
		});
	},
	deleteAllData: function(){
		DBCollection.remove();
	}
});
```
### 2-2. Client side (Processing)
A sample Processing sketch is inside the examples directory.
#### 2-2-1. Connecting to the server
You will need to import the libraries first, and declare using `try`, this initiate a connection between the app. Note that Every operation regarding communication has a slight `delay()` in order to work smoothly. This delay is set to 500ms in default, but you can change it by doing **client.setProcessing_delay(int milliseconds);**.

```java
import org.java_websocket.client.WebSocketClient;
import com.google.gson.Gson;
import ddpclient.DDPClient;

DDPClient client;

void setup(){
	// initialization connects to the server.
	client = new DDPClient(this, "localhost", 3000);
	//client = new DDPClient(this, "yourdomain.com", 80); //remote
}
```
#### 2-2-2. Calling a Meteor.method from sketch
It is similar to calling a method in the browser. If the method has no arguments, pass an empty Object. Again there is a delay for each call.

```java
client.call("addData",new Object[]{"test text"});
client.call("deleteAllData",new Object[]{});
```
#### 2-2-3. Subscribing and listening
Subscribing is possible by adding...
```java
client.subscribe("data",new Object[]{});
```
but it is convinient to have a Observer, which catches updates when something has changed in the collection.
```java
DDPClient client;
DDPObserver observer;

void setup(){

	client = new DDPClient(this,"localhost",3000);
	observer = new DDPObserver(this);
	client.addObserver(observer);

	// a empty arg = new Object[]{}
	client.subscribe("data",new Object[]{},observer);
}
```
A Sample Observer class can be found inside the examples.
I found [this node app ](https://github.com/arunoda/meteor-ddp-analyzer) useful for what is happening. The detail of the Observer/Listener pattern is discussed in the next section

3. JSON data as Map&lt;String,Object>
------
Between serverside meteor apps and the client, DDP([Distributed Data Protocol from Meteor](https://www.meteor.com/ddp)) is used for communication with websockets. This DDP is very similar to JSON. Below is the way to handle data from the server.

**Original remark from Java-ddp-client(Ken Yee):**
> The Map&lt;String,Object> data type is used extensively; this is an interface
so a ConcurrentHashMap or LinkedHashmap is used underneath.  It's a reasonable Java
analogue to Javascripts's associative arrays.  Google's GSON library is used to convert
JSON to maps and ArrayLists (used for arrays of strings or objects).  

> One important thing to note is that integer values are always represented as
Doubles in JSON so that's how they're translated by the GSON library.  If you're
sending numbers to Meteor, note that they will be sent as Doubles and what
you get back from Meteor as numbers show up as Doubles.  This isn't an issue in
Javascript because it will autoconvert objects to the needed datatype, but Java
is strongly typed, so you have to do the conversions yourself.

> Javascript's callback handling is done using Java's Observer/Listener pattern,
which is what most users are familiar with if they've used any of the JDK UI
frameworks.  When issuing a DDP command, you can attach a listener by creating one
and then overriding any methods you want to handle:

```java
ddp.call("login", params, new DDPListener() {
	@Override
	void onResult(Map<String, Object> resultFields) {
		if (resultFields.containsKey(DdpMessageField.ERROR)) {
			Map<String, Object> error = (Map<String, Object>) resultFields.get(DdpMessageField.ERROR);
			errorReason = (String) error.get("reason");
			System.err.println("Login failure: " + errorReason);
		} else {
			loggedIn = true;
		}
	}
});
```

4. Origins/Acknowledgements
------------------------
This is a fork from [Ken Yees' Meteor.js Java DDP Client](https://github.com/kenyee/java-ddp-client) which seems to be a fork of [Peter Kutrumbos'
DDP Client](https://github.com/kutrumbo/java-ddp-client).

the only Differences is:
* stopped using Junit testing and slf4j logging for the intention of simple use in processing.
* make a familiar library file structure for processing users
* has a PApplet parent like other P5 libraries

I consider this as a degeneration of Ken Yees' original library, just to simplify, and make life easier for processing users.

5. DDP Protocol Version
--------------------
Similar from the fork, this library currently supports DDP Protocol 1 (previous version supported pre1).

6. How to build
--------------
I'm using Gradle to build the target jar file. If you ever wish to do so, install Gradle and clone this repo and run `gradle jar`.

7. TODO
--------------
* make processing 3 version
