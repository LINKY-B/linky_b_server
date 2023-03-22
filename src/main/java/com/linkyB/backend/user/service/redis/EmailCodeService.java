package com.linkyB.backend.user.service.redis;

import com.linkyB.backend.user.domain.redis.EmailCode;
import com.linkyB.backend.user.dto.EmailConfirmCodeRequestDto;
import com.linkyB.backend.user.exception.UserNameAlreadyExistException;
import com.linkyB.backend.user.repository.UserRepository;
import com.linkyB.backend.user.repository.redis.EmailCodeRepository;
import com.linkyB.backend.user.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailCodeService {

    private final UserRepository userRepository;
    private final EmailCodeRepository emailCodeRepository;
    private final EmailService emailService;

    @Transactional
    public EmailCode addEmailCode(String userName, String email) {
        String authCode = createCode();
        EmailCode emailCode = EmailCode.builder()
                .userName(userName)
                .email(email)
                .code(authCode)
                .build();

        return emailCodeRepository.save(emailCode);
    }

    @Transactional
    public void sendCodeEmail(EmailConfirmCodeRequestDto confirmCodeRequestDto) throws MessagingException, UnsupportedEncodingException {
        final String userName = confirmCodeRequestDto.getUserName();
        final String email = confirmCodeRequestDto.getEmail();

        if (userRepository.findByUserEmail(email).isPresent()) {
            throw new UserNameAlreadyExistException();
        }

        EmailCode emailCode = addEmailCode(userName, email);
        emailService.sendEmail(emailCode);
    }

    private String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int) random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        return key.toString();
    }
}
