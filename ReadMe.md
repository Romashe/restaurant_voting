# Restaurant Voting API
System for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
  * If it is before 11:00 we assume that he changed his mind.
  * If it is after 11:00 then it is too late, vote can't be changed
* Each restaurant provides a new menu each day.

## Start
* For start app from code:
  * run `src/main/java/com/github/romashe/restvoting/RestaurantVotingApplication.java`
* Or build it and run jar:
  * `mvn package -f pom.xml` from project root
  * run `target/restaurant_voting-1.0.jar`

## Test users
- Admin: admin@mail.com
- User: user@mail.com

## Swagger URL
http://localhost/swagger-ui

## OpenAPI definition in JSON
http://localhost/api-docs
