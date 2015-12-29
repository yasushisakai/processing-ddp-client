Meteor.js Processing(Java) DDP Client
=========================

Origins/Acknowledgements
------------------------
This is a fork from [Ken Yees' Meteor.js Java DDP Client](https://github.com/kenyee/java-ddp-client) which was a
fairly big fleshing out fork of [Peter Kutrumbos' 
DDP Client](https://github.com/kutrumbo/java-ddp-client).


the only Differences is:
* stopped using Junit testing and slf4j logging for the intention of simple use in processing. 
* make a familiar library file structure for processing users

Usage
-----
[TODO:introduce how to use with examples]


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
			if (resultFields.containsKey(DdpMessageField.ERROR)) {
				Map<String, Object> error = (Map<String, Object>) resultFields.get(DdpMessageField.ERROR);
				errorReason = (String) error.get("reason");
				System.err.println("Login failure: " + errorReason);
			} else {
				loggedIn = true;
			}
		}
	});


DDP Protocol Version
--------------------
This library currently supports DDP Protocol 1 (previous version supported pre1).

How to build(if you ever want to)
--------------

