package com.party_up.network.config.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.party_up.network.model.User;
import com.party_up.network.model.enums.AccountStatus;
import com.party_up.network.repository.UserRepository;

/**
 * Database seeder for injecting initial data into the application.
 * This seeder inserts a set of default users if no users exist in the database.
 */
@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for injecting dependencies.
     *
     * @param userRepository  the repository for user data
     * @param passwordEncoder encoder for securely hashing passwords
     */
    public DatabaseSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Runs on application startup to seed initial data if the database is empty.
     *
     * @param args command line arguments
     */
    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User user1 = new User("johny", passwordEncoder.encode("Password123!"),
                    "John", "Doe", "john.doe@example.com", AccountStatus.ACTIVE);
            User user2 = new User("johny1", passwordEncoder.encode("Password123!"),
                    "Jane", "Smith", "jane.smith@example.com", AccountStatus.ACTIVE);
            User user3 = new User("johny2", passwordEncoder.encode("Password123!"),
                    "Test1", "Test1", "test1@example.com", AccountStatus.ACTIVE);
            User user4 = new User("johny3", passwordEncoder.encode("Password123!"),
                    "Test2", "Test2", "test2@example.com", AccountStatus.ACTIVE);
            User user5 = new User("johny4", passwordEncoder.encode("Password123!"),
                    "Test3", "Test3", "test3@example.com", AccountStatus.ACTIVE);


            // Saving initial users to the database
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
            userRepository.save(user5);

            logger.info("Initial users have been successfully injected into the database.");
        } else {
            logger.info("Database already contains users. Seeding skipped");
        }
    }
}
