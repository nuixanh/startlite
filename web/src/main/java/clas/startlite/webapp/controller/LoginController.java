package clas.startlite.webapp.controller;

import clas.startlite.webapp.dto.RestResultDTO;
import clas.startlite.webapp.dto.UserLoginDTO;
import com.clas.startlite.dao.UserDao;
import com.clas.startlite.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Son on 8/6/14.
 */
@RestController
public class LoginController extends ApplicationObjectSupport {

    @RequestMapping(value = "/login", method= RequestMethod.GET, produces={"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public RestResultDTO login(@RequestParam(value="email", required=true, defaultValue="") String email,
                              @RequestParam(value="password", required=true, defaultValue="") String password) {
        RestResultDTO restResultDTO = new RestResultDTO();
        restResultDTO.setSuccessful(true);
        User user = userDao.findOneByEmailAndPassword(email, password);
        UserLoginDTO userDto = new UserLoginDTO();
        userDto.setEmail(user.getEmail());
        restResultDTO.setData(userDto);
        return restResultDTO;
    }

    @Autowired
    private UserDao userDao;
}
