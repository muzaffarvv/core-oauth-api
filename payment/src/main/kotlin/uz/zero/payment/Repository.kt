package uz.zero.payment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long> {
}

@Repository
interface PaymentRepository : BaseRepository<Payment> {
    fun findByIdAndDeletedFalse(id: Long): Payment?
    fun findAllByUserIdAndDeletedFalse(userId: Long): List<Payment>
    fun findAllByDeletedFalse(): List<Payment>
}

@Repository
interface ProductItemRepository : BaseRepository<ProductItem>