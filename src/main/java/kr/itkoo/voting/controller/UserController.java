package kr.itkoo.voting.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.itkoo.voting.data.ResponseData;
import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import kr.itkoo.voting.domain.dto.response.UserResponse;
import kr.itkoo.voting.domain.entity.User;
import kr.itkoo.voting.exception.NotFoundUserException;
import kr.itkoo.voting.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "", notes = "id값으로 회원 정보 조회")
    @GetMapping("/{id}")
    public ResponseData<UserResponse> getUserById(@ApiParam("회원 id") @PathVariable("id") Long id) {
        log.info("getUserById : " + id);

        ResponseData<UserResponse> responseData = null;
        UserResponse userResponse = null;

        try {
            User user = userService.findById(id);

            userResponse = new UserResponse(user.getId(), user.getName());
            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, userResponse);

            log.info(responseData.toString());
        } catch (NotFoundUserException e) {
            log.error("Optional Error" + e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return responseData;
    }
}
