package Controllers;

import com.example.BarclaysTest.Controllers.UserEndpoint;
import com.example.BarclaysTest.ExceptionHandler.NotFoundException;
import com.example.BarclaysTest.Service.UserService;
import com.example.BarclaysTest.model.Requests.CreateUserRequest;
import com.example.BarclaysTest.model.Response.UserResponse;
import com.example.BarclaysTest.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEndpointTest {

    @InjectMocks
    private UserEndpoint userEndpoint;

    @Mock
    private UserService userService;

    private final String authenticatedUserId = "usr-123";

    private MockedStatic<SecurityContextHolder> mockedSecurityContext;

    void setUp() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(authenticatedUserId);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        mockedSecurityContext = mockStatic(SecurityContextHolder.class);
        mockedSecurityContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    }

    @AfterEach
    void tearDownSecurityContext() {
        if (mockedSecurityContext != null) {
            mockedSecurityContext.close();
        }
    }

    @Test
    void shouldCreateUserSuccessfully() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("TestName")
                .email("TestName@test.com")
                .build();

        User mockUser = User.builder()
                .id(authenticatedUserId)
                .name(request.getName())
                .email(request.getEmail())
                .build();

        when(userService.createUser(request)).thenReturn(mockUser);

        ResponseEntity<UserResponse> response = userEndpoint.createUser(request);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(authenticatedUserId, response.getBody().getId());
        assertEquals("TestName", response.getBody().getName());
        assertEquals("TestName@test.com", response.getBody().getEmail());
    }
    @Test
    void shouldFetchUserByIdSuccessfully() {

        setUp();
        String userId = "usr-123";
        User mockUser = User.builder()
                .id(userId)
                .name("TestName")
                .email("TestName@test.com")
                .build();

        when(userService.getUserIfAuthorized(userId, authenticatedUserId)).thenReturn(mockUser);

        ResponseEntity<UserResponse> response = userEndpoint.fetchUserByID(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().getId());
        assertEquals("TestName", response.getBody().getName());
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() {
        setUp();
        String userId = "usr-123";

        when(userService.getUserIfAuthorized(userId, authenticatedUserId))
                .thenThrow(new NotFoundException("User not found"));

        assertThrows(NotFoundException.class, () ->
                userEndpoint.fetchUserByID(userId)
        );

    }
}
