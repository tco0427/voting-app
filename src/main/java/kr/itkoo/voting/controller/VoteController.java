package kr.itkoo.voting.controller;


import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.itkoo.voting.data.ResponseData;
import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import kr.itkoo.voting.domain.entity.User;
import kr.itkoo.voting.domain.entity.Vote;
import kr.itkoo.voting.domain.entity.VoteParticipant;
import kr.itkoo.voting.service.UserService;
import kr.itkoo.voting.service.VoteService;
import kr.itkoo.voting.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.http.HttpRequest;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
@Slf4j
public class VoteController {

    private final UserService userService;
    private final VoteService voteService;
    private final JwtUtil jwtUtil;

    /**
     * 투표 정보 조회
     */
    @ApiOperation(value ="투표 정보 조회", notes ="id값으로 vote 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseData<VoteDto> getVoteById(@ApiParam(name = "투표 id", required = true, example = "1") @PathVariable("id") Long id){
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

    @Data
    @AllArgsConstructor
    static class VoteDto{
        private String title;
    }

    /**
     * 투표 생성
     */
    @ApiOperation(value = "투표 생성", notes = "새로운 vote를 생성 합니다.")
    @PostMapping("/new")
    public ResponseData<CreateVoteResponse> saveVote(@RequestBody @Valid CreateVoteRequest request){

        ResponseData<CreateVoteResponse> responseData =null;
        CreateVoteResponse createVoteResponse = null;
        try{
            Vote vote = new Vote();
            User user = userService.findById(request.getUserId()).get();

            vote.setUser(user);
            vote.setTitle(request.getTitle());
            vote.setCreatedAt((int) System.currentTimeMillis());

            Long id = voteService.join(vote);

            createVoteResponse = new CreateVoteResponse(id,vote.getTitle());
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
    static class CreateVoteRequest{
        private Long userId;
        private String title;
    }

    @Data
    @AllArgsConstructor
    static class CreateVoteResponse{
        private Long id;
        private String title;
    }

    /**
     * 투표 정보 수정
     */
    @ApiOperation(value = "투표 정보 수정", notes = "vote를 id로 조회 후 수정합니다.")
    @PutMapping("/{id}")
    public ResponseData<UpdateVoteResponse> updateVote(@PathVariable("id") Long id,
                                         @RequestBody @Valid UpdateVoteRequest request){
        ResponseData<UpdateVoteResponse> responseData =null;
        UpdateVoteResponse updateVoteResponse = null;
        try{
            voteService.update(id, request.getTitle(), (int) System.currentTimeMillis());
            Vote vote = voteService.findById(id).get();

            updateVoteResponse = new UpdateVoteResponse(vote.getId(),vote.getTitle(),vote.getUpdatedAt());
            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, updateVoteResponse);

        }catch(NoSuchElementException e){
            responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_VOTE, updateVoteResponse);
            log.error("Optional Error" + e.getMessage());
        }catch(Exception e){
            log.error(e.getMessage());
        }
        return responseData;
    }

    @Data
    static class UpdateVoteRequest{
        private String title;
    }

    @Data
    @AllArgsConstructor
    static class UpdateVoteResponse{
        private Long id;
        private String title;
        private Integer updatedAt;
    }

    /**
     * 투표 삭제
     */
    @ApiOperation(value = "투표 삭제", notes = "vote를 id값을 통해 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseData<DeleteVoteDto> deleteVote(@PathVariable("id") Long id){
        ResponseData<DeleteVoteDto> responseData = null;
        try{
            voteService.deleteById(id);
            responseData = new ResponseData<>(StatusCode.OK,ResponseMessage.SUCCESS,new DeleteVoteDto(id));
        }catch(Exception e){
            responseData = new ResponseData<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_VOTE,new DeleteVoteDto(id));
            log.error(e.getMessage());
        }
        return responseData;
    }

    @Data
    @AllArgsConstructor
    static class DeleteVoteDto{
        private Long id;
    }

    @PostMapping("/{voteId}/item/{voteItemId}")
    public ResponseData<VoteParticipant> participateVote(@PathVariable("voteId") Long voteId, @PathVariable("voteItemId") Long voteItemId, HttpServletRequest request){
        ResponseData<VoteParticipant> responseData = null;
        Long userId = null;
        String authenticationHeader = request.getHeader("Authorization");

        // 1. 헤더에서 토큰값이 있는지 체크 & userId 가져오기
        if(authenticationHeader.startsWith("Bearer")){
            String token = authenticationHeader.replace("Bearer", "");
            userId = jwtUtil.getUserIdByToken(token);
        }

        // 1-2. 없을경우 에러 처리
        if(userId == null){
            responseData = new ResponseData<>(StatusCode.UNAUTHORIZED, ResponseMessage.NOT_FOUND_USER, null);
            return responseData;
        }

        // 2. 투표 참여 정보 객체 저장
        VoteParticipant voteParticipant = new VoteParticipant(userId, voteId, voteItemId);

        // 3. 투표 참여 정보 테이블에 저장

        // 3-2. DB 에러시 처리(try-catch 또는 exception 처리)

        // 4. 응답 객체 생성 후 리턴
        responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, voteParticipant);

        return responseData;
   }
}
