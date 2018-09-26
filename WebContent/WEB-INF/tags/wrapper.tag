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
 
 <!-- BEGIN Pre-requisites -->

</script>
<!-- <script src="https://apis.google.com/js/platform.js?onload=start" async defer>  -->
<!-- <script async defer src="https://apis.google.com/js/api.js" onload="this.onload=function(){};handleClientLoad()" onreadystatechange="if (this.readyState === 'complete') this.onload()"></script> -->
<script src="https://apis.google.com/js/api.js"></script>
</script>
<!-- END Pre-requisites -->

<script type="text/javascript">

/* 24-Sept-2018 : 07.54 PM */

/*
 function execute(TheData) 
 {
	 	alert("Trying To Get GAPI-Client-Sheet Object : " + JSON.stringify(gapi.client.sheets.spreadsheets));
	 	
    	return gapi.client.sheets.spreadsheets.values.append(
  		{
	      	"spreadsheetId": "1AcD4rnZtjmtk26JAsogg5pN-HzAGBkK4meLIvfWwy90",
	      	"range": "Sheet1",
	      	"insertDataOption": "INSERT_ROWS",
	      	"valueInputOption": "USER_ENTERED",
	      	
	      	"resource": 
	      	{
	        	"majorDimension": "ROWS",
	        	"range": "Sheet1",
	        	"values":TheData 
      	}
    }).then(function(response) 
 	{
          // Handle the results here (response.result has the parsed body).
          console.log("Response", response);
	},
	
    function(err) { console.error("Execute error", err); });
  }
/*
/*
	function makeApiCall(TheData)
	{
		  var params = 
		  {
		    spreadsheetId: '1AcD4rnZtjmtk26JAsogg5pN-HzAGBkK4meLIvfWwy90',  
		    range: 'Sheet1',
	      	insertDataOption: 'INSERT_ROWS',
	      	valueInputOption: "USER_ENTERED",
	      	resource : TheData 
		  };
		
		  var request = gapi.client.sheets.spreadsheets.values.append(params);
		  
		  request.then(function(response) 
		  {      
		      	console.log("Data : " + JSON.stringify(request));
		    	console.log(response.result); 
		    	populateSheet(response.result);
		  },
		  function(reason)
		  {
		    console.error('error: ' + reason.result.error.message);
		  });
	}
*/

/*
 	function authenticate() 
 	{
		$('#WriteBtn').hide();
	 		
	    return gapi.auth2.getAuthInstance()
		.signIn({scope: "https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/drive.file https://www.googleapis.com/auth/spreadsheets"})
	    .then(function() 
	    { 
		    console.log("Sign-in successful");  
		},
	    function(err) { console.error("Error signing in", err); });
  	}
	
	function loadClient() 
  	{
	    	return gapi.client.load("https://content.googleapis.com/discovery/v1/apis/sheets/v4/rest")
	        .then(function(response) 
   	        {	
	   	        console.log("Response From GAPI Client : " + JSON.stringify(response));
	   	           	        
	        	sessionStorage.setItem("Proceed","Yes");
	   	        console.log("GAPI client loaded for API");
				$('#WriteBtn').show(); 
	        },
	        function(err) { console.error("Error loading GAPI client for API", err); });
	}

	gapi.load("client:auth2", function() 
	{
		gapi.auth2.init({client_id: '938131041382-bmtthdb5j3124p9ef3jeenv0s7karsdk.apps.googleusercontent.com'});
	});
	
    function populateSheet(response)
    {
		var Row=0,Col=0;
		var RowData="";

		$('#GSheet tbody').empty();

		for(Row=0;Row<4;Row++)
		{
			RowData +='<tr>';
				RowData += '<td>'+ response.values[Row][0] + '</td>';
				RowData += '<td>'+ response.values[Row][1] + '</td>';
				RowData += '<td>'+ response.values[Row][2] + '</td>';
				RowData += '<td>'+ response.values[Row][3] + '</td>';  
			RowData +='</tr>';					
		}		

		$('#GSheet tbody').append(RowData);
    }

	function writeData()
	{
		var Name 	= $('#txtName').val();
		var Country = $('#txtCountry').val();

		if($.trim(Name)=="" || $.trim(Country)=="")
		{
			alert("Please enter valid information . . . !");
		}
		else
		{
			var Inner=[]; 
			var Outer=[];

			Inner.push(Name);
			Inner.push(Country);

			Outer.push(Inner);
 
			execute(Outer);
			
			console.log("Printing Inner Values : " + Inner);
			console.log("Printing Outer Values : " + Outer);						
		}	
	}
    
	$(document).ready(function()
	{
		$('#WriteBtn').hide();
		
		$('#WriteBtn').click(function()
		{
			var Status = sessionStorage.getItem("Proceed");
			
			if(Status=="Yes")
			{				
				writeData();
			}
			else
			alert("Please Login To The Google Before Proceed . . . !");
		});
	});

/* 24-Sept-2018 : 07.54 PM */


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
		Two();
		  
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