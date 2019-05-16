package com.tianqiauto.textile.weaving.model.sys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * @ClassName GongYi_Zhengjing_FenTiao
 * @Description TODO
 * @Author xingxiaoshuai
 * @Date 2019-04-13 09:59
 * @Version 1.0
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "sys_gongyi_zhizao")
@EqualsAndHashCode(exclude = {"gongYi","gongYi_paramValues"})
@ToString(exclude = {"gongYi","gongYi_paramValues"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GongYi_ZhiZao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties("gongYi_zhiZaoSet")
    @ManyToOne
    @JoinColumn(name = "gongyi_id")
    private GongYi gongYi;



    private Integer isCurrent;  //当前使用工艺



    private String jishangweimi;  //机上纬密

    private Double luobuchangdu;  //落布长度



    //这三个从订单表同步过来，可以让用户进行修改
    private String pibuyaoqiu; //坯布要求
    private String chengbaoyaoqiu;  //成包要求
    private String baozhuangmaitou; //包装唛头


    @OneToMany
    @JoinColumn(name = "gongyi_id")
    private Set<GongYi_ParamValue> gongYi_paramValues;






}
