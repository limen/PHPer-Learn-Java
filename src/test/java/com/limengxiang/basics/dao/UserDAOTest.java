package com.limengxiang.basics.dao;

import com.limengxiang.basics.model.UserModel;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private UserDAO dao;

    @BeforeEach
    void setUp() throws IOException {
        if (dao == null) {
            InputStream stream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSession session = new SqlSessionFactoryBuilder().build(stream).openSession(true);
            dao = session.getMapper(UserDAO.class);
        }
    }

    @Test
    void insert() {
        // 插入数据
        Integer inserted = dao.insert("allen", "13356986723");
        assertEquals(inserted, 1);
    }

    @Test
    void selectOne() {
        UserModel user = dao.selectOne("users", 1);
        System.out.println("Select one:" + user);
    }

    @Test
    void fuzzySearch() {
        List<UserModel> users = dao.fuzzySearch("all%", "");
        System.out.println("Fuzzy search by username:" + users);
    }

    @Test
    void searchByUsernameOrMobile() {
        List<UserModel> users;
        users = dao.searchByUsernameOrMobile("all%", "135%");
        System.out.println("Search by username:" + users);

        users = dao.searchByUsernameOrMobile("", "135%");
        System.out.println("Search by mobile:" + users);
    }
}