package DAO;

import DAOs.DataAccessException;
import DAOs.Database;
import DAOs.PersonDAO;
import Models.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

public class PersonDAOTest {
    private Database database;
    private Person testPerson;
    private PersonDAO personDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        database = new Database();
        testPerson = new Person("testID", "cordy58", "Cordell", "Thompson",
                "m");
        Connection connection = database.getConnection();
        personDAO = new PersonDAO(connection);
        personDAO.clear();
    }

    @AfterEach
    public void tearDown() {
        database.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        personDAO.createPerson(testPerson);

        Person getTestPerson = personDAO.getPersonByID(testPerson.getPersonID());
        assertNotNull(getTestPerson);
        assertEquals(testPerson, getTestPerson);
    }

    @Test
    public void insertFail() throws DataAccessException {
        personDAO.createPerson(testPerson);
        assertThrows(DataAccessException.class, () -> personDAO.createPerson(testPerson));
    }

    @Test
    public void retrievePass() throws DataAccessException {
        personDAO.createPerson(testPerson);

        Person getTestPerson = personDAO.getPersonByID(testPerson.getPersonID());
        assertNotNull(getTestPerson);
        assertEquals(testPerson, getTestPerson);
    }

    @Test
    public void retrieveFail() throws DataAccessException {
        personDAO.createPerson(testPerson);
        assertNull(personDAO.getPersonByID("fakeID"));
    }

    @Test
    public void getFamilyMembersPass() throws DataAccessException {
        Person mother = new Person("danaht", "cordy58", "Dana", "Thompson", "f");
        Person father = new Person("brentt", "cordy58", "Brent", "Thompson", "m");
        Person spouse = new Person("spouse", "cordy58", "Unknown", "IDK", "f");
        testPerson.setMotherID(mother.getPersonID());
        testPerson.setFatherID(father.getPersonID());
        testPerson.setSpouseID(spouse.getPersonID());
        spouse.setSpouseID(testPerson.getPersonID());
        personDAO.createPerson(testPerson);
        personDAO.createPerson(mother);
        personDAO.createPerson(father);
        personDAO.createPerson(spouse);
        List<Person> constructedFamily = new ArrayList<>();
        constructedFamily.add(father);
        constructedFamily.add(mother);
        constructedFamily.add(spouse);
        constructedFamily.add(testPerson);

        List<Person> returnedFamily = personDAO.getFamilyMembers(testPerson.getAssociatedUsername());

        assertEquals(constructedFamily, returnedFamily);
    }

    @Test
    public void getFamilyMembersFail() throws DataAccessException {
        Person mother = new Person("danaht", "cordy58", "Dana", "Thompson", "f");
        Person father = new Person("brentt", "cordy", "Brent", "Thompson", "m");
        Person spouse = new Person("spouse", "cordy", "Unknown", "IDK", "f");
        testPerson.setMotherID(mother.getPersonID());
        testPerson.setFatherID(father.getPersonID());
        testPerson.setSpouseID(spouse.getPersonID());
        spouse.setSpouseID(testPerson.getPersonID());
        personDAO.createPerson(testPerson);
        personDAO.createPerson(mother);
        personDAO.createPerson(father);
        personDAO.createPerson(spouse);
        List<Person> constructedFamily = new ArrayList<>();
        constructedFamily.add(father);
        constructedFamily.add(testPerson);
        constructedFamily.add(mother);
        constructedFamily.add(spouse);
        List<Person> returnedFamily = personDAO.getFamilyMembers(testPerson.getAssociatedUsername());

        assertNotEquals(constructedFamily, returnedFamily);
    }

    @Test
    public void deletePersonPass() throws DataAccessException {
        personDAO.createPerson(testPerson);
        personDAO.deletePerson(testPerson.getPersonID());
        assertNull(personDAO.getPersonByID(testPerson.getPersonID()));
    }

    @Test
    public void deletePeoplePass() throws DataAccessException {
        personDAO.createPerson(testPerson);
        Person secondPerson = new Person("second", "cordy58", "Cord", "Thompson",
                "m");
        Person thirdPerson = new Person("third", "cordy58", "Cordella", "Thompson",
                "f");
        personDAO.createPerson(secondPerson);
        personDAO.createPerson(thirdPerson);

        personDAO.deletePeople("cordy58");
        assertNull(personDAO.getPersonByID(testPerson.getPersonID()));
        assertNull(personDAO.getPersonByID(secondPerson.getPersonID()));
        assertNull(personDAO.getPersonByID(thirdPerson.getPersonID()));
    }

    @Test
    public void clearTest() throws DataAccessException {
        personDAO.createPerson(testPerson);
        personDAO.clear();

        assertNull(personDAO.getPersonByID(testPerson.getPersonID()));
    }
}
