package com.example.jpaquerydemo;

import com.example.jpaquerydemo.entity.*;
import com.example.jpaquerydemo.repository.DeptRepository;
import com.example.jpaquerydemo.repository.RoleRepository;
import com.example.jpaquerydemo.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Set;

@Slf4j
@SpringBootTest
class JpaQueryDemoApplicationTests {

    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void contextLoads() {
        Dept dept = this.deptRepository.saveAndFlush(new Dept().setName("d1"));
        Role role = this.roleRepository.saveAndFlush(new Role().setName("role1"));
        User xiaoshitou = this.userRepository.saveAndFlush(new User().setName("xiaoshitou").setDept(dept).setRole(Set.of(role)));

        BooleanBuilder predicate = new BooleanBuilder();
        if (true) {
            // 根据用户名条查询
            predicate.and(QUser.user.name.eq(xiaoshitou.getName()));
        }
        if (true) {
            // 根据 join dept 根据 dept 名称
            predicate.and(QUser.user.dept.name.eq(dept.getName()));
        }
        if (true) {
            // 用户包含角色查询
            predicate.and(QUser.user.role.contains(new Role().setId(role.getId())));
        }
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<User> all = this.userRepository.findAll(predicate, pageRequest);
        // Hibernate: select user0_.id as id1_2_0_, role4_.id as id1_1_1_, dept5_.id as id1_0_2_, user0_.dept_id as dept_id3_2_0_, user0_.name as name2_2_0_, role4_.name as name2_1_1_, role3_.user_id as user_id1_3_0__, role3_.role_id as role_id2_3_0__, dept5_.name as name2_0_2_ from user user0_ left outer join user_role role3_ on user0_.id=role3_.user_id left outer join role role4_ on role3_.role_id=role4_.id left outer join dept dept5_ on user0_.dept_id=dept5_.id cross join dept dept1_ where user0_.dept_id=dept1_.id and user0_.name=? and dept1_.name=? and (? in (select role2_.role_id from user_role role2_ where user0_.id=role2_.user_id))
        //2021-09-29 18:20:36.849  INFO 54878 --- [           main] c.e.j.JpaQueryDemoApplicationTests       : data: [User(id=3, name=xiaoshitou, dept=Dept(id=1, name=d1))], total: 1
        log.info("data: {}, total: {}", all.getContent(), all.getTotalElements());
    }

}
