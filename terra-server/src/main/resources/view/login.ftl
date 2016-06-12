<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<#assign ctx=request.contextPath />


<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">

    <title>配置中心</title>

    <!-- Bootstrap Core CSS -->
    <link href="${ctx}/css/bootstrap.min.css" rel="stylesheet">
 

 

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style type="text/css">
   	  
   	 .mt-center{ margin-top:25%; text-align: center;}
   	 .mt-center img{ display:inline-block;}
   	 .form-signin {
		  max-width: 430px;
		  padding: 15px;
		  margin: 0 auto;
		}
        .login-panel {
        margin-top:5%;
        }
        
    </style>
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="mt-center">
             
            <div class="login-panel form-signin">            	
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title"><span class="glyphicon glyphicon-log-in"></span> 请登录</h3>
                    </div>
                    <div class="panel-body">
                        <form role="form" action="${ctx}/login" method="post">
                            <#if RequestParameters["error"]??>        
                                <div class="alert alert-danger" role="alert"><span class="glyphicon glyphicon-warning-sign"></span> 用户名密码不正确</div>
                            </#if>
                            <#if RequestParameters["logout"]??>  
                                <p>
                                    
                                </p>
                          	</#if>
                            <fieldset>
                                <div class="form-group">
                                	<div class="input-group">
								        <span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>
								        <input type="text" class="form-control" placeholder="用户名" name="username" type="username" autofocus>
								    </div>
                                </div>
                                <div class="form-group">
                                	<div class="input-group">
								        <span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
								        <input type="password" class="form-control" placeholder="密码" name="password" type="password" value="">
								    </div>
                                </div>
                                
                                <!-- Change this to a button or input when using this as a form -->
                                <input type="submit" class="btn btn-warning btn-block" value="登录">
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
 </div>
 
    <!-- jQuery -->
    <script src="${ctx}/js/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${ctx}/js/bootstrap.min.js"></script>
</body>

</html>

