# 社群媒體系統 - 需求實現說明

## 📋 專案概述

本專案是一個完整的社群媒體平台，採用 Vue.js + Spring Boot + MySQL 的三層式架構，實現了註冊、登入、發文、留言等核心功能。

---

## 🎯 功能需求實現

### 1. 註冊功能 ✅
**要求：** 使用者可以透過註冊功能註冊帳號，以手機號碼進行註冊與登入

**實現位置：**
- **前端頁面：** `frontend/src/views/RegisterView.vue`
- **後端控制器：** `src/main/java/com/nanco/social/controller/UserController.java` (第 27 行 `@PostMapping("/register")`)
- **業務邏輯：** `src/main/java/com/nanco/social/service/impl/UserServiceImpl.java`
- **資料存取：** `src/main/java/com/nanco/social/repository/impl/UserRepositoryImpl.java` (第 25 行 `createUser` 方法)
- **資料庫操作：** `DB/procedures.sql` (第 4-11 行 `sp_create_user` 程序)

### 2. 登入驗證功能 ✅
**要求：** 系統需實作身份驗證功能，以確保只有登入的使用者可以發文或留言

**實現位置：**
- **前端頁面：** `frontend/src/views/LoginView.vue`
- **後端控制器：** `src/main/java/com/nanco/social/controller/UserController.java` (第 34 行 `@PostMapping("/login")`)
- **JWT 過濾器：** `src/main/java/com/nanco/social/common/filter/JwtRequestFilter.java`
- **安全配置：** `src/main/java/com/nanco/social/common/config/WebSecurityConfig.java`
- **JWT 工具類：** `src/main/java/com/nanco/social/common/util/JwtUtil.java`
- **登入驗證程序：** `DB/procedures.sql` (第 36-42 行 `sp_user_login` 程序)

### 3. 發文功能 ✅
**要求：** 新增發文、列出所有發文、編輯或刪除發文

**實現位置：**
- **前端頁面：** `frontend/src/views/PostsView.vue`
- **後端控制器：** `src/main/java/com/nanco/social/controller/PostController.java`
  - 新增發文：第 22 行 `@PostMapping("/create")`
  - 列出發文：第 28 行 `@GetMapping("/all")`
  - 編輯發文：第 34 行 `@PutMapping("/edit/{postId}")`
  - 刪除發文：第 44 行 `@DeleteMapping("/delete/{postId}")`
- **資料庫程序：** `DB/procedures.sql`
  - 新增：第 14-20 行 `sp_create_post`
  - 列出：第 45-50 行 `sp_get_all_posts`
  - 編輯：第 60-66 行 `sp_edit_post`
  - 刪除：第 69-74 行 `sp_delete_post`

### 4. 留言功能 ✅
**要求：** 使用者可以針對發文新增留言

**實現位置：**
- **前端組件：** `frontend/src/views/PostsView.vue` (留言功能整合在發文頁面)
- **後端控制器：** `src/main/java/com/nanco/social/controller/CommentController.java` (第 19 行 `@PostMapping("/create")`)
- **資料庫程序：** `DB/procedures.sql` (第 23-30 行 `sp_create_comment`)
- **留言查詢：** `DB/procedures.sql` (第 53-58 行 `sp_get_post_comments`)

---

## 🏗️ 系統架構需求實現

### 5. 三層式架構 ✅
**要求：** 使用 Web Server + Application Server + 關聯式資料庫的三層式架構

**實現說明：**
- **Web Server (前端)：** Vue.js 應用 (`frontend/` 目錄)
- **Application Server (後端)：** Spring Boot 應用 (`src/main/java/` 目錄)
- **關聯式資料庫：** MySQL 資料庫 (`DB/` 目錄包含資料庫結構)

### 6. 後端分層設計 ✅
**要求：** 展示層、業務層、資料層以及共用層

**實現位置：**
- **展示層 (Controller)：** `src/main/java/com/nanco/social/controller/`
  - `UserController.java` - 用戶相關 API
  - `PostController.java` - 發文相關 API
  - `CommentController.java` - 留言相關 API
