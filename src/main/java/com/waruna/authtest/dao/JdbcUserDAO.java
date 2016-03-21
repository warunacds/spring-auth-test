package com.waruna.authtest.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by waruna on 3/2/16.
 */
@Repository
public class JdbcUserDAO implements UserDAO, UserDetailsService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);

    }

    @Override
    public User getUserByFacebookID(String fbId) {

        String sql = "SELECT * from user WHERE fb_id="+fbId;

        User user = jdbcTemplate.queryForObject(sql, new Object[]{fbId},
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User();
                        user.setUsername(rs.getString("username"));
                        user.setFb_id(rs.getString("fb_id"));
                        user.setFb_auth_token(rs.getString("fb_auth_token"));
                        user.setUser_id(rs.getInt("user_id"));

                        return user;
                    }
                });

        return user;
    }

    @Override
    public boolean createNewUser(String fbId, String authToken, String username) {

        String sql = "INSERT INTO user (fb_id, fb_auth_token, username, password) VALUES ("+fbId+","+authToken+","+username+","+authToken+")";

        int updated = jdbcTemplate.update(sql);
        if (updated > 1)
            return true;

        return false;
    }

    //    @Override
//    public User getUserById(int id) {
//
//        String sql = "SELECT * FROM user WHERE user_id="+id;
//
//        User user = jdbcTemplate.queryForObject(sql, new Object[]{id},
//                new RowMapper<User>() {
//
//                    @Override
//                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//                        User user = new User();
//                        user.setName(rs.getString("username"));
//                        user.setFb_auth_token(rs.getString("fb_auth_token"));
//                        user.setFb_id(rs.getString("fb_id"));
//                        user.setUser_id(rs.getInt("user_id"));
//
//                        return user;
//                    }
//                });
//
//        return user;
//
//
//    }

    @Override
    public User getUserByUserName(String username) {


        String sql = "SELECT * FROM user WHERE username='"+username+"'";

        User user = jdbcTemplate.queryForObject(sql, null,
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User();
                        user.setUsername(rs.getString("username"));
                        user.setFb_auth_token(rs.getString("fb_auth_token"));
                        user.setFb_id(rs.getString("fb_id"));
                        user.setUser_id(rs.getInt("user_id"));
                        user.setPassword(rs.getString("password"));

                        return user;
                    }
                });

        return user;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("usernamr ------"+username);

        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
        UserDAO userDAO =  (UserDAO) context.getBean("userDAO");

        User user = userDAO.getUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s does not exist!", username));
        }
        return new UserRepositoryUserDetails(user);
    }

        private final static class UserRepositoryUserDetails extends User implements UserDetails {

        private static final long serialVersionUID = 1L;

        private UserRepositoryUserDetails(User user) {
            super(user);
        }

            @Override
            public String getPassword() {
                System.out.println("-------- get password gets called --------");

                return password;
            }

            @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                System.out.println("-------- grant authority gets called --------");
                return null;
        }

        @Override
        public String getUsername() {
            System.out.println("-------- get username gets called --------");
            return username;//();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

    }
}
