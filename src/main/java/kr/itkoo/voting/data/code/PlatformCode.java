package kr.itkoo.voting.data.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlatformCode {

    GENERAL("01"), KAKAO("02");

    private final String value;
}