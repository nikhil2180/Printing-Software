<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<style>
    body {
        font-family: Arial, sans-serif;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
        background-color: #fff; /* White background */
    }
    .login-container {
    	
        background: white;
        padding: 30px;
        border-radius: 15px;
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        width: 350px;
        text-align: center;
        animation: fadeIn 0.5s ease-in-out;
    }
    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(-20px); }
        to { opacity: 1; transform: translateY(0); }
    }
    .login-container h2 {
        margin-bottom: 20px;
        color: #333;
    }
    .login-container label {
        display: block;
        text-align: left;
        margin: 10px 0 5px;
        font-weight: bold;
        color: #555;
    }
    .login-container input {
        width: 100%;
        padding: 12px;
        margin-bottom: 15px;
        border: 1px solid #ccc;
        border-radius: 8px;
        font-size: 16px;
        outline: none;
        transition: border 0.3s;
        
    }
    .login-container input:focus {
        border-color: #6a11cb;
    }
    .login-container button {
        width: 100%;
        padding: 12px;
        background: #333;
        border: none;
        color: white;
        font-size: 18px;
        border-radius: 8px;
        cursor: pointer;
        transition: background 0.3s;
    }
    .login-container button:hover {
        background: #575757;
    }
</style>
</head>
<body>
    <div class="login-container">
        <h2>Login</h2>
        <form action="login" method="post">
            <label for="username">Username</label>
            <input type="text" name="username" id="username" required>
            
            <label for="password">Password</label>
            <input type="password" name="password" id="password" required>
            
            <button type="submit">Log in</button>
        </form>
    </div>
</body>
</html>
