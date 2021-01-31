package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;


    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public String showAllUsers(Model model){
       List<User> list = userService.getAllUsers();
       model.addAttribute("users", list);
       return "users";
   }

   @GetMapping(value = {"/add"})
   public String showAddForm(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "add";
   }

   @PostMapping("/save")
   public String saveUser(@ModelAttribute("addUser") User user,
                          @RequestParam(value = "roleAdmin", required = false) String roleAdmin,
                          @RequestParam(value = "roleUser", required = false) String roleUser) {

       findRoleAddToUser(user, roleAdmin, roleUser);
       userService.add(user);
       return "redirect:/admin/users";
   }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable("id")Long  id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "edit";
    }

    @PostMapping("/user/edit")
    public String editUser(@ModelAttribute("user") User user,
                           @RequestParam(value = "roleAdmin", required = false) String roleAdmin,
                           @RequestParam(value = "roleUser", required = false) String roleUser) {

        findRoleAddToUser(user, roleAdmin, roleUser);
        userService.edit(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id){
        userService.delete(userService.getUser(id));
        return "redirect:/admin/users/";
    }

    private void findRoleAddToUser(@ModelAttribute("addUser") User user,
                                   @RequestParam(value = "roleAdmin", required = false) String roleAdmin,
                                   @RequestParam(value = "roleUser", required = false) String roleUser) {
        Set<Role> roles = new HashSet<>();
        if (roleAdmin != null) {
            roles.add(roleService.findRoleByRoleName(roleAdmin));
        }
        if (roleUser != null) {
            roles.add(roleService.findRoleByRoleName(roleUser));
        }
        user.setRoles(roles);
    }

}
