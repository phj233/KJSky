package top.phj233.kjsky.service

import cn.dev33.satoken.stp.StpUtil
import jakarta.servlet.ServletOutputStream
import jakarta.servlet.http.HttpServletResponse
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import top.phj233.kjsky.common.constant.StatusConstant
import top.phj233.kjsky.common.exception.BaseException
import top.phj233.kjsky.model.dto.*
import top.phj233.kjsky.repository.OrderRepository
import top.phj233.kjsky.repository.UserRepository
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * @author phj233
 * @since 2025/7/3 15:43
 * @version lazy
 */
@Service
class ReportService(
    val orderRepository: OrderRepository,
    val userRepository: UserRepository,
    val workspaceService: WorkspaceService) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    /**
     * 获取营业额统计信息
     * @param beginDate 起始日期
     * @param endDate 结束日期
     */
    fun getTurnoverStatistics( beginDate: LocalDate, endDate: LocalDate ): TurnoverReportVO {
        logger.info("获取营业额统计信息，起始日期：$beginDate，结束日期：$endDate")
        val dateList = mutableListOf<LocalDate>()
        val turnoverList = mutableListOf<Double>()
        var beginDate = beginDate
        dateList.add(beginDate)
        while (beginDate <= endDate) {
            beginDate = beginDate.plusDays(1)
            dateList.add(beginDate)
        }
        dateList.forEach { date ->
            val turnover = orderRepository.sumAmountByOrderTimeBetweenAndStatusIs(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay(),
                StatusConstant.COMPLETED
            )
            turnoverList.add(turnover)
        }
        return TurnoverReportVO(
            //以逗号分隔，例如：2022-10-01,2022-10-02,2022-10-03
            dateList = dateList.joinToString(separator = ",") { it.toString() },
            turnoverList = turnoverList.joinToString(separator = ",") { it.toString() },
        )
    }

    /**
     * 统计指定时间区间内的用户数据
     * @param beginDate 起始日期
     * @param endDate 结束日期
     * @return UserReportVO 用户统计视图对象
     */
    fun getUserStatistics(beginDate: LocalDate, endDate: LocalDate): UserReportVO {
        logger.info("获取用户统计信息，起始日期：$beginDate，结束日期：$endDate")
        val dateList = mutableListOf<LocalDate>()
        val userCountList = mutableListOf<Int>()
        val totalUserList = mutableListOf<Int>()
        val newUserList = mutableListOf<Int>()
        var beginDate = beginDate
        dateList.add(beginDate)
        while (beginDate <= endDate) {
            beginDate = beginDate.plusDays(1)
            dateList.add(beginDate)
        }
        dateList.forEach { date ->
            val userCount = userRepository.countByCreateTimeBetween(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
            )
            userCountList.add(userCount)
            val totalUser = userRepository.countByCreateTimeBetween(
                beginDate.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
            )
            val newUser = userRepository.countByCreateTimeBetween(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
            )
            totalUserList.add(totalUser)
            newUserList.add(newUser)
        }
        return UserReportVO(
            dateList = dateList.joinToString(separator = ",") { it.toString() },
            totalUserList = totalUserList.joinToString(separator = ",") { it.toString() },
            newUserList = newUserList.joinToString(separator = ",") { it.toString() },
        )
    }

    /**
     * 统计指定时间区间内的订单数据
     * @param beginDate 起始日期
     * @endDate 结束日期
     */
    fun getOrderStatistics(beginDate: LocalDate, endDate: LocalDate): OrderReportVO {
        logger.info("获取订单统计信息，起始日期：$beginDate，结束日期：$endDate")
        val dateList = mutableListOf<LocalDate>()
        val orderCountList = mutableListOf<Int>()
        val validOrderCountList = mutableListOf<Int>()
        var beginDate = beginDate
        dateList.add(beginDate)
        while (beginDate <= endDate) {
            beginDate = beginDate.plusDays(1)
            dateList.add(beginDate)
        }
        dateList.forEach { date ->
            val orderCount = orderRepository.countByOrderTimeBetween(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
            )
            val validOrderCount = orderRepository.countByOrderTimeBetweenAndStatusIs(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay(),
                StatusConstant.COMPLETED
            )
            orderCountList.add(orderCount.toInt())
            validOrderCountList.add(validOrderCount.toInt())
        }
        //计算时间区间内的订单总数量
        val totalOrderCount = orderCountList.stream().reduce { a: Int, b: Int -> Integer.sum(a, b) }.get()
        //计算时间区间内的有效订单数量
        val validOrderCount = validOrderCountList.stream().reduce { a: Int, b: Int -> Integer.sum(a, b) }.get()

        var orderCompletionRate = 0.0
        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount.toDouble() / totalOrderCount
        }
        return OrderReportVO(
            dateList = dateList.joinToString(separator = ",") { it.toString() },
            orderCountList = orderCountList.joinToString(separator = ",") { it.toString() },
            validOrderCountList = validOrderCountList.joinToString(separator = ",") { it.toString() },
            totalOrderCount = totalOrderCount,
            validOrderCount = validOrderCount,
            orderCompletionRate = orderCompletionRate
        )
    }

    /**
     * 根据条件统计订单数量
     * @param status 订单状态
     * @param beginDate 起始日期
     * @param endDate 结束日期
     * @return Int 订单数量
     */
    fun countOrdersByStatusAndDateRange(status: Int, beginDate: LocalDateTime, endDate: LocalDateTime): Int {
        logger.info("统计订单数量，状态：$status，起始日期：$beginDate，结束日期：$endDate")
        return orderRepository.countByOrderTimeBetweenAndStatusIs(
            beginDate,
            endDate,
            status
        ).toInt()
    }

    /**
     * 统计指定时间区间内的销量排名前10
     * @param beginDate 起始日期
     * @param endDate 结束日期
     * @return SalesTop10ReportVO 销量前10统计视图对象
     */
    fun getTop10SellingDishes(beginDate: LocalDate, endDate: LocalDate): SalesTop10ReportVO {
        logger.info("获取销量前10统计信息，起始日期：$beginDate，结束日期：$endDate")
        val data = orderRepository.getSalesTop10(
            beginTime = beginDate.atStartOfDay(),
            endTime = endDate.plusDays(1).atStartOfDay(),
        )
        logger.info("销量前10统计数据：$data")
        return SalesTop10ReportVO(
            nameList = data.joinToString(separator = ",") { it.name },
            numberList = data.joinToString(separator = ",") { it.number.toString() }
        )
    }

    /**
     * 导出运营数据报表
     * @param HttpServletResponse 响应对象
     */
    fun exportBusinessData(response: HttpServletResponse) {
        logger.info("${StpUtil.getLoginIdAsLong()} 导出运营数据报表")
        //1. 查询数据库，获取营业数据---查询最近30天的运营数据
        val dateBegin = LocalDate.now().minusDays(30)
        val dateEnd = LocalDate.now().minusDays(1)

        //查询概览数据
        val businessDataVO: BusinessDataVO = workspaceService.getBusinessData(
            LocalDateTime.of(dateBegin, LocalTime.MIN),
            LocalDateTime.of(dateEnd, LocalTime.MAX)
        )
        //2. 通过POI将数据写入到Excel文件中
        val `in` = this.javaClass.getClassLoader().getResourceAsStream("运营数据报表模板.xlsx")
        try {
            //基于模板文件创建一个新的Excel文件
            val excel: HSSFWorkbook = HSSFWorkbook(`in`)

            //获取表格文件的Sheet页
            val sheet: HSSFSheet = excel.getSheet("Sheet1")

            //填充数据--时间
            sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + "至" + dateEnd)

            //获得第4行
            var row: HSSFRow = sheet.getRow(3)
            row.getCell(2).setCellValue(businessDataVO.turnover)
            row.getCell(4).setCellValue(businessDataVO.orderCompletionRate)
            row.getCell(6).setCellValue(businessDataVO.newUsers.toString())

            //获得第5行
            row = sheet.getRow(4)
            row.getCell(2).setCellValue(businessDataVO.validOrderCount.toString())
            row.getCell(4).setCellValue(businessDataVO.unitPrice)

            //填充明细数据
            for (i in 0..29) {
                val date = dateBegin.plusDays(i.toLong())
                //查询某一天的营业数据
                val businessData: BusinessDataVO = workspaceService.getBusinessData(
                    LocalDateTime.of(date, LocalTime.MIN),
                    LocalDateTime.of(date, LocalTime.MAX)
                )
                //获得某一行
                row = sheet.getRow(7 + i)
                row.getCell(1).setCellValue(date.toString())
                row.getCell(2).setCellValue(businessData.turnover)
                row.getCell(3).setCellValue(businessData.validOrderCount.toString())
                row.getCell(4).setCellValue(businessData.orderCompletionRate)
                row.getCell(5).setCellValue(businessData.unitPrice)
                row.getCell(6).setCellValue(businessData.newUsers.toString())
            }
            //3. 通过输出流将Excel文件下载到客户端浏览器
            val out: ServletOutputStream = response.outputStream
            excel.write(out)
            //关闭资源
            out.close()
            excel.close()
        } catch (e: IOException) {
            throw BaseException("导出运营数据报表失败 ${e.message}")
        }
    }
}


