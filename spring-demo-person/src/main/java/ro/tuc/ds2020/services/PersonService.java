package ro.tuc.ds2020.services;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.IPersonRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    @Autowired
    IPersonRepository IPersonRepository;

    public List<Person> getAll(){
        return IPersonRepository.findAll();
    }
    public Person savePerson(Person person){
        return IPersonRepository.save(person);
    }
    public Person editPerson(Integer id, Person updatedPerson) throws NotFoundException {
        Optional<Person> personOptional = IPersonRepository.findById(id);

        if (personOptional.isPresent()) {
            Person existingPerson = personOptional.get();
            existingPerson.setUsername(updatedPerson.getUsername());
            existingPerson.setPassword(updatedPerson.getPassword());
            existingPerson.setRole(updatedPerson.getRole());

            return IPersonRepository.save(existingPerson);
        } else {
            throw new NotFoundException("Persoana nu a fost găsită.");
        }
    }

    public Person getById(Integer id){
        if(IPersonRepository.findById(id).isPresent()){
            return IPersonRepository.findById(id).get();
        }
        return null;
    }
    public void deleteById(Integer id) throws NotFoundException {
        Optional<Person> personOptional = IPersonRepository.findById(id);

        if (personOptional.isPresent()) {
            Person personToDelete = personOptional.get();
            IPersonRepository.delete(personToDelete);
        } else {
            throw new NotFoundException("Persoana nu a fost găsită.");
        }
    }

    public Person authenticate(String username, String password) {
        Person person = IPersonRepository.findByUsername(username);

        if (person != null && person.getPassword().equals(password)) {
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

}




