# Stock API:
---

- I developed this API as part of an assignment project given by ThinkHumble.in while applying for a Backend Developer Intern position.

- Thank you so much thinkhumble.in for giving me this opportunity to create this project.

- This project is a bit more special to me because I am more familiar with Flask, which uses Python—a language that isn’t strictly typed. I challenged myself with this project and encountered several difficulties, but overall, it was a valuable experience. I enjoyed it!!

- This project features a real-time or near real-time API that delivers stock quotes to client applications, with data sourced from the Alpha Vantage API.

- The API is protected with basic http authentication using username and password.

- The API consists of 3 routes:
    - GET "/" : OpenAPI documentation for API
    - GET "/quote/{symbol}" : json object of stock-quote for perticular symbol, for example do GET /quote/IBM
    - GET "/quote/batch" :  json array of stock-quote objects for symbols specified by "symbols" query paramerter, for example do GET /quote/batch?symbols:GOOG,IBM,AMZN

- See the ProblemStatement.pdf.

---

## Installation:

### Prerequisites:
- JDK 17
- Gradle
- Export the below environment variables:
    - API_KEY: alpha-vantage api-key
    - SECURITY_USER_NAME: username for basic auth
    - SECURITY_USER_PASSWORD: password for basic auth

### Test:
- `./gradlew test`

### Run locally:
- `./gradlew bootrun`

### Get Executable
- `./gradlew build`
- the jar file will be built at: build/libs/stock-0.0.1-SNAPSHOT.jar

### Deploy with Docker:
- Build the jar: `./gradlew build`
- Build docker image: `sudo docker build -t stock-api .`
- Run the container with environment variables:
```
    sudo docker run -p 8080:8080 \
    -e API_KEY=api_key \
    -e SECURITY_USER_NAME=username \
    -e SECURITY_USER_PASSWORD=password \
    --name stock-api stock-api
```
- The REST API service should now be running, so browser the "localhost:8080"

#### Stop the application deployed with Docker:
- list all containers: `sudo docker ps -a`
- stop the container which has name "stock-api": `docker stop <container_id>`

#### Uninstall application deployed with Docker:
- list all containers: `sudo docker ps -a`
- remove container with name "stock-api": `docker rm <container_id>`
- list images: `docker images`
- remove the image with name "stock-api":`docker rmi <image_id>`