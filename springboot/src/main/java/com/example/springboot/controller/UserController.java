package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.PasswordDTO;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.entity.User;
import com.example.springboot.service.IUserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Tropinone
 * @since 2022-07-24
 */

@RestController
@Api(tags = "用户接口")
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        return Result.success(userService.saveOrUpdate(user));
    }

    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeByIds(ids));
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/all")
    public Result findAll() {
        return Result.success(userService.list());
    }

    @GetMapping("/username/{username}")
    public Result findUser(@PathVariable String username) {
        return Result.success(userService.findUser(username));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(defaultValue = "username") String optionValue,
                           @RequestParam(defaultValue = "") String username){
        return Result.success(userService.findPage(pageNum, pageSize, optionValue, username));
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        return Result.success(userService.login(userDTO));
    }

    /**
     * 修改密码
     * @param passwordDTO 密码
     * @return
     */
    @PostMapping("/password")
    public Result password(@RequestBody PasswordDTO passwordDTO) {
        userService.updatePassword(passwordDTO);
        passwordDTO.setPassword(SecureUtil.md5(passwordDTO.getPassword()));
        passwordDTO.setNewPassword(SecureUtil.md5(passwordDTO.getNewPassword()));
        return Result.success();
    }
}

