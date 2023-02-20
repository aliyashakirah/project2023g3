<html>
<head>
<style>
body {
  font-family: Arial, Helvetica, sans-serif;
  background-color: white;
}

h1 {
  text-align: center;
  color: #000000;
  margin: 20 0 0 50px;
  padding: 20px;
}

p{
  text-align: center;
}
* {
  box-sizing: border-box;
}

/* Add padding to containers */
.container {
  width: 582px;
        overflow: hidden;
        margin: auto;
        margin: 30 0 0 420px;
        padding: 50px;
        background: #6B6B81;
        border-radius: 15px ;;
}

/* Full-width input fields */
input[type=text], input[type=password] {
  width: 100%;
  padding: 15px;
  margin: 5px 0 22px 0;
  display: inline-block;
  border-radius: 8px;

}

input[type=text]:focus, input[type=password]:focus {
  background-color: #ddd;
  outline: none;
}

/* Set a style for the submit button */
.loginbtn {
  background-color: #2bcece;
  color: black;
  padding: 16px 20px;
  margin: 8px 0;
  border: none;
  cursor: pointer;
  width: 100%;
  opacity: 0.9;
  border-radius: 8px;
}

.loginbtn:hover {
  opacity: 1;
}

/* Add a blue text color to links */
a {
  color: dodgerblue;
}

/* Set a grey background color and center the text of the "sign in" section */
.signin {
  text-align: center;
}
</style>
</head>
<body>

<h1>Edit Profile</strong></h1>

</body>
</html>

<div class="container">

                <label for="avatar"><b>Your Photo</b></label> <br>
                <input type="file" class="btn" required=""/><br>

    <label for="username"><b>Username</b></label>
    <input type="text" placeholder="Enter username" name="username" id="username" required="">

    <label for="psw"><b>New Password</b></label>
    <input type="password" placeholder="Enter password" name="psw" id="psw" required="">

    <label for="cpsw"><b>Confirm Password</b></label>
    <input type="password" placeholder="Enter password" name="cpsw" id="cpsw" required="">

    <a href="View_profile.html">
    <button type="submit" class="loginbtn">Save Changes</button>
  </div>