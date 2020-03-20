package com.bestgroup.userservice.security;

import com.bestgroup.userservice.UserNotFoundException;
import com.bestgroup.userservice.entities.LocalUser;
import com.bestgroup.userservice.repository.LocalUserRepository;
import com.bestgroup.userservice.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class LocalRepoUserDetailsService implements UserDetailsService {

    private LocalUserRepository repository;

    public LocalRepoUserDetailsService(LocalUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<LocalUser> user = repository.findByLogin(login);
        if(!user.isPresent()) {
            throw new UserNotFoundException("id: " + login);
        }
        return new User(user.get().getLogin(), user.get().getPassword(), getGrantedAuthorities());
    }

    private Collection<SimpleGrantedAuthority> getGrantedAuthorities() {
        List<SimpleGrantedAuthority>  grantedAuthorities = new LinkedList();
        grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        return grantedAuthorities;
    }
}
