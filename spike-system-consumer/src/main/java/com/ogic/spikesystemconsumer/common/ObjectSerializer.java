package com.ogic.spikesystemconsumer.common;

import org.apache.kafka.common.serialization.Serializer;
import org.springframework.util.SerializationUtils;

import java.util.Map;

/**
 * @author ogic
 */
public class ObjectSerializer implements Serializer<Object> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, Object o) {
        return SerializationUtils.serialize(o);
    }

    @Override
    public void close() {

    }
}
