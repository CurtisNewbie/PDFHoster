package com.curtisnewbie;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

// this is to enable passing CLI arguments to Quarkus App
@QuarkusMain
public class Main {

    public static void main(String... args) {
        Quarkus.run(args);
    }
}
