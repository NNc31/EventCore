# EventCore
Web application for managing public events

## Technologies
- Frontend: Angular 16
- Backend: Java 17, Spring Boot (microservices)
- Build: Docker, Maven
- Database: PostgreSQL
- API Gateway: Spring Cloud Gateway

## Installation and launch
### 1. Installation
1. Clone repo
   `git clone https://github.com/NNc31/EventCore.git`
2. Create .env and .env.dev files (example in [Configuration](#сonfiguration))
3. Build project JARs with `mvn clean package -DskipTests`

### 2. Local deployment
1. Add `-Dspring.profiles.active=dev` as JVM options for API Gateway launch
2. Add path to .env.dev for environment variables
3. Launch api-gateway and user-service
4. Run `ng serve --port 8031` from */frontend* folder to launch SPA

Web application will be available on http://localhost:8031

### 3. Docker deployment
1. Prepare images for all modules
```
docker build -t eventcore-api-gateway:latest api-gateway
docker build -t eventcore-user-service:latest user-service
docker build -t eventcore-frontend:latest frontend
```
2. Run `docker-compose up -d`

Web application will be available on port 8031

## Author
Nazar Nefodov

## Configuration
Example of .env file with all necessary variables
```
POSTGRES_DB=db_name
POSTGRES_USER=db_user
DATABASE_PASSWORD=db_password
SPRING_DATASOURCE_URL=db_jdbc_link
SPRING_DATASOURCE_USERNAME=db_name
JWT_SECRET=string_of_64+_chars
```
Content of .env.dev should be same, but links should refer to local DB (or you can launch postgres image separately) 

## License
The project is distributed under license [MIT](LICENSE.md).