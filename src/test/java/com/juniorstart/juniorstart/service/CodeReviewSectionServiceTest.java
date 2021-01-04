package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.CodeReviewSection;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.payload.PagedResponse;
import com.juniorstart.juniorstart.repository.CodeReviewSectionRepository;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CodeReviewSectionServiceTest {

    @Mock
    UserDao userRepository;
    @Mock
    CodeReviewSectionRepository codeReviewSectionRepository;
    @InjectMocks
    CodeReviewSectionService codeReviewSectionService;

    User user;
    CodeReviewSection codeReviewSection;
    UserPrincipal userPrincipal;
    CodeReviewSection.CodeReviewSectionDto codeReviewSectionDto;

    private int page = 0;
    private int size = 10;

    UUID uuid = UUID.randomUUID();

    List<String> tags = new ArrayList<>();
    Map<String,String> comments = new HashMap();

    private List<CodeReviewSection> listOfCodeReviewSection = new ArrayList<>();
    private List<CodeReviewSection.CodeReviewSectionDto> codeReviewSectionOutput = new ArrayList<>();


    @BeforeEach
    public void setUp() {
        tags.add("Java");
        comments.put("User1", "Comment1");

        user = User.builder()
                .privateId(uuid)
                .name("Test")
                .email("test2@test.com")
                .emailVerified(true)
                .password("Test%123")
                .provider(AuthProvider.google).build();

        userPrincipal = UserPrincipal.create(user);

        codeReviewSection = CodeReviewSection.builder()
                .code("java code")
                .codeReviewTags(tags)
                .rate((byte)6)
                .comments(comments)
                .numberOfRatings(2)
                .user(user).build();

        //codeReviewSectionDto = CodeReviewSection.CodeReviewSectionDto.builder()
        listOfCodeReviewSection = new ArrayList<>();
        listOfCodeReviewSection.add(codeReviewSection);

        codeReviewSectionOutput = new ArrayList<>();

    }

    @Disabled
    @Test
    public void should_getCodeReviewSection() {

        PagedResponse<CodeReviewSection.CodeReviewSectionDto> pagedResponse = new PagedResponse(codeReviewSectionOutput,page,size,1,1,true);
        codeReviewSectionOutput = listOfCodeReviewSection.stream().map(CodeReviewSection::toCodeReviewSectionDto).collect(Collectors.toList());

        //pagedResponse.setContent(codeReviewSectionOutput,page,size,1,1,true);

         Pageable pageable = PageRequest.of(page, size);
         PageImpl<CodeReviewSection> pageContain = new PageImpl<>(listOfCodeReviewSection, pageable, 1);

        Page<CodeReviewSection> companies = Mockito.mock(Page.class);
        Page<CodeReviewSection> tkl = new PageImpl(listOfCodeReviewSection);
        //codeReviewSection.toCodeReviewSectionDto()
        //codeReviewSectionOutput.add(codeReviewSection.toList().stream().map(CodeReviewSection::toCodeReviewSectionDto).collect(Collectors.toList())

        when(codeReviewSectionRepository.findByCodeReviewTagsIn(tags,pageable)).thenReturn(companies);

        when(codeReviewSectionService.getCodeReviewSection(tags,page,size)).thenReturn(pagedResponse);


        assertEquals(codeReviewSectionService.getCodeReviewSection(tags,page,size), pagedResponse);

    }

    @Test
    public void should_createCodeReviewSection() {

        when(userRepository.findByPrivateId(user.getPrivateId())).thenReturn(Optional.of(user));
        when(codeReviewSectionRepository.save(codeReviewSection)).thenReturn(codeReviewSection);

        codeReviewSectionService.createCodeReviewSection(codeReviewSection, userPrincipal);

        verify(codeReviewSectionRepository, times(1)).save(codeReviewSection);
        verify(userRepository, times(1)).findByPrivateId(user.getPrivateId());

        assertEquals(codeReviewSectionService.createCodeReviewSection(codeReviewSection, userPrincipal).getStatusCode(), HttpStatus.OK);
        assertEquals(codeReviewSectionService.createCodeReviewSection(codeReviewSection, userPrincipal).getBody(), codeReviewSection);
    }

    @Test
    public void should_updateCodeReviewSection() {

        when(userRepository.findByPrivateId(user.getPrivateId())).thenReturn(Optional.of(user));
        when(codeReviewSectionRepository.save(codeReviewSection)).thenReturn(codeReviewSection);
        when(codeReviewSectionRepository.findById(codeReviewSection.getId())).thenReturn(Optional.ofNullable(codeReviewSection));

        codeReviewSection.setCode("Python");
        codeReviewSectionService.editCodeReviewSection(codeReviewSection.getId(),codeReviewSection, userPrincipal);

        verify(userRepository, times(1)).findByPrivateId(user.getPrivateId());
        verify(codeReviewSectionRepository, times(1)).save(codeReviewSection);

        assertEquals(codeReviewSectionService.createCodeReviewSection(codeReviewSection, userPrincipal).getStatusCode(), HttpStatus.OK);
        assertEquals(codeReviewSectionService.createCodeReviewSection(codeReviewSection, userPrincipal).getBody(), codeReviewSection);
    }

    @Test
    public void should_deleteCodeReviewSection() {

        when(userRepository.findByPrivateId(user.getPrivateId())).thenReturn(Optional.of(user));
        when(codeReviewSectionRepository.findById(codeReviewSection.getId())).thenReturn(Optional.ofNullable(codeReviewSection));

        codeReviewSectionService.deleteCodeReviewSection(codeReviewSection.getId(), userPrincipal);

        verify(codeReviewSectionRepository, times(1)).deleteById(codeReviewSection.getId());
        verify(userRepository, times(1)).findByPrivateId(user.getPrivateId());

        assertEquals(codeReviewSectionService.deleteCodeReviewSection(codeReviewSection.getId(), userPrincipal).getStatusCode(), HttpStatus.NO_CONTENT);
    }





}