- **業務層 (Service)：** `src/main/java/com/nanco/social/service/`
- **資料層 (Repository)：** `src/main/java/com/nanco/social/repository/`
- **共用層 (Common)：** `src/main/java/com/nanco/social/common/`
  - `config/` - 配置類
  - `util/` - 工具類
  - `filter/` - 過濾器
  - `exception/` - 異常處理

---

## 🛠️ 技術需求實現

### 7. Vue.js 前端 ✅
**要求：** 使用 Vue.js 做為前端技術

**實現位置：**
- **專案配置：** `frontend/package.json` (第 13 行 `"vue": "^3.5.13"`)
- **主要組件：** `frontend/src/` 目錄下所有 `.vue` 檔案
- **路由配置：** `frontend/src/router/index.js`
- **狀態管理：** `frontend/src/store/`

### 8. Spring Boot 應用 ✅
**要求：** 使用 Spring Boot 搭建相關應用程式

**實現位置：**
- **專案配置：** `pom.xml` (第 7 行 Spring Boot 版本 2.7.0)
- **主程式：** `src/main/java/com/nanco/social/SocialApplication.java`
- **配置檔案：** `src/main/resources/application.properties`

### 9. RESTful API 風格 ✅
**要求：** 使用 RESTful API 風格建立後端服務

**實現說明：**
所有 Controller 都採用 RESTful 設計：
- **GET** `/api/posts/all` - 獲取所有發文
- **POST** `/api/posts/create` - 新增發文
- **PUT** `/api/posts/edit/{postId}` - 編輯發文
- **DELETE** `/api/posts/delete/{postId}` - 刪除發文
- **POST** `/api/user/register` - 用戶註冊
- **POST** `/api/user/login` - 用戶登入
- **POST** `/api/comments/create` - 新增留言

### 10. Maven 專案管理 ✅
**要求：** 使用 Maven 或 Gradle 做為專案建立的工具

**實現位置：**
- **Maven 配置：** `pom.xml`
- **Maven Wrapper：** `mvnw` 和 `mvnw.cmd`
- **Maven 配置目錄：** `.mvn/`

### 11. Stored Procedure 存取 ✅
**要求：** 透過 Stored Procedure 存取資料庫

**實現位置：**
- **程序定義：** `DB/procedures.sql` (包含所有資料庫操作的程序)
- **Java 調用：** 所有 `*RepositoryImpl.java` 檔案都使用 `CALL` 語句調用程序
  - 例如：`UserRepositoryImpl.java` 第 27 行 `"CALL sp_create_user(?, ?, ?, ?)"`

### 12. Transaction 實作 ✅
**要求：** 需同時異動多個資料表時，請實作 Transaction

**實現位置：**
- **資料庫層級：** `DB/procedures.sql` (第 25-29 行在 `sp_create_comment` 中使用 `START TRANSACTION` 和 `COMMIT`)
- **Spring 層級：** Service 層使用 `@Transactional` 註解 (在 `*ServiceImpl.java` 檔案中)

### 13. DDL 和 DML 檔案 ✅
**要求：** 資料庫的 DDL 和 DML 請存放在專案下的 \DB 資料夾內

**實現位置：**
- **DDL (資料定義)：** `DB/ddl.sql` - 包含所有資料表結構
- **DML (資料操作)：** `DB/dml.sql` - 包含測試資料
- **Stored Procedures：** `DB/procedures.sql` - 包含所有程序定義

---

## 🔒 安全性需求實現

### 14. SQL Injection 防護 ✅
**要求：** 需防止 SQL Injection 攻擊

**實現方式：**
- **參數化查詢：** 所有資料庫操作都使用參數化查詢
- **Stored Procedure：** 通過 SP 封裝 SQL 邏輯
- **範例：** `UserRepositoryImpl.java` 第 43 行
  ```java
  jdbcTemplate.query("CALL sp_user_login(?, ?)", 
                     new Object[]{phoneNumber, password}, ...)
  ```

