package top.phj233.kjsky.service

import cn.dev33.satoken.stp.StpUtil
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.common.exception.AddressBookBusinessException
import top.phj233.kjsky.model.AddressBook
import top.phj233.kjsky.model.copy
import top.phj233.kjsky.model.dto.AddressBookQueryDTO
import top.phj233.kjsky.repository.AddressBookRepository

/**
 * 地址簿服务类
 * @author phj233
 * @since 2025/7/4 22:42
 * @version
 */
@Service
class AddressBookService(
    val addressBookRepository: AddressBookRepository) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    /**
     * 条件查询
     * @param addressBook 地址簿对象
     * @return List<AddressBook> 地址簿列表
     */
    fun findByCondition(addressBook: AddressBookQueryDTO): List<AddressBook> {
        logger.info("${StpUtil.getLoginIdAsLong()} 查询地址簿，条件：$addressBook")
        return addressBookRepository.findAddressBooksByUserIdOrPhoneOrDefault(
            userId = StpUtil.getLoginIdAsLong(),
            phone = addressBook.phone,
            isDefault = addressBook.default
        )
    }

    /**
     * 新增地址
     * @param addressBook
     * @return Boolean 是否新增成功
     */
    fun save(addressBook: AddressBook): Boolean {
        logger.info("${StpUtil.getLoginIdAsLong()} 新增地址，地址信息：$addressBook")
        addressBook.copy {
            userId = StpUtil.getLoginIdAsLong()
            default = false
        }.let {
            addressBookRepository.save(it, SaveMode.INSERT_ONLY)
        }
        return true
    }

    /**
     * 根据id查询地址
     * @param id 地址ID
     * @return AddressBook 地址簿对象
     */
    fun findById(id: Long): AddressBook {
        logger.info("${StpUtil.getLoginIdAsLong()} 查询地址，ID：$id")
        return addressBookRepository.findAddressBookById(id)
            ?: throw AddressBookBusinessException(MessageConstant.ADDRESS_NOT_FOUND)
    }

    /**
     * 更新地址
     * @param addressBook 地址簿对象
     * @return Boolean 是否更新成功
     */
    fun update(addressBook: AddressBook): Boolean {
        logger.info("${StpUtil.getLoginIdAsLong()} 更新地址，地址信息：$addressBook")
        addressBookRepository.save(addressBook, SaveMode.UPDATE_ONLY)
        return true
    }

    /**
     * 设置默认地址
     * @param addressBook 地址簿对象
     * @return Boolean 是否设置成功
     */
    fun setDefault(addressBook: AddressBook): Boolean {
        logger.info("${StpUtil.getLoginIdAsLong()} 设置默认地址，地址信息：$addressBook")
        // 先将所有地址的默认状态设置为false
        addressBookRepository.updateAllToNotDefault(StpUtil.getLoginIdAsLong())
        // 再将指定地址设置为默认
        addressBook.copy {
            default = true
        }.let {
            addressBookRepository.save(it, SaveMode.UPDATE_ONLY)
        }
        return true
    }

    /**
     * 删除地址
     * @param id 地址ID
     * @return Boolean 是否删除成功
     */
    fun delete(id: Long): Boolean {
        logger.info("${StpUtil.getLoginIdAsLong()} 删除地址，ID：$id")
        addressBookRepository.deleteById(id)
        return true
    }
}
