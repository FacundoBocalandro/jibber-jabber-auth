package com.ingsis.jibberjabberauth.services;

import com.ingsis.jibberjabberauth.models.RegisterUserDto;
import com.ingsis.jibberjabberauth.models.User;
import com.ingsis.jibberjabberauth.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder bcryptEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder bcryptEncoder) {
        this.userRepository = userRepository;
        this.bcryptEncoder = bcryptEncoder;
    }

    public void register(RegisterUserDto userDto) {
        User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getUsername(),
                bcryptEncoder.encode(userDto.getPassword()), "ROLE_USER");
        userRepository.save(user);
    }

    public void updateUser(RegisterUserDto user, long id) throws NotFoundException {
        User oldUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        oldUser.setEmail(user.getEmail());

        userRepository.save(oldUser);
    }
}
