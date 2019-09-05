package com.ogic.spikesystempayservice.common;

import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.util.SerializationUtils;

import java.util.Map;

/**
 * @author ogic
 */
public class ObjectDeserializer implements Deserializer<Object> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public Object deserialize(String s, byte[] bytes) {
        return SerializationUtils.deserialize(bytes);
    }

    @Override
    public void close() {

    }
}
