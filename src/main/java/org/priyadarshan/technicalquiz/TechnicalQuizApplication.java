package org.priyadarshan.technicalquiz;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.priyadarshan.technicalquiz.dao.UsersDao;
import org.priyadarshan.technicalquiz.pojo.Users;
import org.priyadarshan.technicalquiz.utility.CustomPasswordEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@SpringBootApplication
public class TechnicalQuizApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechnicalQuizApplication.class, args);

    }

    @Component
    @RequiredArgsConstructor
    public static class DatabaseChecker {

        private final UsersDao usersDao;
        private final CustomPasswordEncoder customPasswordEncoder;

        @PostConstruct
        public void checkDatabase() {
            Optional<Users> ad = usersDao.findById("gahukarpv@rknec.edu");
            if (ad.isEmpty()) {

                String password = "123456";
                String salt = customPasswordEncoder.generateSalt();
                String encodedPassword = customPasswordEncoder.hashPassword(password, salt);

                Users rootUser = new Users("gahukarpv@rknec.edu", "Priyadarshan", encodedPassword, salt, "admin");
                usersDao.save(rootUser);
                System.out.println("if executed");
            }
            else{
                System.out.println("else executed");
            }

        }
    }


}
