package uz.zero.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long> {
    fun findAllByDeletedFalse(): List<T>
    fun findByCodeAndDeletedFalse(code: String): Product?
    fun existsByNameIgnoreCaseAndDeletedFalse(name: String): Boolean
    fun existsByCode(code: String): Boolean
}

@Repository
interface ProductRepository : BaseRepository<Product>
