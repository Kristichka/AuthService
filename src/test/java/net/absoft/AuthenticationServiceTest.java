package net.absoft;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.absoft.data.Response;
import net.absoft.services.AuthenticationService;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.fail;

public class AuthenticationServiceTest  extends BaseTest{

    private AuthenticationService authenticationService;
    private String massage;

    public AuthenticationServiceTest(String massage) {
        this.massage = massage;
    }

    @BeforeMethod(groups = {"positive", "negative"})
    public void setUp() {
        authenticationService = new AuthenticationService();
        System.out.println("setup");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("teardown");
    }

    @Test (
            groups = "positive"

    )
    public void testSample() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("testSample: " + new Date());
        fail("FAILING TEST");
    }

    @Test (
            description = "Test Successful Authentication",
            groups = "positive"
    )
    @Parameters({"email-address", "password"})
    public void testSuccessfulAuthentication(@Optional("user1@test.com") String email,@Optional("password1") String password ) throws InterruptedException {
        Response response = authenticationService.authenticate(email, password);

        SoftAssert sa = new SoftAssert();
        sa.assertEquals(response.getCode(), 200, "Response code should be 200");
        sa.assertTrue(validateToken(response.getMessage()),
                "Token should be the 32 digits string. Got: " + response.getMessage());
        sa.assertAll();

        Thread.sleep(2000);
        System.out.println("testSuccessfulAuthentication: " + new Date());
    }

    @Test (
            enabled = false,
            groups = "negative"
    )
    public void testAuthenticationWithWrongPassword() {
        validateErrorResponse(
                authenticationService.authenticate("user1@test.com", "wrong_password1"),
                401,
                "Invalid email or password"
        );
    }

    private void validateErrorResponse(Response response, int code, String message) {
        SoftAssert sa = new SoftAssert();
        sa.assertEquals(response.getCode(), code, "Response code should be 401");
        sa.assertEquals(response.getMessage(), message,
                "Response message should be \"Invalid email or password\"");
        sa.assertAll();
        System.out.println("testAuthenticationWithWrongPassword");
    }

    @DataProvider( name = "invalidLogins", parallel = true)
    public Object[][] invalidLogins(){
        return new Object[][]{
                new Object[] {"user1@test.com", "wrong_password1" , new Response(401, "Invalid email or password")},
                new Object[] {"", "password1" , new Response(400, "Email should not be empty string")},
                new Object[] {"user1@test.com", "" , new Response(400, "Password should not be empty string")},
                new Object[] {"user1", "password1" , new Response(400, "Invalid email")},
        };
    }

    @Test (
            groups = "negative",
            dataProvider = "invalidLogins"
    )
    public void testInvalidAuthentication(String email,String password, Response expectedResponse) throws InterruptedException {
        Response actualResponse = authenticationService
                .authenticate(email, password);
        SoftAssert sa = new SoftAssert();
        sa.assertEquals(actualResponse.getCode(), expectedResponse.getCode(), expectedResponse.getMessage());
        sa.assertEquals(actualResponse.getMessage(), expectedResponse.getMessage(),
                "Response message should be \"Invalid email or password\"");
        sa.assertAll();

        Thread.sleep(2000);
        System.out.println("testInvalidAuthentication: " + new Date());
    }

//    @Test (
//            priority = 3,
//            groups = "negative"
//    )
//
////    public void testAuthenticationWithEmptyEmail() throws InterruptedException {
////
////        Response expectedResponse = new Response(400,"Email should not be empty string" );
////        Response actualResponse = authenticationService.authenticate("", "password1");
////        assertEquals(actualResponse, expectedResponse, "Unexpected response");
////        System.out.println("testAuthenticationWithEmptyEmail");
////
////        Thread.sleep(2000);
////        System.out.println("testAuthenticationWithEmptyEmail: " + new Date());
////    }

    @Test (
            groups = "negative"
    )
    public void testAuthenticationWithInvalidEmail() throws InterruptedException {
        Response response = authenticationService.authenticate("user1", "password1");
        SoftAssert sa = new SoftAssert();
        sa.assertEquals(response.getCode(), 400, "Response code should be 200");
        sa.assertEquals(response.getMessage(), "Invalid email",
                "Response message should be \"Invalid email or password\"");
        sa.assertAll();

        Thread.sleep(2000);
        System.out.println("testAuthenticationWithInvalidEmail: " + new Date());
    }

    @Test (
            groups = "negative",
            priority = 2
    )
    public void testAuthenticationWithEmptyPassword() throws InterruptedException {
        Response response = authenticationService.authenticate("user1@test", "");
        SoftAssert sa = new SoftAssert();
        sa.assertEquals(response.getCode(), 400, "Response code should be 400");
        sa.assertEquals(response.getMessage(), "Password should not be empty string",
                "Response message should be \"Password should not be empty string\"");
        sa.assertAll();

        Thread.sleep(2000);
        System.out.println("testAuthenticationWithEmptyPassword: " + new Date());

    }

    private boolean validateToken(String token) {
        final Pattern pattern = Pattern.compile("\\S{32}", Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(token);
        return matcher.matches();
    }
}
