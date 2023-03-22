package com.linkyB.backend.user.application;


import com.linkyB.backend.user.domain.CustomUserDetails;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User findUser = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 가진 유저를 찾을 수 없습니다. -> " + email));

        if(findUser != null){
            CustomUserDetails userDetails = new CustomUserDetails(findUser);
            return  userDetails;
        }
        return null;
    }

}

