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

####  **UserService**

* **为controller层提供的接口**

   * ```java
     Response login(String userPhone, String password, boolean admin);
     ```

   * ```java
     Response register(User newUser, String cToken);
     ```

   * ```java
       Response register(User newUser, String cToken, String regCode);
       ```

   * ```java
     Response update(User newInfo, String userPhone, String uToken);
     ```

   * ```java
       Response update(String userPhone, String uToken, String cToken, String newPassword);
       ```

   * ```java
     Response studentCertification(StudentCertificate certificate, 
                                          String userPhone, String uToken)
     ```

   * ```java
     Response getByFilter(UserFilter filter, String userPhone, String uToken, 
                                 Integer requestCount)
     ```

* **私有方法**

    * ```java
        private Response ordinaryLogin(String userPhone, String password);
        ```

    * ```java
        private Response adminLogin(String userPhone, String password);
        ```

    * ```java
        private boolean cTokenValid(String phone, String cToken);
        ```

    * ```java
        private boolean regCodeValid(String regCode);
        ```

    * ```java
        private UserToken generateUToken(String userPhone, String userName);
        ```

    * ```java
        private boolean uTokenValid(String userPhone, String uToken);
        ```

    * ```java
        private boolean studentInfoValid(StudentCertificate certificate);
        ```

#### **ResourceService**

* **为controller层提供的接口**

   * ```java
     Response post(Resource resource, String ownerPhone, String uToken);
     ```

   * ```java
     Response release(String resourceCode, String ownerPhone, String uToken, Item item);
     ```

   * ```java
     Response retract(String itemCode, String ownerPhone, String uToken);
     ```

   * ```java
     Response delete(String resourceCode, String ownerPhone, String uToken);
     ```

   * ```java
     Response update(Resource newResource, String resourceCode, 
                            String ownerPhone, String uToken);
     ```

   * ```java
     Response check(String resourceCode, String adminPhone, String uToken, boolean accept);
     ```

   * ```java
     Response getItemByFilter(ItemFilter filter, String userPhone,
                                         String userToken, Integer requestCount);
     ```

   * ```java
     Response getResourceByFilter(ResourceFilter filter, String userPhone,
                                             String userToken, Integer requestCount);
     ```

#### **OrderService**

 * **为controller层提供的接口**

    * ```java
      Response post(String getterPhone, String userToken, String itemCode, Order order);
      ```

    * ```java
      Response delete(String userPhone, String userToken, String orderCode);
      ```

    * ```java
      Response accept(String ownerPhone, String userToken, String orderCode);
      ```

    * ```java
      Response complete(String userPhone, String userToken, String orderCode);
      ```

* **私有方法**

  * ```java
    private boolean isOwnerOfOrder(String userPhone, Order order);
    ```

  * ```java
    private boolean isOwnerOfOrder(String userPhone, Order order);
    ```

  * ```java
    private Response cancel(Order order);
    ```

  * ```java
    private Response acceptOrReject(Order order, boolean accept);
    ```

  * ```java
    private Response getterCompleteOrder(Order order);
    ```

  * ```java
    private Response ownerCompleteOrder(Order order);
    ```

* **Scheduled methods**

    * ```java
      @Scheduled(fixedRate = 600000L)
      void checkOrders();
      ```

    * ```java
      private boolean expiredFromOwner(Order order);
      ```

    * ```java
      private boolean completionExpired(Order order);
      ```

#### **ConfirmationTokenService**

* **为controller层提供的接口**
  
    * ```java
        ConfirmationToken send(String phone);
        ```
    
* **私有方法**
  
    * ```java
      private void sendConfirmationToken(String phone, String token);
      ```

#### **AdminRegistrationCodeService**

* **为controller层提供的接口**
  
    * ```java
      Response addCode(String phone, String userToken);
      ```
    



### 4.Controller的实现

