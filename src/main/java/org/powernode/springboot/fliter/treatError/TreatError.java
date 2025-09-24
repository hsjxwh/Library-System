package org.powernode.springboot.fliter.treatError;

import jakarta.servlet.http.HttpServletResponse;
import org.powernode.springboot.exception.AuthorityError;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public final class TreatError {
    public static void handleJwtException(HttpServletResponse resp, AuthorityError error) throws IOException {
        resp.setStatus(HttpStatus.UNAUTHORIZED.value());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(error.getErrorType());
    }
}
