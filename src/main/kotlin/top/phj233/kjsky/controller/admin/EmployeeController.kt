package top.phj233.kjsky.controller.admin

import cn.dev33.satoken.annotation.SaCheckRole
import cn.dev33.satoken.annotation.SaIgnore
import cn.dev33.satoken.stp.StpUtil
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.model.dto.*
import top.phj233.kjsky.service.EmployeeService

/**
 * 管理端/员工控制器
 * @author phj233
 * @since 2025/6/30 23:25
 * @version
 */
@RestController
@RequestMapping("/admin/employee")
@SaCheckRole("employee")
class EmployeeController(val employeeService: EmployeeService) {
    /**
     * 员工登录
     * @param employeeLoginDTO 员工登录DTO
     */
    @PostMapping("/login")
    @SaIgnore
    fun login(@RequestBody employeeLoginDTO: EmployeeLoginDTO): ApiResponse<EmployeeLoginVO> {
        return ResponseUtil.success(employeeService.login(employeeLoginDTO))
    }

    /**
     * 员工退出
     * @return ApiResponse<String> 退出成功消息
     */
    @PostMapping("/logout")
    fun logout(): ApiResponse<String> {
        // 退出登录
        StpUtil.logout()
        return ResponseUtil.success(MessageConstant.LOGOUT_SUCCESS)
    }

    /**
     * 注册新员工
     * @param employeeDTO 员工DTO
     * @return ApiResponse<EmployeeVO> 新注册的员工
     */
    @PostMapping
    @SaIgnore
    fun save(@RequestBody employeeDTO: EmployeeDTO): ApiResponse<EmployeeVO> {
        return ResponseUtil.success(employeeService.register(employeeDTO))
    }

    /**
     * 员工账号启用禁用
     * @param status 新状态
     * @param id 员工ID
     * @return ApiResponse<String> 操作结果消息
     */
    @PostMapping("/status/{status}")
    fun status(@PathVariable status: Int,id: Long): ApiResponse<EmployeeVO> {
        return ResponseUtil.success(employeeService.updateStatus(id, status))
    }

    /**
     * 分页查询员工
     * @param employeePageQueryDTO 员工分页查询DTO
     * @return ApiResponse<Page<EmployeeVO>> 分页查询结果
     */
    @GetMapping("/page")
    fun page(employeePageQueryDTO: EmployeePageQueryDTO): ApiResponse<Page<EmployeeVO>> {
        return ResponseUtil.success(employeeService.pageQuery(employeePageQueryDTO))
    }

    /**
     * 根据ID查询员工信息
     * @param id 员工ID
     * @return ApiResponse<EmployeeVO> 员工信息
     */
    @GetMapping("/{id}")
    fun findEmployeeById(@PathVariable id: Long): ApiResponse<EmployeeVO> {
        return ResponseUtil.success(employeeService.findEmployeeById(id))
    }

    /**
     * 更新员工信息
     * @param employeeDTO 员工DTO
     * @return ApiResponse<EmployeeVO> 更新后的员工信息
     */
    @PutMapping
    fun updateEmployee(@RequestBody employeeDTO: EmployeeDTO): ApiResponse<EmployeeVO> {
        return ResponseUtil.success(employeeService.updateEmployee(employeeDTO))
    }
}
