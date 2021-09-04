package kr.itkoo.voting.service;

import java.util.List;
import kr.itkoo.voting.domain.entity.FAQ;
import kr.itkoo.voting.domain.repository.FAQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FAQService {

	private final FAQRepository faqRepository;

	public List<FAQ> findAll() {
		return faqRepository.findAllByIsVisible(true);
	}
}
