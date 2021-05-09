# Scoring Service

## Intro

- Application is initialized by pulling some (5) userids and user names from github (5 users who signed up right after the username entered in resources/config.edn)
- The application also initiates 500 events for the above 5 users randomly as an *initial state*.
- There is an event stream producer which puts events on a sliding buffered channel.
- There is an event stream consumer which takes events from the channel, and keeps adding it to an atom to mimick a database.
- Rest endpoints are exposed using http-kit/ring/compojure.
- Application runs on port 9090, and can be altered from the configuration file in resources/config.edn.
- The unit tests can be run using lein test

## Usage

- Open a REPL using ```lein repl``` once you have entered the project root directory from command line
- Call the ```start-app``` function at the REPL. (REPL will default to rentpath.core namespace)
- This will start all the pieces of the application.
- Calling ```stop-app``` function will stop the event producer and http-kit.
- Query it with no parameters to get scores for all the users

```
$ curl "http://localhost:9090/scores"
[{"id":5148649,"login":"1nk0gn1t0","score":170},{"id":5148647,"login":"tillmannw","score":155},{"id":5148648,"login":"marccharlton","score":176},{"id":5148651,"login":"deadjerk","score":154},{"id":5148650,"login":"ReggaePP","score":182}]
```

- Or query it with a user id to get the score of that specific user. The possible user ids are - 5148647, 5148648, 5148649, 5148650, 5148651

```
$ curl "http://localhost:9090/scores?id=5148650"
[{"id":5148650,"login":"ReggaePP","score":151}]
```

### PS
- The scores are aggregated at *query time*. I felt like that is what the requirements are asking for. It could be tweaked and scores could be aggregated by *event-consumer* on every event that it reads from channel, and that could lead to better performance when the routes are queried. 