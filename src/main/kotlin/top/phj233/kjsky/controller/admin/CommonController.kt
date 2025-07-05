package top.phj233.kjsky.controller.admin

import cn.dev33.satoken.annotation.SaCheckRole
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.service.MinioService

/**
 * 管理端/公共控制器
 * @author phj233
 * @since 2025/7/1 19:35
 * @version
 */
@RestController
@RequestMapping("/admin/common")
@SaCheckRole("employee")
class CommonController(private val minioService: MinioService) {
    /**
     * 文件上传接口
     * @param file MultipartFile 文件
     * @return ApiResponse<String> 文件上传结果-包含链接
     */
    @PostMapping("/upload")
    fun uploadFile(file: MultipartFile): ApiResponse<String> {
        return ResponseUtil.success(minioService.upload(file))
    }
}
