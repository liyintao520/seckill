//存放主要交互逻辑js代码
// JavaScript 模块化
var seckill = {
		//封装秒杀相关 ajax 的 url
		URL : {
			now : function(){
				return "/seckill/time/now";
			},
			exposer : function(seckillId){
				return "/seckill/" + seckillId + "/exposer";
			},
			execution : function(seckillId, md5){
				return "/seckill/" + seckillId "/" + md5 + "/execution";
			}
			
		},
		//获取秒杀地址，控制现实逻辑，执行秒杀
		handleSeckillkill : function(seckillId, node){
			//处理秒杀逻辑
			node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
			$.post(seckill.URL.exposer(seckillId), {}, function(result){
				//在回调函数中，执行交互流程
				if(result && result['success']){
					var exposer = result['data'];
					if(exposer['exposed']){
						//开启秒杀
						//获取秒杀地址
						var md5 = exposer['md5'];
						var killUrl = seckill.URL.execution(seckillId, md5);
						
						console.log("killUrl ： " + killUrl);
						//绑定一次点击事件
						$('#killBtn').one('click', function(){
							//执行秒杀请求
							//1、先禁用按钮
							$(this).addClass('disabled');
							//2、发死你个秒杀请求执行秒杀
							$.post(killUrl, {}, function(result){
								if(result && result['success']){
									var killResult = result['data'];
									var state = killResult['state'];
									var stateInfo = killResult['stateInfo'];
									//3、显示秒杀结果
									node.html('<span class="label label-success">' + stateInfo +'</span>');
								}
							});
						});
						node.show();
					}else{
						//未开启秒杀
						var now = exposer['now'];
						var start = exposer['start'];
						var end = exposer['end'];
						//重新计算计时逻辑【有的个别电脑或者浏览器会出现跟服务器时间的偏差】
						seckill.countdown(seckillId, now, start, end);
						
						
					}
				} else {
					console.log('result: ' + result);
				}
			});
		},
		//验证手机号
		validatePhone : function(phone){
			if(phone && phone.length == 11 && !isNaN(phone)){
				return true;
			} else {
				return false;
			}
		},
		//对比时间大小 的函数方法
		countdown : function(seckillId, nowTime, startTime, endTime){
			var seckillBox = $('#seckill-box');
			//时间判断
			if(nowTime > endTime){//现在时间比结束时间大
				//秒杀结束
				seckillBox.html('秒杀结束！');
			} else if(nowTime < startTime){
				//秒杀未开始，计时时间绑定
				var killTime = new Date(startTime + 1000);
				seckillBox.countdown(killTime, function(event){
					//时间格式
					var format = event.strftime('秒杀倒计时：%D天   %H时   %M分    %S秒');
					seckillBox.html(format);
					/** 
					 * 加上 on() 方法  时间完成后回调事件  
					 * 要不然时间到了的话什么都不显示，正常时间到了要显示秒杀按钮或者地址。。。
					 * 
					 * */
				}).on('finish.countdown', founction(){
					//获取秒杀地址，控制现实逻辑，执行秒杀
					seckill.handleSeckillkill(seckillId, seckillBox);
				});
			} else {
				//秒杀开始
				seckill.handleSeckillkill(seckillId, seckillBox);
			}
		},
		//详情页秒杀逻辑
		detail : {
			//详情页初始化
			init : function(params){
				//手机验证和登录，计时交互
				//规划我们的交互流程
				//在cookie中查找手机号
				var killPhone = $.cookie('killPhone');
				
				//验证手机号
				if(seckill.validatePhone(killPhone)){
					//绑定phone
					//控制输出
					var killPhoneModal = $('#killPhoneModal');//选中该节点
					//显示弹出层
					killPhoneModal.modal({
						show : true,//显示弹出层
						backdrop : 'static',//禁止位置关闭，比如 你点击一下别的地方
						keyboard : false // 关闭键盘事件，比如你不小心按了 Esc按钮
					});
					//提交按钮添加点击事件
					$('#killPhoneBtn').click({
						var inputPhone = $('#killPhoneKey').val();
						// TODO
						console.log("inputPhone = " + inputPhone);
						if(seckill.validatePhone(inputPhone)){
							//电话写入cookie    只在 /seckill目录下有效，有效期 7天
							$.cookie('killPhone', inputPhone, {expires : 7, path : '/seckill'});
							//刷新页面
							window.location.reload();
						} else {
							$('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show(300);
						}
					});
				}
				//已经登录
				//计时交互
				var startTime = params['startTime'];
				var endTime = params['endTime'];
				var seckillId = params['seckillId'];
				$.get(seckill.URL.now(), {}, function(result){
					if(result && result['success']){
						var nowTime = result['data'];
						//时间判断
						seckill.countdown(seckillId, nowTime, startTime, endTime);
					} else {
						console.log('result: ' + result);
					}
				});
			}
		}
}