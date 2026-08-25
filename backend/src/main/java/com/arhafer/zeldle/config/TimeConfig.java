package com.arhafer.zeldle.config;

import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class TimeConfig {

    public Clock clock() {
        return Clock.system(ZoneId.of("America/New_York"));
    }
}
