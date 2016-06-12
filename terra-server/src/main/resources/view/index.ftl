<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<#assign ctx=request.contextPath />
<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"] />

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<title>配置中心</title>
<!-- Bootstrap Core CSS -->
<link href="${ctx}/css/bootstrap.min.css" rel="stylesheet">
<!-- MetisMenu CSS -->
<link href="${ctx}/css/dashboard.css" rel="stylesheet">
<link href="${ctx}/css/Terra.css" rel="stylesheet">
<link href="${ctx}/css/jstree/default/style.css" rel="stylesheet">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
	<nav class="navbar navbar-default">
		<div class="container-fluid">
			<a class="navbar-brand" href=""><span class="glyphicon glyphicon-home"></span> 配置中心</a>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="#"><span class="glyphicon glyphicon-user"></span> <@sec.authentication property="principal.username" />,您好</a></li>
				<li><a href="${ctx}/logout"><span class="glyphicon glyphicon-off"></span> 退出</a></li>
			</ul>
		</div>

	</nav>
	<div class="container-fluid">
		<div class="row">
			<div id="spin"></div>
		</div>
		<div class="row">
			<div class="col-md-2">
				<div class="panel panel-primary">
					<div class="panel-heading">
						<span class="glyphicon glyphicon-list"></span> <strong>系统列表</strong>
					</div>
					<div class="panel-body">
						<div id="tree"></div>
						<div id="drag-and-drop-zone" class="uploader">
							<div>拖放文件以创建系统</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-10">
				<div class="panel panel-primary" id="detail-panel" style="display: none">
					<!-- Default panel contents -->
					<div class="panel-heading" id="content-panel-heading">
						<strong>配置详情</strong>
					</div>
					<div class="panel-body">
						<div class="row">

							<div class="col-md-6">
								<form class="form-inline">
									<div class="form-group">
										<label for="acl">配置提取码</label> <input class="form-control" id="acl" placeholder="acl" id="acl">
										<@sec.authorize access="hasRole('ROLE_CONFIGADMINGROUP')">
											<button class="btn btn-danger" type="button" id="save-acl_button">
												<span class="glyphicon glyphicon-edit"></span> 重新生成
											</button>
										</@sec.authorize>
									</div>
								</form>
							</div>
							<div class="col-md-6">

								<ul class="list-group" id="content-panel-body-clients">

								</ul>
							</div>
						</div>
						<div class="row row-margin-top">
							<div class="col-md-12">
								<div class="table-responsive">
									<table id="env-properties-table" class="table table-bordered table-condensed table-hover">
										<thead>
											<tr>
												<th class="text-center"><span class="glyphicon glyphicon-book"></span> key</th>
												<th class="text-center"><span class="glyphicon glyphicon-info-sign"></span> value</th>
												<th class="text-center"><span class="glyphicon glyphicon-info-sign"></span> memo</th>
												<th class="text-center"><span class="glyphicon glyphicon-cog"></span> 操作</th>
											</tr>
										</thead>
									</table>
								</div>


							</div>
							<div class="row">
								<div class="col-md-4 text-center">
									<button class="btn btn-success" type="button" id="add-property-button" data-toggle="modal" data-target="#add-property-modal">
										<span class="glyphicon glyphicon-plus"></span> 增加属性
									</button>
								</div>
								<div class="col-md-4 text-center">
									<button class="btn btn-success" type="button" id="export-button">
										<span class="glyphicon glyphicon-export"></span> application.properties
									</button>
								</div>
								<div class="col-md-4 text-center">
									<button style="height: 35px; width: 80px" data-toggle="modal" data-target="#history-property-modal" type="button" class="btn btn-success">History</button>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>
		<div class="row row-margin-top"></div>
		<div class="row row-margin-top"></div>
		<div class="row row-margin-top"></div>
		<div class="row row-margin-top"></div>
	</div>
	<!--新建系统模态框-->
	<div class="modal fade" id="create-system-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="exampleModalLabel">新建系统</h4>
				</div>
				<form id="create-system-form" data-toggle="validator" action="${ctx}/createSystem" method="POST">
					<div class="modal-body">
						<input type="hidden" name="properties" id="properties-input">
						<table id="create-system-modal-table" style="table-layout: fixed; word-wrap: break-word;" class="table table-bordered table-condensed table-hover">
							<thead>
								<tr>
									<th>key</th>
									<th>value</th>
								</tr>
							</thead>
							<TBODY>

							</TBODY>
						</table>
						<div class="form-group">
							<label for="system-name" class="control-label">系统名称:</label> <input type="text" class="form-control" name="systemName" id="system-name" maxlength="50" required>
							<div class="help-block with-errors"></div>
						</div>
						<div class="form-group">
							<label for="system-code" class="control-label">系统编号:</label> <input type="number" min="1000" max="9999" class="form-control" name="systemCode" id="system-code" placeholder="系统编号前四位" data-remote="${ctx}/validateSystemCode" required>
							<div class="help-block with-errors"></div>
						</div>
						<div class="form-group">
							<label for="system-version" class="control-label">版本:</label> <input type="text" class="form-control" name="systemVersion" id="system-version" maxlength="50" required>
							<div class="help-block with-errors"></div>
						</div>
						<div class="form-group">
							<label for="system-env" class="control-label">环境:</label> <input type="text" class="form-control" name="systemEnv" id="system-env" maxlength="50" required>
							<div class="help-block with-errors"></div>
						</div>

					</div>
					<div class="modal-footer">
						<div class="form-group">
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
							<button type="submit" class="btn btn-primary" id="create-system-button">创建</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<!--编辑属性模态框-->
	<div class="modal fade" id="property-modal" tabindex="-1" role="dialog" aria-labelledby="propertyModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="propertyModalLabel">编辑属性</h4>
				</div>
				<form id="property-form" action="${ctx}/updateProperty" method="POST">
					<div class="modal-body">
						<input type="hidden" name="envPropertyId" id="envPropertyIdInput">
						<div class="form-group">
							<label for="system-name" class="control-label">key:</label> <input type="text" class="form-control" name="propertyKey" id="property-key">
						</div>
						<div class="form-group">
							<label for="system-code" class="control-label">value:</label> <input type="text" class="form-control" name="propertyValue" id="property-value">
						</div>
						<div class="form-group">
							<label for="propertyLabel" class="control-label">label:</label> <input type="text" class="form-control" name="propertyLabel" id="property-label" placeholder="请输入合适的标签值用来对属性名归类">
						</div>
						<div class="form-group">
							<label for="propertyMemo" class="control-label">memo:</label> <input type="text" class="form-control" name="propertyMemo" id="property-memo">
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">CANCEL</button>
						<button type="submit" class="btn btn-primary" id="property-ok-button">OK</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	<!--新建属性模态框-->
	<div class="modal fade" id="add-property-modal" tabindex="-1" role="dialog" aria-labelledby="addPropertyModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="addPropertyModalLabel">新建属性</h4>
				</div>
				<form id="add-property-form" role="form" data-toggle="validator" action="${ctx}/addProperty" method="POST">
					<div class="modal-body">
						<input type="hidden" name="envId" id="add-property-env-id-input">
						<div class="form-group">
							<label for="add-property-key" class="control-label">key:</label> <input type="text" class="form-control" name="propertyKey" id="add-property-key" required>
							<div class="help-block with-errors"></div>
						</div>
						<div class="form-group">
							<label for="dd-property-value" class="control-label">value:</label> <input type="text" class="form-control" name="propertyValue" id="add-property-value" required>
							<div class="help-block with-errors"></div>
						</div>
						<div class="form-group">
							<label for="dd-property-value" class="control-label">label:</label> <input type="text" class="form-control" name="propertyLabel" id="add-property-label" placeholder="请输入合适的标签值用来对属性名归类">
							<div class="help-block with-errors"></div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">CANCEL</button>
						<button type="submit" class="btn btn-primary" id="add-property-ok-button">OK</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	<!--删除属性模态框-->
	<div class="modal fade" id="delete-property-modal" tabindex="-1" role="dialog" aria-labelledby="deletePropertyModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="deletePropertyModalLabel">确认删除?</h4>
				</div>
				<form id="delete-property-form" action="${ctx}/deleteProperty" method="POST">
					<input type="hidden" name="envPropertyId" id="envPropertyIdInput">
					<div class="modal-body bg-warning">
						<p style="font-size: 40px; margin: 10px" class="text-danger glyphicon  glyphicon-warning-sign"></p>
						<span style="font-size: 20px;" class="text-danger">环境类型分为生产和非生产,删除/增加操作会影响同一个版本下与当前环境相同的环境。<span>

					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">CANCEL</button>
						<button type="submit" class="btn btn-primary" id="delete-property-ok-button">OK</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	<!--显示Value修改历史-->
	<div class="modal fade" id="history-property-modal" tabindex="-1" role="dialog" aria-labelledby="propertyModalLabel" aria-hidden="true">
		<div style="width: 1200px;" class="modal-dialog">
			<div style="display: block;" class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="propertyModalLabe">历史记录</h4>
				</div>
				<input type="hidden" name="envPropertyId" id="envPropertyIdInput">



				<table style="table-layout: fixed; word-wrap: break-word;" id="history-env-properties-table" class="table table-bordered table-condensed table-hover">
					<thead>
						<tr>
							<th class="text-center"><span class="glyphicon glyphicon-user"></span> Modified by</th>
							<th class="text-center"><span class="glyphicon glyphicon-info-sign"></span>Key</th>
							<th class="text-center"><span class="glyphicon glyphicon-info-sign"></span> Old Value</th>
							<th class="text-center"><span class="glyphicon glyphicon-info-sign"></span> Value</th>
							<th class="text-center"><span class="glyphicon glyphicon-time"></span> Modified Date</th>
							<th class="text-center"><span class="glyphicon glyphicon-wrench"></span> Action</th>
							<th class="text-center"><span class="glyphicon glyphicon-comment"></span> Comments</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	<footer class="navbar-fixed-bottom">
		<div class="foot-space">
			<div class="container">
				<p class="text-center">版权所有 ©</p>
			</div>
		</div>
	</footer>
	<!-- jQuery -->
	<script src="${ctx}/js/jquery.min.js"></script>
	<!-- Bootstrap Core JavaScript -->
	<script src="${ctx}/js/bootstrap.min.js"></script>
	<script src="${ctx}/js/jstree.min.js"></script>
	<script src="${ctx}/js/dmuploader.min.js"></script>
	<script src="${ctx}/js/spin.min.js"></script>
	<script src="${ctx}/js/validator.min.js"></script>
	<script>
		var properties;
		var env_id;
		var env_id_for_history;
		var opts = {
			lines : 13, // The number of lines to draw
			length : 8, // The length of each line
			width : 6, // The line thickness
			radius : 16, // The radius of the inner circle
			corners : 1, // Corner roundness (0..1)
			rotate : 0, // The rotation offset
			direction : 1, // 1: clockwise, -1: counterclockwise
			color : '#000', // #rgb or #rrggbb or array of colors
			speed : 1.3, // Rounds per second
			trail : 100, // Afterglow percentage
			shadow : false, // Whether to render a shadow
			hwaccel : false, // Whether to use hardware acceleration
			className : 'spinner', // The CSS class to assign to the spinner
			zIndex : 2e9, // The z-index (defaults to 2000000000)
			top : '20%', // Top position relative to parent
			left : '50%' // Left position relative to parent
		};
		var spin = document.getElementById('spin');
		var spinner = new Spinner(opts).spin();
		var forcompare="FFFF";
		$.ajaxSetup({
			cache : false
		});
		$(document).ajaxStart(function() {
			console.log("ajax start");
			spinner.spin();
			spin.appendChild(spinner.el);
		});
		$(document).ajaxStop(function() {
			console.log("ajax stop");
			spinner.stop();
			if (event.target.responseURL.indexOf('login') >= 0) {
				$(location).attr("href", event.target.responseURL);
			}
		});
		$(document).ajaxError(function(event, response, target) {
			console.log("ajax error:" + target.url);
			if (target.url.indexOf('validate') < 0) {
				alert('内部异常,请联系管理员!');
			}
		});
		$("#tree")
				.on(
						'select_node.jstree',
						function(e, data) {
							var i, j;
							for (i = 0, j = data.selected.length; i < j; i++) {
								if (data.instance.get_node(data.selected[i]).id
										.indexOf('env') === 0) {
									var envId = data.instance
											.get_node(data.selected[i]).id
											.substring(3);
									console.log('env:' + envId);
									refresh_content_panel(envId);
								}

							}

						}).on('rename_node.jstree', function(e, data) {
					$.post("${ctx}/renameVersionOrEnv", {
						nodeId : data.node.id,
						text : data.text
					}, function(result) {
						console.log('重命名OK' + result);
						$("#tree").jstree(true).refresh();
					});

				}).jstree({
					"core" : {
						"data" : {
							"url" : "${ctx}/systemTree"
						},
						"check_callback" : true
					},
					"contextmenu" : {
						"items" : function($node) {
							if ($node.parent == '#') {
								return null;
							}
							var tree = $("#tree").jstree(true);
							return {
								"Create" : {
									"label" : "复制",
									"action" : function(obj) {
										$.post("${ctx}/copyVersionOrEnv", {
											nodeId : $node.id
										}, function(result) {
											console.log('复制OK' + result);
											$("#tree").jstree(true).refresh();
										});

									}
								},
								"Rename" : {
									"label" : "重命名",
									"shortcut" : 113,
									"shortcut_label" : "F2",
									"action" : function(obj) {
										tree.edit($node);
									}
								},
								"Delete" : {
									"label" : "删除",
									"action" : function(obj) {
										$.post("${ctx}/deleteVersionOrEnv", {
											nodeId : $node.id
										}, function(result) {
											console.log('deleteOK' + result);
											$("#tree").jstree(true).refresh();
											location.reload(true); //refresh the whole page
										});

									}
								}
							};
						}
					},
					"plugins" : [ "wholerow", "contextmenu", "types" ]
				});

		function toggletime(obj) {
			var ip = $(obj).parent().html().split('<')[0];

			$('.classforsearch').each(function() {
				if (ip == ($(this).children().html()))
					$(this).addClass("classfortoggle")
			});

			$('.classfortoggle').slideToggle();

			$('.classforsearch').each(function() {

				$(this).removeClass("classfortoggle")

			});
		}

		function getRandomColor() { 
			return '#'+(Math.random()*0xffffff<<0).toString(16);
		} 	

		function refresh_content_panel(envId) {
			console.log('开始刷新详情面板:' + envId);
			env_id_for_history = envId;
			$('#detail-panel').show();
			env_id = envId;
			$
					.get(
							"${ctx}/envDetail?envId=" + envId,
							function(result) {
								forcompare="FFFF";
								console.log(result);
								$('#content-panel-heading').html(
										'<span class="glyphicon glyphicon-list-alt"></span> <strong>'
												+ result.systemName + '('
												+ result.systemCode + ')>'
												+ result.versionName + '>'
												+ result.envName + ':&nbsp'
												+ 'envId=' + result.envId
												+ '</strong>');
								$('#content-panel-body-clients').empty();
								for ( var i in result.clients) {
									$('#content-panel-body-clients')
											.append(
													'<li class="list-group-item list-group-item-info">'
															+ i
															+ '<button onclick="toggletime(this)"  style="width:50px;height:25px;" class="badge alert-success" title="显示详情">'
															+ result.clients[i]
															+ '</button></li>');
									for ( var t in result.clients_time) {
										if (result.clients_time[t] == i)
											$('#content-panel-body-clients')
													.append(
															'<li class="classforsearch form-control list-group-item">'
																	+ t
																	+ '<span class="hide badge alert-success" data-toggle="collapse" data-placement="left">'
																	+ result.clients_time[t]
																	+ '</span></li>');
									}
									$('.classforsearch').hide();
								}
								$('#acl').val(result.acl);
								$('#env-properties-table>tbody').remove();
								
					var rand =0; 
					var rand_backup=0;
					var labelclass;
					
				for ( var i in result.propertyVos) {
								
								if((rand > 0xf)	||	(rand_backup > 0xf)){
									rand =0;
									rand_backup=0;
								}

					 if(result.propertyVos[i].labelofkey == "")
							labelclass="";
						else if(result.propertyVos[i].labelofkey != forcompare){
									labelclass="bg_label_parent tag"+rand.toString(16);
									rand_backup=rand;
									rand+=1;
									forcompare=result.propertyVos[i].labelofkey;	
								}else labelclass="bg_label_parent tag"+rand_backup.toString(16);
								
									$('#env-properties-table')
											.append(
													'<tr ><td style="white-space:nowrap;">' 
															+'<label title="'+result.propertyVos[i].labelofkey+'"'+'class="'+labelclass+'">'+'<div style="margin-left:2px; margin-right:2px;">'+result.propertyVos[i].labelofkey+'</div>'+'</label>'+result.propertyVos[i].key 
															+ '</td><td style="white-space:nowrap;" ondblclick="DoubleClickEdit(this)" title="'
															+ result.propertyVos[i].escapedValue
															+ '">'
															+ result.propertyVos[i].shortValue
															+ '</td><td style="word-break:break-all;"title="'+result.propertyVos[i].memo+'">'
															+ result.propertyVos[i].memo
															+ '</td><td><div style="white-space:nowrap;text-align:center"><button style="text-align:center;" type="button" class="btn btn-primary btn-xs" data-toggle="modal" data-target="#property-modal" data-vid="'+result.propertyVos[i].envPropertyValueId+'" data-key="'+result.propertyVos[i].key+'" data-value="'+result.propertyVos[i].escapedValue+'" data-label="'+result.propertyVos[i].labelofkey+'" data-memo="'+result.propertyVos[i].memo+'"><span class="glyphicon glyphicon-edit"></span></button>&nbsp;&nbsp;&nbsp;&nbsp;<@sec.authorize access="hasRole('ROLE_CONFIGDELETEGROUP')"><button style="text-align:center;" data-toggle="modal" data-target="#delete-property-modal" data-vid="'+result.propertyVos[i].envPropertyValueId+'" type="button" class="btn btn-danger btn-xs"><span class="glyphicon glyphicon-trash"></span></button></@sec.authorize></div></td></tr>');
								}
						});
		}

		function DoubleClickEdit(obj) {
			$(obj).next().next().children(":first").children(":first").trigger(
					"click");
		}

		$('#history-property-modal')
				.on(
						'show.bs.modal',
						function(event) {
							var button = $(event.relatedTarget);
							var modal = $(this);
							modal.find('#envPropertyIdInput').val(
									button.data('vid'));

							$
									.get(
											"${ctx}/getHistory?envId="
													+ env_id_for_history,
											function(result) {
												$(
														'#history-env-properties-table>tbody')
														.remove();
												for ( var i in result.propertyHistoryVos) {
													$(
															'#history-env-properties-table')
															.append(
																	'<tr> <td>'
																			+ result.propertyHistoryVos[i].modifiedby
																			+ '</td> <td title="'+result.propertyHistoryVos[i].escapedValue+'">'
																			+ result.propertyHistoryVos[i].key
																			+ '</td> <td title="'+result.propertyHistoryVos[i].escapedValue+'">'
																			+ result.propertyHistoryVos[i].previousvalue
																			+ '</td> <td title="'+result.propertyHistoryVos[i].escapedValue+'">'
																			+ result.propertyHistoryVos[i].value
																			+ '</td> <td title="'+result.propertyHistoryVos[i].escapedValue+'">'
																			+ result.propertyHistoryVos[i].userupdateTime
																			+ '</td> <td title="'+result.propertyHistoryVos[i].escapedValue+'">'
																			+ result.propertyHistoryVos[i].action
																			+ '</td>	<td title="'+result.propertyHistoryVos[i].escapedValue+'">'
																			+ result.propertyHistoryVos[i].comments
																			+ '</td><tr>');
												}
											});
						});

		function add_log(message) {
			console.log(message);

		}

		$('#create-system-modal').on(
				'show.bs.modal',
				function(event) {
					var modal = $(this)
					modal.find('#properties-input').val(
							JSON.stringify(properties));
					console.log('=========' + $('#properties-input').val());
					modal.find('#create-system-modal-table>tbody').empty();
					modal.find('#system-name').val('');
					modal.find('#system-code').val('');
					modal.find('#system-version').val('');
					modal.find('#system-env').val('');
					for ( var prop in properties) {
						console.log(prop + "=" + properties[prop]);
						modal.find('#create-system-modal-table').append(
								'<tr><td>' + prop + '</td><td>'
										+ properties[prop] + '</td></tr>');
					}
				});
		$('#create-system-modal').on("hide.bs.modal",function() {
				$(this).find(".has-error").removeClass("has-error");
				$(this).find(".with-errors").html("");
		});				
		$('#add-property-modal').on(
				'show.bs.modal',
				function(event) {
					var button = $(event.relatedTarget);
					var modal = $(this);
					modal.find('#add-property-env-id-input').val(env_id);
					modal.find('#add-property-key').val('');
					modal.find('#add-property-value').val('');
					modal.find('#add-property-key').data('remote',
							'${ctx}/validatePropertyKey?envId=' + env_id);
				});
		$("#add-property-modal").on("hide.bs.modal",function() {
			$(this).find(".has-error").removeClass("has-error");
			$(this).find(".with-errors").html("");
		});
		$('#property-modal').on('show.bs.modal', function(event) {
			var button = $(event.relatedTarget);
			var modal = $(this);
			modal.find('#envPropertyIdInput').val(button.data('vid'));
			modal.find('#property-key').val(button.data('key'));
			modal.find('#property-key').prop("disabled", true);
			modal.find('#property-value').val(button.data('value'));
			modal.find('#property-label').val(button.data('label'));
			modal.find('#property-memo').val(button.data('memo'));
		});
		$('#delete-property-modal').on('show.bs.modal', function(event) {
			var button = $(event.relatedTarget);
			var modal = $(this);
			modal.find('#envPropertyIdInput').val(button.data('vid'));

		});
		$('#add-property-ok-button').click(function() {
			var frm = $('#add-property-form');
			frm.submit(function(ev) {
				if (ev.isDefaultPrevented()) {
					// handle the invalid form...
				} else {
					// everything looks good!
					$.ajax({
						type : frm.attr('method'),
						url : frm.attr('action'),
						data : frm.serialize(),
						success : function(data) {
							$('#add-property-ok-button').unbind('click');
							$('#add-property-modal').modal('hide');
							refresh_content_panel(data);
						}
					});

					ev.preventDefault();
				}

			});
		});
		$('#property-ok-button').click(function() {
			var frm = $('#property-form');
			frm.submit(function(ev) {
				$.ajax({
					type : frm.attr('method'),
					url : frm.attr('action'),
					data : frm.serialize(),
					success : function(data) {
						$('#property-ok-button').unbind('click');
						$('#property-modal').modal('hide');
						refresh_content_panel(data);
					}
				});

				ev.preventDefault();
			});
		});

		$('#delete-property-ok-button').click(function() {
			var frm = $('#delete-property-form');
			frm.submit(function(ev) {
				$.ajax({
					type : frm.attr('method'),
					url : frm.attr('action'),
					data : frm.serialize(),
					success : function(data) {
						$('#delete-property-ok-button').unbind('click');
						$('#delete-property-modal').modal('hide');
						refresh_content_panel(data);
					}
				});

				ev.preventDefault();
			});
		});

		$('#create-system-button').click(function() {
			var frm = $('#create-system-form');
			frm.submit(function(ev) {
				if (ev.isDefaultPrevented()) {
					// handle the invalid form...
				} else {
					// everything looks good!
					$.ajax({
						type : frm.attr('method'),
						url : frm.attr('action'),
						data : frm.serialize(),
						success : function(data) {
							$('#create-system-button').unbind('click');
							$('#create-system-modal').modal('hide');
							$("#tree").jstree(true).refresh();
							refresh_content_panel(data);
						}
					});

					ev.preventDefault();
				}
			});
		});
		$('#save-acl_button').click(function() {
			$.post("${ctx}/updateAcl", {
				envId : env_id,
				acl : $('#acl').val()
			}, function(result) {

				refresh_content_panel(env_id);
			});
		});
		$('#export-button').click(function() {
			window.open("${ctx}/export?envId=" + env_id);
		});
		$('#drag-and-drop-zone')
				.dmUploader(
						{
							url : '${ctx}/upload',
							dataType : 'json',
							allowedTypes : '*',
							/*extFilter: 'jpg;png;gif',*/
							onInit : function() {
								add_log('Penguin initialized :)');
							},
							onBeforeUpload : function(id) {
								add_log('Starting the upload of #' + id);

								add_log(id, 'uploading', 'Uploading...');
							},
							onNewFile : function(id, file) {
								add_log('New file added to queue #' + id);

							},
							onComplete : function() {
								add_log('All pending tranfers finished');
							},
							onUploadProgress : function(id, percent) {
								var percentStr = percent + '%';

								add_log(id, percentStr);
							},
							onUploadSuccess : function(id, data) {
								add_log('Upload of file #' + id + ' completed');

								add_log('Server Response for file #' + id
										+ ': ' + JSON.stringify(data));

								add_log(id, 'success', 'Upload Complete');

								add_log(id, '100%');
								properties = data;
								$('#create-system-modal').modal(data);			

							},
							onUploadError : function(id, message) {
								add_log('Failed to Upload file #' + id + ': '
										+ message);
								add_log(id, 'error', message);
							},
							onFileTypeError : function(file) {
								add_log('File \''
										+ file.name
										+ '\' cannot be added: must be an image');
							},
							onFileSizeError : function(file) {
								add_log('File \''
										+ file.name
										+ '\' cannot be added: size excess limit');
							},
							/*onFileExtError: function(file){
							  $.danidemo.addLog('#demo-debug', 'error', 'File \'' + file.name + '\' has a Not Allowed Extension');
							},*/
							onFallbackMode : function(message) {
								alert('Browser not supported(do something else here!): '
										+ message);
							}
						});
	</script>
</body>

</html>