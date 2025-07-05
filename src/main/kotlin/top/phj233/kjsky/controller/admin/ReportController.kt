package top.phj233.kjsky.controller.admin

import cn.dev33.satoken.annotation.SaCheckRole
import jakarta.servlet.http.HttpServletResponse
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.model.dto.OrderReportVO
import top.phj233.kjsky.model.dto.SalesTop10ReportVO
import top.phj233.kjsky.model.dto.TurnoverReportVO
import top.phj233.kjsky.model.dto.UserReportVO
import top.phj233.kjsky.service.ReportService
import java.time.LocalDate

/**
 * 管理端/报表控制器
 * @author phj233
 * @since 2025/7/3 20:30
 * @version
 */
@RestController
@RequestMapping("/admin/report")
@SaCheckRole("employee")
class ReportController(private val reportService: ReportService) {
    /**
     * 营业额统计
     * @param begin 起始日期
     * @param end 结束日期
     * @return ApiResponse<TurnoverReportVO> 营业额统计结果
     *
     */
    @GetMapping("/turnoverStatistics")
    fun turnoverStatistics(
        @DateTimeFormat(pattern = "yyyy-MM-dd") begin:  LocalDate,
        @DateTimeFormat(pattern = "yyyy-MM-dd") end: LocalDate): ApiResponse<TurnoverReportVO> {
        return ResponseUtil.success(reportService.getTurnoverStatistics(begin, end))
    }

    /**
     * 用户统计
     * @param begin 起始日期
     * @param end 结束日期
     * @return ApiResponse<UserReportVO> 用户统计结果
     */
    @GetMapping("/userStatistics")
    fun userStatistics(
        @DateTimeFormat(pattern = "yyyy-MM-dd") begin: LocalDate,
        @DateTimeFormat(pattern = "yyyy-MM-dd") end: LocalDate): ApiResponse<UserReportVO> {
        return ResponseUtil.success(reportService.getUserStatistics(begin, end))
    }

    /**
     * 订单统计
     * @param begin 起始日期
     * @param end 结束日期
     * @return ApiResponse<OrderReportVO> 订单统计结果
     */
    @GetMapping("/ordersStatistics")
    fun ordersStatistics(
        @DateTimeFormat(pattern = "yyyy-MM-dd") begin: LocalDate,
        @DateTimeFormat(pattern = "yyyy-MM-dd") end: LocalDate): ApiResponse<OrderReportVO> {
        return ResponseUtil.success(reportService.getOrderStatistics(begin, end))
    }

    /**
     * 销量排名top10
     * @param begin 起始日期
     * @param end 结束日期
     * @return ApiResponse<SalesTop10ReportVO> 销量排名结果
     */
    @GetMapping("/top10")
    fun top10(
        @DateTimeFormat(pattern = "yyyy-MM-dd") begin: LocalDate,
        @DateTimeFormat(pattern = "yyyy-MM-dd") end: LocalDate): ApiResponse<SalesTop10ReportVO> {
        return ResponseUtil.success(reportService.getTop10SellingDishes(begin, end))
    }
    /**
     * 导出运营数据报表
     * @param response HttpServletResponse 用于导出文件
     */
    @GetMapping("/export")
    fun exportReport(response: HttpServletResponse) {
        reportService.exportBusinessData(response)
    }
}
