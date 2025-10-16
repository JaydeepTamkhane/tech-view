# Tech View Blog Backend

**Tech View** is a blog and content management backend built with **Spring Boot**.  
It allows authenticated users to create, read, update, and delete posts, comments, tags, and categories, with support for cover images, likes, and JWT-based authentication.

---

## Tech Stack
- **Backend:** Java 17, Spring Boot 3.1
- **Database:** PostgreSQL
- **Authentication & Security:** JWT Bearer tokens, Spring Security
- **File Storage:** Cloudinary for post cover images
- **API Documentation:** Swagger / OpenAPI 3.1
- **Build Tool:** Maven
- **Libraries:** Lombok, Hibernate, Jakarta Persistence, BCryptPasswordEncoder
- **Testing:** JUnit, Mockito 

---

## Architecture



### Explanation
1. Client sends REST requests via **Swagger UI**, **Postman**, or frontend.
2. Controllers handle validation and route requests to services.
3. **JWT filter** ensures authenticated access and role-based checks.
4. Services implement business logic for posts, comments, likes, tags, and categories.
5. Repositories manage database access (PostgreSQL).
6. Cloudinary stores post cover images and provides URLs for frontend.
7. Security is **stateless**, simplifying scaling and caching.

---

## Core Features
- **User Authentication:** Signup, Login, Refresh JWT
- **Post Management:** CRUD operations, cover image upload/delete
- **Comments & Replies:** Nested comments with ownership validation
- **Likes & Dislikes:** Users can like/unlike posts
- **Tags & Categories:** CRUD for tags and categories
- **Search & Pagination:** Filter posts by tags, categories, year
- **Security:** Stateless JWT with role checks
- **File Storage:** Cloudinary integration for cover images

---

## Dynamic Post Search

Tech View supports a flexible and dynamic search for posts using the `/post/search` endpoint.  
Users can filter posts by multiple criteria simultaneously, including:

- **Keywords**: Searches in post titles and author names (case-insensitive, supports multiple words).
- **Categories**: Filter posts belonging to one or more categories.
- **Tags**: Filter posts by one or more tags.
- **Year**: Filter posts by the year they were created.

The endpoint accepts a `PostSearchRequestDto` in the request body and supports **pagination** with `page` and `size` query parameters.

Internally, the service layer uses `PostSpecifications` to build **dynamic JPA Criteria queries**.  
This allows the backend to construct `AND` / `OR` predicates based on which filters are provided.  
As a result, clients can send any combination of filters and get precise results efficiently.

The search response returns a paginated `Page<PostResponseDto>` object including post details, tags, categories, likes, and comment counts.

Example request body:

```json
{
  "searchInput": "java spring",
  "categories": ["Backend", "Tech"],
  "tags": ["SpringBoot", "JWT"],
  "year": 2025
}
```
---

## Setup & Run

### Prerequisites
- Java 17+
- Maven
- PostgreSQL
- Cloudinary account (for image storage)

Got it! Here's just the **Environment Variables** part in Markdown format for direct use in your README:

### Environment Variables

Set these in **IntelliJ Run/Debug Configuration** or your terminal:

```bash
# Example for terminal (Linux/macOS)
export CLOUDINARY_NAME=""
export CLOUDINARY_KEY=""
export CLOUDINARY_SECRET=""
export DB_USERNAME=""
export DB_PASSWORD=""
export DB_URL=""
export JWT_SECRET=""

# Example for Windows PowerShell
$env:CLOUDINARY_NAME=""
$env:CLOUDINARY_KEY=""
$env:CLOUDINARY_SECRET=""
$env:DB_USERNAME=""
$env:DB_PASSWORD=""
$env:DB_URL=""
$env:JWT_SECRET=""
````

IntelliJ: Go to **Run → Edit Configurations → Environment Variables** and add these key-value pairs.

### Run

```bash
# Build and run with Maven
mvn spring-boot:run

# Swagger UI
http://localhost:8080/api/v1/swagger-ui/index.html#/

# OpenAPI docs
http://localhost:8080/api/v1/v3/api-docs
```

---

## API Highlights

### Authentication

| Endpoint        | Method | Description                    | Body / Params      |
| --------------- | ------ | ------------------------------ | ------------------ |
| `/auth/signup`  | POST   | User registration              | `SignUpRequestDto` |
| `/auth/login`   | POST   | Login user                     | `LoginDto`         |
| `/auth/refresh` | POST   | Refresh JWT token              | —                  |
| `/user/me`      | GET    | Get current authenticated user | —                  |

### Posts

| Endpoint                                    | Method | Description                           |
| ------------------------------------------- | ------ | ------------------------------------- |
| `/post`                                     | GET    | Get paginated posts                   |
| `/post`                                     | POST   | Create post (`PostRequestDto`)        |
| `/post/{postId}`                            | GET    | Get post by ID                        |
| `/post/{postId}/like`                       | POST   | Like a post                           |
| `/post/{postId}/like`                       | DELETE | Unlike a post                         |
| `/post/{postId}/cover-image`                | POST   | Upload cover image                    |
| `/post/{postId}/cover-image`                | DELETE | Delete cover image                    |
| `/post/{postId}/comment`                    | POST   | Add comment (`CommentRequestDto`)     |
| `/post/{postId}/comment`                    | GET    | Get top-level comments                |
| `/post/{postId}/comment/{parentId}/replies` | GET    | Get replies for comment               |
| `/post/search`                              | GET    | Search posts (`PostSearchRequestDto`) |

### Tags

| Endpoint    | Method | Description  |
| ----------- | ------ | ------------ |
| `/tag`      | GET    | Get all tags |
| `/tag`      | POST   | Create tag   |
| `/tag/{id}` | PUT    | Update tag   |
| `/tag/{id}` | DELETE | Delete tag   |

### Categories

| Endpoint         | Method | Description        |
| ---------------- | ------ | ------------------ |
| `/category`      | GET    | Get all categories |
| `/category`      | POST   | Create category    |
| `/category/{id}` | PUT    | Update category    |
| `/category/{id}` | DELETE | Delete category    |

### Admin

| Endpoint | Method | Description         |
| -------- | ------ | ------------------- |
| `/admin` | GET    | Admin-only endpoint |

---

## Database & Models

### Key Entities

* **User:** id, name, roles
* **Post:** title, content, coverImageUrl, author, tags, categories, likes, comments
* **Comment:** post, author, parent, content, replies
* **Tag / Category:** name
* **PostLike:** user-post relation

---

## Deployment

* Not deployed yet.

---

## Impact & Metrics

* Handles multiple users with stateless JWT
* Pagination for posts and comments ensures performance
* Cloudinary reduces server storage load
* Prepared for scaling by splitting services if needed

---

## What’s Next

* Add **frontend integration** (React / Next.js)
* Implement **unit & integration tests**
* Add **rate limiting** and advanced security features
* Optimize queries for **high-traffic scenarios**
* Enhance **search** with full-text search and filters

---

