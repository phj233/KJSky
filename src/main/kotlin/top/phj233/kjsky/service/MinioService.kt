package top.phj233.kjsky.service

import cn.hutool.crypto.SecureUtil
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.errors.MinioException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import top.phj233.kjsky.config.KJSkyProperties
import java.security.MessageDigest

/**
 * Minio 文件上传服务
 * @author phj233
 * @since 2025/7/1 16:47
 * @version
 */
@Service
class MinioService(private val kjSkyProperties: KJSkyProperties) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)
    val minioClient: MinioClient = MinioClient.builder()
        .endpoint(kjSkyProperties.minio.endpoint)
        .credentials(kjSkyProperties.minio.accessKey, kjSkyProperties.minio.secretKey)
        .build()

    /**
     * 上传文件
     * md5作为文件名
     * @param file 文件
     * @return 文件url
     */
    fun upload(file: MultipartFile): String {
        val fileKind = file.originalFilename?.substringAfterLast(".") ?: ""
        val md5 = SecureUtil.md5(file.inputStream)
        val fileName = "$md5.$fileKind"

        log.info("上传文件: $fileName")
        try {
            file.inputStream.use {
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(kjSkyProperties.minio.bucketName)
                        .`object`(fileName)
                        .stream(it, file.size, -1)
                        .contentType(file.contentType)
                        .build()
                )
            }
        } catch (e: MinioException){
            log.error("Minio 上传失败: ${e.message} file: $file")
        }
        return "${kjSkyProperties.minio.endpoint}/${kjSkyProperties.minio.bucketName}/$fileName"
    }

    fun getMd5(file: MultipartFile): String {
        val bytes = file.bytes
        val md5Digest = MessageDigest.getInstance("MD5")
        val md5Bytes = md5Digest.digest(bytes)
        return bytesToHex(md5Bytes)
    }

    fun bytesToHex(bytes: ByteArray): String {
        val hexArray = "0123456789ABCDEF".toCharArray()
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}

