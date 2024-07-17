package com.example.final_project_part3_springmvc.service;


import com.example.final_project_part3_springmvc.dto.expert.ExpertCriteriaDto;
import com.example.final_project_part3_springmvc.email.EmailSender;
import com.example.final_project_part3_springmvc.exception.*;
import com.example.final_project_part3_springmvc.model.ConfirmationToken;
import com.example.final_project_part3_springmvc.model.Expert;
import com.example.final_project_part3_springmvc.model.ExpertStatus;
import com.example.final_project_part3_springmvc.model.Role;
import com.example.final_project_part3_springmvc.repository.ExpertRepository;
import com.example.final_project_part3_springmvc.specifications.ExpertSpecifications;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpertService {
    private final ExpertRepository expertRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final PersonService personService;
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();


    public boolean validate(Expert entity) {

        Set<ConstraintViolation<Expert>> violations = validator.validate(entity);
        if (violations.isEmpty())
            return true;
        else {
            log.warn("Invalid user data found:");
            for (ConstraintViolation<Expert> violation : violations) {
                log.warn(violation.getMessage());
            }
            return false;
        }
    }

    public List<Expert> findAllExpertByStatus(ExpertStatus expertStatus) {
        return expertRepository.findAllByExpertStatus(expertStatus);
    }

    public Expert registerExpert(Expert expert) {
        expert.setExpertStatus(ExpertStatus.NEW);
        expert.setRole(Role.ROLE_EXPERT);
        expert.setRegisterDate(LocalDateTime.now());
        if (expertRepository.findByUsername(expert.getUsername()).isPresent())
            throw new DuplicateInformationException(String.format("the expert with %s is duplicate", expert.getUsername()));
        if (!validate(expert))
            throw new InvalidEntityException(String.format("the expert with %s have invalid variable", expert.getUsername()));
        expert.setPassword(passwordEncoder.encode(expert.getPassword()));
        Expert saveExpert = expertRepository.save(expert);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                expert
        );
        confirmationTokenService.saveConfirmationToken(
                confirmationToken);
//        TODO: SEND EMAIL

        String link = "http://localhost:8080/customer/confirm?token=" + token;
        emailSender.send(
                saveExpert.getEmail(),
                buildEmail(expert.getFirstname(), link));
        return saveExpert;
    }

    public Expert updateExpert(Expert expert) {
        findById(expert.getId());
        if (!validate(expert)) {
            throw new InvalidEntityException(String.format("the expert with %s have invalid variable", expert.getUsername()));
        }
        return expertRepository.save(expert);
    }

    public Expert findById(Long id) {
        return expertRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("the entity with %s not found", id)));
    }

    public Expert findByUsername(String username) {
        return expertRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(String.format("the entity with %s not found", username)));
    }


    public void deleteByUsername(String username) {
        expertRepository.delete(findByUsername(username));
    }

    @Transactional
    public void updatePassword(String username, String oldPassword, String newPassword, String finalNewPassword) {
        String passRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        if (expertRepository.findByUsername(username).isEmpty()) {
            throw new NotFoundException(String.format("the entity with %s not found", username));
        }
        if (!newPassword.matches(passRegex) || !oldPassword.matches(passRegex) || !finalNewPassword.matches(passRegex)) {
            throw new NotValidPasswordException("this password not valid");
        }
        if (!findByUsername(username).getPassword().equals(oldPassword) || !newPassword.equals(finalNewPassword))
            throw new NotValidPasswordException("this password not valid");
        expertRepository.updatePassword(finalNewPassword, username);
    }

    @Transactional
    public void confirmExpert(String username) {
        if (expertRepository.findByUsername(username).isEmpty())
            throw new NotFoundException(String.format("the entity with %s username not found", username));
        if (findByUsername(username).getExpertStatus().equals(ExpertStatus.CONFIRMED))
            throw new ConfirmationException(String.format("the entity with %s username was confirmed before your confirmation ", username));
        if (findByUsername(username).getExpertStatus().equals(ExpertStatus.NEW))
            throw new ConfirmationException(String.format("the entity with %s username must be WAITING_FOR_CONFIRMATION ", username));
        expertRepository.confirmExpert(ExpertStatus.CONFIRMED, username);
    }

    @Transactional
    public void updateScore(double score, String username) {
        if (expertRepository.findByUsername(username).isEmpty())
            throw new NotFoundException(String.format("the entity with %s username not found", username));
        if (score < 0)
            throw new InvalidEntityException(String.format("the Expert with %s have invalid variable", username));
        expertRepository.updateScore(score, username);
    }

    public List<Expert> expertSearch(ExpertCriteriaDto expertCriteriaDto) {
        Specification<Expert> specification = ExpertSpecifications.getExpertSpecification(expertCriteriaDto);
        return expertRepository.findAll(specification);
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new ConfirmationException("email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ConfirmationException("token expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        Expert byUsername = findByUsername(confirmationToken.getPerson().getUsername());
        byUsername.setExpertStatus(ExpertStatus.WAITING_FOR_CONFIRMATION);
        byUsername.setEnabled(true);
        updateExpert(byUsername);
        return "confirmed";
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}
