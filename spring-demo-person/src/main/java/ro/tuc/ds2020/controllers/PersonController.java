package ro.tuc.ds2020.controllers;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.services.PersonService;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins="*")
@RestController
@RequestMapping(value="/person")
public class PersonController {

    @Autowired
    PersonService personService;


    @RequestMapping(method = RequestMethod.GET, value = "/all")
    @ResponseBody
    public List<Person> getAll() {
        return personService.getAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getById")
    @ResponseBody
    public Person getById(@RequestParam(name = "id") Integer id) {
        return personService.getById(id);
    }
    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteById")
    @ResponseBody
    public ResponseEntity<String> deleteById(@RequestParam(name = "id") Integer id) throws NotFoundException {
        personService.deleteById(id);
        return ResponseEntity.ok("Persoana a fost ștearsă.");
    }

    @RequestMapping(method= RequestMethod.POST, value ="/save")
    @ResponseBody
    public Person savePerson(@RequestBody Person person) throws IOException {
        return personService.savePerson(person);
    }
    @RequestMapping(method = RequestMethod.POST, value = "/edit/{id}")
    @ResponseBody
    public ResponseEntity<String> editPerson(@PathVariable Integer id, @RequestBody Person updatedPerson) throws NotFoundException {
        Person editedPerson = personService.editPerson(id, updatedPerson);
        if (editedPerson != null) {
            return ResponseEntity.ok("Persoana a fost actualizată.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/getPersonId/{username}")
    @ResponseBody
    public Integer getPersonId(@PathVariable("username") String username) {
        return this.personService.getPersonId(username);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Person person) {
        Map<String, Object> response = new HashMap<>();
        Person persons = personService.authenticate(person.getUsername(), person.getPassword());

        if (persons != null) {
            response.put("message", "Login successful");
            response.put("role", persons.getRole());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}


