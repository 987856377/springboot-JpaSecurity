package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class IndexController {

    @Resource
    private
    UserService userService;

    @RequestMapping(value = "/")
    public String index(){
        return "get index success";
    }

    @RequestMapping("signup")
    public String signup(User user){
        if ((user.getUsername()!=null&&user.getPassword()!=null)){
            List<Role> roles = new ArrayList<>();
            Role role = new Role((long)3,"user","USER");
            roles.add(role);
            user.setRoles(roles);
            if(userService.createOrUpdate(user)){
                return "register success: \nusrinfo: "+user.toString();
            }
        }
        return "register failure";
    }

    @RequestMapping("findById")
    public String findById(long id){
        User user = userService.findById(id);
        return "find success: \nusrinfo: "+user.toString();
    }

    @RequestMapping("findByUsername")
    public String findByUsername(String username){
        User user = userService.findByUsername(username);
        return "find success: \nusrinfo: "+user.toString();
    }

    @RequestMapping("deleteUser")
    public String deleteUser(long id){
        if (userService.deleteUser(id)){
            return "delete seccess";
        }
        return "delete failure";
    }

    @RequestMapping("findAll")
    public List<User> findAll(){
        return userService.findAll();
    }

    @RequestMapping("updateUserPwd")
    public String updateUserPwd(User user, String oldPwd, String newPwd){
        if(userService.updateUserPwd(user,oldPwd,newPwd)){
            return "update success";
        }
        return "update failure";
    }

    @RequestMapping("/dba/index")
    public String dba(){
        return "hello dba";
    }

//    @Secured("ROLE_ADMIN")
    @RequestMapping("/admin/index")
    public String admin(){
        return "hello admin";
    }

//    @Secured("ROLE_USER")
    @RequestMapping("/user/index")
    public String user(){
        return "hello user";
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @RequestMapping("/all/index")
    public String manager(){
        return "hello admin & user";
    }
}
