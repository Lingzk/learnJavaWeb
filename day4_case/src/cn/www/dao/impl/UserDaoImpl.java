package cn.www.dao.impl;

import cn.www.dao.UserDao;
import cn.www.domain.User;
import cn.www.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ling
 * Created on 2020/1/29
 */
public class UserDaoImpl implements UserDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<User> findAll() {
        String sql = "select * from user";

        List<User> users = template.query(sql, new BeanPropertyRowMapper<User>(User.class));
        return users;
    }

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        try {
            // 1、编写SQL
            String sql = "select * from user where username = ? and password = ?";
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class),
                    username,
                    password);
            return user;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void add(User user) {
        String sql = "insert into user values(null,?,?,?,?,?,?,null,null)";
        template.update(sql, user.getName(), user.getGender(), user.getAge(), user.getAddress(), user.getQq(), user.getEmail());
    }

    @Override
    public void delete(int id) {
        String sql = "delete from user where id = ?";
        template.update(sql, id);
    }

    @Override
    public User findUserById(int parseInt) {
        String sql = "select * from user where id = ?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), parseInt);
    }

    @Override
    public void update(User user) {
        String sql = "update user set name = ?, gender = ?, age = ?, address = ?, qq = ?, email = ? where id = ?";
        template.update(sql, user.getName(), user.getGender(), user.getAge(), user.getAddress(), user.getQq(), user.getEmail(), user.getId());
    }

    @Override
    public int findTotalCount(Map<String, String[]> map) {
        String sql = "SELECT count(*) from user where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        Set<String> keys = map.keySet();
        List<Object> para = new ArrayList<>();
        for (String key : keys) {
            if ("currentPage".equals(key) || "rows".equals(key))
                continue;
            String value = map.get(key)[0];
            if (value != null && !"".equals(value)) {
                sb.append(" and " + key + " like ? ");
                para.add("%" + value + "%");
            }
        }
        return template.queryForObject(sb.toString(), Integer.class, para.toArray());
    }

    @Override
    public List<User> findUserByPage(int start, int rows, Map<String, String[]> map) {
//        String sql = "select * from user limit ? , ?";
//        return template.query(sql, new BeanPropertyRowMapper<User>(User.class), start, rows);
        String sql = "select * from user WHERE 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        Set<String> keys = map.keySet();
        List<Object> para = new ArrayList<>();
        for (String key : keys) {
            if ("currentPage".equals(key) || "rows".equals(key))
                continue;
            String value = map.get(key)[0];
            if (value != null && !"".equals(value)) {
                sb.append(" and " + key + " like ? ");
                para.add("%" + value + "%");
            }
        }
        sb.append(" limit ? , ? ");
        para.add(start);
        para.add(rows);

        System.out.println(sb.toString());
        System.out.println(para);

        return template.query(sb.toString(), new BeanPropertyRowMapper<User>(User.class), para.toArray());
    }

}
