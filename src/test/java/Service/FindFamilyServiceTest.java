package Service;

import DAOs.AuthTokenDAO;
import DAOs.DataAccessException;
import DAOs.Database;
import Models.AuthToken;
import Request.LoadRequest;
import Result.FindFamilyResult;
import Result.LoadResult;
import Services.ClearService;
import Services.FindFamilyService;
import Services.LoadService;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FindFamilyServiceTest {
    private Database database;
    private LoadService loadService;
    private LoadRequest loadRequest;
    private LoadResult loadResult;

    private AuthTokenDAO authTokenDAO;
    private AuthToken authToken;

    @BeforeEach
    public void setUp() {
        ClearService clearService = new ClearService();
        clearService.clear();

        try {
            Gson gson = new Gson();
            Reader loadRequestReader = new FileReader("passoffFiles/LoadData.json");
            loadRequest = gson.fromJson(loadRequestReader, LoadRequest.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error loading data from Json files for testing LoadService");
        }
        loadService = new LoadService();
        loadResult = loadService.load(loadRequest);
    }

    @AfterAll
    static void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void findFamilyServicePass() throws DataAccessException {
        database = new Database();
        database.openConnection();
        Connection connection = database.getConnection();

        authTokenDAO = new AuthTokenDAO(connection);
        authToken = authTokenDAO.getByUsername("patrick");

        database.closeConnection(true);
        FindFamilyService findFamilyService = new FindFamilyService();
        FindFamilyResult findFamilyResult = findFamilyService.findFamily(authToken.getAuthToken());

        assertEquals(3, findFamilyResult.getData().size());
    }

    @Test
    public void findFamilyServiceFail() {
        FindFamilyService findFamilyService = new FindFamilyService();
        FindFamilyResult findFamilyResult = findFamilyService.findFamily("fakeToken");

        assertEquals("Error: Invalid AuthToken", findFamilyResult.getMessage());
    }
}
