package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.model.CodeReviewSection;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.payload.PagedResponse;
import com.juniorstart.juniorstart.repository.CodeReviewRepository;
import com.juniorstart.juniorstart.repository.UserDao;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.util.ValidatePageUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.text.WordUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CodeReviewSectionService {

    private final UserDao userRepository;
    private final CodeReviewRepository codeReviewRepository;

    public PagedResponse<CodeReviewSection.CodeReviewSectionDto> getCodeReviewSection(HashSet<String> listOfTags, int page, int size) {

        List<String> convertedTechnology = listOfTags.stream().map(WordUtils::capitalize).collect(Collectors.toList());
        size = ValidatePageUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<CodeReviewSection> foundCode = codeReviewRepository.findByCodeReviewTagsIn(listOfTags, pageable);

        return getUserProfilePagedResponse(foundCode);
    }
    private PagedResponse<CodeReviewSection.CodeReviewSectionDto> getUserProfilePagedResponse(Page<CodeReviewSection> foundCode) {
        if(foundCode.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), foundCode.getNumber(), foundCode.getSize(), foundCode.getTotalElements(), foundCode.getTotalPages(), foundCode.isLast());
        }
        return new PagedResponse<>(foundCode.toList().stream().map(CodeReviewSection::toCodeReviewSectionDto).collect(Collectors.toList()), foundCode.getNumber(), foundCode.getSize(), foundCode.getTotalElements(), foundCode.getTotalPages(), foundCode.isLast());
    }


    public ResponseEntity<?> createCodeReviewSection(CodeReviewSection codeReviewSection, UserPrincipal userPrincipal) {

        User user = userRepository.findByPrivateIdNotOptional(userPrincipal.getId());
        codeReviewSection.setUser(user);

        codeReviewRepository.save(codeReviewSection);


        return ResponseEntity.ok(codeReviewRepository.save(codeReviewSection));
    }


    public ResponseEntity<?> editCodeReviewSection(Long id, CodeReviewSection codeReviewSection, UserPrincipal userPrincipal) {

        User user = userRepository.findByPrivateIdNotOptional(userPrincipal.getId());

        Optional<CodeReviewSection> find = codeReviewRepository.findById(id);

        if (find.isPresent() && find.get().getUser() == user) {
            codeReviewSection.setId(id);
            return ResponseEntity.ok(codeReviewRepository.save(codeReviewSection));
        } else {
            throw new BadRequestException("This Code Review Section ID does not exists");
        }
    }

    public ResponseEntity<?> deleteCodeReviewSection(Long id, UserPrincipal userPrincipal) {

        User user = userRepository.findByPrivateIdNotOptional(userPrincipal.getId());
        Optional<CodeReviewSection> find = codeReviewRepository.findById(id);

        if (find.isPresent() && find.get().getUser() == user && find.get().getId() == id) {
            codeReviewRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new BadRequestException("This Code Review Section ID does not exists");
        }
    }

}
