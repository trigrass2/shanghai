layui.define(['table', 'laydate', 'form', 'upload'], function (exports) {
    var $ = layui.$
        , table = layui.table
        , form = layui.form
        , upload = layui.upload
        , laydate = layui.laydate;

    //查询表头部初始化
    var initSele = [
        {eleId:'chengpindengji_sele',dictCode:'chengpindengji',val:'id'},
        {eleId:'status_sele',dictCode:'cp_rukushenqingzhuangtai',val:'id'}
    ];
    InitSelect('heyuehao_sele', null, 'dingdanguanli/heyuehaoguanli/findAll', 'get', {}, 'name', 'id');
    dictInitSele(initSele,false);
    form.render();
    // var date = new Date();
    // laydate.render({
    //     elem: '#kaishiriqi_sele',
    //     value: date
    // });
    // laydate.render({
    //     elem: '#jieshuriqi_sele',
    //     value: (date.getFullYear()-1)+'-'+(date.getMonth()+1)+'-'+date.getDate()
    // });

    //监听搜索
    form.on('submit(form_search)', function (data) {
        var field = data.field;
        table.reload('table', {
            where: field
        });
        return false;
    });

    //设置表格头
    var cols = [[
        {field: 'id', title: 'id', hide: true}
        , {title: '申请时间',field: 'createTime', sort: true}
        , {title: '合约号',templet: repNull('chengpinruku.heyuehao.name'),field: 'chengpinruku.heyuehao.name', sort: true}
        , {title: '申请状态',templet: repNull('status.name'), field: 'status.name',sort: true}
        , {title: '成品来源',templet: repNull('chengpinruku.laiyuan.name')}
        , {title: '成品等级',templet: repNull('chengpinruku.chengpindengji.name')}
        , {title: '申请长度', field: 'changdu',sort: true}
        , {title: '申请备注',field: 'beizhu'}
        , {title: '实际长度',templet: repNull('chengpinruku.changdu')}
        , {title: '确认备注',templet: repNull('chengpinruku.beizhu')}
        , {title: '确认时间',templet: repNull('chengpinruku.cangkuquerenshijian')}
        , {title: '仓库确认人',templet: repNull('chengpinruku.cangkuquerenren.xingming')}
        , {title: '操作', toolbar: '#caozuo', fixed: 'right',width:100}
    ]];

    //初始化表格
    initTable("table", 'chengpinguanli/chengpinrukuqueren/query_page', 'get', cols, table,"from");

    //监听操作列
    table.on('tool(table)', function (obj) {
        var data = obj.data;
       if (obj.event === 'edit') {
           InitSelect('cangkuquerenren_edit', null, 'dingdanguanli/dingdanguanli/getUser', 'get', {}, 'ghxm', 'id');
            form.render();
            editI = layer.open({
                type: 1
                , title: '登记成品入库申请信息'
                , content: $('#div_form_edit')
                , area: ['50%', '50%']
                , btn: ['确认', '取消']
                , btn1: function (editIndex, layero) {
                    form.on('submit(form_edit_submit)', function (data) {
                        layer.confirm('确认要登记成品入库信息吗?'
                            , function (i) {
                                var formData = data.field;
                                encObject(formData);
                                $.ajax({
                                    url: layui.setter.host + 'chengpinguanli/chengpinrukuqueren/update',
                                    contentType: "application/json;charset=utf-8",
                                    type: 'POST',
                                    data: JSON.stringify(formData),
                                    success: function (data) {
                                        ajaxSuccess(data, table);
                                    }
                                });
                                layer.close(i);
                                layer.close(editI);
                            });
                    });
                    $("#form_edit_submit").trigger('click');
                }
                , success: function () {
                    fromSetVel(form, 'form_edit', data);
                }
            })
        }
    });

    /**
     * 2019/03/23 bjw
     * 添加和更新处理关联对象
     * @param obj
     */
    function encObject(obj) {
        $.each(obj, function (key, val) {
            val = !val ? "''" : "'"+val+"'";
            var arr = key.split('.');
            if (arr.length <= 1) {
                return true;
            }
            var textObj = 'obj';
            var currentObj = obj; //当前对象
            for (var i = 0; i < arr.length; i++) {
                if (i == arr.length - 1) {
                    eval(textObj + "." + arr[i] + "=" + val);
                } else {
                    textObj += '.' + arr[i];
                    if (currentObj[arr[i]] == undefined || null == currentObj[arr[i]]) {
                        eval(textObj + '= {}');
                    }
                }
            }
        });
    }

    /**
     * 2019/03/24 bjw
     * 处理根据对象默认select选择列表。
     * @param formId  表单div的id
     * @param ObjVal  值对象
     */
    function fromSetVel(from, formId, data) {
        var arrObj = $('#' + formId).find(":input[name *= '.']");
        for (var i = 0; i < arrObj.length; i++) {
            var name = arrObj[i].name;
            var arr = name.split('.');
            if (arr.length <= 1) continue;
            var currentObj = data;
            for (var j = 0; j < arr.length; j++) {
                if (currentObj != undefined && null != currentObj)
                    currentObj = currentObj[arr[j]]
            }
            if (currentObj != undefined && null != currentObj)
                data[name] = currentObj;
        }
        form.val(formId, data);
    }

    tq_verify(form);//给form添加自定义校验

    /**
     * 渲染select
     * @param eleId select原素id
     * @param selectedId 默认选中值 value
     * @param url 查询值的路径
     * @param type 类型
     * @param data 查询的参数
     * @param key   取值的key-name
     * @param val   取值的val-name
     * @constructor
     */
    function InitSelect(eleId, selectedId, url, type, data, key, val) {
        $.ajax({
            url: layui.setter.host + url,
            async: false,
            data: data,
            type: type,
            success: function (data) {
                initDownList(data, eleId, selectedId, key, val, true);
                form.render();
            }
        });
    }


    exports('chengpinrukuqueren', {})
});