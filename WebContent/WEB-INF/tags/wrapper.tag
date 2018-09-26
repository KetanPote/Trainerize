<%@tag description="Page Wrapper Tag" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="title" required="true" %>

<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${pageTitle}</title>
  <link rel="stylesheet" href="<c:url value="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/css/bootstrap.min.css" />
  "integrity="sha384-AysaV+vQoT3kOAXZkl02PThvDr8HYKPZhNT5h/CXfBThSRXQ6jW5DO2ekP5ViFdi" crossorigin="anonymous">
  <link href="<c:url value="/styles.css" />
  " rel="stylesheet">
  
 <script src="<c:url value="JS/jquery-1.11.3.js"/>" type="text/javascript" lang="javascript"></script>

<script type="text/javascript">

$(document).ready(function()
{		
	var Status =$('#StatusView').text();


	//var waitTime = 30 * 60 * 1000;
	var waitTime = 20 * 60 * 1000;	

	setTimeout(function()
	{
		Status = $('#StatusView').text();
		
		//alert("Showing Status : " + Status)
		if(Status=="Yes")
		{
			Console.log("Revision of Scrap Click Button On 20M Interval : ");
			Two();
			
		}
		  
	}, waitTime);

	$('#ScrapBtn').click(function()
	{
		//$('#frmData').submit();
		//auth2.grantOfflineAccess().then(signInCallback);
		$('#frmData').submit();		
	});
});

function Two()
{
	//alert("About to execute  . . . !");	
	$("#ScrapBtn").click();
	//$('#StatusView').text("Yes");
}

</script>
  
</head>

<body>

  <nav class="navbar navbar-light bg-faded">
    <h3 class="site-name float-md-left">Trainerize Scrapper</h3>
    <ul class="nav navbar-nav float-md-right">
      <li class="nav-item">
        <a class="nav-link" href="${pageContext.request.contextPath}/">Home</a>
      </li>
      <li class="nav-item">
      
        <a class="nav-link" href="${pageContext.request.contextPath}/productList">Scrape</a>

      </li>
      <%-- <li class="nav-item">
        <a class="nav-link" href="${pageContext.request.contextPath}/userInfo">My Account</a>
      </li> --%>
      <%-- <li class="nav-item dropdown">
        <a class="nav-link" href="${pageContext.request.contextPath}/login">Login</a>
      </li> --%>
    </ul>
  </nav>

  <div class="container">
    <div class="row header">
      <div class="col-sm-8">
        <h2 class="page-title">${title}</h2>
      </div>

      <div class="col-sm-4">
        <!-- User store in session with attribute: loginedUser -->
        <%-- <p class="greeting text-sm-right">
          Hello <b>${loginedUser.userName}</b>
        </p> --%>
      </div>
    </div>

    <jsp:doBody/>
  </div>

  <script src="<c:url value="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/js/bootstrap.min.js" /> integrity="sha384-BLiI7JTZm+JWlgKa0M0kGRpJbF2J8q+qreVrKBC47e3K6BW78kGLrCkeRX6I9RoK" crossorigin="anonymous"></script>
  
</body>


</html>