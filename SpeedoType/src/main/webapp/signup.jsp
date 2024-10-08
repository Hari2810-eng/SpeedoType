<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title> Registration or Sign Up form in HTML CSS | CodingLab </title> 
    <link rel="stylesheet" href="SignUpStyles.css">
    <script>
        function validateForm() {
        	const errorMessages = document.querySelectorAll('.error-message');
        	errorMessages.forEach(message=>message.innerText='');
        	
            const username = document.getElementById("username").value;
            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;
            const confirmPassword = document.getElementById("confirmPassword").value;

            if (!username){
            	document.getElementById("usernameError").innerText = "Username is required.";
            	return false;
            }
            
            if( !email ){
            	document.getElementById("emailError").innerText = "Email is required.";
            	return false;
            }else{
            	const emailPattern = /^[^ ]+@[^ ]+\.[a-z]{2,3}$/;
                if (!email.match(emailPattern)) {
                	document.getElementById("emailError").innerText = "Please enter a valid email address.";
                    return false;
                }
            } 
            
            if(!password) {
            	document.getElementById("passwordError").innerText = "Password is required.";
            	return false;
            }
            
            if(!confirmPassword) {
            	document.getElementById("confirmPasswordError").innerText = "Please confirm your password.";
            	return false;
            } else if (password !== confirmPassword) {
            	document.getElementById("confirmPasswordError").innerText = "Passwords do not match.";
            	return false;
            }
            
            const strongPassword = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
            if(!password.match(stringPassword)) {
            	document.getElementById("passwordError").innerText = "Password must be at least 8 characters long and contain uppercase letters, lowercase letters, digits, and special characters.";
                return false;
            }

            return true;
        }
    </script>
   </head>
<body>
  <div class="wrapper">
    <div class="title"><span>Registration</span></div>
    <form action="/SpeedoType/LoginServlet" method="POST" onsubmit="return validateForm();">
      <input type="hidden" name="action" value="signup"/>
      <div class="input-box">
        <input type="text" id="username" name="username" placeholder="Enter your username" required>
      	<span class="error-message" id="usernameError" style="color: red;"></span>
      	
      </div>
      <div class="input-box">
        <input type="text" id="email" name="email" placeholder="Enter your email" required>
      	<span class="error-message" id="emailError" style="color: red;"></span>
      	
      </div>
      <div class="input-box">
        <input type="password" id="password" name="password" placeholder="Create password" required>
        <span class="error-message" id="passwordError" style="color: red;"></span>
      	
      </div>
      <div class="input-box">
        <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm password" required>
      	<span class="error-message" id="confirmPasswordError" style="color: red;"></span>
      </div>
      <div class="input-box button">
        <input type="submit" value="Register Now" />
      </div>
      <div class="login-link">
        Already have an account? <a href="login.html">Login now</a>
      </div>
    </form>
  </div>
</body>
</html>