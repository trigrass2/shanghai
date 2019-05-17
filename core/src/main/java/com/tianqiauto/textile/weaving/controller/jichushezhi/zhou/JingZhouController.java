package com.tianqiauto.textile.weaving.controller.jichushezhi.zhou;

import com.tianqiauto.textile.weaving.model.base.Dict;
import com.tianqiauto.textile.weaving.model.sys.Beam_JingZhou;
import com.tianqiauto.textile.weaving.model.sys.Beam_JingZhou_Current;
import com.tianqiauto.textile.weaving.repository.BeamjingzhoucurrentRepository;
import com.tianqiauto.textile.weaving.repository.JingZhouRepository;
import com.tianqiauto.textile.weaving.service.dingdanguanli.OrderService;
import com.tianqiauto.textile.weaving.util.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName JingZhouController
 * @Description TODO
 * @Author lrj
 * @Date 2019/3/21 10:31
 * @Version 1.0
 **/

@RestController
@RequestMapping("jichushuju/zhou/jingzhou")
@Api(description = "经轴管理")
public class JingZhouController {

    @Autowired
    JingZhouRepository jingZhouRepository;

    @Autowired
    BeamjingzhoucurrentRepository beamjingzhoucurrentRepository;

    @Autowired
    OrderService orderService;

    @GetMapping("findAllJingZhou")
    @ApiOperation(value = "查询经轴信息")
    public Result findAllJingZhou(Pageable pageable){
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.Direction.ASC, "zhouhao");
        Page<Beam_JingZhou> list = jingZhouRepository.findAll(pageRequest);
        return Result.ok("查询成功!",list);
    }

    @GetMapping("findAllJZBH")
    @ApiOperation(value = "查询经轴编号")
    public Result findAllJZBH(){
        List<Beam_JingZhou> list = jingZhouRepository.findAll();
        return Result.ok("查询成功!",list);
    }

    @PostMapping("updateJingZhou")
    @ApiOperation(value = "修改经轴信息")
    public Result updateJingZhou(@RequestBody Beam_JingZhou jingZhou){
        String beizhu = StringUtils.isEmpty(jingZhou.getBeizhu())?null:jingZhou.getBeizhu();
        jingZhouRepository.updateJingZhou(beizhu, jingZhou.getId());
        return Result.ok("修改成功",jingZhou);
    }

    @PostMapping("addJingZhou")
    @ApiOperation(value = "新增经轴")
    public Result addJingZhou(@RequestBody Beam_JingZhou jingZhou){
        boolean flag = jingZhouRepository.existsByZhouhao(jingZhou.getZhouhao());
        if(!flag){
            Beam_JingZhou new_jingzhou = jingZhouRepository.save(jingZhou);
            Beam_JingZhou_Current cur_jingzhou = new Beam_JingZhou_Current();
            cur_jingzhou.setJingZhou(new_jingzhou);

            Dict dict = orderService.findByTypenameAndValue("jingzhouzhuangtai","10");
            cur_jingzhou.setStatus(dict);
            beamjingzhoucurrentRepository.save(cur_jingzhou);
            return Result.ok("新增成功",jingZhou);
        }else{
            return Result.result(666,"经轴号已存在",jingZhou);
        }
    }

    @PostMapping("deleteJingZhou")
    @ApiOperation(value = "删除经轴")
    public Result deleteJingZhou(@RequestBody Beam_JingZhou jingZhou){
        Beam_JingZhou_Current cur_jingzhou = beamjingzhoucurrentRepository.findByJingZhou(jingZhou);
        beamjingzhoucurrentRepository.delete(cur_jingzhou);
        jingZhouRepository.delete(jingZhou);
        return Result.ok("删除成功",jingZhou);
    }


}
