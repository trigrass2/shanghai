window.onload=function(){
				  setInterval(function(){
				   var date=new Date();
				   var year=date.getFullYear(); //获取当前年份
				   var mon=date.getMonth()+1; //获取当前月份
				   var da=date.getDate(); //获取当前日
				   var day=date.getDay(); //获取当前星期几
				   var h=date.getHours(); //获取小时
				   var m=date.getMinutes(); //获取分钟
				   var s=date.getSeconds(); //获取秒
				   if (s<10){
				   	  s="0"+s;
				   }
				   if (m<10){
				   	  m="0"+m;
				   }
				   if (h<10){
				   	  h="0"+h;
				   }
				   switch(day){
					  case 0:
						  day ="日";
					  break;
					  case 1:
						  day ="一";
					  break;
					  case 2:
						  day ="二";
					  break;
					  case 3:
						  day ="三";
					  break;
					  case 4:
						  day ="四";
					  break;
					  case 5:
						  day ="五";
					  break;
					  case 6:
						  day ="六";
					  break;
					}
				   var d=document.getElementById('Date'); 
				   var shi=document.getElementsByClassName('s')[0];
				   var fen=document.getElementsByClassName('f')[0];
				   var miao=document.getElementsByClassName('m')[0];
				   d.innerHTML=year+'年'+mon+'月'+da+'日'+'&nbsp;&nbsp;'+'星期'+day+'&nbsp;&nbsp;'+h+':'+m+':'+s;
				  },500) 
				 }