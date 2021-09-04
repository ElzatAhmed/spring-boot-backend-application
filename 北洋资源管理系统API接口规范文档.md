## 北洋资源管理系统API接口规范文档

### 1.用户信息接口

 * **用户登录接口**

   * HTTP报头：`GET`

   * 请求URI：`api/v1/user`

   * 请求参数：

     | 参数名     | 参数类型 | 参数说明   |
     | :--------- | :------- | :--------- |
     | `phone`    | String   | 用户手机号 |
     | `password` | String   | 密码       |

   * Json参数：无

   * 用例示范

     `GET localhost:8080/api/v1/user/?phone=176********&password=******`

* **用户注册接口**

  * HTTP报头：`POST`

  * 请求URI：`api/v1/user`

  * 请求参数：

    | 参数名   | 参数类型 | 参数说明             |
    | -------- | -------- | -------------------- |
    | `cToken` | String   | 用户收到的短信验证码 |

  * json参数：

    | 参数名     | 参数类型 | 参数说明   |
    | ---------- | -------- | ---------- |
    | `phone`    | String   | 用户手机号 |
    | `userName` | String   | 用户名     |
    | `password` | String   | 用户密码   |

  * 用例示范：

    `POST	localhost:8080/api/v1/user/?cToken=******`

    ```json
    {
        "phone": "",
        "userName": "",
        "password": ""
    }
    ```

* **用户更改信息接口**

  * HTTP报头：`PUT`

  * 请求URL：`api/v1/user`

  * 请求参数：

    | 参数名   | 参数类型 | 参数说明            |
    | -------- | -------- | ------------------- |
    | `phone`  | String   | 用户手机号          |
    | `uToken` | String   | 用户被分配的token码 |

  * json参数：

    | 参数名      | 参数类型 | 参数说明 |
    | ----------- | -------- | -------- |
    | `userName`  | String   | 用户名   |
    | `qqId`      | String   | qq号     |
    | `wechatId`  | String   | 微信号   |
    | `avatarUrl` | String   | 头像url  |

  * 用例示范：

    `PUT	localhost:8080/api/v1/user/?phone=176********&uToken=*********`

    ```json
    {
        "userName": "",
        "qqId": "",
        "wechatId": "",
        "avatarUrl": ""
    }
    ```

* **用户更改密码接口**

   * HTTP报头：`PUT`

   * 请求URL：`api/v1/user/password`

   * 请求参数：

     | 参数名        | 参数类型 | 参数说明              |
     | ------------- | -------- | --------------------- |
     | `phone`       | String   | 用户手机号            |
     | `uToken`      | String   | 用户在被分配的token码 |
     | `cToken`      | String   | 短信验证码            |
     | `newPassword` | String   | 新密码                |

  * json参数：无

  * 用例示范：

    `PUT	localhost:8080/api/v1/user/?phone=***&uToken***&cToken=***&newPassword=***`

* **学生认证接口**

  * HTTP报头：`POST`

  * 请求URL：`api/v1/user/student`

  * 请求参数：

    | 参数名   | 参数类型 | 参数说明              |
    | -------- | -------- | --------------------- |
    | `phone`  | String   | 用户手机号            |
    | `uToken` | String   | 用户在被分配的token码 |

  * json参数：

    | 参数名            | 参数类型 | 参数说明           |
    | ----------------- | -------- | ------------------ |
    | `studentId`       | String   | 天津大学学生号     |
    | `studentName`     | String   | 学生真实姓名       |
    | `studentPassword` | String   | 天津大学办公网密码 |

  * 用例示范：

    `PUT	localhost:8080/api/v1/user/student/?phone=***&uToken=***`

    ```json
    {
        "studentId": "",
        "studentName": "",
        "studentPassword": ""
    }
    ```

* **管理员登录接口**

  * HTTP报头：`GET`

  * 请求URL：`api/v1/admin`

  * 请求参数：

    | 参数名     | 参数类型 | 参数说明     |
    | ---------- | -------- | ------------ |
    | `phone`    | String   | 管理员手机号 |
    | `password` | String   | 密码         |

  * json参数：无

  * 用例示范：

    `GET	localhost:8080/api/v1/admin/?phone=***&password=***`

