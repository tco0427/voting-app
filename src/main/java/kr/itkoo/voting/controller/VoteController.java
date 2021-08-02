package kr.itkoo.voting.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.itkoo.voting.data.ResponseData;
import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import kr.itkoo.voting.domain.entity.User;
import kr.itkoo.voting.domain.entity.Vote;
import kr.itkoo.voting.domain.repository.UserRepository;
import kr.itkoo.voting.service.UserService;
import kr.itkoo.voting.service.VoteService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
@Slf4j
public class VoteController {

    private final UserService userService;
    private final VoteService voteService;

    @ApiOperation(value ="", notes ="id값으로 vote 정보 조회")
    @GetMapping("/{id}")
    public ResponseData<VoteDto> getVoteById(@ApiParam("투표 id") @PathVariable("id") Long id){
        log.info("getVoteById : " + id);
        ResponseData<VoteDto> responseData =null;

        VoteDto voteDto = null;
        try{
            Vote vote = voteService.findById(id).get();
            voteDto = new VoteDto(vote.getTitle());
            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, voteDto);
            log.info(responseData.toString());
        }catch(NoSuchElementException e){
            responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_VOTE, voteDto);
            log.error("Optional Error" + e.getMessage());
        }catch(Exception e){
            log.error(e.getMessage());
        }

        return responseData;
    }

    @ApiOperation(value = "", notes = "새로운 vote 생성")
    @PostMapping("/new")
    public ResponseData<CreateVoteResponse> saveVote(@RequestBody @Valid CreateVoteRequest request){

        ResponseData<CreateVoteResponse> responseData =null;
        CreateVoteResponse createVoteResponse = null;
        try{
            Vote vote = new Vote();
            User user = userService.findById(request.getUserId()).get();
            vote.setUser(user);
            vote.setTitle(request.getTitle());
            vote.setCreatedAt(request.getCreatedAt());

            Long id = voteService.join(vote);

            createVoteResponse = new CreateVoteResponse(id);
            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, createVoteResponse);
            log.info(responseData.toString());
        }catch(NoSuchElementException e){
            responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER,createVoteResponse);
            log.error("Optional Error" + e.getMessage());
        }catch(Exception e){
            log.error(e.getMessage());
        }

        return responseData;
    }

    @Data
    @AllArgsConstructor
    static class VoteDto{
        private String title;
    }

    @Data
    static class CreateVoteRequest{
        private Long userId;
        private String title;
        private Integer createdAt;
    }

    @Data
    static class CreateVoteResponse{
        private Long id;

        public CreateVoteResponse(Long id){
            this.id = id;
        }
    }

}
