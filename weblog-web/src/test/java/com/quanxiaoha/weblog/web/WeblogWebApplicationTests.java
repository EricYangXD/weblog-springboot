package com.quanxiaoha.weblog.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.quanxiaoha.weblog.common.domain.dos.UserDO;
import com.quanxiaoha.weblog.common.domain.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
@Slf4j
class WeblogWebApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testLog() {
        log.info("这是一行 Info 级别日志");
        log.warn("这是一行 Warn 级别日志");
        log.error("这是一行 Error 级别日志");

        // 占位符
        String author = "犬小哈";
        log.info("这是一行带有占位符日志，作者：{}", author);
    }

    @Test
    void insertTest() {
        // 构建数据库实体类
        UserDO userDO = UserDO.builder().username("犬小哈5").password("123456").createTime(new Date()).updateTime(new Date()).isDeleted(false).build();

        int rows = userMapper.insert(userDO);
        log.info("影响行数：{}", rows);
    }

    @Test
    void selectTest() {
        // UserDO userDO = userMapper.findByUsername("犬小哈");
        // log.info("查询结果：{}", userDO);

        // 通过 id 查询
        UserDO userDO = userMapper.selectById(2L);
        log.info("通过 id 查询结果：{}", userDO);
    }

    @Test
    void selectIdsTest() {
        List<Long> list = Arrays.asList(1L, 2L, 3L, 4L);
        // 通过 id 查询
        List<UserDO> userList = userMapper.selectBatchIds(list);
        log.info("通过 id 查询结果：{}", userList);
    }

    @Test
    void deleteTest() {
        int rows = userMapper.deleteById(6L);
        log.info("影响行数：{}", rows);
    }

    @Test
    void updateByUsernameTest() {
        UserDO userDO = UserDO.builder().id(1L).username("犬小哈1").password("12345678").updateTime(new Date()).build();

        int rows = userMapper.updateById(userDO);
        log.info("影响行数：{}", rows);

    }

    @Test
    void updateTest() {
        UserDO userDO = userMapper.findByUsername("犬小哈2");
        userDO.setPassword("987654");
        userDO.setUpdateTime(new Date());
        userDO.setUsername("EricYang");

        int rows = userMapper.updateById(userDO);
        log.info("影响行数：{}", rows);
    }

    @Test
    void selectByMap() {
        Map<String, Object> columnMap = new HashMap<>();
        // columnMap.put("username", "犬小哈3");
        columnMap.put("password", "123456");
        List<UserDO> userDOList = userMapper.selectByMap(columnMap);
        log.info("查询结果：{}", userDOList);
    }


    @Test
    void selectByWrapper() {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>();
        // QueryWrapper<UserDO> queryWrapper = Wrappers.<UserDO>query();

        // queryWrapper.like("username", "犬小哈").isNull("email");
        queryWrapper.eq("password", "123456");

        // queryWrapper.likeRight("username", "犬").or().ge("age", 18).orderByDesc("id");

        // queryWrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", "2024-11-12").inSql("email","select email from t_user where id > 1");
        // 下面这句演示SQL注入
        // queryWrapper.apply("date_format(create_time, '%Y-%m-%d') = '2024-11-12' or true or true").inSql("email","select email from t_user where id > 1");

        // queryWrapper.likeRight("username", "犬").and(wq-> wq.lt("age",40).or().isNotNull("email"));
        // queryWrapper.in("age", Arrays.asList(20, 40, 50));
        // 有sql注入的风险
        // queryWrapper.in("age", Arrays.asList(20, 40, 50)).last("limit 1");

        queryWrapper.nested(wq -> wq.lt("age", 40).or().isNotNull("email")).likeRight("name", "犬");

        List<UserDO> userList = userMapper.selectList(queryWrapper);
        log.info("查询结果：{}", userList);

    }

    @Test
    void selectByWrapperSuper() {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>();
        // 选择特定的列column
        queryWrapper.select("id", "username").like("username", "犬").lt("age", 50);
        // 排除特定的列column
        queryWrapper.like("username", "犬").lt("age", 50).select(UserDO.class, info -> !info.getColumn().equals("create_time") && !info.getColumn().equals("isDeleted"));

        List<UserDO> userList = userMapper.selectList(queryWrapper);
        log.info("查询结果：{}", userList);

    }

    @Test
    void testCondition() {
        String name = "";
        String email = "x";
        condition(name, email);
    }

    // 如果不用注解就需要自己写if做条件判断
    private void condition(String name, String email) {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>();
        // if(StringUtils.isNotEmpty(name)){
        //     queryWrapper.like("username",name);
        // }
        // if (StringUtils.isNotEmpty(email)){
        //     queryWrapper.like("email",email);
        // }
        // 这样就不用写if判断了
        queryWrapper.like(StringUtils.isNotEmpty(name), "username", name).like(StringUtils.isNotEmpty(email), "email", email);

        List<UserDO> userList = userMapper.selectList(queryWrapper);
        log.info("查询结果：{}", userList);

    }

    // 实体作为条件构造器构造方法的参数
    @Test
    void selectByWrapperEntity() {
        UserDO user = new UserDO();
        user.setUsername("JasonKid");
        user.setPassword("111111");
        user.setUpdateTime(new Date());

        // 这里传入user实例
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>(user);
        userMapper.insert(user);

        List<UserDO> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    void selectByWrapperAllEq() {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", "JasonKid");
        params.put("password", "111111");
        queryWrapper.allEq(params);
        // queryWrapper.allEq((k, v) -> !k.equals("username"), params);

        List<UserDO> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    void selectByWrapperMaps() {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>();
        queryWrapper.like("username", "犬").lt("age", 50);
        // queryWrapper.select("id","username","age").like("username", "犬").lt("age", 50);
        // 返回值中不包含null的项
        List<Map<String, Object>> userList = userMapper.selectMaps(queryWrapper);
        userList.forEach(System.out::println);
    }

    // 类似的还有selectCount等方法，这里就不一一列举了

    @Test
    void selectLambda(){
        LambdaQueryWrapper<UserDO> user1 = new QueryWrapper<UserDO>().lambda();
        LambdaQueryWrapper<UserDO> user2 = new LambdaQueryWrapper<UserDO>();
        LambdaQueryWrapper<UserDO> user3 = Wrappers.<UserDO>lambdaQuery();

        user3.like(UserDO::getUsername,"犬").lt(UserDO::getAge,50);
        userMapper.selectList(user3).forEach(System.out::println);
    }

}