* **管理员注册接口**

  * HTTP报头：`POST`

  * 请求URL：`api/v1/admin`

  * 请求参数：

    | 参数名    | 参数类型 | 参数说明       |
    | --------- | -------- | -------------- |
    | `regCode` | String   | 管理员注册代码 |
    | `cToken`  | String   | 短信验证码     |

  * json参数：

    | 参数名     | 参数类型 | 参数说明 |
    | ---------- | -------- | -------- |
    | `phone`    | String   | 手机号   |
    | `userName` | String   | 用户名   |
    | `password` | String   | 密码     |

  * 用例示范：

    `POST	api/v1/admin/?regCode=***&cToken=***`

    ```json
    {
        "phone": "",
        "userName": "",
        "password": ""
    }
    ```

* **获取用户信息**

  * HTTP报头：`GET`

  * 请求URL：`api/v1/users`

  * 请求参数：

    | 参数名             | 参数类型 | 参数说明                       |
    | ------------------ | -------- | ------------------------------ |
    | `uPhone`           | String   | 请求者手机号                   |
    | `uToken`           | String   | 请求者token码                  |
    | `phone`            | String   | 手机号，过滤参数，非必需       |
    | `name`             | String   | 用户名，过滤参数，非必需       |
    | `qqId`             | String   | qq号，过滤参数，非必需         |
    | `wechatId`         | String   | 微信号，过滤参数，非必需       |
    | `studentCertified` | Boolean  | 是否学生认证，过滤参数，非必需 |

  * json参数：无

  * 用例示范：

    * 获取所有用户信息：

      `GET	localhost:8080/api/v1/users/?uPhone=***&uToken=***`

    * 获取手机号为xxx的用户：

      `GET	localhost:8080/api/v1/users/?uPhone=***&uToken=***&phone=xxx`

    * 获取用户名中包含sh的用户：

      `GET	localhost:8080/api/v1/users/?uPhone=***&uToken=***&name=sh`

    * 获取用户名中包含sh且学生认证过的用户：

      `GET	localhost:8080/api/v1/users/?uPhone=***&uToken=***&name=sh&studentCertified=true`

### 2.资源信息接口

 * **添加新资源接口**

   * HTTP报头：`POST`

   * 请求URL：`api/v1/resource`

   * 请求参数：

     | 参数名   | 参数类型 | 参数说明              |
     | -------- | -------- | --------------------- |
     | `phone`  | String   | 用户手机号            |
     | `uToken` | String   | 用户在被分配的token码 |

   * json参数

     | 参数名         | 参数类型 | 参数说明   |
     | -------------- | -------- | ---------- |
     | `resourceName` | String   | 资源名     |
     | `description`  | String   | 资源描述   |
     | `resourceTag`  | String   | 资源标签   |
     | `imageUrl`     | String   | 资源图链接 |

   * 用例示范：

     `POST	localhost:8080/api/v1/resource/?phone=***&uToken=***`

     ```json
     {
         "resourceName": "",
         "description": "",
         "resourceTag": "",
         "imageUrl": ""
     }
     ```

 * **发布资源接口**

    * HTTP报头：`POST`

    * 请求URL：`api/v1/item`

    * 请求参数：

      | 参数名         | 参数类型 | 参数说明    |
      | -------------- | -------- | ----------- |
      | `phone`        | String   | 用户手机号  |
      | `resourceCode` | String   | 资源编号    |
      | `uToken`       | String   | 用户token码 |

   * json参数：

     | 参数名      | 参数类型 | 参数说明                  |
     | ----------- | -------- | ------------------------- |
     | `count`     | Integer  | 数量                      |
     | `itemType`  | String   | 类型（销售、租赁等）      |
     | `needs2Pay` | Boolean  | 是否需要付款              |
     | `fee`       | Integer  | 价格                      |
     | `feeUnit`   | String   | 价格单位（元、元/小时等） |
     | `campus`    | Integer  | 校区（0老、1新、2都）     |

   * 用例示范：

     `POST	localhost:8080/api/v1/item/?phone=***&resourceCode=***&uToken=***`

     ```json
     {
         "count": "",
         "itemType": "",
         "needs2Pay": "",
         "fee": "",
         "feeUnit": "",
         "campus": ""
     }
     ```

* **撤回商品接口**

  * HTTP报头：`DELETE`

  * 请求URL：`api/v1/item`

  * 请求参数：

    | 参数名     | 参数类型 | 参数说明    |
    | ---------- | -------- | ----------- |
    | `phone`    | String   | 用户手机号  |
    | `itemCode` | String   | 商品编号    |
    | `uToken`   | String   | 用户token码 |

  * json参数：无

  * 用例示范：

    `DELETE	localhost:8080/api/v1/item/?phone=***&itemCode=***&uToken=***`

