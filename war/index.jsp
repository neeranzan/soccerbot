<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="http://www.w3schools.com/lib/w3.css">
<title>SoccerBOT </title>

<style type="text/css">
div{
	width:600px;
	height:400px;
	overflow:scroll;
}

</style>

<script type="text/javascript">

var req,isIE,inputMsg;

	function init() {
	    inputMsg = document.getElementById("txtMsg");
	  // alert(inputMsg);
	   var url = "clearSession?msg=clear";
	        req = initRequest();
	        req.open("GET", url, true);
	        req.onreadystatechange = clearSessionAlert;
	        req.send(null);
	
	  
	    document.getElementById("txtMsg")
	    .addEventListener("keyup", function(event) {
	    event.preventDefault();
	    if (event.keyCode == 13) {
	        document.getElementById("btnChat").click();
	    }
	});
	}
	
	function clearSession(){
		
		location.reload(); 
		
	}
	function clearSessionAlert(){
		//location.reload(); 
		
	}
	
	function sendMessage() {
		var row;
		
		row=document.createElement("li");
		
		row.appendChild(document.createElement("label"));
		row.firstChild.innerHTML=inputMsg.value;
		document.getElementById("tbl").insertBefore(row, document.getElementById("rowTbl"));
		
        var url = "getResponse?msg=" + escape(inputMsg.value);
        req = initRequest();
        req.open("GET", url, true);
        req.onreadystatechange = callback;
        req.send(null);
	}
	
	function initRequest() {
	    if (window.XMLHttpRequest) {
	        if (navigator.userAgent.indexOf('MSIE') != -1) {
	            isIE = true;
	        }
	        return new XMLHttpRequest();
	    } else if (window.ActiveXObject) {
	        isIE = true;
	        return new ActiveXObject("Microsoft.XMLHTTP");
	    }
	}
	
	function callback() {
		//alert("reached");
	    if (req.readyState == 4) {//completion of interaction
	        if (req.status == 200) {//if it is a success
	        	
	           var row;
	        
	        	var xmlMessage=req.responseXML;
	        
	        	var responseMsg= xmlMessage.getElementsByTagName("Message")[0].innerHTML;
	        	
	        	row=document.createElement("li");
	    		
	    		row.appendChild(document.createElement("label"));
	    		row.firstChild.innerHTML=responseMsg;
	    		row.firstChild.setAttribute("style", "color:green;");
	    		
	    		document.getElementById("tbl").insertBefore(row, document.getElementById("rowTbl"));
	    		document.getElementById("txtMsg").value="";
	    		document.getElementById("txtMsg").focus();
	    		
	        }
	    }
	}


</script>
</head>
<body onload="init()">
<form>
<h1><b>Soccerbot</b></h1>
 Obviously its not intelligent but some canned response might try to make some sense. Ask and see if you wanna play match. also ask if you want to see live match. this is just an example how you can play with.
  ask about players,worldcup, rules, league games etc
please use "football" word instead of soccer as i say it. but you can use "soccer" word sometimes to see how it reacts. Start by saying "hello" or "how are you doing" or your choice :)
<div class="w3-container w3-section" >
<ul id="tbl" class="w3-ul w3-border">
<li id="rowTbl"> <textarea cols="40" id="txtMsg"></textarea></li>
<li><input type="button" id="btnChat" value="chat" onclick="sendMessage()"/>
<input type="button" id="btnClearSession" value="start new session" onclick="clearSession();return false"/>
</li>
</ul>
    
</div>

</form>
</body>
</html>