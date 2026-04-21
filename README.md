# E Store Backend

A backend application for an e-commerce platform built using Spring Boot.  
It provides RESTful APIs to manage users, products, carts, orders, and categories with secure authentication and efficient data handling.

---

## Overview

This project is designed following a layered architecture to ensure scalability and maintainability.  
It supports core e-commerce functionalities such as product management, cart operations, and order processing, along with authentication and image handling.

---

## Tech Stack

- Java  
- Spring Framework  
- Spring Boot  
- Spring Data JPA  
- Hibernate  
- MySQL  
- Swagger (OpenAPI)  

Deployment:
- AWS EC2 (Backend)  
- Railway (Database)

---

## Key Features

- RESTful API design  
- JWT-based authentication and authorization  
- Pagination and sorting support  
- Search and filtering capabilities  
- Image upload and retrieval for users, products, and categories  

---

## Modules

- Auth Module  
- User Module  
- Product Module  
- Cart Module  
- Order Module  
- Category Module  

---




<h2>Auth Module</h2>
<table>
<tr><th>HTTP Method</th><th>Endpoint</th><th>Description</th></tr>
<tr><td>POST</td><td><code>/auth/generate-token</code></td><td>Authenticates user and returns JWT</td></tr>
</table>

<h2>User Module</h2>
<table>
<tr><th>HTTP Method</th><th>Endpoint</th><th>Description</th></tr>
<tr><td>POST</td><td><code>/users/save</code></td><td>Create a new user</td></tr>
<tr><td>PUT</td><td><code>/users/update/{userId}</code></td><td>Update user details</td></tr>
<tr><td>DELETE</td><td><code>/users/delete/{userId}</code></td><td>Delete a user</td></tr>
<tr><td>GET</td><td><code>/users</code></td><td>Get all users</td></tr>
<tr><td>GET</td><td><code>/users/{userId}</code></td><td>Get user by ID</td></tr>
<tr><td>GET</td><td><code>/users/mail/{email}</code></td><td>Get user by email</td></tr>
<tr><td>GET</td><td><code>/users/search/{name}</code></td><td>Search users</td></tr>
</table>

<h2>User Image APIs</h2>
<table>
<tr><th>HTTP Method</th><th>Endpoint</th><th>Description</th></tr>
<tr><td>POST</td><td><code>/users/image/{userId}</code></td><td>Upload profile image</td></tr>
<tr><td>GET</td><td><code>/users/image/{userId}</code></td><td>Serve profile image</td></tr>
</table>

<h2>Category Module</h2>
<table>
<tr><th>HTTP Method</th><th>Endpoint</th><th>Description</th></tr>
<tr><td>POST</td><td><code>/category/save</code></td><td>Create category</td></tr>
<tr><td>PUT</td><td><code>/category/update/{categoryId}</code></td><td>Update category</td></tr>
<tr><td>DELETE</td><td><code>/category/delete/{categoryId}</code></td><td>Delete category</td></tr>
<tr><td>GET</td><td><code>/category</code></td><td>Get all categories</td></tr>
<tr><td>GET</td><td><code>/category/{categoryId}</code></td><td>Get category by ID</td></tr>
</table>

<h2>Product Module</h2>
<table>
<tr><th>HTTP Method</th><th>Endpoint</th><th>Description</th></tr>
<tr><td>POST</td><td><code>/product/save</code></td><td>Create product</td></tr>
<tr><td>PUT</td><td><code>/product/update/{productId}</code></td><td>Update product</td></tr>
<tr><td>DELETE</td><td><code>/product/delete/{productId}</code></td><td>Delete product</td></tr>
<tr><td>GET</td><td><code>/product/{productId}</code></td><td>Get product by ID</td></tr>
<tr><td>GET</td><td><code>/product</code></td><td>Get all products</td></tr>
<tr><td>GET</td><td><code>/product/stock</code></td><td>Get in-stock products</td></tr>
<tr><td>GET</td><td><code>/product/search?name={productName}</code></td><td>Search products</td></tr>
<tr><td>GET</td><td><code>/product/brand/{brandName}</code></td><td>Get by brand</td></tr>
<tr><td>GET</td><td><code>/product/range?minPrice={min}&maxPrice={max}</code></td><td>Filter by price</td></tr>
<tr><td>POST</td><td><code>/product/upload/{productId}</code></td><td>Upload image</td></tr>
<tr><td>GET</td><td><code>/product/download/{productId}</code></td><td>Download image</td></tr>
<tr><td>POST</td><td><code>/product/save/category/{categoryId}</code></td><td>Create under category</td></tr>
<tr><td>PUT</td><td><code>/product/assign/{productId}/category/{categoryId}</code></td><td>Assign category</td></tr>
<tr><td>GET</td><td><code>/product/category/{categoryId}</code></td><td>Get by category</td></tr>
</table>

<h2>Cart Module</h2>
<table>
<tr><th>HTTP Method</th><th>Endpoint</th><th>Description</th></tr>
<tr><td>POST</td><td><code>/cart/save/{userId}</code></td><td>Add item</td></tr>
<tr><td>PUT</td><td><code>/cart/remove/{userId}/{cartItemId}</code></td><td>Remove item</td></tr>
<tr><td>DELETE</td><td><code>/cart/clear/{userId}</code></td><td>Clear cart</td></tr>
<tr><td>GET</td><td><code>/cart/{userId}</code></td><td>Get cart</td></tr>
</table>

<h2>Order Module</h2>
<table>
<tr><th>HTTP Method</th><th>Endpoint</th><th>Description</th></tr>
<tr><td>POST</td><td><code>/orders/create</code></td><td>Create order</td></tr>
<tr><td>DELETE</td><td><code>/orders/remove/{orderId}</code></td><td>Delete order</td></tr>
<tr><td>GET</td><td><code>/orders/users/{userId}</code></td><td>Get user orders</td></tr>
<tr><td>GET</td><td><code>/orders</code></td><td>Get all orders</td></tr>
<tr><td>PUT</td><td><code>/orders/update/{orderId}</code></td><td>Update order</td></tr>
</table>
