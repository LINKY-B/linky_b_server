package com.linkyB.backend.user.service.redis;

import com.linkyB.backend.common.exception.LinkyBusinessException;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.domain.redis.EmailCode;
import com.linkyB.backend.user.dto.EmailSendConfirmCodeRequestDto;
import com.linkyB.backend.user.exception.EmailAlreadyExistException;
import com.linkyB.backend.user.exception.UserNotFoundException;
import com.linkyB.backend.user.repository.UserRepository;
import com.linkyB.backend.user.repository.redis.EmailCodeRepository;
import com.linkyB.backend.user.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import static com.linkyB.backend.common.exception.ErrorCode.EMAIL_NOT_CONFIRMED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailCodeService {

    private final UserRepository userRepository;
    private final EmailCodeRepository emailCodeRepository;
    private final EmailService emailService;

    public EmailCode getEmailCode(String email, String userNickName) {
        return emailCodeRepository
                .findByEmailAndUserNickName(email, userNickName)
                .orElseThrow(() -> new LinkyBusinessException(EMAIL_NOT_CONFIRMED));
    }

    @Transactional
    public EmailCode addEmailCode(String nickName, String email) {
        String authCode = createCode();
        EmailCode emailCode = EmailCode.builder()
                .userNickName(nickName)
                .email(email)
                .code(authCode)
                .build();

        return emailCodeRepository.save(emailCode);
    }

    @Transactional
    public void sendCodeEmail(EmailSendConfirmCodeRequestDto confirmCodeRequestDto) throws MessagingException, UnsupportedEncodingException {
        final String userNickName = confirmCodeRequestDto.getUserNickName();
        final String email = confirmCodeRequestDto.getEmail();

        if (userRepository.findByUserEmail(email).isPresent()) {
            throw new EmailAlreadyExistException();
        }

        EmailCode emailCode = addEmailCode(userNickName, email);
        emailService.sendEmail(emailCode);
    }

    @Transactional
    public void sendCodeEmail(String email) throws MessagingException, UnsupportedEncodingException {
        User user = userRepository.findByUserEmail(email).orElseThrow(UserNotFoundException::new);
        final String nickName = user.getUserNickName();

        removeCode(email, nickName); // for case with duplicated code due to signup

        EmailCode emailCode = addEmailCode(nickName, email);
        emailService.sendEmail(emailCode);
    }

    @Transactional
    public void removeCode(String email, String nickName) {
        List<EmailCode> codes = emailCodeRepository.findAllByEmailAndUserNickName(email, nickName);
        emailCodeRepository.deleteAll(codes);
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
