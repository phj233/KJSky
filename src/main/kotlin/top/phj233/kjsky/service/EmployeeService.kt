package top.phj233.kjsky.service

import cn.dev33.satoken.stp.StpUtil
import cn.dev33.satoken.stp.parameter.SaLoginParameter
import cn.hutool.crypto.digest.BCrypt
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import top.phj233.kjsky.common.constant.JwtClaimsConstant
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.common.exception.AccountNotFoundException
import top.phj233.kjsky.common.exception.LoginFailedException
import top.phj233.kjsky.model.copy
import top.phj233.kjsky.model.dto.*
import top.phj233.kjsky.repository.EmployeeRepository

/**
 * 员工服务类
 * @author phj233
 * @since 2025/6/30 23:27
 * @version
 */
@Service
class EmployeeService(val employeeRepository: EmployeeRepository) {
    /**
     * 员工登录
     * @param employeeLoginDTO 员工登录DTO
     * @return EmployeeLoginVO 员工登录VO
     */
    fun login(employeeLoginDTO: EmployeeLoginDTO): EmployeeLoginVO {
        employeeRepository.findEmployeeByUsername(employeeLoginDTO.username).let{
            if(it == null){
                throw LoginFailedException(MessageConstant.ACCOUNT_NOT_FOUND)
            }
            if(BCrypt.checkpw(employeeLoginDTO.password, it.password)){
                StpUtil.login(it.id, SaLoginParameter().setExtra(JwtClaimsConstant.EMP_ID, it.id).setExtra(JwtClaimsConstant.EMP_ID, it.name))
                return employeeRepository.viewer(EmployeeLoginVO::class).findNullable(it.id)!!.copy(
                    token = StpUtil.getTokenValue()
                )
            }else{
                throw LoginFailedException(MessageConstant.PASSWORD_ERROR)
            }
        }
    }

    /**
     * 注册新员工
     * @param employeeDTO 员工DTO
     * @return Employee 新注册的员工
     */
    fun register(employeeDTO: EmployeeDTO): EmployeeVO {
        val newEmployee = employeeDTO.copy(password = BCrypt.hashpw(employeeDTO.password))
        return employeeRepository.viewer(EmployeeVO::class).findNullable(
            employeeRepository.save(newEmployee, SaveMode.UPSERT).id
        )!!
    }

    /**
     * 员工账号启用禁用
     * @param id 员工ID
     * @param status 员工状态 1启用 0禁用
     * @return EmployeeVO 更新后的员工信息
     */
    fun updateStatus(id: Long, status: Int): EmployeeVO {
        employeeRepository.findEmployeeById(id).copy {
            this.status = status
        }.let {
            return employeeRepository.viewer(EmployeeVO::class).findNullable(
                employeeRepository.save(it, SaveMode.UPDATE_ONLY).id
            )!!
        }
    }

    /**
     * 根据ID查询员工信息
     * @param id 员工ID
     * @return EmployeeVO 员工信息
     */
    fun findEmployeeById(id: Long): EmployeeVO {
        return employeeRepository.viewer(EmployeeVO::class).findNullable(id)
            ?: throw AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND)
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO 员工分页查询DTO
     * @return Page<EmployeeVO> 分页查询结果
     */
    fun pageQuery(employeePageQueryDTO: EmployeePageQueryDTO): Page<EmployeeVO> {
        return employeeRepository.findByNameLike(
            employeePageQueryDTO.name,
            PageRequest.of(employeePageQueryDTO.page, employeePageQueryDTO.pageSize),
            EmployeeVO::class
        )
    }

    /**
     * 更新员工信息
     * @param employeeDTO 员工DTO
     * @return EmployeeVO 更新后的员工信息
     */
    fun updateEmployee(employeeDTO: EmployeeDTO): EmployeeVO {
        employeeRepository.save(employeeDTO.let {
            if(!it.password.isEmpty()){
                it.copy(password = BCrypt.hashpw(it.password))
            } else {
                it
            }
        }, SaveMode.UPDATE_ONLY).let {
            return employeeRepository.viewer(EmployeeVO::class).findNullable(it.id)
                ?: throw AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND)
        }
    }
}