* **删除资源接口**

  * HTTP报头：`DELETE`

  * 请求URL：`api/v1/resource`

  * 请求参数：

    | 参数名         | 参数类型 | 参数说明    |
    | -------------- | -------- | ----------- |
    | `phone`        | String   | 用户手机号  |
    | `resourceCode` | String   | 资源编号    |
    | `uToken`       | String   | 用户token码 |

  * 用例示范：

    `DELETE	localhost:8080/api/v1/resource/?phone=***&resourceCode=***&uToken=***`

* **更新资源信息接口**

  * HTTP报头：`PUT`

  * 请求URL：`api/v1/resource`

  * 请求参数：

    | 参数名         | 参数类型 | 参数说明    |
    | -------------- | -------- | ----------- |
    | `phone`        | String   | 用户手机号  |
    | `resourceCode` | String   | 资源编号    |
    | `uToken`       | String   | 用户token码 |

  * json参数：

    | 参数名         | 参数类型 | 参数说明   |
    | -------------- | -------- | ---------- |
    | `resourceName` | String   | 资源名     |
    | `description`  | String   | 资源描述   |
    | `resourceTag`  | String   | 资源标签   |
    | `imageUrl`     | String   | 资源图链接 |

  * 用例示范：

    `PUT	localhost:8080/api/v1/resource/?phone=***&resourceCode=***&uToken=***`

    ```json
    {
        "resourceName": "",
        "description": "",
        "resourceTag": "",
        "imageUrl": ""
    }
    ```

* **管理员审核资源接口**

  * HTTP报头：`POST`

  * 请求URL：`api/v1/resource/admin`

  * 请求参数：

    | 参数名         | 参数类型 | 参数说明     |
    | -------------- | -------- | ------------ |
    | `phone`        | String   | 管理员手机号 |
    | `resourceCode` | String   | 资源编号     |
    | `uToken`       | String   | token码      |
    | `valid`        | Boolean  | 是否审核通过 |

  * json参数：无

  * 用例示范：

    * 审核通过:	

      `POST	localhost:8080/api/v1/resource/admin/?phone=*&resourceCode=*&uToken=*&valid=true`

    * 审核拒绝：

      `POST	localhost:8080/api/v1/resource/admin/?phone=*&resourceCode=*&uToken=*&valid=false`

* **获取商品信息接口**

  * HTTP报头：`GET`

  * 请求URL：`api/v1/items`

  * 请求参数：

    | 参数名         | 参数类型 | 参数说明                       |
    | -------------- | -------- | ------------------------------ |
    | `uPhone`       | String   | 请求者手机号                   |
    | `uToken`       | String   | 请求者token码                  |
    | `code`         | String   | 商品编号，过滤参数，非必需     |
    | `type`         | String   | 商品类型，过滤参数，非必需     |
    | `needs2Pay`    | Boolean  | 是否需要付款，过滤参数，非必需 |
    | `campus`       | Integer  | 校区，过滤参数，非必需         |
    | `resourceCode` | String   | 资源编号，过滤参数，非必需     |

  * 用例示范：

    * 获取所有商品

      `GET	localhost:8080/api/v1/items/?uPhone=***&uToken=***`

    * 获取指定编号的商品

      `GET	localhost:8080/api/v1/items/?uPhone=***&uToken=***&code=***`

    * 获取不需要付款且在双校区都能获取的商品

      `GET	localhost:8080/api/v1/items/?uPhone=***&uToken=***&needs2Pay=false&campus=2`

* **获取资源信息接口**

  * HTTP报头：`GET`

  * 请求URL：`api/v1/resources`

  * 请求参数：

    | 参数名        | 参数类型 | 参数说明                         |
    | ------------- | -------- | -------------------------------- |
    | `uPhone`      | String   | 请求者手机号                     |
    | `uToken`      | String   | 请求者token码                    |
    | `code`        | String   | 资源编号，过滤参数，非必需       |
    | `name`        | String   | 资源名，过滤参数，非必需         |
    | `verified`    | Boolean  | 是否已审核，过滤参数，非必需     |
    | `released`    | Boolean  | 是否已发布，过滤参数，非必需     |
    | `accepted`    | Boolean  | 是否已审核通过，过滤参数，非必需 |
    | `description` | String   | 资源描述，过滤参数，非必需       |
    | `tag`         | String   | 资源标签，过滤参数，非必需       |
    | `phone`       | String   | 资源主手机号，过滤参数，非必需   |

  * 用例示范：

    * 获取所有资源：

      `GET	localhost:8080/api/v1/resource/?uPhone=***&uToken=***`

    * 获取指定编号的资源：

      `GET	localhost:8080/api/v1/resource/?uPhone=***&uToken=***&code=***`

    * 获取名字中包含food且已审核通过的资源：

      `GET	localhost:8080/api/v1/resource/?uPhone=***&uToken=***&name=food&verified=true&accepted=true`

