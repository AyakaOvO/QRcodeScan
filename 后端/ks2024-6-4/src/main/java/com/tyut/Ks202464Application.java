package com.tyut;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RetrofitScan("com.example.retrofitinterface")
public class Ks202464Application {

    public static void main(String[] args) {
        SpringApplication.run(Ks202464Application.class, args);
    }

}
