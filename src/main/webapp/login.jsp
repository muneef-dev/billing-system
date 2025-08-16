<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">
    <div class="bg-white p-8 rounded shadow-md w-96">
        <h1 class="text-2xl font-bold mb-6 text-center">Login</h1>
        <form action="/auth/login" method="POST">
            <div class="mb-4 flex items-center space-x-2">
                <i class="fa-solid fa-user text-gray-700"></i>
                <label for="username" class="block text-gray-700">Username</label>
                <input type="text" id="username" name="username" value="moha1234" class="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500" required>
            </div>
            <div class="mb-4 flex items-center space-x-2">
                <i class="fa-solid fa-lock text-gray-700"></i>
                <label for="password" class="block text-gray-700">Password</label>
                <input type="password" id="password" name="password" value="1234" class="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500" required>
            </div>
            <div class="text-red-500 text-sm mb-4">
                <% if (request.getAttribute("error") != null) { %>
                    <%= request.getAttribute("error") %>
                <% } %>
            </div>
            <button type="submit" class="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600">Login</button>
        </form>
        <div class="text-center mt-4">
            <a href="/register.jsp" class="text-blue-500 hover:underline">Create a new account</a>
        </div>
    </div>
</body>
</html>
