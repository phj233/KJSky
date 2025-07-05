package top.phj233.kjsky.controller.user

import cn.dev33.satoken.annotation.SaCheckRole
import cn.dev33.satoken.stp.StpUtil
import org.springframework.web.bind.annotation.*
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.model.AddressBook
import top.phj233.kjsky.model.copy
import top.phj233.kjsky.model.dto.AddressBookQueryDTO
import top.phj233.kjsky.service.AddressBookService

/**
 * 用户端/用户地址簿控制器
 * @author phj233
 * @since 2025/7/4 22:15
 * @version
 */
@RestController("userAddressBookController")
@RequestMapping("/user/addressBook")
@SaCheckRole("user")
class AddressBookController(val addressService: AddressBookService) {

    /**
     * 查询当前登录用户的所有地址信息
     * @return ApiResponse<List<Address>> 地址列表
     */
    @GetMapping("/list")
    fun listAddress(): ApiResponse<List<AddressBook>> {
        return ResponseUtil.success(
            addressService.findByCondition(
                AddressBookQueryDTO(
                    userId = StpUtil.getLoginIdAsLong()
                )
            )
        )
    }


    /**
     * 新增地址
     *
     * @param addressBook 地址信息
     * @return ApiResponse<String> 新增结果
     */
    @PostMapping
    fun addAddress(@RequestBody addressBook: AddressBook): ApiResponse<String> {
        addressService.save(
            addressBook.copy {
                userId = StpUtil.getLoginIdAsLong()
            }
        ).let {
            return ResponseUtil.success(MessageConstant.SUCCESS)
        }
    }

    /**
     * 根据地址ID查询地址信息
     * @param id 地址ID
     * @return ApiResponse<AddressBook> 地址信息
     */
    @GetMapping("/{id}")
    fun getAddressById(@PathVariable id: Long): ApiResponse<AddressBook> {
        return ResponseUtil.success(addressService.findById(id))
    }

    /**
     * 根据id修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    fun updateAddress(@RequestBody addressBook: AddressBook): ApiResponse<String> {
        addressService.update(
            addressBook.copy {
                userId = StpUtil.getLoginIdAsLong()
            }
        ).let {
            return ResponseUtil.success(MessageConstant.SUCCESS)
        }
    }

    /**
     * 设置默认地址
     * @param addressBook 地址信息
     * @return ApiResponse<String> 设置结果
     */
    @PostMapping("/default")
    fun setDefaultAddress(@RequestBody addressBook: AddressBook): ApiResponse<String> {
        addressService.setDefault(
            addressBook.copy {
                userId = StpUtil.getLoginIdAsLong()
            }
        ).let {
            return ResponseUtil.success(MessageConstant.SUCCESS)
        }
    }

    /**
     * 删除地址
     * @param id 地址ID
     * @return ApiResponse<String> 删除结果
     */
    @DeleteMapping
    fun deleteAddress( id: Long): ApiResponse<String> {
        addressService.delete(id)
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }

    /**
     * 查询默认地址
     */
    @GetMapping("/default")
    fun getDefaultAddress(): ApiResponse<AddressBook> {
        return ResponseUtil.success(
            addressService.findByCondition(
                AddressBookQueryDTO(
                    userId = StpUtil.getLoginIdAsLong(),
                    default = true
                )
            ).firstOrNull()
        )
    }

}
