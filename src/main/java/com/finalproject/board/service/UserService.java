package com.finalproject.board.service;


//import com.finalproject.board.entity.Role;
import com.finalproject.board.entity.SiteUser;
import com.finalproject.board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public SiteUser save(SiteUser siteUser) {

        String encodedPassword = passwordEncoder.encode(siteUser.getPassword());
        siteUser.setPassword(encodedPassword);
        siteUser.setEnabled(true);
//        Role role = new Role();
//        role.setId(1L);

        return userRepository.save(siteUser);
    }

}
