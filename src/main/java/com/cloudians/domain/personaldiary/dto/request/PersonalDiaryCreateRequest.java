package com.cloudians.domain.personaldiary.dto.request;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class PersonalDiaryCreateRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 50, message = "제목은 최대 50자까지 입력 가능합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    private PersonalDiaryCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PersonalDiary toEntity(User user, PersonalDiaryEmotion emotions, String photoUrl) {
        return PersonalDiary.builder()
                .user(user)
                .emotion(emotions)
                .title(title.trim())
                .content(content.trim())
                .photoUrl(photoUrl)
                .date(emotions.getDate())
                .build();
    }
}