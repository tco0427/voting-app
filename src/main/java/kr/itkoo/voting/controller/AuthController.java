package kr.itkoo.voting.controller;

import kr.itkoo.voting.data.ResponseData;
import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import kr.itkoo.voting.domain.dto.request.SignUpRequest;
import kr.itkoo.voting.domain.dto.response.SignUpResponse;
import kr.itkoo.voting.domain.entity.User;
import kr.itkoo.voting.service.UserService;
import kr.itkoo.voting.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseData<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest){
        log.info(signUpRequest.toString());
        ResponseData<SignUpResponse> responseData = null;
        try {
            // 1. user 정보 저장
            User user = signUpRequest.toUserEntity();
            User savedUser = userService.save(user);

            // 2. 토큰 발급
            String token = jwtUtil.generateToken(savedUser.getId(), savedUser.getPlatformId());
            SignUpResponse jwtResponse = new SignUpResponse(savedUser.getId(), token);
            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, jwtResponse);
            log.info(responseData.toString());
        } catch(Exception e){
            log.error(e.getMessage());
        }

        return responseData;
    }
}
