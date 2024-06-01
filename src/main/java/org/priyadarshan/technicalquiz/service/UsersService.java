package org.priyadarshan.technicalquiz.service;

import lombok.RequiredArgsConstructor;
import org.priyadarshan.technicalquiz.dao.ApiKeyDao;
import org.priyadarshan.technicalquiz.dao.UsersDao;
import org.priyadarshan.technicalquiz.pojo.Users;
import org.priyadarshan.technicalquiz.utility.CustomPasswordEncoder;
import org.priyadarshan.technicalquiz.utility.KeyAuthenticator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersDao usersDao;
    private final ApiKeyDao apiKeyDao;
    private final CustomPasswordEncoder passwordEncoder;
    private final KeyAuthenticator keyAuthenticator;

    private boolean userExists(String email) {
        return usersDao.findById(email).isPresent();
    }

    public ResponseEntity<String> register(Users user) {

        if (!userExists(user.getEmail())) {
            try {
                String password = user.getPassword();
                String salt = passwordEncoder.generateSalt();
                String encodedPassword = passwordEncoder.hashPassword(password, salt);

                user.setRole("user");
                user.setPassword(encodedPassword);
                user.setSalt(salt);
                usersDao.save(user);

                return new ResponseEntity<>("User added successfully", HttpStatus.CREATED);
            } catch (Exception e) {
                String errorMessage = "{\"error\": \"An error occurred while processing your request\"}";
                return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
            }
        }
        else{
            String errorMessage = "{\"error\": \"User already exists\"}";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<String> delete(String key,String email) {

        keyAuthenticator.setKey(key);
        if (keyAuthenticator.isKeyValid()) {
            if (userExists(email)) {
                try {
                    usersDao.deleteById(email);
                    return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
                } catch (Exception e) {
                    String errorMessage = "{\"error\": \"An error occurred while processing your request\"}";
                    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
                }
            }
            else{
                String errorMessage = "{\"error\": \"User does not exist\"}";
                return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Invalid key", HttpStatus.UNAUTHORIZED);

    }

    public ResponseEntity<String> authenticate(String email, String password) {

        if (userExists(email)) {
            try {
                String storedPassword = usersDao.findById(email).get().getPassword();
                String storedSalt = usersDao.findById(email).get().getSalt();
                String encodedPassword = passwordEncoder.hashPassword(password, storedSalt);

                if(encodedPassword.equals(storedPassword)){;
                    return new ResponseEntity<>("{\"success\": \"User authenticated successfully\"}", HttpStatus.OK);
                }
                else{
                    String errorMessage = "{\"error\": \"Invalid password\"}";
                    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                String errorMessage = "{\"error\": \"An error occurred while processing your request\"}";
                return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            String errorMessage = "{\"error\": \"User does not exist\"}";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }
}
