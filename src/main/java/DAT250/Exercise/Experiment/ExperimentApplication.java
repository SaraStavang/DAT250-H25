package DAT250.Exercise.Experiment;

import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import redis.clients.jedis.UnifiedJedis;

@SpringBootApplication
public class ExperimentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExperimentApplication.class, args);
    }

    @Bean
    UnifiedJedis jedisClient() {
        return new UnifiedJedis("redis://localhost:6379"); 
    }
    @Bean
    CommandLineRunner demo(UnifiedJedis jedis) {
        return args -> {
            String users = "logged_in_users";
            jedis.del(users);
            jedis.sadd(users, "alice");
            jedis.sadd(users, "bob");
            jedis.srem(users, "alice");
            jedis.sadd(users, "eve");
            System.out.println("Online: " + jedis.smembers(users)); // [bob, eve]
            System.out.println("Count: " + jedis.scard(users));      // 2

            String poll = "poll:03ebcb7b-bd69-440b-924e-f5b7d664af7b";
            jedis.del(poll);
            jedis.hset(poll, Map.of(
                "title", "Pineapple on Pizza?",
                "option:1:caption", "Yes, yammy!",      "option:1:votes", "269",
                "option:2:caption", "Mamma mia, nooooo!", "option:2:votes", "268",
                "option:3:caption", "I do not really care ...", "option:3:votes", "42"
            ));
            jedis.hincrBy(poll, "option:2:votes", 1); // atomic increment
            System.out.println("Option 2 votes: " + jedis.hget(poll, "option:2:votes"));
            System.out.println("Poll hash: " + jedis.hgetAll(poll));
        };
    }


}
