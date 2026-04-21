#**Electronic Store Backend (E-Store)**

A backend application for an e-commerce platform built using Spring Boot.
It provides RESTful APIs to manage users, products, carts, orders, and categories with secure authentication and efficient data handling.


**Overview**

This project is designed following a layered architecture to ensure scalability and maintainability.
It supports core e-commerce functionalities such as product management, cart operations, and order processing, along with authentication and image handling.


**Tech Stack**

Java
Spring Framework
Spring Boot
Spring Data JPA
Hibernate
MySQL
Swagger (OpenAPI)


**Key Features**

RESTful API design
JWT-based authentication and authorization
Pagination and sorting support
Search and filtering capabilities
Image upload and retrieval for users, products, and categories
Clean layered architecture (Controller, Service, Repository)

<h2>Auth Module</h2>
<table>
<tr><th>HTTP Method</th><th>Endpoint</th><th>Description</th></tr>
<tr><td>POST</td><td><code>/auth/generate-token</code></td><td>Authenticates user and returns JWT</td></tr>
</table>
