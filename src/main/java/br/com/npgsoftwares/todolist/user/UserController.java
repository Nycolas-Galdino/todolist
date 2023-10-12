package br.com.npgsoftwares.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public UserModel create(@RequestBody UserModel userModel){
        System.out.println(userModel.getName());
        System.out.println(userModel.getUsername());
        System.out.println(userModel.getPassword());
         
        return this.userRepository.save(userModel);
    }
}
