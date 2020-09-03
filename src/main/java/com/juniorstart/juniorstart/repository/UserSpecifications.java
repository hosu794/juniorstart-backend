package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> publicIdEquals(Long publicId) {
        return (user, cq, cb) -> cb.equal(user.get("publicId"), publicId);
    }

    public static Specification<User> nameEquals(String name) {
        return (user, cq, cb) -> cb.equal(user.get("name"), name);
    }

    public static Specification<User> ageEquals(Integer age) {
        return (user, cq, cb) -> cb.equal(user.get("age"), age);
    }

    public static Specification<User> ageNotEqual(Integer age) {
        return (user, cq, cb) -> cb.notEqual(user.get("age"), age);
    }

    public static Specification<User> ageGraterThan(Integer age) {
        return (user, cq, cb) -> cb.greaterThan(user.get("age"), age);
    }

    public static Specification<User> ageGraterEqualThan(Integer age) {
        return (user, cq, cb) -> cb.greaterThanOrEqualTo(user.get("age"), age);
    }

    public static Specification<User> ageLowerThan(Integer age) {
        return (user, cq, cb) -> cb.lessThan(user.get("age"), age);
    }

    public static Specification<User> ageLowerEqualThan(Integer age) {
        return (user, cq, cb) -> cb.lessThanOrEqualTo(user.get("age"), age);
    }

    public static Specification<User> ageBetween(Integer from, Integer to) {
        return (user, cq, cb) -> cb.between(user.get("age"), from, to);
    }

    public static Specification<User> hiddenFromSearch(boolean show) {
        return (user, cq, cb) -> cb.equal(user.get("hiddenFromSearch"), show);
    }

    public static Specification<User> emailEquals(String email) {
        return (user, cq, cb) -> cb.equal(user.get("email"), email);
    }
}
