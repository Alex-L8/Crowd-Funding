package lcx.mvc.handler;

import lcx.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Create by LCX on 2/10/2022 10:57 PM
 */
@Controller
public class AuthHandler {

    @Autowired
    private AuthService authService;

}
