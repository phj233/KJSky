package top.phj233.kjsky.service

import cn.dev33.satoken.stp.StpUtil
import cn.hutool.http.HttpUtil
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import top.phj233.kjsky.common.WeChatPayUtil
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.common.constant.StatusConstant
import top.phj233.kjsky.common.exception.AddressBookBusinessException
import top.phj233.kjsky.common.exception.OrderBusinessException
import top.phj233.kjsky.common.exception.ShoppingCartBusinessException
import top.phj233.kjsky.config.KJSkyProperties
import top.phj233.kjsky.model.OrderDetail
import top.phj233.kjsky.model.ShoppingCart
import top.phj233.kjsky.model.copy
import top.phj233.kjsky.model.dto.*
import top.phj233.kjsky.repository.*
import top.phj233.kjsky.websocket.WebSocketServer
import java.time.LocalDateTime

/**
 * 订单服务类
 * @author phj233
 * @since 2025/7/2 11:26
 * @version lazy
 */
@Service
class OrderService(
    val orderRepository: OrderRepository,
    val orderDetailRepository: OrderDetailRepository,
    val userRepository: UserRepository,
    val addressBookRepository: AddressBookRepository,
    val shoppingCartRepository: ShoppingCartRepository,
    val weChatPayUtil: WeChatPayUtil,
    val webSocketServer: WebSocketServer,
    val kjSkyProperties: KJSkyProperties
) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    /**
     *  用户下单
     *  @param ordersSubmitDTO 订单提交DTO
     *  @return OrderSubmitVO 订单提交结果VO
     */
    fun submitOrder(ordersSubmitDTO: OrdersSubmitDTO): OrderSubmitVO {
        logger.info("用户下单: $ordersSubmitDTO")
        // 1. 获取用户信息
        val orderUserId= StpUtil.getLoginIdAsLong()
        // 2. 获取地址信息
        val addressBook = addressBookRepository.findById(ordersSubmitDTO.addressBookId)
            .orElseThrow { AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL) }

        // 3. 获取购物车信息
        val shoppingCart = shoppingCartRepository.findByUserId(orderUserId).let {
            if (it.isEmpty()) {
                throw ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL)
            }
            it
        }

        // 4. 创建订单
        val order = orderRepository.save(ordersSubmitDTO.toEntity {
            orderTime = LocalDateTime.now()
            payStatus = StatusConstant.UN_PAID
            number = System.currentTimeMillis().toString()
            address = addressBook.detail
            phone = addressBook.phone
            consignee = addressBook.consignee
            userId = orderUserId
        })
        logger.info("订单创建成功: $order")
        // 4.1 保存订单详情
        shoppingCart.forEach { item ->
            val orderDetail = OrderDetail{
                this.orderId = order.id
                this.dishId = item.dishId!!
                this.setmealId = item.setmealId!!
                this.name = item.name
                this.image = item.image
                this.amount = item.amount
                this.number = item.number
                this.dishFlavor = item.dishFlavor
            }
            orderDetailRepository.save(orderDetail)
        }
        // 5. 清空购物车
        shoppingCartRepository.deleteByUserId(orderUserId)
        // 6. 封装OrderSubmitVO
        logger.info("订单提交成功: $order")
        return OrderSubmitVO(
            id = order.id,
            orderTime = order.orderTime,
            orderNumber = order.number!!,
            orderAmount = order.amount!!
        )
    }

    /**
     * 订单支付
     * @param ordersPaymentDTO 订单支付DTO
     * @return OrderPaymentVO 订单支付结果VO
     */
    fun paymentOrder(ordersPaymentDTO: OrdersPaymentDTO): OrderPaymentVO {
        logger.info("用户支付订单: $ordersPaymentDTO")
        val userId = StpUtil.getLoginIdAsLong()
        /**
         * TODO()
         * 1.根据 userId 查询 用户 openId
         * 2.调用微信支付统一下单接口
         */
        val order = orderRepository.findByNumber(ordersPaymentDTO.orderNumber)
        val res: JsonNode = ObjectMapper().readTree("fake response from WeChat Pay API")
        res.get("code").equals("ORDERPAID").takeIf { it } ?: run {
            throw OrderBusinessException("订单已支付")
        }
        logger.info("微信支付统一下单成功: $res")
        return OrderPaymentVO(
            nonceStr = res.get("nonce_str").asText() ?: "fake nonce str",
            paySign = res.get("prepay_id").asText() ?: "fake pay sign",
            timeStamp = res.get("time_stamp").asText() ?: System.currentTimeMillis().toString(),
            signType = res.get("sign_type").asText() ?: "fake sign type",
            packageStr = res.get("package").asText() ?: "fake package",
        )
    }

    /**
     * 成功支付后更新订单状态
     * @param outTradeNo 商户订单号
     * @return Boolean 是否更新成功
     */
    fun paySuccess(outTradeNo: String): Boolean {
        // 1. 根据商户订单号 和 用户Id查询订单
        orderRepository.findOrderByNumberAndUserId( outTradeNo,StpUtil.getLoginIdAsLong()).copy {
            logger.info("订单状态更新前: $this")
            this.status = StatusConstant.TO_BE_CONFIRMED
            this.payStatus = StatusConstant.PAID
            this.checkoutTime = LocalDateTime.now()
        }.apply {
        logger.info("订单状态更新后: $this")
            // 2. 更新订单状态
            orderRepository.save(this)
            // 3. 通过websocket向客户端浏览器推送消息 type orderId content
            hashMapOf(
                "type" to "order",
                "orderId" to this.id,
                "content" to "订单号+$outTradeNo"
            ).let { webSocketServer.sendToAllClient(
                ObjectMapper().writeValueAsString(it)
            ) }
        }
        return true
    }

    /**
     * 用户端订单分页查询
     * @param page 页码
     * @param pageSize 页大小
     * @param status 订单状态
     * @return Page<OrderVO> 订单分页查询结果
     */
    fun findOrdersByUserId(page: Int, pageSize: Int, status: Int): Page<OrderVO>{
        logger.info("用户查询订单: page=$page, pageSize=$pageSize, status=$status")
        val userId = StpUtil.getLoginIdAsLong()
        // 1. 查询订单
        return orderRepository.findByUserIdAndStatus(userId, status, PageRequest.of(
            page,pageSize
        ), OrderVO::class)
    }

    /**
     * 根据Id查询订单详情
     * @param orderId 订单Id
     * @return OrderVO 订单详情VO
     */
    fun findOrderDetailById(orderId: Long): OrderVO {
        logger.info("查询订单详情: orderId=$orderId")
        return orderRepository.viewer(OrderVO::class).findNullable(orderId) ?: throw OrderBusinessException("订单不存在或已删除")
    }

    /**
     * 用户取消订单
     * @param orderId 订单Id
     * @return Boolean 是否取消成功
     */
    fun cancelOrder(orderId: Long): Boolean {
        logger.info("用户取消订单: orderId=$orderId")
        val order = orderRepository.findNullable(orderId) ?: throw OrderBusinessException(MessageConstant.ORDER_NOT_FOUND)
        val ordersDTO = OrdersDTO(order)
        if (order.status > 2){
            throw OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR)
        }
        if (order.status == StatusConstant.TO_BE_CONFIRMED) {
            /**
             * 如果订单处于待确认，则退款
             * TODO() 实现微信退款
             */
            logger.info("订单处于待确认状态，进行退款处理")
        }
        // 更新订单状态、取消原因、取消时间
        orderRepository.save(ordersDTO.copy(
            status = if (ordersDTO.status == StatusConstant.TO_BE_CONFIRMED) StatusConstant.REFUND else StatusConstant.CANCELLED,
            cancelReason = "用户取消订单",
            cancelTime = LocalDateTime.now()
        ), SaveMode.UPDATE_ONLY)
        orderRepository
        return true

    }

    /**
     * 再来一单
     * @param orderId 订单Id
     * @return Boolean 再来一单是否成功
     */
    fun repeatOrder(orderId: Long): Boolean {
        logger.info("用户再来一单: orderId=$orderId")
        // 1. 查询订单
        val orderDetailList = orderDetailRepository.findOrderDetailsByOrderId(orderId) ?: throw OrderBusinessException(MessageConstant.ORDER_NOT_FOUND)
        // 2.orderDetail 转换成 ShoppingCart
        val shoppingCartList = orderDetailList.map { orderDetail ->
            val shoppingCart = ShoppingCart{
                this.dishId = orderDetail.dishId
                this.setmealId = orderDetail.setmealId
                this.name = orderDetail.name
                this.image = orderDetail.image
                this.amount = orderDetail.amount
                this.number = orderDetail.number
                this.dishFlavor = orderDetail.dishFlavor
                this.userId = StpUtil.getLoginIdAsLong()
                this.createTime = LocalDateTime.now()
            }
            shoppingCartRepository.save(shoppingCart, SaveMode.INSERT_ONLY)
        }
        return true
    }

    /**
     * 订单分页查询
     * @param ordersPageQueryDTO 订单分页查询DTO
     * @return Page<OrderVO> 订单分页查询结果
     */
    fun pageQuery(ordersPageQueryDTO: OrdersPageQueryDTO): Page<OrderVO> {
        logger.info("订单分页查询: $ordersPageQueryDTO")
        // 1. 查询订单
        val orderVO =
            orderRepository.findByUserIdLikeOrStatusIsOrNumberLikeOrPhoneLikeOrOrderTimeBetween(
                ordersPageQueryDTO.userId,
                ordersPageQueryDTO.status,
                ordersPageQueryDTO.number,
                ordersPageQueryDTO.phone,
                ordersPageQueryDTO.beginTime,
                ordersPageQueryDTO.endTime,
                PageRequest.of(ordersPageQueryDTO.page, ordersPageQueryDTO.pageSize),
                OrderVO::class
            )
        // 将每一条订单菜品信息拼接为字符串（格式：宫保鸡丁*3；）
        orderVO.content.forEach { order ->
            val orderDetails = orderDetailRepository.findOrderDetailsByOrderId(order.id)
            val dishNames = orderDetails?.joinToString(";") { "${it.name}*${it.number}" }
            order.copy(
                orderDishes = dishNames ?: "无菜品信息"
            )
        }
        return orderVO
    }

    /**
     * 各个状态的订单数量统计
     * @return OrderStatusCountDTO 订单状态数量统计DTO
     */
    fun countOrderStatus(): OrderStatisticsVO {
        logger.info("统计各个状态的订单数量")
        val toBeConfirmed = orderRepository.countByStatusEquals(StatusConstant.TO_BE_CONFIRMED)
        val confirmed = orderRepository.countByStatusEquals(StatusConstant.CONFIRMED)
        val deliveryInProgress = orderRepository.countByStatusEquals(StatusConstant.DELIVERY_IN_PROGRESS)
        return OrderStatisticsVO(
            confirmed = toBeConfirmed,
            toBeConfirmed = confirmed,
            deliveryInProgress = deliveryInProgress,
        )
    }

    /**
     * 接单
     * @param ordersConfirmDTO 订单确认DTO
     * @return Boolean 接单是否成功
     */
    fun confirmOrder(ordersConfirmDTO: OrdersConfirmDTO): Boolean {
        orderRepository.updateOrderStatus(
            ordersConfirmDTO.id!!, StatusConstant.CONFIRMED
        )
        return true
    }

    /**
     * 拒单
     * @param ordersRejectDTO 订单拒绝DTO
     */
    fun rejectOrder(ordersRejectDTO: OrdersRejectionDTO): Boolean {
        logger.info("拒单: id={}, rejectionReason={}", ordersRejectDTO.id, ordersRejectDTO.rejectionReason)
        return handleOrderCancellation(ordersRejectDTO.id!!, ordersRejectDTO.rejectionReason!!, true)
    }

    /**
     * 取消订单
     * @param ordersCancelDTO 订单取消DTO
     * @return Boolean 取消订单是否成功
     */
    fun cancelOrder(ordersCancelDTO: OrdersCancelDTO): Boolean {
        logger.info("取消订单: id={}, cancelReason={}", ordersCancelDTO.id, ordersCancelDTO.cancelReason)
        return handleOrderCancellation(ordersCancelDTO.id!!, ordersCancelDTO.cancelReason!!, false)
    }

    private fun handleOrderCancellation(orderId: Long, reason: String, isRejection: Boolean): Boolean {
        val order = orderRepository.findNullable(orderId)
            ?: throw OrderBusinessException(MessageConstant.ORDER_NOT_FOUND).also {
                logger.error("订单不存在: id=$orderId")
            }

        if (order.status != StatusConstant.TO_BE_CONFIRMED) {
            throw OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR).also {
                logger.error("订单状态错误: id=$orderId, status=${order.status}")
            }
        }

        if (order.payStatus == StatusConstant.PAID) {
            logger.info("订单已支付，进行退款处理: id=$orderId")
            // FIXME: 实现微信退款(WXPayRefundRequest)
            TODO("WXPayRefundRequest") // 实现微信退款
        }

        orderRepository.save(order.copy {
            this.status = StatusConstant.CANCELLED
            if (isRejection) {
                this.rejectionReason = reason
            } else {
                this.cancelReason = reason
            }
            this.cancelTime = LocalDateTime.now()
        }, SaveMode.UPDATE_ONLY)
        return true
    }

    /**
     * 派送订单
     * @param id 订单Id
     */
    fun deliveryOrder(id: Long): Boolean {
        logger.info("派送订单: id=$id")
        val order = orderRepository.findNullable(id)
            ?: throw OrderBusinessException(MessageConstant.ORDER_NOT_FOUND)
        if (order.status != StatusConstant.CONFIRMED) {
            throw OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR)
        }
        // 更新订单状态为派送中
        orderRepository.updateOrderStatus(
            id, StatusConstant.DELIVERY_IN_PROGRESS
        ).also { logger.info("订单状态更新为派送中: id=$id") }
        return true
    }

    /**
     * 完成订单
     * @param id 订单Id
     * @return Boolean 完成订单是否成功
     */
    fun completeOrder(id: Long): Boolean {
        logger.info("完成订单: id=$id")
        val order = orderRepository.findNullable(id)
            ?: throw OrderBusinessException(MessageConstant.ORDER_NOT_FOUND)
        if (order.status != StatusConstant.DELIVERY_IN_PROGRESS) {
            throw OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR)
        }
        // 更新订单状态为已完成
        orderRepository.save(order.copy {
            this.status = StatusConstant.COMPLETED
            this.deliveryTime = LocalDateTime.now()
        }, SaveMode.UPDATE_ONLY).also { logger.info("订单状态更新为已完成: id=$id") }
        return true
    }

    /**
     * 客户催单
     * @param id 订单Id
     * @return Boolean 催单是否成功
     */
    fun urgeOrder(id: Long): Boolean {
        logger.info("客户催单: id=$id")
        val order = orderRepository.findNullable(id)
            ?: throw OrderBusinessException(MessageConstant.ORDER_NOT_FOUND)
        // 检查订单状态是否为派送中 意思是 订单正在派送中才能催单
//        if (order.status != StatusConstant.DELIVERY_IN_PROGRESS) {
//            throw OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR)
//        }
        // 通过websocket向客户端浏览器推送消息 type orderId content
        hashMapOf(
            "type" to 2,
            "orderId" to id,
            "content" to "订单号+${order.number}"
        ).let { webSocketServer.sendToAllClient(ObjectMapper().writeValueAsString(it)) }
        return true
    }

    /**
     * 检查客户的收货地址是否超出配送范围
     * @param address 用户收货地址
     */
    private fun checkOutOfRange(address: String?) {
        val map = HashMap<String?, Any?>()
        map.put("address", kjSkyProperties.shop.address)
        map.put("output", "json")
        map.put("ak", kjSkyProperties.baidu.ak)

        //获取店铺的经纬度坐标
        val shopCoordinate: String? = HttpUtil.get("https://api.map.baidu.com/geocoding/v3", map)
        //使用jackson将字符串转换为JSON对象
        var jsonObject = ObjectMapper().readTree(shopCoordinate)
        jsonObject.get("status").equals("0").takeIf { it } ?: run {
            throw OrderBusinessException("店铺地址解析失败")
        }
        //数据解析
        var location: JsonNode? = jsonObject.get("result").get("location")
        var lat: String? = location?.get("lat")?.textValue()
        var lng: String? = location?.get("lng")?.textValue()
        //店铺经纬度坐标
        val shopLngLat = "$lat,$lng"

        map.put("address", address)
        //获取用户收货地址的经纬度坐标
        val userCoordinate: String? =  HttpUtil.get("https://api.map.baidu.com/geocoding/v3", map)

        jsonObject = ObjectMapper().readTree(userCoordinate)
        jsonObject.get("status").equals("0").takeIf { it } ?: run {
            throw OrderBusinessException("用户地址解析失败")
        }
        //数据解析
        location = jsonObject.get("result").get("location")
        lat = location.get("lat").textValue()
        lng = location.get("lng").textValue()
        //用户收货地址经纬度坐标
        val userLngLat = "$lat,$lng"
        map.put("origin", shopLngLat)
        map.put("destination", userLngLat)
        map.put("steps_info", "0")
        //路线规划
        val json: String? = HttpUtil.get("https://api.map.baidu.com/directionlite/v1/driving", map)

        jsonObject = ObjectMapper().readTree(json)
        jsonObject.get("status").equals("0").takeIf { it } ?: run {
            throw OrderBusinessException("路线规划失败")
        }
        //数据解析
        val result = jsonObject.get("result")
        val jsonArray = result.get("routes")
        val distance = (jsonArray.get(0)).get("distance").toString().toInt()
        if (distance > 5000) {
            //配送距离超过5000米
            throw OrderBusinessException("超出配送范围")
        }
    }

}
