package com.ecommerce.api.factory;

import com.ecommerce.api.model.User;

public final class UserTestFactory {

    private UserTestFactory() {}

    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("João Miguel");
        user.setEmail("joao@email.com");
        user.setPassword("123456");

        return user;
    }

    public static User createUser(Long id, String name, String email, String password) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }
}