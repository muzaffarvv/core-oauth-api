package uz.zero.product

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.io.Serializable
import java.util.Date

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class BaseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @CreatedDate @Temporal(TemporalType.TIMESTAMP)
    var createdDate: Date? = null,

    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP)
    var modifiedDate: Date? = null,

    @Column(nullable = false) @ColumnDefault(value = "false")
    var deleted: Boolean = false
) : Serializable

@Entity
@Table(name = "products")
class Product(
    @Column(nullable = false, length = 72)
    var name: String,

    @Column(nullable = false, unique = true, length = 20)
    var code: String,

    @Column(nullable = false)
    var price: BigDecimal? = BigDecimal.ZERO,

    @Column(length = 120)
    var description: String? = null,

    @Column(nullable = false)
    var stockQuantity: Int = 0

) : BaseEntity()



