package com.instagram.domain.port.in;

import com.instagram.domain.model.AuthResult;

public interface LoginUseCase {
    AuthResult login(Command command);

    record Command(String identifier, String password) {
    }
}