#### User Controller

 * ```java
   @GetMapping("/user")
       public Response ordinaryLogin(@RequestParam(name = "phone") String userPhone,
                                     @RequestParam(name = "password") String password){
           return userService.login(userPhone, password, false);
       }
   ```

 * ```java
   @PostMapping("/user")
       public Response ordinaryRegister(
               @RequestBody User info,
               @RequestParam(name = "cToken") String confirmationToken){
           return userService.register(info, confirmationToken);
       }
   ```

 * ```java
   @PutMapping("user/password")
       public Response updatePassword(
               @RequestParam(name = "phone") String userPhone,
               @RequestParam(name = "uToken") String userToken,
               @RequestParam(name = "cToken") String cToken,
               @RequestParam(name = "newPassword") String newPassword){
           return userService.update(userPhone, userToken, cToken, newPassword);
       }
   ```

 * ```java
   @PutMapping("user")
       public Response updateInfo(
               @RequestBody User user,
               @RequestParam(name = "phone") String userPhone,
               @RequestParam(name = "uToken") String userToken){
           return userService.update(user, userPhone, userToken);
       }
   ```

 * ```java
   @PostMapping("user/student")
       public Response studentCertification(
               @RequestBody StudentCertificate certificate,
               @RequestParam(name = "phone") String userPhone,
               @RequestParam(name = "uToken") String userToken){
           return userService.studentCertification(certificate, userPhone, userToken);
       }
   ```

 * ```java
   @GetMapping("admin")
       public Response adminLogin(@RequestParam(name = "phone") String userPhone,
                                  @RequestParam(name = "password") String password){
           return userService.login(userPhone, password, true);
       }
   ```

 * ```java
   @PostMapping("admin")
       public Response adminRegister(
               @RequestBody User info,
               @RequestParam(name = "regCode") String registrationCode,
               @RequestParam(name = "cToken") String confirmationToken){
           return userService.register(info, registrationCode, confirmationToken);
       }
   ```

 * ```java
   @GetMapping("users")
       public Response getByFilter(
               @RequestParam(name = "uPhone") String userPhone,
               @RequestParam(name = "uToken") String userToken,
               @RequestParam(name = "phone", required = false) String phone,
               @RequestParam(name = "name", required = false) String name,
               @RequestParam(name = "qqId", required = false) String qqId,
               @RequestParam(name = "wechatId", required = false) String wechatId,
               @RequestParam(name = "studentCertified", required = false) Boolean student_certified,
               @RequestParam(name = "requestCount", required = false) Integer requestCount){
           return userService.getByFilter(filter, userPhone, userToken, requestCount);
       }
   ```

#### ResourceController

 * ```java
   @PostMapping("resource")
       public Response postNewResource(
               @RequestBody Resource resource,
               @RequestParam(name = "phone") String phone,
               @RequestParam(name = "uToken") String userToken){
           return resourceServiceImpl.post(resource, phone, userToken);
       }
   ```

 * ```java
   @PostMapping("item")
   public Response releaseResource(
           @RequestParam(name = "phone") String phone,
           @RequestParam(name = "resourceCode") String resourceCode,
           @RequestParam(name = "uToken") String userToken,
           @RequestBody Item item){
       return resourceServiceImpl.release(resourceCode, phone, userToken, item);
   }
   ```

 * ```java
   @DeleteMapping("item")
       public Response retractResource(@RequestParam(name = "phone") String phone,
                                       @RequestParam(name = "itemCode") String itemCode,
                                       @RequestParam(name = "uToken") String userToken){
           return resourceServiceImpl.retract(itemCode, phone, userToken);
       }
   ```

 * ```java
   @PutMapping("resource")
       public Response updateResourceInfo(
               @RequestBody Resource resource,
               @RequestParam(name = "phone") String phone,
               @RequestParam(name = "resourceCode") String resourceCode,
               @RequestParam(name = "uToken") String userToken){
           return resourceServiceImpl.update(resource, resourceCode, phone, userToken);
       }
   ```

 * ```java
   @PostMapping("resource/admin")
       public Response checkResource(@RequestParam(name = "phone") String userPhone,
                                     @RequestParam(name = "resourceCode") String resourceCode,
                                     @RequestParam(name = "uToken") String userToken,
                                     @RequestParam(name = "valid") Boolean valid){
           return resourceServiceImpl.check(resourceCode, userPhone, userToken, valid);
       }
   ```

 * ```java
   @GetMapping("items")
       public Response getItem(@RequestParam(name = "uPhone") String userPhone,
                               @RequestParam(name = "uToken") String userToken,
                               @RequestParam(required = false) String code,
                               @RequestParam(required = false) String type,
                               @RequestParam(required = false) Boolean needs2pay,
                               @RequestParam(required = false) Integer campus,
                               @RequestParam(required = false) String resourceCode,
                               @RequestParam(required = false) Integer requestCount){
           return Response.success(resourceServiceImpl.getItemByFilter(filter, userPhone, 
                                                                   userToken, requestCount));
       }
   ```

 * ```java
   @GetMapping("resources")
       public Response getResource(@RequestParam(name = "uPhone") String userPhone,
                                   @RequestParam(name = "uToken") String userToken,
                                   @RequestParam(name = "code", required = false) String code,
                                   @RequestParam(name = "name", required = false) String name,
                                   @RequestParam(name = "verified", required = false) Boolean verified,
                                   @RequestParam(name = "released", required = false) Boolean released,
                                   @RequestParam(name = "accepted", required = false) Boolean accepted,
                                   @RequestParam(name = "description", required = false) String description,
                                   @RequestParam(name = "tag", required = false) String tag,
                                   @RequestParam(name = "phone", required = false) String owner_phone,
                                   @RequestParam(name = "requestCount", required = false) 
                                   Integer requestCount){
           return Response.success(resourceServiceImpl.getResourceByFilter(filter, userPhone, 
                                                                       userToken, requestCount));
       }
   ```

