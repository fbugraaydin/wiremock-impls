## Testing Server with WireMock

Tests a server with WireMock. It can be implemented a mock server to manage request/response body and test your server.


## Tech / Framework
- [Java 8](https://docs.oracle.com/javase/8/docs/)
- [GoLang](https://golang.org/)
- [WireMock](http://wiremock.org/)
- [Maven](https://maven.apache.org/)
- [JUnit4](https://junit.org/junit4/)

#### Just start Server(proxy.go) that will be tested (implemented as just proxy behaviour with GoLang)
- Build project command:
```bash
go build
```

- Run project command:
```bash
./proxy
```

- Run test scenarios with maven by using command:
```bash
mvn test
```

## Licence
Developed by © [Fuat Buğra AYDIN](https://www.linkedin.com/in/fuatbugraaydin/)