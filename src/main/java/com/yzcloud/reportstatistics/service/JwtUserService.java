package com.yzcloud.reportstatistics.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzcloud.reportstatistics.model.jwt.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class JwtUserService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public JwtUserService() {
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();  //默认使用 bcrypt， strength=10
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<JwtUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        JwtUser jwtUser = userService.getOne(queryWrapper);
        if (jwtUser == null) {
            return User.builder().build();
        }
        return User.builder().username(jwtUser.getUsername()).password(passwordEncoder.encode(jwtUser.getPassword())).roles(jwtUser.getRole()).build();
        /**
         * @todo 数据库操作
         */
//        return User.builder().username("chengming").password(passwordEncoder.encode("Cheng80233")).roles("USER").build();
    }

    public String saveUserLoginInfo(UserDetails user) {
        String salt = BCrypt.gensalt();
        redisTemplate.opsForValue().set("token: " + user.getUsername(), salt, 3600, TimeUnit.SECONDS);
//        String salt = "123456ef"; //BCrypt.gensalt();  正式开发时可以调用该方法实时生成加密的salt
        /**
         * @todo 将salt保存到数据库或者缓存中, 不然重新登录 token不到过期时间之前不会失效
         * redisTemplate.opsForValue().set("token:"+username, salt, 3600, TimeUnit.SECONDS);
         */
        Algorithm algorithm = Algorithm.HMAC256(salt);
        Date date = new Date(System.currentTimeMillis() + 3600 * 1000);
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(date)
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    public UserDetails getUserLoginInfo(String username) {
        String salt = redisTemplate.opsForValue().get("token: " + username);

        //        String salt = "123456ef";
        /**
         * @todo 从数据库或者缓存中取出jwt token生成时用的salt
         * salt = redisTemplate.opsForValue().get("token:"+username);
         */
        UserDetails user = loadUserByUsername(username);
        //将salt放到password字段返回
        return User.builder().username(user.getUsername()).password(salt).authorities(user.getAuthorities()).build();
    }

    public void deleteUserLoginInfo(String username) {
        redisTemplate.delete("token: " + username);
        /**
         * @todo 将salt从缓存中删除
         */
    }
}
