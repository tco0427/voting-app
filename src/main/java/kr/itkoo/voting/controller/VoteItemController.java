package kr.itkoo.voting.controller;

import java.util.Optional;
import kr.itkoo.voting.data.ResponseData;
import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import kr.itkoo.voting.domain.dto.response.CreateVoteItemResponse;
import kr.itkoo.voting.domain.entity.Vote;
import kr.itkoo.voting.domain.entity.VoteItem;
import kr.itkoo.voting.exception.NotFoundUserException;
import kr.itkoo.voting.service.VoteItemService;
import kr.itkoo.voting.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/vote/item")
public class VoteItemController {

    private final VoteService voteService;
    private final VoteItemService voteItemService;

    @PostMapping("/{voteId}")
    public ResponseData<CreateVoteItemResponse> saveVoteItem(@PathVariable("voteId") Long voteId,
        @RequestParam("name") String name) {
        ResponseData<CreateVoteItemResponse> responseData = null;

        try {
            // 1. 투표 아이디 유효성 체크
            // 1-2. 없을겨우 에러 처리
            Optional<Vote> vote = voteService.findById(voteId);
            if (vote.isEmpty()) {
                throw new NotFoundUserException();
            }

            // 2. 투표 아이템 저장
            VoteItem voteItem = new VoteItem(vote.get(), name);
            Long voteItemId = voteItemService.save(voteItem);
            CreateVoteItemResponse voteItemResponse = new CreateVoteItemResponse(voteItemId, name);

            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS,
                voteItemResponse);
        } catch (NotFoundUserException ne) {
            responseData = new ResponseData<>(ne.getCode(), ne.getMessage(), null);
            return responseData;
        } catch (Exception e) {
            responseData = new ResponseData<>(StatusCode.INTERNAL_SERVER_ERROR,
                ResponseMessage.NOT_FOUND_USER, null);
            return responseData;
        }

        return responseData;
    }
}
