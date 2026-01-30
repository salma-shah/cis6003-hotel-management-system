<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Ocean View Hotel - Login</title>

    <!-- bootstrap -->
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
            rel="stylesheet"
    >

    <!-- creating custom css for rounded corners and edges-->
    <style>


        body {
            min-height: 100vh;
            background: linear-gradient(to bottom, #4682A9, #FFFBDE);
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .login-card {
            width: 380px;
            border-radius: 16px;
            overflow: hidden;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
        }

        .login-header {
            background: white;
            padding: 30px 20px;
            text-align: center;
        }

        .login-header h1 {
            font-size: 22px;
            margin-bottom: 5px;
            color: #4682A9;
        }

        .login-header h2 {
            font-size: 24px;
            font-weight: 700;
            color: #4682A9;
        }

        .login-body {
            background: #4682A9;
            padding: 30px;
            color: white;
        }

        .login-body label {
            font-size: 16px;
            margin-bottom: 6px;
        }

        .login-body input {
            border-radius: 20px;
            padding: 8px 15px;
        }

        .login-btn {
            margin-top: 20px;
            border-radius: 20px;
            padding: 6px 25px;
            font-size: 14px;
        }
    </style>
</head>
<body>

<div class="login-card">
    <!-- heading part of form -->
    <div class="login-header">
        <h1>Welcome to</h1>
        <h2>Ocean View Hotel</h2>
    </div>

    <!-- form body -->
    <div class="login-body">
        <form action = ""  method="post">
            <div class="mb-3">
                <label>Username</label>
                <input type="text" class="form-control" id="username" name="username">
            </div>

            <div class="mb-3">
                <label>Password</label>
                <input type="password" class="form-control" id="password" name="password">
            </div>

            <div class="text-center">
                <button type="submit" class="btn btn-light login-btn" >
                    Login
                </button>
            </div>
        </form>
    </div>
</div>

</body>
</html>
