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
import kr.itkoo.voting.service.VoteParticipantService;
import kr.itkoo.voting.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/voteParticipant")
@RequiredArgsConstructor
@Slf4j
public class VoteParticipantController {
    private final VoteParticipantService voteParticipantService;
    private final JwtUtil jwtUtil;

    @ApiOperation(value = "내가 참여한 투표 목록 조회", notes = "내가 참여한 투표 목록 조회 with Paging")
    @GetMapping("/myParticipant/{page}")
    public ResponseData<VoteParticipantListResponse> getVoteListByMember(@PathVariable("page") Integer page, HttpServletRequest httpServletRequest) {
        ResponseData<VoteParticipantListResponse> responseData = null;
        VoteParticipantListResponse voteParticipantListResponse = null;

        try {
            String token = jwtUtil.getTokenByHeader(httpServletRequest);

            jwtUtil.isValidToken(token);

            Long userId = jwtUtil.getUserIdByToken(token);

            PageRequest pageRequest = PageRequest.of(page,10,Sort.by(Sort.Direction.ASC, "createdDate"));

            Slice<VoteParticipant> slice = voteParticipantService.getVoteParticipantByUserId(userId, pageRequest);

            List<VoteParticipant> content = slice.getContent();

            List<VoteParticipantResponse> voteParticipantResponseList = new ArrayList<>();

            for (VoteParticipant voteParticipant : content) {
                Long id = voteParticipant.getId();
                User user = voteParticipant.getUser();
                Vote vote = voteParticipant.getVote();

                UserResponse userResponse = new UserResponse(user.getId(), user.getName());

                VoteResponse voteResponse = new VoteResponse(vote.getId(), vote.getTitle());

                VoteParticipantResponse voteParticipantResponse = new VoteParticipantResponse(id, userResponse, voteResponse);

                voteParticipantResponseList.add(voteParticipantResponse);
            }

            voteParticipantListResponse = new VoteParticipantListResponse(voteParticipantResponseList);
            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, voteParticipantListResponse);
        } catch (NotFoundUserException e) {
            log.error(e.getMessage());
            responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER,
                    null);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return responseData;
    }

    @Data
    @AllArgsConstructor
    static class VoteParticipantListResponse{
        List<VoteParticipantResponse> voteParticipantResponses;
    }

    @Data
    @AllArgsConstructor
    static class VoteParticipantResponse{
        private Long id;
        private UserResponse userResponse;
        private VoteResponse voteResponse;
    }
}