### 15. XSS 攻擊防護 ✅
**要求：** 需防止 XSS 攻擊

**實現位置：**
- **Spring Security 配置：** `WebSecurityConfig.java` 提供基本防護
- **前端過濾：** Vue.js 預設提供 XSS 防護
- **內容安全：** 前端模板使用 `{{ }}` 語法自動轉義

### 16. 密碼安全存儲 ✅
**要求：** 密碼請加鹽(salt)並經雜湊(Hash)後儲存

**實現位置：**
- **資料庫層級：** `DB/procedures.sql` 中所有涉及密碼的程序都使用：
  ```sql
  SHA2(CONCAT(p_password, 'salt123'), 256)
  ```
- **範例程序：**
  - 註冊：第 10 行 `sp_create_user`
  - 登入：第 41 行 `sp_user_login`
- **測試資料：** `DB/dml.sql` 中所有用戶密碼都經過雜湊處理

---

## 📊 資料表設計

### 17. 基礎資料表 ✅
**實現位置：** `DB/ddl.sql`

**User 資料表 (第 2-14 行)：**
- ✅ User ID (Primary Key)
- ✅ User Name (使用者名稱)
- ✅ Email (電子郵件)
- ✅ Password (雜湊後密碼)
- ✅ Phone Number (手機號碼 - 用於登入)
- ✅ Biography (自我介紹)

**Post 資料表 (第 17-26 行)：**
- ✅ Post ID (Primary Key)
- ✅ User ID (Foreign Key)
- ✅ Content (發文內容)
- ✅ Created At (發文時間)

**Comment 資料表 (第 30-42 行)：**
- ✅ Comment ID (Primary Key)
- ✅ User ID (Foreign Key)
- ✅ Post ID (Foreign Key)
- ✅ Content (留言內容)
- ✅ Created At (留言時間)

---

## 🚀 運行說明

### 環境要求
- Java 11+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### 啟動步驟

1. **準備資料庫：**
   ```bash
   mysql -u root -p < DB/ddl.sql
   mysql -u root -p < DB/procedures.sql
   mysql -u root -p < DB/dml.sql
   ```

2. **啟動後端：**
   ```bash
   mvn spring-boot:run
   ```

3. **啟動前端：**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

4. **訪問應用：**
   - 前端：http://localhost:5173
   - 後端 API：http://localhost:8080

---

## 📈 專案完成度

| 功能需求 | 狀態 | 實現度 |
|---------|------|--------|
| 註冊功能 | ✅ | 100% |
| 登入驗證 | ✅ | 100% |
| 發文功能 | ✅ | 100% |
| 留言功能 | ✅ | 100% |
| 三層架構 | ✅ | 100% |
| Vue.js 前端 | ✅ | 100% |
| Spring Boot 後端 | ✅ | 100% |
| RESTful API | ✅ | 100% |
| Maven 專案管理 | ✅ | 100% |
| Stored Procedure | ✅ | 100% |
| Transaction | ✅ | 100% |
| 安全防護 | ✅ | 100% |
| 密碼加密 | ✅ | 100% |

**總體完成度：100%** 🎉

---

## 📝 測試帳號

使用 `DB/dml.sql` 中的測試資料，可以使用以下帳號登入：

| 用戶名 | 手機號碼 | 密碼 |
|--------|----------|------|
| Alice_Chen | 0911111111 | password123 |
| Bob_Wang | 0922222222 | securepass456 |
| Cathy_Lin | 0933333333 | mypassword789 |

---

## 🔧 開發工具配置

- **IDE：** 建議使用 IntelliJ IDEA 或 VS Code
- **資料庫管理：** 建議使用 MySQL Workbench 或 phpMyAdmin
- **API 測試：** 建議使用 Postman 或 curl

---

## 📞 聯絡資訊

如有任何問題，請參考程式碼註釋或聯絡開發團隊。 