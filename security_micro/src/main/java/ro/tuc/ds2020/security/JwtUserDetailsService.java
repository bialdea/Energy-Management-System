package ro.tuc.ds2020.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.services.PersonService;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final PersonService personService;

    @Autowired
    public JwtUserDetailsService(@Lazy PersonService personService) {
        this.personService = personService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personService.getByUsername(username);

        if (person == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        } else {
            // Assuming you have a method getAuthorities() that returns a collection of authorities
            return new User(person.getUsername(), person.getPassword(), getAuthorities(person));
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Person person) {
        // Assuming Person has a getRole() method that returns the role
        // Convert role to GrantedAuthority and return
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + person.getRole()));
    }
}