package com.tianqiauto.textile.weaving.model.sys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * @ClassName TV_ZhanShiYeMian
 * @Description TODO
 * @Author lrj
 * @Date 2019/3/29 16:39
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "sys_tv_zhanshiyemian")
@EqualsAndHashCode(exclude = {"fangAns"})
@ToString(exclude = {"fangAns"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TV_ZhanShiYeMian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;//页面名称

    private String url;//页面跳转地址

    private Integer tingliushichang;//停留时长秒

    private Integer sort;//排序号

    @Column
    @CreatedDate
    private Date createDate;
    @Column
    @LastModifiedDate
    private Date lastModifiedDate;
    @Column
    @CreatedBy
    private String createdBy;
    @Column
    @LastModifiedBy
    private String modifiedBy;
//    @Column
//    @Version
//    private Long version;

    @JsonIgnoreProperties("zhanShiYeMians")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_tv_ym_plan", joinColumns = @JoinColumn(name = "zhanshiyemian_id"),
            inverseJoinColumns = @JoinColumn(name = "dianshifangan_id"))
    private Set<TV_DianShiFangAn> fangAns;




}
