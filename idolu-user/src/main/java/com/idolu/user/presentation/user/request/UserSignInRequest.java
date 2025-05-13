package com.idolu.user.presentation.user.request;

import com.idolu.user.application.user.command.UserSignInCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class UserSignInRequest {

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 4, max = 16, message = "비밀번호는 4자 이상, 16자 이하로 입력해주세요.")
    private String password;

    public UserSignInCommand toCommand() {
        return UserSignInCommand.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }
}
