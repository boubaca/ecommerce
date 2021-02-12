package com.example.demo.services;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CartService cartService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, CartService cartService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.cartService = cartService;
    }

    public User saveUser(CreateUserRequest createUserRequest){
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        String salt = createSalt();
        user.setSalt(salt);
        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        Cart cart = new Cart();
        user.setCart(cart);
        userRepository.save(user);
        
        return user;
    }

    public User getUser(Long id){
        return userRepository.findById(id).get();
    }




    private static String createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean checkPassword(String password,User user){
        return bCryptPasswordEncoder.matches(password,user.getPassword());
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
