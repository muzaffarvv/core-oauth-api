package uz.zero.payment

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
@Table(
    name = "payments",
    indexes = [Index(name = "idx_payment_user", columnList = "userId")]
)
class Payment(
    @Column(nullable = false)
    var userId: Long,

    @OneToMany(mappedBy = "payment", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var items: MutableList<ProductItem> = mutableListOf(),

    @Column(nullable = false, precision = 19, scale = 2)
    var totalAmount: BigDecimal = BigDecimal.ZERO
) : BaseEntity()

@Entity
@Table(name = "product_items")
class ProductItem(
    @Column(nullable = false)
    var productId: Long,

    @Column(nullable = false)
    var productName: String,

    @Column(nullable = false)
    var quantity: Int,

    @Column(nullable = false, precision = 19, scale = 2)
    var unitPrice: BigDecimal,

    @Column(nullable = false, precision = 19, scale = 2)
    var totalPrice: BigDecimal, //  unitPrice * quantity

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    var payment: Payment
) : BaseEntity()