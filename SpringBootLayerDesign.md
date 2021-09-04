## 北洋资源管理系统后端设计

### 1.模型设计

 * **User**

   ```sql
   CREATE TABLE users (
   		id 					INTEGER 		NOT NULL,
           avatar_url 			TEXT,
           password 			VARCHAR(16) 	NOT NULL,
           phone 				VARCHAR(11) 	NOT NULL,
           qq_id 				VARCHAR(20),
           student_certified 	BOOLEAN 		NOT NULL,
           student_id 			VARCHAR(20),
           user_name 			VARCHAR(20) 	NOT NULL,
           user_tag 			VARCHAR(20) 	NOT NULL,
           wechat_id 			VARCHAR(20),
           PRIMARY KEY (id)
       )
   ```

   

 * **Resource**

   ```sql
   CREATE TABLE resources (
          	resource_code 		VARCHAR 		NOT NULL,
           accepted 			BOOLEAN 		NOT NULL,
           description 		TEXT 			NOT NULL,
           image_url 			TEXT 			NOT NULL,
           owner_phone 		VARCHAR(11),
           released 			BOOLEAN 		NOT NULL,
           resource_name 		VARCHAR(20) 	NOT NULL,
           resource_tag 		VARCHAR(20) 	NOT NULL,
           verified 			BOOLEAN 		NOT NULL,
           PRIMARY KEY (resource_code)
       )
   ```

* **Order**

  ```sql
  CREATE TABLE orders (
         	order_code 					VARCHAR 			NOT NULL,
          accepted 					BOOLEAN 			NOT NULL,
          accepted_or_rejected 		BOOLEAN 			NOT NULL,
          accepted_rejected_time 		TIMESTAMP,
          accept_expires_at 			TIMESTAMP 			NOT NULL,
          canceled 					BOOLEAN 			NOT NULL,
          canceled_time 				TIMESTAMP,
          closed_time 				TIMESTAMP,
          comments 					TEXT,
          completed 					BOOLEAN 			NOT NULL,
          completed_by_getter 		BOOLEAN 			NOT NULL,
          completed_by_owner 			BOOLEAN 			NOT NULL,
          completion_expires_at 		TIMESTAMP 			NOT NULL,
          count 						INTEGER 			NOT NULL,
          expired 					BOOLEAN 			NOT NULL,
          getter_phone 				VARCHAR(11) 		NOT NULL,
          item_code 					VARCHAR				NOT NULL,
          opened_time 				TIMESTAMP 			NOT NULL,
          owner_phone 				VARCHAR(11) 		NOT NULL,
          uncompleted_by_getter 		BOOLEAN 			NOT NULL,
          uncompleted 				BOOLEAN 			NOT NULL,
          uncompleted_by_owner 		BOOLEAN 			NOT NULL,
          PRIMARY KEY (order_code)
      )
  ```

* **Item**

  ```sql
  CREATE TABLE items (
          item_code			VARCHAR 		NOT NULL,
          campus 				INTEGER 		NOT NULL,
          count 				INTEGER 		NOT NULL,
          ends_at 			TIMESTAMP,
          fee 				INTEGER,
          fee_unit 			VARCHAR(10),
          item_type 			TEXT 			NOT NULL,
          needs2pay 			boolean 		NOT NULL,
          on_time 			TIMESTAMP 		NOT NULL,
          ordered 			boolean 		NOT NULL,
          owner_phone 		VARCHAR(11),
          resource_code 		VARCHAR			NOT NULL,
          starts_at 			TIMESTAMP,
          PRIMARY KEY (item_code)
      )
  ```

* **ConfirmationToken**

  ```sql
  CREATE TABLE confirmation_tokens (
          id 				INTEGER 		NOT NULL,
          confirmed 		BOOLEAN 		NOT NULL,
          confirmed_at 	TIMESTAMP,
          created_at 		TIMESTAMP 		NOT NULL,
          expires_at 		TIMESTAMP 		NOT NULL,
          token 			VARCHAR 		NOT NULL,
          user_phone 		VARCHAR(11) 	NOT NULL,
          PRIMARY KEY (id)
      )
  ```

* **AdminRegistrationCode**

  ```sql
  CREATE TABLE admin_registration_codes (
          id 		INTEGER not null,
          code 	VARCHAR not null,
          used 	BOOLEAN not null,
          PRIMARY KEY (id)
      )
  ```

* **StudentId**

  ```sql
  CREATE TABLE student_id (
          id 					INTEGER not null,
          student_id 			VARCHAR not null,
          student_name 		VARCHAR not null,
          student_password 	VARCHAR not null,
          used 				BOOLEAN not null,
          PRIMARY KEY (id)
      )
  ```

* **UserToken**

  ```sql
  CREATE TABLE user_tokens (
          user_phone 		VARCHAR(11) NOT NULL,
          token 			VARCHAR 	NOT NULL,
          user_name 		VARCHAR(20) NOT NULL,
          PRIMARY KEY (user_phone)
      )
  ```

* **Constraints**

  ```sql
  ALTER TABLE admin_registration_codes 
         ADD CONSTRAINT admin_code_unique UNIQUE (code);
      
  ALTER TABLE student_id 
         ADD CONSTRAINT student_id_unique UNIQUE (student_id);
      
  ALTER TABLE users 
         ADD CONSTRAINT user_phone_unique UNIQUE (phone);
      
  ALTER TABLE users 
         ADD CONSTRAINT user_studentId_unique UNIQUE (student_id);
  ```

### 2.Repository设计

* 建立了以下Repository接口：
  * `UserRepository`
  * `ResourceRepository`
  * `ItemRepository`
  * `OrderRepository`
  * `StudentIdRepository`
  * `ConfirmationTokenRepository`
  * `AdminResgitrationCodeRepository`
  * `UserTokenRepository`
* 上述所有的Repository接口都继承了`JpaRepository`

### 3.Service设计

 * **UserService**

    * ```java
      public Response ordinaryLogin(String userPhone, String password);
      ```

    * ```java
      public Response updateInfo(String userPhone, User user);
      ```

    * ```java
      public Response adminLogin(String userPhone, String password);
      ```

    * ```java
      public void addNewUser(User user, boolean isAdmin);
      ```

    * ```java
      public Response isAdmin(String phone);
      ```

    * ```java
      public List<User> getByFilter(UserFilter filter);
      ```

 * **ResourceService**

    * ```java
      public Response postNewResource(Resource resource, String ownerPhone);
      ```

    * ```java
      public Response releaseResource(String resourceCode, String ownerPhone, Item itemInfo);
      ```

    * ```java
      public Response retractItem(String itemCode, String ownerPhone);
      ```

    * ```java
      public Response deleteResource(String resourceCode, String ownerPhone);
      ```

    * ```java
      public Response updateResourceInfo(Resource resourceInfo, String resourceCode, String ownerPhone)
      ```

    * ```java
      public Respone acceptOrRejectResource(String resourceCode, String adminPhone, boolean accepted);
      ```

    * ```java
      public Response getItemByFilter(ItemFilter filter);
      ```

    * ```java
      public Response getResourceByFilter(ResourceFilter filter);
      ```

    * ```java
      public Response getPage(ItemPage page); 
      ```

* **OrderService**

  

