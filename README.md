# orders-project

This multimodule project helps to manage orders. Orders contain information about customer, product, quantity and date 
of placing an order. You can simply add orders from .json, .txt files as well as from MySql database. 
Due to implemented `abstract factory` design pattern there is an easy way to implement more sources from where you can
add your orders.
Project comes up with functionality of registering users to database, security and signing in.

# Set-up

- Java 20,
- Maven,
- Docker,
- Spring,
- Spark,
- Jjwt,
- Gson,
- Simplejavamail,
- iText 7 Core,
- Junit,
- Mockito,
- Lombok,
- Hamcrest,
- Jdbi.

# Design patterns
- Abstract factory
- Observer
- Facade
- Builder
- Singleton

# How to install?

Use command below to install the package into your local repository.
```
mvn clean install
```
If you don't want to create local repository use:
```
mvn clean package -DskipTests
```

# How to run?

Run with docker:
```
docker-compose up -d --build mysql_main orders_web_app
```
Application image from dockerhub will be downloaded.

# Running unit tests

To run unit tests properly go to project destination in terminal and then execute below command:
```
docker-compose up -d --build mysql_test
```
This will download MySql image from dockerhub based on docker-compose.yml file, create test database and fill with 
values that are required for unit tests. To run unit tests type:
```
mvn test
```

# Change data source

To change data source to .json, .txt or db go to [application.properties](api/src/main/resources/application.properties) file and choose source, that you want to use.

# Sending mails

To send mails properly go to [application.properties](api/src/main/resources/application.properties) and fill the values with proper data.
Orders service contains method for sending mail to customer email with generated pdf attachment which consist all 
products bought by this customer. Due to implemented `observer` design pattern after sending mail to a customer, 
the notification about sent mail is also being sent to chosen mail.

# Generating .pdf files

To create .pdf file you have to create instance of `PdfServiceImpl` which is located in [pdf_service](service/src/main/java/com/app/service/pdf).
Then use `generatePdf` method by passing content and filename as arguments. Content is list of Strings. Pdf file will
be saved inside [resources](service/src/main/resources/pdf).

# Abstract factory

Abstract factory is based in [reader](https://github.com/pietrasFilip/orders-proj/tree/main/persistence/src/main/java/com/app/persistence/data/reader).
To create objects using abstract factory you have to fill [application.properties](api/src/main/resources/application.properties) file config. 
Choose what processor type you want to use: db, json or txt.

Serialization/deserialization data from .json files is made using `Gson` library. For reading purposes you can find 
use of gson library in [Loader](persistence/src/main/java/com/app/persistence/data/reader/loader) and for writing [JsonWriter](persistence/src/main/java/com/app/persistence/data/writer). For .txt serialization/deserialization stream
methods are used and can be found inside the same loader path as for .json format. Library responsible for working with
database is Jdbi library.

# Docker-compose 

Docker-compose file provides three containers: 
- test database, 
- main database,
- application.

# Api

Api has prepared following routings:
- ErrorRouting,
- OrdersRouting,
- ProductsRouting,
- SecurityRouting,
- UserRouting.

Endpoints of each routing are described as comment inside [routing](api/src/main/java/com/app/web/routing) directory.

Thanks to Spring framework there is `AppConfig` class which initializes all necessary objects for application
to work correctly.
`Main` class starts Spark developer server and gets necessary beans and runs them.

# Persistence

Persistence module is responsible for creating business models - in this case:
- Customer,
- Product,
- Order,
- User.

Module also contains:
- necessary mappers,
- models methods,
- repositories,
- dto,
- models for serialization and deserialization,
- abstract factory.

# Service

Service module is responsible for managing the list of orders, sending emails, generating .pdf files, managing
registration of users and generating access tokens. All methods from orders service are described in 
`OrdersServiceImpl.java` file.