package kr.itkoo.voting.controller;

import io.swagger.annotations.ApiOperation;
import kr.itkoo.voting.data.ResponseData;
import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import kr.itkoo.voting.domain.dto.response.*;
import kr.itkoo.voting.domain.entity.User;
import kr.itkoo.voting.domain.entity.Vote;
import kr.itkoo.voting.domain.entity.VoteParticipant;
import kr.itkoo.voting.exception.NotFoundUserException;
import kr.itkoo.voting.service.UserService;
import kr.itkoo.voting.service.VoteItemService;
import kr.itkoo.voting.service.VoteParticipantService;
import kr.itkoo.voting.service.VoteService;
import kr.itkoo.voting.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/voteParticipant")
@RequiredArgsConstructor
@Slf4j
public class VoteParticipantController {

    private final VoteParticipantService voteParticipantService;
    private final UserService userService;
    private final VoteService voteService;
    private final VoteItemService voteItemService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{voteId}/item/{voteItemId}")
    public ResponseData<VoteParticipantResponse> participateVote(
            @PathVariable("voteId") Long voteId, @PathVariable("voteItemId") Long voteItemId,
            HttpServletRequest request) {
        ResponseData<VoteParticipantResponse> responseData;
        Long userId = null;
        String authenticationHeader = request.getHeader("Authorization");

        // 1. 헤더에서 토큰값이 있는지 체크 & userId 가져오기
        if (authenticationHeader != null && authenticationHeader.startsWith("Bearer")) {
            String token = authenticationHeader.replace("Bearer", "");
            userId = jwtUtil.getUserIdByToken(token);
        }

        // 1-2. 없을경우 에러 처리
        if (userId == null) {
            responseData = new ResponseData<>(StatusCode.UNAUTHORIZED,
                    ResponseMessage.NOT_FOUND_USER, null);
            return responseData;
        }

        User user = userService.findById(userId);

        // 2. 투표 참여 정보 객체 저장
        VoteParticipant voteParticipant = new VoteParticipant(user, voteId, voteItemId);

        // 3. 투표 참여 정보 테이블에 저장
        try {
            Long voteParticipantId = voteParticipantService.save(voteParticipant);
            // 3-1. 응답 객체 생성 후 리턴
            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS,
                    new VoteParticipantResponse(voteParticipantId, voteParticipant.getVoteId()));
        } catch (Exception e) {
            // 3-2. DB 에러시 처리(try-catch 또는 exception 처리)
            responseData = new ResponseData<>(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.FAILED_TO_SAVE_VOTE_PARTICIPANT, null);
            log.error(e.toString());
        }
        return responseData;
    }

    /**
     * 내가 참여한 투표 목록 조회인데, 페이징 처리로 구현하였습니다.
     * 현재는 페이지를 넘겨가며 보이는 것이 아니라 오직 최신순 10개만을 조회하는 것이지만
     * 향후에 확장성을 고려하여 그대로 유지해두겠습니다.
     * @param page
     * @param httpServletRequest
     * @return
     */
    @ApiOperation(value = "내가 참여한 투표 목록 조회", notes = "내가 참여한 투표 목록 조회 with Paging")
    @GetMapping("/{page}")
    public ResponseData<MyVoteParticipantListResponse> getVoteListByMember(@PathVariable("page") Integer page,
                                                                         HttpServletRequest httpServletRequest) {
        ResponseData<MyVoteParticipantListResponse> responseData = null;
        MyVoteParticipantListResponse myVoteParticipantListResponse;

        try {
            String token = jwtUtil.getTokenByHeader(httpServletRequest);

            jwtUtil.isValidToken(token);

            Long userId = jwtUtil.getUserIdByToken(token);

            //현재는 오직 최신순 10개만을 조회하는 것이므로 page에 임의로 0의 값을 주도록 하겠습니다.
            //향후에는 parameter로 넘어온 page를 넘겨주면 됩니다.
            PageRequest pageRequest = PageRequest.of(0,10,Sort.by(Sort.Direction.DESC, "createdDate"));

            List<VoteParticipant> content = voteParticipantService.getVoteParticipantByUserId(userId, pageRequest);

            List<MyVoteParticipantResponse> myVoteParticipantList= new ArrayList<>();

            for (VoteParticipant voteParticipant : content) {
                Long voteId = voteParticipant.getVoteId();
                Long voteItemId = voteParticipant.getVoteItemId();

                String title = voteService.findById(voteId).getTitle();
                String name = voteItemService.findById(voteItemId).getName();

                MyVoteParticipantResponse myVoteParticipantResponse = new MyVoteParticipantResponse(voteId, voteItemId, title, name);

                myVoteParticipantList.add(myVoteParticipantResponse);
            }

            myVoteParticipantListResponse = new MyVoteParticipantListResponse(myVoteParticipantList);

            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, myVoteParticipantListResponse);
        } catch (NotFoundUserException e) {
            log.error(e.getMessage());
            responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, null);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_MY_VOTE, null);
        }
        return responseData;
    }

    @Data
    @AllArgsConstructor
    static class MyVoteParticipantListResponse {
        private List<MyVoteParticipantResponse> myVoteParticipantResponses;
    }

    @Data
    @AllArgsConstructor
    static class MyVoteParticipantResponse {
        private Long voteId;
        private Long voteItemId;
        private String voteTitle;
        private String voteItemName;
    }
}
