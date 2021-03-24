package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.repositories.IpAddressRepository;
import nl.tudelft.oopp.app.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class UserTestsMock {

    @InjectMocks
    private UserService userService;

    @Mock
    private IpAddressRepository ipAddressRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void userIsNotBanned() {
        when(ipAddressRepository.checkForIpBan("2020", 3))
                .thenReturn(Stream.of(Boolean.TRUE, Boolean.TRUE)
                        .collect(Collectors.toList()));
        Assertions.assertFalse(userService.isUserBanned("2020", 3L).contains(false));
    }

    @Test
    public void userIsBanned() {
        when(ipAddressRepository.checkForIpBan("2020", 3))
                .thenReturn(Stream.of(Boolean.TRUE, Boolean.FALSE)
                        .collect(Collectors.toList()));
        Assertions.assertTrue(userService.isUserBanned("2020", 3L).contains(false));
    }



}
