package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.Technology;
import com.juniorstart.juniorstart.payload.*;
import com.juniorstart.juniorstart.security.CurrentUser;
import com.juniorstart.juniorstart.security.UserPrincipal;
import com.juniorstart.juniorstart.service.TechnologyService;
import com.juniorstart.juniorstart.util.AppConstants;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/technology")
public class TechnologyController {


    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    private final TechnologyService technologyService;

    @PostMapping
    public Technology createTechnology(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody TechnologyRequest technologyRequest) {
        return technologyService.createTechnology(currentUser, technologyRequest);
    }

    @GetMapping
    public PagedResponse<TechnologyResponse> findAll(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                     @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return technologyService.findAll(page, size);
    }

    @GetMapping("/{technologyId}")
    public TechnologyResponse findById(@PathVariable Long technologyId) {
        return technologyService.findById(technologyId);
    }

    @PutMapping
    public TechnologyResponse updateTechnology(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody UpdateTechnologyRequest updateTechnologyRequest) {
        return technologyService.updateTechnology(currentUser, updateTechnologyRequest);
    }

    @DeleteMapping("/{technologyId}")
    public ResponseEntity<?> deleteTechnology(@CurrentUser UserPrincipal currentUser, @PathVariable Long technologyId) {
        return technologyService.deleteTechnology(currentUser, technologyId);
    }

    @PostMapping("/project")
    public ResponseEntity<?> addTechnologyToProject(@CurrentUser UserPrincipal currentUser, @RequestBody @Valid TechnologyIdentityRequest technologyIdentityRequest) {
      return technologyService.addToProject(technologyIdentityRequest.getProjectId(), technologyIdentityRequest.getTechnologyId(), currentUser);
    }

    @DeleteMapping("/project")
    public ResponseEntity<?> deleteTechnologyFromProject(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody TechnologyIdentityRequest technologyIdentityRequest) {
        return technologyService.deleteFromProject(technologyIdentityRequest.getProjectId(), technologyIdentityRequest.getTechnologyId(), currentUser);
    }


}
