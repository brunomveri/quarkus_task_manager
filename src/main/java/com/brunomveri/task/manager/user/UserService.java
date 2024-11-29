package com.brunomveri.task.manager.user;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.ObjectNotFoundException;

import java.util.List;

@ApplicationScoped
public class UserService {

    public Uni<User> findById(long id) {
        return User.<User>findById(id)
                   .onItem()
                   .ifNull()
                   .failWith(() -> new ObjectNotFoundException(id, "User"));
    }

    public Uni<User> findByName(String name) {
        return User.find("name", name).firstResult();
    }

    public Uni<List<User>> list() {
        return User.listAll();
    }


}
