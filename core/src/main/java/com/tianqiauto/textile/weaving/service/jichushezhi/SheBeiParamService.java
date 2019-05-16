package com.tianqiauto.textile.weaving.service.jichushezhi;


import com.tianqiauto.textile.weaving.model.sys.Param;
import com.tianqiauto.textile.weaving.util.procedure.core.ProcedureParamUtlis;
import com.tianqiauto.textile.weaving.util.procedure.model.ProcedureContext;
import com.tianqiauto.textile.weaving.util.procedure.service.BaseService;
import com.tianqiauto.textile.weaving.util.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SheBeiParamService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BaseService baseService;

    //根据工序机型参数类别查询参数
    public Result findAll(String gx_id, String jx_id, String cslb_id){
        ProcedureParamUtlis ppu=new ProcedureParamUtlis();
        ppu.addInVarchar(gx_id).addInVarchar(jx_id).addInVarchar(cslb_id);
        ProcedureContext pro=baseService.callProcedure("pc_base_shebei_param", ppu.getList());
        return Result.ok(pro.getDatas());
    }

    //设备参数是否已存在
    public boolean existsByNameAndLeiBie(String name,Long lbid,Long id){
        String sql = "select count(*) from sys_param where name=? and leibie_id=? and id != ?";
        int num = jdbcTemplate.queryForObject(sql,Integer.class,name,lbid,id);
        if(num>0)
            return false;
        return true;
    }

    //修改设备参数
    public int updSheBeiParam(Param param){
        String sql = "update sys_param set name=?,danwei=?,leibie_id=?,cunchuzhouqi=?,cunchushichang=?,xuhao=? where id=?";

        String name = StringUtils.isEmpty(param.getName())?null:param.getName();
        String dw = StringUtils.isEmpty(param.getDanwei())?null:param.getDanwei();
        Integer cczq = param.getCunchuzhouqi();
        Integer ccsc = param.getCunchushichang();
        Integer xh = param.getXuhao();

        int num = jdbcTemplate.update(sql,name,dw,param.getLeiBie().getId(),cczq,ccsc,xh,param.getId());
        return num;
    }

    //修改是否报警，是否展示，是否记录历史曲线
    public int updSheBeiParam_flag(String id,String name,String val){
        String sql = "update sys_param set "+name+"=? where id=?";
        int num = jdbcTemplate.update(sql,val,id);
        return num;
    }

    //批量修改设备参数

    public Result updSheBeiParam_Batch(String idStr,String dw,
                                       String cslb_id, String ccsc, String cczq,
                                       String sfbj,String sfzs,String sfjlqx){
        ProcedureParamUtlis ppu=new ProcedureParamUtlis();
        ppu.addInVarchar(idStr).addInVarchar(dw).addInVarchar(cslb_id).addInVarchar(ccsc).addInVarchar(cczq)
                .addInVarchar(sfbj).addInVarchar(sfzs).addInVarchar(sfjlqx)
                .addOut();
        ProcedureContext pro=baseService.callProcedure("pc_upd_shebei_param", ppu.getList());
        return Result.ok(pro.getDatas());
    }



}
