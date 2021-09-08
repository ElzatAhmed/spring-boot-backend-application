package team.peiYangCoders.PeiYangResourceManagement.service.implementation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.UserToken;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Resource;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.ItemRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.ResourceRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserTokenRepository;
import team.peiYangCoders.PeiYangResourceManagement.service.ResourceService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ResourceServiceImplTest {

    @Mock
    private ResourceRepository resourceRepo;
    @Mock
    private  UserTokenRepository userTokenRepo;
    @Mock
    private  UserRepository userRepo;
    @Mock
    private  ItemRepository itemRepo;

    private ResourceService underTest;

    private Resource r1;

    private Resource r2;

    private User ordinary;

    private User admin;

    private UserToken uToken1;

    private UserToken uToken2;

    @BeforeEach
    void setUp() {
        underTest = new ResourceServiceImpl(resourceRepo, userTokenRepo, userRepo, itemRepo);

        ordinary = new User(
                null, "17627669320", "3019244086", "1208915986",
                "1208915986", "Elzat", "123456", "https://github.com",
                false, "ordinary"
        );
        admin = new User(
                null, "18799081038", "3019244086", "1208915986",
                "1208915986", "Elzat", "456789", "https://github.com",
                true, "ordinary"
        );

        uToken1 = new UserToken(ordinary.getPhone(), ordinary.getUserName(), "123456");
        uToken2 = new UserToken(admin.getPhone(), admin.getUserName(), "456789");

        r1 = new Resource(null, "r1", "this is r1",
                "book", false, false, false, "", ordinary.getPhone());
        r2 = new Resource(null, "r2", "this is r2",
                "book", false, false, false, "", ordinary.getPhone());
    }

    @Test
    void canPost(){
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(ordinary));
        given(userTokenRepo.findByUserPhone(anyString())).willReturn(Optional.of(uToken1));
        // when
        Response r = underTest.post(r1, ordinary.getPhone(), uToken1.getToken());
        ArgumentCaptor<Resource> resource = ArgumentCaptor.forClass(Resource.class);
        // then
        verify(resourceRepo).save(resource.capture());
        assertThat(resource.getValue().getOwnerPhone()).isEqualTo(ordinary.getPhone());
        assertThat(r.getCode()).isEqualTo(Response.SUCCESS_CODE);
    }
}