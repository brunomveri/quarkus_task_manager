package com.brunomveri.task.manager.user;

import com.brunomveri.task.manager.project.Project;
import com.brunomveri.task.manager.task.Task;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
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

    @Transactional
    public Uni<User> create(User user) {
        user.password = BcryptUtil.bcryptHash(user.password);
        return user.persistAndFlush();
    }

    @Transactional
    public Uni<User> update(User user) {
        return findById(user.id)
                .chain(u -> User.getSession())
//                .chain(User::getSession)
                .chain(s -> s.merge(user));
    }

    @Transactional
    public Uni<Void> delete(long id) {
        return findById(id)
                .chain(u -> Uni.combine().all().unis(
                        Task.delete("user.id", u.id),
                        Project.delete("user.id", u.id)
                               ).asTuple()
                               .chain(t -> u.delete()));
    }

    public Uni<User> getCurrentUser() {
        // TODO: replace implementation once security is added to the project
        return User.find("order by ID").firstResult();
    }
}
