package com.huaducdat.storemanagerclient.shared.session;

import com.huaducdat.storemanagerclient.auth.domain.dto.response.CurrentUserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionState {

    private String token;

    private CurrentUserResponse currentUser;

    public boolean hasToken() {

        return token != null && !token.isBlank();
    }
}
