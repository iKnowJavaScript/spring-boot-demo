package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Repository is equivalent to Component annotation.
@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao {

    private static List<Person> DB = new ArrayList<>();

    @Override
    public int insertPerson(UUID id, Person person) {
        DB.add(new Person(id, person.getName()));
        return 1;
    }

    @Override
    public List<Person> selectAllPeople() {
        return DB;
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
        return DB.stream()
                .filter((person -> person.getId().equals(id)))
                .findFirst();
    }

    @Override
    public int deletePersonById(UUID id) {
        Optional<Person> maybePerson = selectPersonById(id);
        if (maybePerson.isEmpty()) {
            return 0;
        } else {
            DB.remove(maybePerson.get());
            return 1;
        }
    }

    @Override
    public int updatePersonById(UUID id, Person update) {
        return selectPersonById(id)
                .map(p -> {
                    int indexOfSelectedPerson = DB.indexOf(p);
                    if (indexOfSelectedPerson >= 0) {
                        DB.set(indexOfSelectedPerson, new Person(id, update.getName()));
                        return 1;
                    }
                    return 0;
                })
                .orElse(0);
    }
}
