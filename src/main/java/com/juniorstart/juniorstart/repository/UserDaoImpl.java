package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.QUser;
import com.juniorstart.juniorstart.model.User;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public User save(final User var) {
        em.persist(var);
        return var;
    }

    @Transactional
    @Override
    public Iterable<User> saveAll(final Iterable<User> var) {
        var.forEach(em::persist);
        return var;
    }

    @Override
    public Optional<User> findById(UUID id) {
        JPAQuery<User> query = new JPAQuery<>(em);
        QUser user = QUser.user;

        return Optional.ofNullable(query
                .from(user)
                .where(user.privateId.eq(id))
                .fetchOne()
        );
    }

    @Override
    public Iterable<User> findAll() {
        JPAQuery<User> query = new JPAQuery<>(em);
        QUser user = QUser.user;

        return query.from(user).fetch();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        JPAQuery<User> query = new JPAQuery<>(em);
        QUser user = QUser.user;

        return Optional.ofNullable(query
                .from(user)
                .where(user.email.eq(email))
                .fetchOne()
        );
    }

    @Override
    public Optional<User> findByPublicId(Long publicId) {
        JPAQuery<User> query = new JPAQuery<>(em);
        QUser user = QUser.user;

        return Optional.ofNullable(query
                .from(user)
                .where(user.publicId.eq(publicId))
                .fetchOne()
        );
    }
}
