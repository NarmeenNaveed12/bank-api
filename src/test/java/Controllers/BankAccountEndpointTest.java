package Controllers;

import com.example.BarclaysTest.Controllers.BankAccountEndpoint;
import com.example.BarclaysTest.ExceptionHandler.ForbiddenException;
import com.example.BarclaysTest.Service.BankService;
import com.example.BarclaysTest.Service.UserService;
import com.example.BarclaysTest.model.*;
import com.example.BarclaysTest.model.Requests.CreateBankAccountRequest;
import com.example.BarclaysTest.model.Requests.CreateTransactionRequest;
import com.example.BarclaysTest.model.Response.BankAccountResponse;
import com.example.BarclaysTest.model.Response.TransactionResponse;
import com.example.BarclaysTest.util.JwtUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankAccountEndpointTest {

    @InjectMocks
    BankAccountEndpoint bankAccountEndpoint;

    @Mock
    BankService bankService;

    @Mock
    UserService userService;

    @Mock
    User testUser;
    @Mock
    private JwtUtil jwtUtil;

    private final String authenticatedUserId = "user1";

    private static MockedStatic<SecurityContextHolder> mockedSecurityContext;

    @BeforeAll
    static void setupClass() {
        mockedSecurityContext = mockStatic(SecurityContextHolder.class);
    }


    @BeforeEach
    void setup() {

        MockitoAnnotations.openMocks(this);
        Address address = Address.builder()
                .addressLine1("address")
                .county("town")
                .postCode("postcode")
                .build();

        testUser = User.builder()
                .id("user1")
                .name("userName")
                .address(address)
                .phoneNumber("+19988")
                .email("test@test.com")
                .bankAccounts(new ArrayList<>())
                .build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("user1");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        mockedSecurityContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);

    }


    @Test
    void shouldCreateBankAccountSuccessfully() {

        CreateBankAccountRequest request = CreateBankAccountRequest.builder()
                .name("testAcc")
                .accountType(AccountType.PERSONAL)
                .build();

        BankAccount mockAccount = BankAccount.builder()
                .accountNumber("acc123")
                .name("My Account")
                .build();

        testUser.getBankAccounts().add(mockAccount);
        when(bankService.createBankAccount(request, authenticatedUserId)).thenReturn(mockAccount);

        ResponseEntity<BankAccountResponse> response = bankAccountEndpoint.createAccount(request);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("acc123", response.getBody().getAccountNumber());
        assertEquals("My Account", response.getBody().getName());
        assertEquals(1, testUser.getBankAccounts().size());
    }

    @Test
    void shouldFetchAccountByAccountNumberSuccessfully() {

        BankAccount bankAccount = BankAccount.builder()
                .accountNumber("01234567")
                .accountType(AccountType.PERSONAL)
                .build();

        when(bankService.fetchAccountByAccountNumber("01234567", authenticatedUserId))
                .thenReturn(bankAccount);

        ResponseEntity<BankAccountResponse> response =
                bankAccountEndpoint.fetchAccountByAccountNumber("01234567");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("01234567", response.getBody().getAccountNumber());
    }

    @Test
    void shouldThrowForbiddenExceptionWhenFetchingAccount() {

        when(bankService.fetchAccountByAccountNumber("01234567", authenticatedUserId))
                .thenThrow(new ForbiddenException("The user is not allowed to access the bank account details"));

        assertThrows(ForbiddenException.class, () ->
                bankAccountEndpoint.fetchAccountByAccountNumber("01234567")
        );
    }

    @Test
    void shouldCreateTransactionSuccessfully() {

        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .currency(Currency.GBP)
                .transactionType(TransactionType.DEPOSIT)
                .amount(100.0)
                .reference("Making a deposit")
                .build();

        Transaction transaction = Transaction.builder()
                .id("txn123")
                .amount(100.0)
                .build();

        when(bankService.createTransaction("01234567", request, authenticatedUserId))
                .thenReturn(transaction);

        ResponseEntity<TransactionResponse> response = bankAccountEndpoint.createTransaction("01234567", request);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("txn123", response.getBody().getId());
        assertEquals(100.0, response.getBody().getAmount());
    }

    @Test
    void shouldThrowUnprocessableEntityForInsufficientFunds() {

        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .currency(Currency.GBP)
                .transactionType(TransactionType.WITHDRAWAL)
                .amount(5000.0)
                .reference("Withdrawal")
                .build();

        when(bankService.createTransaction("01234567", request, authenticatedUserId))
                .thenThrow(new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Insufficient funds to process transaction"
                ));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                bankAccountEndpoint.createTransaction("01234567", request)
        );

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
        assertEquals("Insufficient funds to process transaction", exception.getReason());
    }
}