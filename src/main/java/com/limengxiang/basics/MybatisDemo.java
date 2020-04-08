package com.limengxiang.basics;

import com.limengxiang.basics.dao.UserDAO;
import com.limengxiang.basics.model.UserModel;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MybatisDemo {
    public static void main(String[] args) {
        try {
            InputStream stream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSession sqlSession = new SqlSessionFactoryBuilder().build(stream).openSession(true);
            UserDAO userDAO = sqlSession.getMapper(UserDAO.class);
            // 插入数据
            Integer inserted = userDAO.insert("allen", "13356986723");
            System.out.println("Insert result:" + inserted);
            inserted = userDAO.insert("alfred", "13856986723");
            System.out.println("Insert result:" + inserted);

            UserModel user = userDAO.selectOne("users", 1);
            System.out.println("Select one:" + user);

            List<UserModel> users = userDAO.fuzzySearch("all%", "");
            System.out.println("Fuzzy search by username:" + users);

            users = userDAO.fuzzySearch("", "133%");
            System.out.println("Fuzzy search by mobile:" + users);


            users = userDAO.fuzzySearch("all%", "133%");
            System.out.println("Fuzzy search by username and mobile:" + users);

            users = userDAO.searchByUsernameOrMobile("all%", "135%");
            System.out.println("Search by username:" + users);

            users = userDAO.searchByUsernameOrMobile("", "135%");
            System.out.println("Search by mobile:" + users);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
