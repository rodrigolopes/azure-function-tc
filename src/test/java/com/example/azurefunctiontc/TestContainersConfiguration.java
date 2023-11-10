package com.example.azurefunctiontc;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;

@TestConfiguration
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
