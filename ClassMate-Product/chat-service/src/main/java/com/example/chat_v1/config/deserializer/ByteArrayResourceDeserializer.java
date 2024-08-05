package com.example.chat_v1.config.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.Base64;

public class ByteArrayResourceDeserializer extends JsonDeserializer<ByteArrayResource> {

    @Override
    public ByteArrayResource deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String base64 = p.getValueAsString();
        byte[] bytes = Base64.getDecoder().decode(base64);
        return new ByteArrayResource(bytes);
    }
}
