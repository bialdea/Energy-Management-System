package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.security.JwtRequest;
import ro.tuc.ds2020.security.JwtResponse;
import ro.tuc.ds2020.services.PersonService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ro.tuc.ds2020.security.JwtTokenUtil;
import ro.tuc.ds2020.security.JwtUserDetailsService;
import org.springframework.security.core.GrantedAuthority;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/person")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PersonController {

    private final RestTemplate restTemplate;
    private final String authServiceUrl = "http://localhost:8080/person";
    @Autowired
    PersonService personService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    public PersonController(RestTemplate restTemplate){this.restTemplate = restTemplate;}
    private AuthenticationManager authenticationManager = new AuthenticationManager() {
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            return null;
        }
    };

    @RequestMapping(method = RequestMethod.GET, value = "/auth/allper")
    @ResponseBody
    public List<Person> getAllPeople(){
        Person[] people = restTemplate.exchange("http://localhost:8080/person/all", HttpMethod.GET, null, Person[].class).getBody();
        return Arrays.asList(people);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/auth/getById")
    @ResponseBody
    public Person getById(@RequestParam(name = "id") Integer id) {
        ResponseEntity<Person> response = restTemplate.exchange(
                authServiceUrl + "/getById?id=" + id, HttpMethod.GET, null, Person.class);
        return response.getBody();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/auth/deleteById")
    @ResponseBody
    public ResponseEntity<String> deleteAuthById(@RequestParam(name = "id") Integer id) {
        restTemplate.exchange(authServiceUrl + "/deleteById?id=" + id, HttpMethod.DELETE, null, String.class);
        return ResponseEntity.ok("Person deleted successfully.");
    }


    @RequestMapping(method= RequestMethod.POST, value ="/auth/save")
    @ResponseBody
    public ResponseEntity<Person> saveAuthPerson(@RequestBody Person person) {
        ResponseEntity<Person> response = restTemplate.postForEntity(
                authServiceUrl + "/save", person, Person.class);
        return response;
    }

    @GetMapping("/auth/getPersonId/{username}")
    @ResponseBody
    public ResponseEntity<Integer> getAuthPersonId(@PathVariable("username") String username) {
        ResponseEntity<Integer> response = restTemplate.getForEntity(
                authServiceUrl + "/getPersonId/" + username, Integer.class);
        return response;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Retrieve roles from userDetails
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", roles); // Include roles in the response

        return ResponseEntity.ok(response);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}


