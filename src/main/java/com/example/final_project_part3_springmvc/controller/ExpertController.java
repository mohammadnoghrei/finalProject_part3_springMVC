package com.example.final_project_part3_springmvc.controller;

import com.example.final_project_part3_springmvc.dto.expert.ExpertCriteriaDto;
import com.example.final_project_part3_springmvc.dto.expert.ExpertSaveRequest;
import com.example.final_project_part3_springmvc.dto.expert.ExpertSaveResponse;
import com.example.final_project_part3_springmvc.mapper.ExpertMapper;
import com.example.final_project_part3_springmvc.model.Expert;
import com.example.final_project_part3_springmvc.model.ExpertStatus;
import com.example.final_project_part3_springmvc.service.ExpertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/expert")
public class ExpertController {
    private final ExpertService expertService;

    @PostMapping("/register-expert")
    public ResponseEntity<ExpertSaveResponse> registerExpert(@RequestBody ExpertSaveRequest request) {
        Expert mappedExpert = ExpertMapper.INSTANCE.expertSaveRequestToModel(request);
        Expert savedExpert = expertService.registerExpert(mappedExpert);
        return new ResponseEntity<>(ExpertMapper.INSTANCE.modelToExpertSaveResponse(savedExpert), HttpStatus.CREATED);
    }

    @GetMapping("/get-by-id-expert/{id}")
    public ResponseEntity<ExpertSaveResponse> getExpertById(@PathVariable Long id) {
        return new ResponseEntity<>(ExpertMapper.INSTANCE.modelToExpertSaveResponse(expertService.findById(id)), HttpStatus.OK);
    }

    @GetMapping("/get-by-username-expert/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ExpertSaveResponse getExpertByUsername(@PathVariable String username) {
        return ExpertMapper.INSTANCE.modelToExpertSaveResponse(expertService.findByUsername(username));
    }


    @DeleteMapping("/delete-by-expert-username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteUser(@PathVariable String username) {
        expertService.deleteByUsername(username);
        return "deleted customer with username:" + username;
    }

    @RequestMapping("/change-expert-password/{username}/{oldPassword}/{newPassword}/{finalNewPassword}")
    @ResponseStatus(HttpStatus.OK)
    public String changeExpertPassword(@PathVariable String username, @PathVariable String oldPassword, @PathVariable String newPassword, @PathVariable String finalNewPassword) {
        expertService.updatePassword(username, oldPassword, newPassword, finalNewPassword);
        return "change expert password with username: " + username;
    }

    @GetMapping("/find-all-expert-by-status/{status}")
    @ResponseStatus(HttpStatus.OK)
    public List<ExpertSaveResponse> findAllExpertByStatus(@PathVariable ExpertStatus status) {
        List<Expert> experts = expertService.findAllExpertByStatus(status);
        List<ExpertSaveResponse> expertSaveResponses= new ArrayList<>();
        experts.forEach(a->expertSaveResponses.add(ExpertMapper.INSTANCE.modelToExpertSaveResponse(a)));
        return expertSaveResponses;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("search-expert")
    public List<ExpertSaveResponse> searchExpert(@RequestBody ExpertCriteriaDto expertCriteriaDto){
        List<Expert> experts =expertService.expertSearch(expertCriteriaDto);
        return experts.stream()
                .map(ExpertMapper.INSTANCE::modelToExpertSaveResponse).collect(Collectors.toList());
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return expertService.confirmToken(token);
    }
}
