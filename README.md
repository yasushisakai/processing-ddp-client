Meteor.js Java DDP Client
=========================

Origins/Acknowledgements
------------------------
This is a fork and fairly big fleshing out of [Peter Kutrumbos' 
DDP Client](https://github.com/kutrumbo/java-ddp-client).

Differences include:

* switched to using Gradle for builds to remove duplicated Websocket 
  and Gson libraries from source code
* added JUnit testing for all the DDP messages/results and auth/collections
* returns DDP command results and removes handlers when done
* handles all the DDP message fields (switched to static strings instead of
  using extraneous class) from server
* handles all the DDP message types from the server
* websocket closed/error state changes converted to regular observer events instead
  of dumping errors to System.out

Usage
-----
The best thing to do is to look at the JUnit tests.  The tests are separated 
into authentication tests and collection tests.  

The TestDDPClientObserver in the JUnit tests is the core handler of DDP message 
results and is a simple example of holding enough state to implement a simple 
Meteor client.  Note that in a real application, you'll probably want to use an 
eventbus to implement the DDP message handling.

Note that you may want to use a local SQLite DB to store the data instead of using 
Maps if you are memory constrained and/or if you need to do any sorting.  Otherwise,
you'll have to have separate SortedMap collection for each of your sorts.

If you're planning to use this with Android, look at the 
[Android DDP Library](https://github.com/kenyee/android-ddp-client)
which builds on top of this library
to make it easier to work with an Android application.

Design
------
The Map&lt;String,Object> data type is used extensively; this is an interface 
so a ConcurrentHashMap or LinkedHashmap is used underneath.  It's a reasonable Java 
analogue to Javascripts's associative arrays.  Google's GSON library is used to convert 
JSON to maps and ArrayLists (used for arrays of strings or objects).  

One important thing to note is that integer values are always represented as 
Doubles in JSON so that's how they're translated by the GSON library.  If you're 
sending numbers to Meteor, note that they will be sent as Doubles and what 
you get back from Meteor as numbers show up as Doubles.  This isn't an issue in
Javascript because it will autoconvert objects to the needed datatype, but Java
is strongly typed, so you have to do the conversions yourself.

Javascript's callback handling is done using Java's Observer/Listener pattern,
which is what most users are familiar with if they've used any of the JDK UI
frameworks.  When issuing a DDP command, you can attach a listener by creating one
and then overriding any methods you want to handle:

	ddp.call("login", params, new DDPListener() {
		@Override
		void onResult(Map<String, Object> resultFields) {
			if (jsonFields.containsKey("error")) {
				Map<String, Object> error = (Map<String, Object>) jsonFields.get(DdpMessageField.ERROR);
				errorReason = (String) error.get("reason");
				System.err.println("Login failure: " + errorReason);
			} else {
				loggedIn = true;
			}
		}
	});


DDP Protocol Version
--------------------
This library currently supports DDP Protocol 0.5.7 (works with Meteor 0.6.4.1 though).

Maven Artifact
--------------
This library is in the Maven Central Library hosted by Sonatype.
In Gradle, you can reference it with this in your dependencies:

    compile group: 'com.keysolutions', name: 'java-ddp-client', version: '0.5.7.+'

And in Maven, you can reference it with this:

    <dependency>
      <groupId>com.keysolutions</groupId>
      <artifactId>java-ddp-client</artifactId>
      <version>0.5.7.1</version>
      <type>pom</type>
    </dependency>

The version of the library will match the Meteor.js DDP protocol version with the 
library revision in the last digit (0.5.7.1, 0.5.7.2, etc.)

To-Do
-----
* Add SRP and OAuth login support.
* Add "create new user" test.
* Test all possible EJSON data types.
* Handle insertBefore and insertAfter collection update messages (may be 
difficult because LinkedHashMap can only append) when Meteor adds them.
