package com.juniorstart.juniorstart.controller;


import com.juniorstart.juniorstart.model.CodeReviewSection;
import com.juniorstart.juniorstart.payload.PagedResponse;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.service.CodeReviewSectionService;
import com.juniorstart.juniorstart.util.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("user/codeReviewSection")
public class CodeReviewSectionController {

    private final CodeReviewSectionService codeReviewSectionService;


    @GetMapping()
    public PagedResponse<CodeReviewSection.CodeReviewSectionDto> getCodeReviewSection(List<String> listOfTags,
                                                                                      @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                                      @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return codeReviewSectionService.getCodeReviewSection(listOfTags, page, size);
    }


    @PreAuthorize("hasRole('user')")
    @PostMapping()
    public ResponseEntity<?> createCodeReviewSection(@RequestBody CodeReviewSection codeReviewSection, @CurrentUser UserPrincipal userPrincipal) {

        return codeReviewSectionService.createCodeReviewSection(codeReviewSection, userPrincipal);
    }

    @PreAuthorize("hasRole('user')")
    @PutMapping("{id}")
    public ResponseEntity<?> editCodeReviewSection(@PathVariable long id, @RequestBody CodeReviewSection codeReviewSection, @CurrentUser UserPrincipal userPrincipal) {

        return codeReviewSectionService.editCodeReviewSection(id, codeReviewSection, userPrincipal);
    }

    @PreAuthorize("hasRole('user')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCodeReviewSection(@PathVariable long id, @CurrentUser UserPrincipal userPrincipal) {

        return codeReviewSectionService.deleteCodeReviewSection(id, userPrincipal);
    }



}
