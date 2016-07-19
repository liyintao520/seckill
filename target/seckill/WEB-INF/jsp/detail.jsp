<%@ page contentType="text/html; charset=UTF-8"%>
<!-- 引入jstl -->
<%@include file="common/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>秒杀详情页</title>
<%@include file="common/head.jsp"%>
</head>
<body>
	<div class="container">
		<div class="panel panel-default text-center">
			<div class="panel-heading">
				<h1>${seckill.name }</h1>
			</div>
		</div>
		<div class="panel-body">
			<h2 class="text-danger">
				<!-- 显示time图标 -->
				<span class="glyphicon glyphicon-time"></span>
				<!-- 展示倒计时 -->
				<span class="glyphicon" id="seckill-box"></span>
			</h2>
		</div>
	</div>
	
	<!-- 登录弹出层，输入电话（正常吧情况应该有登录逻辑的，但是这个项目关心秒杀逻辑，所有在前台简单的设计了一下） -->
	<!-- 默认是隐藏的   modal fade -->
	<div id="killPhoneModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h3 class="modal-title text-center">
						<span class="glyphicon glyphicon-phone"></span>秒杀电话;
					</h3>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-xs-offset-2">
							<input type="text" name="killPhone" id="killPhoneKey" 
								placeholder="填写手机号" class="form-control" />
						</div>
					</div>
				</div>
				
				<div class="modal-footer">
					<!-- 验证信息 -->
					<span id="killPhoneMessage" class="glyphicon"></span>
					<button type="button" id="killPhoneBtn" class="btn btn-success">
						<span class="glyphicon glyphicon-phone"></span>
						提交【Submit】
					</button>
				</div>
			</div>
		</div>
	</div>
</body>
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script
	src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>

<!-- 使用CDN 获取公共的js  http://www.bootcdn.cn/ -->
<!-- JQuery cookie操作插件 -->
<script src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>

<!-- JQuery countDown倒计时插件 -->
<script src="http://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>


<!-- 开始编写交互逻辑 -->

<script type="text/javascript" src="/resources/script/seckill.js"></script>

<script type="text/javascript">
	$(function(){
		//使用EL表达式传入参数
		seckill.detail.init({
			seckillId : ${seckill.seckillId },
			startTime : ${seckill.startTime.time },//拿到的是毫秒时间startTime.time
			endTime : ${seckill.endTime.time }
		});
	});
</script>






</html>