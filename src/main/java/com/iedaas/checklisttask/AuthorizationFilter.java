package com.iedaas.checklisttask;

import com.iedaas.checklisttask.dao.CustomRepository;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

public class AuthorizationFilter{
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

    private static final String HEADER_STRING = "Authorization";

    @Value("${secretKey}")
    String secret;

    @Autowired
    private CustomRepository customRepository;

    public String authenticate(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        String userUid = null;
        String userEmail;

        if(token==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Token Found");
        }

        try {
            logger.info("Validating Jwt token :={}", token);
            userEmail = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token.replace("Bearer", ""))
                    .getBody()
                    .get("email", String.class);
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token or token is expired");
        }
        userUid = String.valueOf(customRepository.getUserUUID(userEmail));
        return userUid;
    }
}
