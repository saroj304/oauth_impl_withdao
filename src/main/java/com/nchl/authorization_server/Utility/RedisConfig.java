package com.nchl.connectips.configuration;

import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Arrays;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.sentinel.master}")
    private String redisMaster;

    @Value("${spring.data.redis.sentinel.nodes}")
    private String sentinelNodes;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Value("${spring.data.redis.lettuce.pool.max-active}")
    private int maxActive;

    @Value("${spring.data.redis.lettuce.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.data.redis.lettuce.pool.min-idle}")
    private int minIdle;
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        String[] sentinelAddresses = sentinelNodes.split(",");

        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master(redisMaster);
        Arrays.stream(sentinelAddresses)
                .forEach(sentinel -> {
                    String[] hostPort = sentinel.split(":");
                    sentinelConfig.sentinel(hostPort[0], Integer.parseInt(hostPort[1]));
                });

        sentinelConfig.setPassword(RedisPassword.of(redisPassword)); // Set the password

        // Connection Pool Configuration
        GenericObjectPoolConfig<StatefulConnection<?, ?>> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(maxActive); // Max connections
        poolConfig.setMaxIdle(maxIdle); // Max idle connections
        poolConfig.setMinIdle(minIdle); // Min idle connections
        poolConfig.setMaxWaitMillis(10000);  // Max wait time for a connection from the pool


        // Configure Lettuce Client with pooling
        ClientResources clientResources = DefaultClientResources.create();
        LettucePoolingClientConfiguration poolingConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .clientResources(clientResources)
                .commandTimeout(Duration.ofMillis(30000))
                .shutdownTimeout(Duration.ofMillis(30000))
                .build();
        // Return LettuceConnectionFactory
        return new LettuceConnectionFactory(sentinelConfig, poolingConfig);
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // Cache TTL
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .build();
    }

//    @Autowired
//    private CacheManager cacheManager;
//
//    @DeleteMapping("/cache/clear")
//    public String clearSpecificCaches() {
//        // Hard-coded cache names to be cleared
//        List<String> allowedCaches = Arrays.asList("products", "orders", "users");
//
//        StringBuilder response = new StringBuilder("Cleared caches: ");
//
//        allowedCaches.forEach(cacheName -> {
//            if (cacheManager.getCache(cacheName) != null) {
//                cacheManager.getCache(cacheName).clear();
//                response.append(cacheName).append(", ");
//            }
//        });
//
//        return response.substring(0, response.length() - 2); // Remove trailing comma and space
//    }
}


//}
