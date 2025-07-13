package com.nchl.authorization_server.security;

import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("customPasswordEncoder")
//@Primary
public class CustomPasswordEncoder  {

    public String encode(CharSequence rawPassword) {
        String passwordString = rawPassword.toString();
        String hashed = BCrypt.hashpw(passwordString, BCrypt.gensalt(10));
        return hashed;
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String passwordString = rawPassword.toString();
        passwordString = passwordString.substring(3);
        return BCrypt.checkpw(passwordString, encodedPassword);
    }

//todo:remove this
/*    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new CustomPasswordEncoder();
        System.out.println(passwordEncoder.encode("D#51464049"));
    }*/
}
