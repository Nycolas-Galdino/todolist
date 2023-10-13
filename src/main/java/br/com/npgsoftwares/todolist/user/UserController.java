package br.com.npgsoftwares.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){
        System.out.println(userModel.getName());
        System.out.println(userModel.getUsername());
        System.out.println(userModel.getPassword());

        var user = this.userRepository.findByUsername(userModel.getUsername());

        if(user != null){
            System.out.print("O usuário já está cadastrado no sistema");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já está cadastrado no sistema");
        }

        var passHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passHashred);
         
        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
