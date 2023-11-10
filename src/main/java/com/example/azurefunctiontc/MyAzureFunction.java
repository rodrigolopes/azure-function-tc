package com.example.azurefunctiontc;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class MyAzureFunction {
    private final Function<String, String> uppercaseFunction;

    public MyAzureFunction(Function<String, String> uppercaseFunction) {
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
