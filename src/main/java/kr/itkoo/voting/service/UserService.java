package kr.itkoo.voting.service;

import kr.itkoo.voting.domain.entity.User;
import kr.itkoo.voting.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }
    public User save(User user){ return userRepository.save(user); }
}
