
    $.ajax({
        url:"http://localhost:8080/tv/findJiangSha_JiHua",
        type:"GET",
        dataType:'json',
        data:{},
        success: function(res){
            var ban=res.data.datas;
           // $("#banci").html(ban[0].banci);
           // $("#lunban").html(ban[0].lunban);
            var data=res.data.columns;
            var item;
            $.each(data,function (i, result) {
                item="<tr><td>"+result['xuhao']+"</td><td>"+result['riqi']+"</td><td>"+result['banci']+"</td><td>"+result['ganghao']+"</td><td>"+result['heyuehao']+"</td><td>"+result['zhoukuan']+"</td><td>"+result['zhoushu']+"</td><td>已完成"+result['status']+"个轴</td><td>";
                $('#body').append(item);
            })
        }
    });




