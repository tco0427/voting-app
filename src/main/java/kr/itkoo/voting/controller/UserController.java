package kr.itkoo.voting.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.itkoo.voting.data.ResponseData;
import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import kr.itkoo.voting.domain.entity.User;
import kr.itkoo.voting.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @ApiOperation(value = "", notes = "id값으로 회원 정보 조회")
    @GetMapping("/{id}")
    public ResponseData<User> getUserById(@ApiParam("회원 id") @PathVariable("id") Long id){
        log.info("getUserById : " + id);
        ResponseData responseData = null;
        try {
            Optional<User> user = userService.findById(id);
            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, user);
            log.info(responseData.toString());

        } catch (Exception e){
            log.error(e.getMessage());
        }

        return responseData;
    }
}
