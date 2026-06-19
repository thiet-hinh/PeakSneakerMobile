package com.example.shoestore.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "user")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "district_id")
    private Integer districtId;

    @Column(name = "district_name", length = 100)
    private String districtName;

    @Column(name = "province_id")
    private Integer provinceId;

    @Column(name = "province_name", length = 100)
    private String provinceName;

    @Column(name = "ward_id")
    private Integer wardId;

    @Column(name = "ward_name", length = 100)
    private String wardName;

    @Column(name = "street_detail")
    private String streetDetail;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Builder.Default
    @Column(name = "is_default")
    private Boolean isDefault = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_address_user"))
    private User user;
}
