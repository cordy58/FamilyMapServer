package DAOs.FamilyTree;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import DAOs.*;
import Models.Person;
import Models.Event;
import Models.User;

import com.google.gson.Gson;
import java.sql.Connection;

public class FamilyTreeGenerator {
    private Locations locations;
    private FemaleNames femaleNames;
    private MaleNames maleNames;
    private Surnames surnames;

    private String associatedUsername;

    private PersonDAO personDAO;
    private EventDAO eventDAO;
    private UserDAO userDAO;

    private Database database;

    private void initializeData() {
        try {
            Gson gson = new Gson();

            Reader locationReader = new FileReader("json/locations.json");
            locations = gson.fromJson(locationReader, Locations.class);

            Reader femaleNamesReader = new FileReader("json/fnames.json");
            femaleNames = gson.fromJson(femaleNamesReader, FemaleNames.class);

            Reader maleNamesReader = new FileReader("json/mnames.json");
            maleNames = gson.fromJson(maleNamesReader, MaleNames.class);

            Reader surnamesReader = new FileReader("json/snames.json");
            surnames = gson.fromJson(surnamesReader, Surnames.class);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error loading data from Json files for creating a family tree");
        }

    }

    public Person generateTree(String associatedUsername, String gender, int generations) throws GeneratorException {
        initializeData();

        this.associatedUsername = associatedUsername;

        database = new Database();
        try {
            database.openConnection();
            Connection connection = database.getConnection();

            personDAO = new PersonDAO(connection);
            eventDAO = new EventDAO(connection);
            userDAO = new UserDAO(connection);

            Person person = generateFamily(true, gender, generations, 1995);

            database.closeConnection(true);

            return person;
        } catch (DataAccessException e) {
            e.printStackTrace();

            database.closeConnection(false);

            String errorMessage = e.getMessage();
            throw new GeneratorException(errorMessage);
        }
    }

    private Person generateFamily(boolean isUser, String gender, int generations, int birthYear) throws GeneratorException {
        Person mother = null;
        Person father = null;
        try {
            if (generations > 0) {
                mother = generateFamily(false, "f", generations - 1, birthYear - 30);
                father = generateFamily(false, "m", generations - 1, birthYear - 30);

                mother.setSpouseID(father.getPersonID());
                personDAO.addSpouseID(mother.getSpouseID(), mother.getPersonID());

                father.setSpouseID(mother.getPersonID());
                personDAO.addSpouseID(father.getSpouseID(), father.getPersonID());

                Location marriageLocation = getRandomLocation();

                String motherMarriageID = createUniqueID("EventID");
                Event motherMarriage = new Event(motherMarriageID, associatedUsername, mother.getPersonID(),
                        marriageLocation.getLatitude(), marriageLocation.getLongitude(), marriageLocation.getCountry(),
                        marriageLocation.getCity(), "Marriage", birthYear);

                String fatherMarriageID = createUniqueID("EventID");
                Event fatherMarriage = new Event(fatherMarriageID, associatedUsername, father.getPersonID(),
                        marriageLocation.getLatitude(), marriageLocation.getLongitude(), marriageLocation.getCountry(),
                        marriageLocation.getCity(), "Marriage", birthYear);

                eventDAO.insert(motherMarriage);
                eventDAO.insert(fatherMarriage);
            }
            Person person = createPerson(isUser, gender);
            if (mother != null) {
                person.setFatherID(father.getPersonID());
                person.setMotherID(mother.getPersonID());
            }

            Event birth = createBirthOrDeath("Birth", person.getPersonID(), birthYear);
            Event death = createBirthOrDeath("Death", person.getPersonID(), birthYear);

            personDAO.createPerson(person);
            eventDAO.insert(birth);
            eventDAO.insert(death);
            return person;
        } catch (DataAccessException e) {
            e.printStackTrace();

            database.closeConnection(false);
            String errorMessage = e.getMessage();
            throw new GeneratorException(errorMessage);
        }
    }

    private Person createPerson(boolean isUser, String gender) throws DataAccessException {
        String firstName;
        String lastName;
        String personID;
        if (isUser) {
            User user = userDAO.getUserByUsername(associatedUsername);
            firstName = user.getFirstName();
            lastName = user.getLastName();
            personID = user.getPersonID();
        } else {
            firstName = getRandomFirstName(gender);
            lastName = getRandomLastName();
            personID = createUniqueID("personID");
        }

        return new Person(personID, associatedUsername, firstName, lastName, gender);
    }

    private Event createBirthOrDeath(String eventType, String personID, int year) throws DataAccessException {
        String id = createUniqueID("EventID");
        Location location = getRandomLocation();
        if (eventType.equals("Death")) {
            year = year + 90;
        }
        return new Event(id, associatedUsername, personID, location.getLatitude(), location.getLongitude(), location.country,
                location.city, eventType, year);
    }

    private String createUniqueID(String IDType) throws DataAccessException {
        String id;
        if (Objects.equals(IDType, "personID")) {
            do {
                id = UUID.randomUUID().toString();
            } while (personDAO.getPersonByID(id) != null);
        } else {
            do {
                id = UUID.randomUUID().toString();
            } while (eventDAO.find(id) != null);
        }
        return id;
    }

    private String getRandomFirstName(String gender) {
        if (Objects.equals(gender, "m")) {
            Random generator = new Random();
            int randomIndex = generator.nextInt(maleNames.data.length);
            return maleNames.data[randomIndex];
        } else {
            Random generator = new Random();
            int randomIndex = generator.nextInt(femaleNames.data.length);
            return femaleNames.data[randomIndex];
        }
    }

    private String getRandomLastName() {
        Random generator = new Random();
        int randomIndex = generator.nextInt(surnames.data.length);
        return surnames.data[randomIndex];
    }

    private Location getRandomLocation() {
        Random generator = new Random();
        int randomIndex = generator.nextInt(locations.data.length);
        return locations.data[randomIndex];
    }
}
