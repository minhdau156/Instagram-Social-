package com.instagram.domain.port.in;

import com.instagram.domain.model.User;

public interface RegisterUserUseCase {
    User register(Command command);

    record Command(String username, String email, String password, String fullName) {
    }
}
