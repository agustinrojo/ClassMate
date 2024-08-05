package com.example.chat_v1.config.deserializer;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;

@Configuration
public class JacksonConfig {

    @Bean
    public Module byteArrayResourceModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ByteArrayResource.class, new ByteArrayResourceDeserializer());
        module.addSerializer(ByteArrayResource.class, new ByteArrayResourceSerializer());
        return module;
    }
}
