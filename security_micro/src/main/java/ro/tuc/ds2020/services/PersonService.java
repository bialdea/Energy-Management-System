package ro.tuc.ds2020.services;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.IPersonRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private IPersonRepository IPersonRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(IPersonRepository IPersonRepository, PasswordEncoder passwordEncoder) {
        this.IPersonRepository = IPersonRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> getAll(){
        return IPersonRepository.findAll();
    }
    public Person savePerson(Person person) {
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
        return IPersonRepository.save(person);
    }

    public Person editPerson(Integer id, Person updatedPerson) {
        return IPersonRepository.findById(id).map(existingPerson -> {
            existingPerson.setUsername(updatedPerson.getUsername());
            if (!existingPerson.getPassword().equals(updatedPerson.getPassword())) {
                existingPerson.setPassword(passwordEncoder.encode(updatedPerson.getPassword()));
            }
            existingPerson.setRole(updatedPerson.getRole());
            return IPersonRepository.save(existingPerson);
        }).orElseThrow(() -> new NoSuchElementException("Persoana nu a fost găsită."));
    }

    public Person getById(Integer id){
        if(IPersonRepository.findById(id).isPresent()){
            return IPersonRepository.findById(id).get();
        }
        return null;
    }
    public void deleteById(Integer id) {
        Optional<Person> personOptional = IPersonRepository.findById(id);

        if (personOptional.isPresent()) {
            Person personToDelete = personOptional.get();
            IPersonRepository.delete(personToDelete);
        }
    }

    public Person authenticate(String username, String password) {
        Person person = IPersonRepository.findByUsername(username);

        if (person != null && passwordEncoder.matches(password, person.getPassword())) {
            return person;
        }

        return null;
    }

    public Integer getPersonId(String username) {
        Person person = IPersonRepository.findByUsername(username);
        if (person != null) {
            return person.getId();
        }
        return null;
    }

    public Person getByUsername(String username) {
        Optional<Person> user = IPersonRepository.findAll().stream().filter(o -> o.getUsername().equals(username)).findFirst();
        return user.orElse(null);
    }
}




