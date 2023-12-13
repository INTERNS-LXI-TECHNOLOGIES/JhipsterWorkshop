package com.lxisoft.socialmedia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PostTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Post getPostSample1() {
        return new Post().id(1L).content("content1").hashTags("hashTags1");
    }

    public static Post getPostSample2() {
        return new Post().id(2L).content("content2").hashTags("hashTags2");
    }

    public static Post getPostRandomSampleGenerator() {
        return new Post().id(longCount.incrementAndGet()).content(UUID.randomUUID().toString()).hashTags(UUID.randomUUID().toString());
    }
}
