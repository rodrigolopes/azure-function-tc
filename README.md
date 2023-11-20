# azure-function-tc

Testing HTTP-triggered azure functions with TestContainers.
This repo has a simple HTTP-triggered Azure function written in Java.
```
@Component
public class UppercaseAzureFunction {
    private final Function<String, String> uppercaseFunction;

    public UppercaseAzureFunction(Function<String, String> uppercaseFunction) {
        this.uppercaseFunction = uppercaseFunction;
    }

    @FunctionName("uppercase")
    public String uppercase(
            @HttpTrigger(name = "req",
                    methods = { HttpMethod.POST },
                    authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            ExecutionContext context) {
        return uppercaseFunction.apply(request.getBody().get());
    }
}

```
We use TestContainers to run the azure function using the azure-functions-docker image provided by Microsoft and RestAssured to test the endpoint provided by the azure function.
his is how we setup the GenericContainer:
(make sure to run ```mvn clean package``` before running this)

```
public class TestContainersConfiguration {

    @Bean("function")
    public GenericContainer<?> functionContainer() {

        MountableFile functionFiles = MountableFile.forHostPath(Paths.get("target/azure-functions/azure-function-tc"));

        return new GenericContainer<>("mcr.microsoft.com/azure-functions/java:4-java17-appservice")
                .withCopyFileToContainer(functionFiles, "/home/site/wwwroot")
                .withEnv("AzureWebJobsScriptRoot", "/home/site/wwwroot")
                .withEnv("AzureFunctionsJobHost__Logging__Console__IsEnabled", "true")
                .withEnv("AZURE_FUNCTIONS_ENVIRONMENT", "Test")
                .withEnv("AzureWebJobsStorage", "UseDevelopmentStorage=true")
                .withEnv("FUNCTIONS_WORKER_RUNTIME", "java")
                .withEnv("MAIN_CLASS", "com.example.azurefunctiontc.AzureFunctionTcApplication")
                .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Application started.*"))
                .withExposedPorts(80)
                .withNetworkAliases("function");
    }
}

```

And this is the test:
```
@Testcontainers
@Import(TestContainersConfiguration.class)
@SpringBootTest
class MyAzureFunctionIT {

    @Autowired
    @Qualifier("function")
    private GenericContainer<?> function;

    @Test
    void shouldReturnUppercase() {

        var response = given()
                .body("testcontainers")
                .when()
                .post(String.format("http://localhost:%s/api/uppercase", function.getMappedPort(80)))
                .then()
                .statusCode(200)
                .extract()
                .response()
                .asString();

        assertThat(response ).isNotNull();
        assertThat(response ).isEqualTo("TESTCONTAINERS");
    }
}
```
We inject the function container in order to obtain the local port that is mapped to its port 80.
