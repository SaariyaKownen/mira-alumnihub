package com.alumni.portal;

import com.alumni.portal.model.Admin;
import com.alumni.portal.model.Alumni;
import com.alumni.portal.repository.AdminRepository;
import com.alumni.portal.repository.AlumniRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    AlumniRepository alumniRepository;

    @Autowired
    AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Admin admin = adminRepository.findByEmail(email);
        if (admin != null) {
            List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new User(admin.getEmail(), admin.getPassword(), authorities);
        }

        Alumni alumni = alumniRepository.findByEmail(email);
        if (alumni != null) {
            List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_USER"));
            return new User(alumni.getEmail(), alumni.getPassword(), authorities);
        }

        throw new UsernameNotFoundException("User not found: " + email);
    }
}