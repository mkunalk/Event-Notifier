# Event Notification System
## Simple Project for listening to an event via http request and then adding the event in respective RabbitMQ queue and then processing the event asynchronously
### Credentials Needed
```
1: Gmail id and password -> you can get app password from google account -> security -> app password
2: Twilio account id, auth token and phone number -> can get after creating twilio account
3: Google firebase configuration file (something.json) -> register project on firebase and download the configuration file
4: Need client for firebase, you can create one or use a sample one that i have created firebase client (https://github.com/mkunalk/React-Client-for-firebase)

```
### Steps to run the application:
```
1: Clone the project
2: Put the firebase configuration file in root folder and update docker and docker compose file
3: Put all credentials in docker-compose.yml file
4: Run docker compose up
```


