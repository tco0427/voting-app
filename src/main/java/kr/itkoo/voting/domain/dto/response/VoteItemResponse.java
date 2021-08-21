package kr.itkoo.voting.domain.dto.response;

import kr.itkoo.voting.domain.entity.VoteItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VoteItemResponse {
    private List<VoteItem> list;
}
