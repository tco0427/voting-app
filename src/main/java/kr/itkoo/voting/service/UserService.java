package kr.itkoo.voting.service;

import kr.itkoo.voting.domain.entity.User;
import kr.itkoo.voting.domain.repository.UserRepository;
import kr.itkoo.voting.exception.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(NotFoundUserException::new);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }
}
