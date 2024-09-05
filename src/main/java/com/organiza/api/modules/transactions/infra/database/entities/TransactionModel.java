    package com.organiza.api.modules.transactions.infra.database.entities;

    import com.organiza.api.modules.users.infra.database.entities.UserModel;
    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import org.springframework.data.annotation.CreatedBy;
    import org.springframework.data.annotation.CreatedDate;
    import org.springframework.data.annotation.LastModifiedBy;
    import org.springframework.data.annotation.LastModifiedDate;
    import org.springframework.data.jpa.domain.support.AuditingEntityListener;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.Objects;
    import java.util.UUID;


    @Getter
    @Setter
    @NoArgsConstructor
    @Entity
    @EntityListeners(AuditingEntityListener.class)
    @Table(name = "tb_transactions")
    public class TransactionModel {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private UserModel user;

        @Column(name = "category", nullable = false, length = 150)
        private String category;

        @Enumerated(EnumType.STRING)
        @Column(name = "type_transaction", nullable = false, length = 10)
        private Type type;

        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false, length = 8)
        private Status status;

        public enum Type {
            RECEITA, DESPESA
        }

        public enum Status {
            PENDING, FINISHED
        }

        @Column(name = "value", nullable = false)
        private double value;

        @Column(name = "description", nullable = false, length = 200)
        private String description;

        @Column(name = "date_payment", nullable = false)
        private LocalDate date_payment;

        @CreatedDate
        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @LastModifiedDate
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @CreatedBy
        @Column(name = "created_by")
        private String createdBy;

        @LastModifiedBy
        @Column(name = "updated_by")
        private String updatedBy;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TransactionModel that = (TransactionModel) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }

        @Override
        public String toString() {
            return "Transactions{" +
                    "id=" + id +
                    '}';
        }
    }
