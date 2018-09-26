<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>


<t:wrapper title="Home">

	Scrapping Trainerize.
	
	<br><br><br>
	
	
	<center>
		
		<h2>
				
			<hr width="100%"/>
			Demo Purpose				
		</h2>

		<button id="signin-button" onclick="authenticate().then(loadClient)">Sign In</button>
		<button id="signout-button" onclick="handleSignOutClick()">SignOut</button>
		<hr width="100%"/>
		

		
		<input type="text" name="txtName" id="txtName" value="" placeholder="Your Name" class="form-control"/>
		<input type="text" name="txtCountry" id="txtCountry" value="" placeholder="Country You Live" class="form-control"/>
		<input type="button" name="WriteBtn" id="WriteBtn" value="Write" class="btn btn-success"/>
		
		<h4>
			<div id="Response"></div>
		</h4>	
		
	</center>
	
	<br>
	<!-- <ul class="list-unstyled">
		<li>Login</li>
		<li>Storing user information in cookies</li>
		<li>Product List</li>
		<li>Create Product</li>
		<li>Edit Product</li>
		<li>Delete Product</li>
	</ul> -->

</t:wrapper>