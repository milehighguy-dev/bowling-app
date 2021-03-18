Hello interviewer,

This is my bowling app. It is still a WIP... The game logic still has some kinks in it. But you should get the idea.

This app is written in Java with the Spring Boot framework. 
To run it from this source you will need Java 11 and Gradle 5 installed, 
then run `gradle bootrun` from within the project folder.

if you want to run it from the jar, first navigate to it and then run:

`java -jar bowling-app-0.0.1-SNAPSHOT.jar`

It should be using port 8080 by default

From there you can POST some JSON messages to the app

new game: 

`curl -X POST -H "Content-Type: application/json" localhost:8080/game -d @src/test/resources/newGame.json`

add roll:

`curl -X POST -H "Content-Type: application/json" localhost:8080/game -d @src/test/resources/frameUpdateBob.json`
`curl -X POST -H "Content-Type: application/json" localhost:8080/game -d @src/test/resources/frameUpdateJoe.json`

You can also check out the database at the H2 console here (no password): 

`http://localhost:8080/h2-console`