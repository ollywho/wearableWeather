function login(username, password){
	  var postParameters = {username: username, password:password};
	  $.post("/checklogin", postParameters,  function(responseJSON){
	    returnedObject = JSON.parse(responseJSON);
	    var is_valid = returnedObject.is_valid;
	    if(is_valid){
	    	window.location.href = "http://localhost:4567/home#";
	    	
	    } else {
	    	alert("Incorrect login details");
	    }
	  });
  }

function newUser(username, password){
	  var postParameters = {username: username, password:password};
	  $.post("/newuser", postParameters,  function(responseJSON){
	    returnedObject = JSON.parse(responseJSON);
	    var is_taken = returnedObject.result;
	    if(!is_taken){
	    	window.location.href = "http://localhost:4567/home#";
	    	
	    } else {
	    	alert("Username already taken");
	    }
	  });
}
  
  
  $("#submit-btn").click(function(){
	  login($("#username").val(), $("#password").val());
  });
  
  $("#signup-btn").click(function(){
	  newUser($("#username").val(), $("#password").val());
  });