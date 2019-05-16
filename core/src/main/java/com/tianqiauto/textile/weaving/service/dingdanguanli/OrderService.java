package com.tianqiauto.textile.weaving.service.dingdanguanli;

import com.tianqiauto.textile.weaving.model.base.Dict;
import com.tianqiauto.textile.weaving.model.base.Dict_Type;
import com.tianqiauto.textile.weaving.model.base.User;
import com.tianqiauto.textile.weaving.model.sys.Heyuehao;
import com.tianqiauto.textile.weaving.model.sys.Order;
import com.tianqiauto.textile.weaving.repository.Dict_TypeRepository;
import com.tianqiauto.textile.weaving.repository.HeYueHaoRepository;
import com.tianqiauto.textile.weaving.repository.OrderRepository;
import com.tianqiauto.textile.weaving.repository.UserRepository;
import com.tianqiauto.textile.weaving.util.JPASql.Container;
import com.tianqiauto.textile.weaving.util.JPASql.DynamicUpdateSQL;
import com.tianqiauto.textile.weaving.util.model.ModelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author bjw
 * @Date 2019/3/14 9:55
 */
@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private HeYueHaoRepository heYueHaoRepository;

    @Autowired
    private Dict_TypeRepository dict_typeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    public Order save(Order order) {
        //坯布规格=入库规格=【幅宽/经密/纬密/经纱成分/经纱支数/纬纱支数/纬纱支数/特殊要求】拼接而成
        Set<Heyuehao> temp = new HashSet<>();
        Set<Heyuehao> heyuehaos = order.getHeyuehaos();
        if(null != heyuehaos){
            for (Heyuehao heyuehao:heyuehaos){
                temp.add(heYueHaoRepository.save(heyuehao));
            }
        }
        order.setHeyuehaos(temp);
        User jingli = userRepository.getOne(order.getJingli().getId());
        User yingxiaoyuan = userRepository.getOne(order.getYingxiaoyuan().getId());
        order.setJingli(jingli);
        order.setYingxiaoyuan(yingxiaoyuan);
        order.setDeleted(0);
        return orderRepository.save(order);
    }

    public void deleteById(Long id) {
//        orderRepository.deleteById(id);
        String sql = "update sys_order SET deleted = 1 WHERE id = ?";
        jdbcTemplate.update(sql,id);

    }

    public Order findByid(Long id){
        return orderRepository.findById(id).get();
    }

    public Page<Order> findAll(Order order,Pageable pageable){
        ModelUtil mu = new ModelUtil(order);
        Specification<Order> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList();
            //开始日期和结束日期
            if(!StringUtils.isEmpty(order.getXiadankaishiriqi()) || !StringUtils.isEmpty(order.getXiadanjieshuriqi())) {
                predicates.add(criteriaBuilder.between(root.get("jiaohuoriqi"), order.getXiadankaishiriqi(),order.getXiadanjieshuriqi()));
            }
            //订单号
            if(!StringUtils.isEmpty(order.getDingdanhao())) {
                predicates.add(criteriaBuilder.like(root.get("dingdanhao"), "%" + order.getDingdanhao() + "%"));
            }
            //订单状态
            if(!mu.paramIsEmpty("status.value")) {
                predicates.add(criteriaBuilder.equal(root.get("status").get("value"), order.getStatus().getValue()));
            }
            //客户信息
            if(!mu.paramIsEmpty("kehuxinxi.value")) {
                predicates.add(criteriaBuilder.equal(root.get("kehuxinxi").get("value"),order.getKehuxinxi().getId()));
            }
            predicates.add(criteriaBuilder.equal(root.get("deleted"),0));//查询未删除的
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return orderRepository.findAll(specification,pageable);
    }

    public int update(Order order) {
        Container RUSql = new DynamicUpdateSQL<>(order).getUpdateSql();
        return jdbcTemplate.update(RUSql.getSql(),RUSql.getParam());
    }

    public Dict findByTypenameAndValue(String codeType,String value){
        Dict dictR = new Dict();
        Dict_Type dict_types = dict_typeRepository.findByCode(codeType);
        for(Dict dict:dict_types.getDicts()){
            if(value.equals(dict.getValue())){
                dictR.setId(dict.getId());
                return dictR;
            }
        }
        return dictR;
    }

    public List<Order> findByDingdanhao(String dingdanhao) {
        return orderRepository.findAllByDingdanhaoAndDeleted(dingdanhao,0);
    }
}
