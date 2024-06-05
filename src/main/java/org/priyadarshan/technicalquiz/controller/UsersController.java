package org.priyadarshan.technicalquiz.controller;

import lombok.RequiredArgsConstructor;
import org.priyadarshan.technicalquiz.dto.UserDTO;
import org.priyadarshan.technicalquiz.pojo.Users;
import org.priyadarshan.technicalquiz.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UsersController {

    private final UsersService usersService;

    //http://localhost:8080/user/register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Users user) {
        return usersService.register(user);
    }

    //http://localhost:8080/user/delete?key=yourKey?email=yourEmail
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam String password,@RequestParam String email) {
        System.out.println(email);
        return usersService.delete(password,email);
    }

    @GetMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestParam String email,@RequestParam String password) {
        return usersService.authenticate(email,password);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody UserDTO user) {
        System.out.println(user.toString());
        return usersService.update(user);
    }
}
