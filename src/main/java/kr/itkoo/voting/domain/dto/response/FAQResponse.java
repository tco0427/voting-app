package kr.itkoo.voting.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FAQResponse {

	private String title;
	private String content;
}
