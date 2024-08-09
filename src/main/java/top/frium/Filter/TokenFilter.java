package top.frium.Filter;


import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.frium.common.R;
import top.frium.pojo.LoginUser;
import top.frium.uitls.JwtUtil;

import java.io.IOException;
import java.util.Objects;

import static top.frium.common.StatusCodeEnum.NOT_LOGIN;
import static top.frium.common.StatusCodeEnum.NO_PERMISSION;


@Component
@SuppressWarnings("all")
public class TokenFilter extends OncePerRequestFilter {
    @Value("${jwt.key}")
    private String secretKey;

    @Value("${jwt.name}")
    private String tokenName;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(tokenName);
        if (token == null || token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        Long id;
        LoginUser loginUser = null;
        try {
            // 解析Token，获取其中的Claims对象
            Claims claims = JwtUtil.parseToken(secretKey, token);
            id = Long.valueOf(Objects.requireNonNull(claims.get("userId")).toString());
            loginUser = (LoginUser) Objects.requireNonNull(redisTemplate.opsForValue().get("loginUser" + id));
        } catch (Exception ex) {
            // 解析Token失败，抛出自定义业务异常
            authentiactionEntryPoint(response);
            return;
        }
        try {
            //存入SecurityContexHolder
            UsernamePasswordAuthenticationToken passwordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(passwordAuthenticationToken);
        } catch (Exception e) {
            accessDeniedHandler(response);
            return;
        }

        filterChain.doFilter(request, response);
        return;
    }

    private void accessDeniedHandler(HttpServletResponse response) throws IOException {
        //处理异常
        response.setContentType("application/json");
        response.setStatus(403);
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(JSON.toJSON(R.error(NO_PERMISSION)));
    }

    private void authentiactionEntryPoint(HttpServletResponse response) throws IOException {
        //处理异常
        response.setContentType("application/json");
        response.setStatus(401);
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(JSON.toJSON(R.error(NOT_LOGIN)));
    }
}