#### Order Controller

 * ```java
   @PostMapping ("order/new")
       public Response newOrder(@RequestParam(name = "phone") String phone,
                                @RequestParam(name = "itemCode") String itemCode,
                                @RequestParam(name = "uToken") String userToken,
                                @RequestBody(required = false) Order order){
           return orderService.post(phone, userToken, itemCode, order);
       }
   ```

 * ```java
   @DeleteMapping("order")
       public Response deleteOrder(@RequestParam(name = "phone") String phone,
                                   @RequestParam(name = "orderCode") String orderCode,
                                   @RequestParam(name = "uToken") String userToken){
           return orderService.delete(phone, userToken, orderCode);
       }
   ```

 * ```java
   @PostMapping("order")
       public Response acceptOrder(@RequestParam(name = "phone") String phone,
                                   @RequestParam(name = "orderCode") String orderCode,
                                   @RequestParam(name = "uToken") String userToken){
           return orderService.accept(phone, userToken, orderCode);
       }
   ```

 * ```java
   @PutMapping("order")
       public Response completeOrder(@RequestParam(name = "phone") String phone,
                                     @RequestParam(name = "orderCode") String orderCode,
                                     @RequestParam(name = "uToken") String userToken){
           return orderService.complete(phone, userToken, orderCode);
       }
   ```

### 5.Response

 * **属性**

    * ```java
      private String message;
      ```

    * ```java
      private int code;
      ```

    * ```java
      private Object data
      ```

 * **定义了多个public static方法，可直接获取相对应的Response结构：**

   | code | message                              | data   |
   | ---- | ------------------------------------ | ------ |
   | 100  | success                              | Object |
   | 600  | sending confirmation token failed    | null   |
   | 601  | invalid phone                        | null   |
   | 602  | invalid confirmation token           | null   |
   | 603  | invalid password                     | null   |
   | 604  | invalid student information          | null   |
   | 605  | invalid registration code            | null   |
   | 606  | insufficient authority               | null   |
   | 607  | invalid user token                   | null   |
   | 701  | invalid resource code                | null   |
   | 702  | resource unavailable to release      | null   |
   | 703  | resource already released            | null   |
   | 704  | resource not belong to the user      | null   |
   | 801  | invalid item code                    | null   |
   | 802  | item already ordered                 | null   |
   | 803  | item not belong to the user          | null   |
   | 804  | item is not sufficient               | null   |
   | 901  | invalid order code                   | null   |
   | 902  | order not belonged to the user       | null   |
   | 903  | order already accepted by owner      | null   |
   | 904  | order is closed by at least one side | null   |
   | 905  | order already canceled               | null   |
   | 906  | order expired                        | null   |
   | 907  | order not accepted                   | null   |
   | 908  | order is closed at your side         | null   |
   | 909  | order closed                         | null   |
   | 1001 | image not found                      | null   |

   

