package Controllers;

import com.example.BarclaysTest.Controllers.AuthEndpoint;
import com.example.BarclaysTest.ExceptionHandler.NotFoundException;
import com.example.BarclaysTest.Service.UserService;
import com.example.BarclaysTest.model.Authentication.AuthRequest;
import com.example.BarclaysTest.model.Authentication.AuthResponse;
import com.example.BarclaysTest.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthEndpoint authEndpoint;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserService userService;


    @Test
    void testAuthenticateUser_Success(){

        when(userService.isUserCreated("usr-123")).thenReturn(true);
        when(jwtUtil.generateToken("usr-123")).thenReturn("mock-jwt");

        AuthRequest request = AuthRequest.builder().id("usr-123").build();
        ResponseEntity<AuthResponse> authResponseResponseEntity = authEndpoint.authenticateUser(request);
        assertEquals("success", 200, authResponseResponseEntity.getStatusCode().value());
    }

    @Test
    void testAuthenticateUserNotFound(){

        when(userService.isUserCreated("usr-123")).thenReturn(false);
        AuthRequest request = AuthRequest.builder().id("usr-123").build();
        assertThrows(NotFoundException.class, () -> authEndpoint.authenticateUser(request));
    }


}

