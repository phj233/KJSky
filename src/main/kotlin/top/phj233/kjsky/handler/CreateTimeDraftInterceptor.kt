package top.phj233.kjsky.handler

import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.DraftInterceptor
import org.springframework.stereotype.Component
import top.phj233.kjsky.model.CreateTimeBase
import top.phj233.kjsky.model.CreateTimeBaseDraft
import java.time.LocalDateTime

/**
 * 创建时间拦截器
 * @author phj233
 * @since 2025/6/30 20:42
 * @version
 */
@Component
class CreateTimeDraftInterceptor : DraftInterceptor<CreateTimeBase, CreateTimeBaseDraft> {
    override fun beforeSave(
        draft: CreateTimeBaseDraft,
        original: CreateTimeBase?
    ) {
        if (original === null) {
            if (!isLoaded(draft, CreateTimeBase::createTime)) {
                draft.createTime = LocalDateTime.now()
            }
        }
    }
}
