package Service;

import Models.*;
import Request.LoadRequest;
import Services.LoadService;
import Result.LoadResult;
import Services.ClearService;

import com.google.gson.Gson;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;

public class LoadServiceTest {
    private LoadRequest loadRequest;

    @BeforeEach
    public void setUp() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @AfterAll
    static void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void LoadServicePass() {
        try {
            Gson gson = new Gson();
            Reader loadRequestReader = new FileReader("passoffFiles/LoadData.json");
            loadRequest = gson.fromJson(loadRequestReader, LoadRequest.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error loading data from Json files for testing LoadService");
        }
        LoadService loadService = new LoadService();
        LoadResult result = loadService.load(loadRequest);

        assertTrue(result.isSuccess());
        assertEquals("Successfully added 2 users, 11 persons, and 19 events to the database.", result.getMessage());
    }

    @Test
    public void LoadServiceFail() {
        User[] users = new User[1];
        Person[] persons = new Person[1];
        Event[] events = new Event[1];
        users[0] = new User("", "test", "test", "", "test", "m", "4321");
        loadRequest = new LoadRequest(users, persons, events);

        LoadService loadService = new LoadService();
        LoadResult loadResult = loadService.load(loadRequest);
        assertEquals("Error: User array has invalid or missing values", loadResult.getMessage());
    }
}
