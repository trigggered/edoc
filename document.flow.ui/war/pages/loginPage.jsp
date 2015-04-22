<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Авторизация</title>

    <!-- Bootstrap -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/signin.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/typica-login.css" rel="stylesheet" >

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="${pageContext.request.contextPath}/js/html5shiv.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/respond.min.js"></script>
    <![endif]-->
</head>
<body>




<div class="container" >
 <div id="login-wraper" >
    <form class="form-signin" name='loginForm'  action="${pageContext.request.contextPath}/login" method="post">
        <!--  <h2 class="form-signin-heading">Document managment system</h2> -->
        <legend>Авторизация в системе <span class="blue">DMS</span></legend>
        
        <div>
        	<c:if test="${'fail' eq param.auth}">		
										Ошибка авторизации	
						                <br />
				                		Причина : ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
				                		<br />                        
			</c:if>
					
			<c:if test="${'timeout' eq param.auth}">		
													Session  Timeout. Please authorize again
				<br />
			</c:if>
		</div>		
		 
		 <div class="body">
		 						
	        <label for="username" class="control-label" >Логин</label>
	        <input id="username" name="j_username"  type="text" class="form-control" placeholder="Login name to AD" required autofocus autocomplete="on">
	        
	        <label for="password" >Пароль</label>
	        <input id="password" name="j_password" type="password"  class="form-control" placeholder="Password" required autocomplete="off">
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Вход</button>
    </form>

	</div>
</div> <!-- /container -->


<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<!-- <script src="${pageContext.request.contextPath}/js/jquery.min.js"></script> -->
<!-- Include all compiled plugins (below), or include individual files as needed -->
<!--  <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script> -->
</body>
</html>