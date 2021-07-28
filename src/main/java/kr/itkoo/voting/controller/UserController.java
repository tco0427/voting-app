package kr.itkoo.voting.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.itkoo.voting.data.ResponseData;
import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import kr.itkoo.voting.domain.entity.User;
import kr.itkoo.voting.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "", notes = "id값으로 회원 정보 조회")
    @GetMapping("/{id}")
    public ResponseData<User> getUserById(@ApiParam("회원 id") @PathVariable("id") Long id){
        log.info("getUserById : " + id);
        ResponseData<User> responseData = null;
        try {
            User user = userService.findById(id).get(); //비어있는 경우 NoSuchElementException
            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, user);
            log.info(responseData.toString());
        }catch(NoSuchElementException e){
            log.error("Optional Error" + e.getMessage());
        }catch(Exception e){
            log.error(e.getMessage());
        }

        return responseData;
    }
}
