package com.mavriksc.overcomp;

import java.util.Collection;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@SpringBootApplication
public class HeroServiceApplication {    
    @Bean
    CommandLineRunner commandLineRunner(HeroRepo heroRepo) {
        return srtings ->{
            Stream.of("Mr. Nice","Narco","Bombasto","Celeritas","Magneta","RubberMan"
                    ,"Dynama","Dr IQ","Magma","Tornado")
            .forEach(n-> heroRepo.save(new Hero(n)));
        };
    }
	public static void main(String[] args) {
		SpringApplication.run(HeroServiceApplication.class, args);
	}
}

@CrossOrigin
@RestController
@RequestMapping("/heroes")
class HeroRestController{
    @Autowired
    HeroRepo heroRepo;
    
    @GetMapping
    public Collection<Hero> listHeros(){
        return heroRepo.findAll();
    }
    @GetMapping(value= "/{id}")
    public Hero findById(@PathVariable Long id) {
        return heroRepo.findOne(id);
    }
    @PutMapping(value= "/{id}")
    public void updateById(@PathVariable Long id, @RequestBody Hero hero) {
        Hero heroToUpdate = heroRepo.findOne(id);
        heroToUpdate.setName(hero.getName());
        heroRepo.save(heroToUpdate);
    }
    @PostMapping
    public Hero newHero(@RequestBody Hero hero){
        hero = heroRepo.save(hero);
        return hero;
    }
    @DeleteMapping(value= "/{id}")
    public void delete(@PathVariable Long id) {
        Hero hero = heroRepo.findOne(id);
        heroRepo.delete(hero);        
    }
    @GetMapping(value = "/search")
    public Collection<Hero> searchHeros(@RequestParam String name){
        return heroRepo.nameContaining(name);
    }
}

interface HeroRepo extends JpaRepository<Hero, Long>{
    Collection<Hero> nameContaining(String name);
}

@Data
@Entity
class Hero {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    
    public Hero() {
        super();
    }
    
    public Hero(String name) {
        super();
        this.name = name;
    }    
}
