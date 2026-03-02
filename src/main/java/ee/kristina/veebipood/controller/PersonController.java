package ee.kristina.veebipood.controller;

import ee.kristina.veebipood.dto.PersonLoginDto;
import ee.kristina.veebipood.dto.PersonPublicDto;
import ee.kristina.veebipood.dto.PersonSignupDto;
import ee.kristina.veebipood.dto.PersonDto;
import ee.kristina.veebipood.entity.Person;
import ee.kristina.veebipood.entity.Role;
import ee.kristina.veebipood.model.AuthToken;
import ee.kristina.veebipood.repository.PersonRepository;
import ee.kristina.veebipood.service.JwtService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Log4j2
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    private JwtService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping("persons")
    public List<PersonDto> findPersons () {
         return List.of(mapper.map(personRepository.findAll(), PersonDto[].class));
    }

    @GetMapping("persons/public")
    public List<PersonPublicDto> findAll () {
        log.info(mapper);
        return List.of(mapper.map(personRepository.findAll(), PersonPublicDto[].class));
    }

    @PostMapping("signup")
    public PersonSignupDto save(@RequestBody Person person){
        log.debug(mapper);
        //person.setRole(Role.CUSTOMER); lives tagasi, testimiseks saab rolli muuta
        person.setPassword(encoder.encode(person.getPassword()));
        Person dbPerson = personRepository.save(person);
        return mapper.map(dbPerson, PersonSignupDto.class); // maps password out before sends it back
    }


    @PostMapping("login")
    public AuthToken login(@RequestBody PersonLoginDto personLoginDto) {
        Person dbPerson = personRepository.findByEmail(personLoginDto.email());
        if (dbPerson == null) {
            throw new RuntimeException("Invalid email");
        }
        // input password hash matches hash on db
        if (!encoder.matches(personLoginDto.password(), dbPerson.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return jwtService.generateToken(dbPerson);
    }

    @GetMapping("profile")
    public PersonDto getProfile(){
        Long personId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Person dbPerson =personRepository.findById(personId).orElseThrow();
        return mapper.map(dbPerson, PersonDto.class);
    }

    @PutMapping("update-profile")
    public PersonDto updateProfile(@RequestBody Person person) {
        Person dbPerson = personRepository.save(person);
        return mapper.map(dbPerson, PersonDto.class);
    }
}
