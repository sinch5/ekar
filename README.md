# ekar
 
run command bellow to start start mysql and spring boot app
* docker-compose up --build
Once containers are ran using postman send PUT query to create consumers and producers
http://localhost:8090/producers/0/consumers/50
To initialize counter by value make PUT request 
http://localhost:8090/counter/30

For examle we have initial value 50, If we set producer to 150 and consumers to 100, it means, that there will be more producers on 50 items,
 and when counter reach 100, 50 producers will be in wait status waiting when counter is decreased by consumer or is set to some value.