* **获取商品页**

  * HTTP报头：`GET`

  * 请求URL：`api/v1/items/page`

  * 请求参数：

    | 参数名     | 参数类型 | 参数说明                       |
    | ---------- | -------- | ------------------------------ |
    | `pageNum`  | Integer  | 页数                           |
    | `pageSize` | Integer  | 页容量，非必需，默认值30       |
    | `sortBy`   | String   | 排序参数，非必需，默认值onTime |

  * 用例示范：

    * 获取第一页（默认参数）：

      `GET	localhost:8080/api/v1/items/page/?pageNum=1`

    * 获取第二页（自定义参数）：

      `GET	localhost:8080/api/v1/items/?pageNum=2&pageSize=10&sortBy=count`

### 3.订单信息接口

 * **用户下单**

   * HTTP报头：`POST`

   * 请求URL：`api/v1/order/new`

   * 请求参数：

     | 参数名     | 参数类型 | 参数说明    |
     | ---------- | -------- | ----------- |
     | `phone`    | String   | 用户手机号  |
     | `itemCode` | String   | 商品编号    |
     | `uToken`   | String   | 用户token码 |

   * json参数（下单时必需，接受订单时不需要）：

     | 参数名    | 参数类型 | 参数说明 |
     | --------- | -------- | -------- |
     | `count`   | Integer  | 数量     |
     | `comment` | String   | 备注     |

   * 用例示范：

     `POST	localhost:8080/api/v1/order/new/?phone=***&itemCode=***&uToken=***`

     ```json
     {
         "count": "",
         "comment": ""
     }
     ```

 * **订单失效（买家取消订单或卖家拒绝订单）**

   * HTTP报头：`DELETE`

   * 请求URL：`api/v1/order`

   * 请求参数：

     | 参数名      | 参数类型 | 参数说明    |
     | ----------- | -------- | ----------- |
     | `phone`     | String   | 用户手机号  |
     | `orderCode` | String   | 订单编号    |
     | `uToken`    | String   | 用户token码 |

   * 用例示范：

     `DELETE	localhost:8080/api/v1/order/?phone=***&orderCode=***&uToken=***`

* **卖家接受订单**

  * HTTP报头：`POST`

  * 请求URL：`api/v1/order`

  * 请求参数：

    | 参数名      | 参数类型 | 参数说明    |
    | ----------- | -------- | ----------- |
    | `phone`     | String   | 用户手机号  |
    | `orderCode` | String   | 订单编号    |
    | `uToken`    | String   | 用户token码 |

  * 用例示范：

    `POST	localhost:8080/api/v1/order/?phone=***&orderCode=***&uToken=***`

* **完成订单（双方都一样）**

  * HTTP报头：`PUT`

  * 请求URL：`api/v1/order`

  * 请求参数

    | 参数名      | 参数类型 | 参数说明    |
    | ----------- | -------- | ----------- |
    | `phone`     | String   | 用户手机号  |
    | `orderCode` | String   | 订单编号    |
    | `uToken`    | String   | 用户token码 |

  * 用例示范

    `PUT	localhost:8080/api/v1/order/?phone=***&orderCode=***&uToken=***`

### 4. 其他接口

 * **用户上传头像**

    * HTTP报头：`POST`

    * 请求URL：`api/v1/user/image`

    * 请求参数：

      | 参数名   | 参数类型      | 参数说明     |
      | -------- | ------------- | ------------ |
      | `phone`  | String        | 用户手机号   |
      | `uToken` | String        | 用户token码  |
      | `image`  | MultiPartFile | 用户头像文件 |

      

 * **下载用户头像**

    * HTTP报头：`GET`

    * 请求URL：`api/v1/user/image`

    * 请求参数：

      | 参数名   | 参数类型 | 参数说明    |
      | -------- | -------- | ----------- |
      | `phone`  | String   | 用户手机号  |
      | `uToken` | String   | 用户token码 |

      

