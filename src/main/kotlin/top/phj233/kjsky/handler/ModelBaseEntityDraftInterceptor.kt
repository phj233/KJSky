package top.phj233.kjsky.handler

import cn.dev33.satoken.stp.StpUtil
import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.DraftInterceptor
import org.springframework.stereotype.Component
import top.phj233.kjsky.model.ModelBase
import top.phj233.kjsky.model.ModelBaseDraft
import java.time.LocalDateTime

/**
 * 基础实体拦截器
 */
@Component
class ModelBaseEntityDraftInterceptor : DraftInterceptor<ModelBase, ModelBaseDraft> {
    override fun beforeSave(draft: ModelBaseDraft, original: ModelBase?) {
        if (!isLoaded(draft, ModelBase::updateTime)) {
            draft.updateTime = LocalDateTime.now()
        }
        if (original === null) {
            if (!isLoaded(draft, ModelBase::createTime)) {
                draft.createTime = LocalDateTime.now()
            }
        }
        if (!isLoaded(draft, ModelBase::updateUser)) {
            StpUtil.isLogin().takeIf { it }?.let {
                StpUtil.getLoginIdAsLong().let { empId ->
                    draft.updateUser = empId
                }
            } ?: run {
                draft.updateUser = null // 如果未登录，则不设置更新用户
            }
        }
        if (original === null) {
            if (!isLoaded(draft, ModelBase::createUser)) {
                StpUtil.isLogin().takeIf { it }?.let {
                    StpUtil.getLoginIdAsLong().let { empId ->
                        draft.createUser = empId
                    }
                } ?: run {
                    draft.createUser = null // 如果未登录，则不设置创建用户
                }
            }
        }
    }
}
