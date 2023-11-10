package com.example.azurefunctiontc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestAzureFunctionTcApplication {

    public static void main(String[] args) {
        SpringApplication.from(AzureFunctionTcApplication::main).with(TestAzureFunctionTcApplication.class).run(args);
    }

}
