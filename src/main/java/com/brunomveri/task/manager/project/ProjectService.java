package com.brunomveri.task.manager.project;

import com.brunomveri.task.manager.user.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProjectService {

    private final UserService userService;
    @Inject
    public ProjectService(UserService userService) {
        this.userService = userService;
    }
}
