package io.megafair.jinfra.vibra.lambda.rest.app;

import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.Quarkus;

@QuarkusMain
public class Main {

    public static void main(String ... args) {
        System.out.println("Running main method - Lambda");
        Quarkus.run(args);
    }
}
