# Scoring Service

Intro

- Application is initialized by pulling some (5) userid and user names from github (5 users who signed up right after the username entered in resources/config.edn)
- The application also initiates 500 events for the above 5 users randomly as an "initial state".
- There is an event stream producer which puts events on a sliding buffered channel
- There is an event stream consumer which takes events from the channel, and keeps adding it to an atom to mimick a database
- Rest endpoints are exposed using http-kit/ring/compojure
- The project is Leiningen based.
- Configurations are in resources/config.edn

Usage

- Use lein repl once you have entered the project root directory from command line
- Call the start-app function at the REPL. (REPL will default to rentpath.core namespace)
- This will start all the pieces of the application.
- Query it with no parameters to get scores for all the users

```
$ curl "http://localhost:9090/scores"
[{"id":5148649,"login":"1nk0gn1t0","score":170},{"id":5148647,"login":"tillmannw","score":155},{"id":5148648,"login":"marccharlton","score":176},{"id":5148651,"login":"deadjerk","score":154},{"id":5148650,"login":"ReggaePP","score":182}]
```

- Or query it with a user id to get the score of that specific user. The possible user ids are - 5148647,5148648,5148649,5148650,5148651

```
$ curl "http://localhost:9090/scores"
[{"id":5148649,"login":"1nk0gn1t0","score":170},{"id":5148647,"login":"tillmannw","score":155},{"id":5148648,"login":"marccharlton","score":176},{"id":5148651,"login":"deadjerk","score":154},{"id":5148650,"login":"ReggaePP","score":182}]
```
