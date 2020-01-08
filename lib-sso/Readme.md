# RBAAC integration with microservices and UI

## Components consuming RBAAC data

- SERVER\-BRANCH​
- BRANCHWEBAPP​
- OFFSITEWEBAPP (still in development)

------------------------------------------------------

## Flowchart representing the Login Flow and consumption of RBAAC roles and permissions

![alt text](https://drive.google.com/uc?export=view&id=1XscwWOsST9sN7GDseezlGq3KWe-mEX_t)

------------------------------------------------------

## SERVER-BRANCH

- Spring Security allows seemless incorporation of Roles and Permissions

- After successfully logging in fully populated `Authentication` instance is created.

- Authentication instance consists of the following
    - Principal (user details which can be just username or more details)
    - Authorities / Permissions (this is where we store RBAAC permissions)
    - additional details

------------------------------------------------------

## The @PreAuthorize annotation

#### Controller level:

- The PreAuthorize annoation utilizes the `Authorization` object and can determine access to a controller or specific methods
- This annotation will prevent the execution of method if unauthorized.
- Inside PreAuthorize we can pass various conditions and combination of multiple conditions
- hasAuthority only verifies permissions, while hasPermission is customised to accept permission, action and scope as well.

```java
@PreAuthorize("hasPermission('ORDER_MGMT.FXORDER', 'VIEW')")
@RestController
@Api(value = "Order Management APIs")
public class FxOrderBranchController {
    //...
}
```

#### Method level:
```java
@PreAuthorize("hasPermission('ORDER_MGMT.FCINQUIRY', 'VIEW', 'COUNTRY') or hasPermission('ORDER_MGMT.FXORDER', 'VIEW')")
@RequestMapping(value = "/api/fxo/order/details", method = { RequestMethod.POST })
public AmxApiResponse<FcSaleOrderManagementDTO, Object> getOrderDetails() {
    return fxOrderBranchClient.fetchBranchOrderDetails();
}



@PreAuthorize("hasAuthority('MRKT_MGMT.PUSH_NOTIFICATION')")
@RequestMapping(value = "/pub/list/tenant", method = RequestMethod.POST)
public List<Tenant> listOfTenants() throws PostManException, InterruptedException, ExecutionException {
    return Arrays.asList(Tenant.values());
}

```

------------------------------------------------------

## The @PostAuthorize annotation

- The PostAuthorize as the name suggests will evaluate the access after executing the method.
- A possible example could be comparing employeeId of the return object with the principal.

```java
@PostAuthorize("returnObject.employeeId == principal.employeeId")
@RequestMapping(value = "/api/remitt/order/list", method = { RequestMethod.GET })
public AmxApiResponse<FcSaleOrderManagementDTO, Object>  getOrderList() {
    return someClient.getSomeDTO();
}
```


## Branch Application

In the Branch Application

- Once logged in Branch Application fetches the RBAAC data via the `user/meta` call and caches this data in memory 

- The visibility of links on the Left __Navigation Sidebar__ is determined by the permissions the user has.

- The access to a certain module via __Browser url__ is also determined based on the permissions the user has.


------------------------------------------------------


## Navigation Sidebar

- Permissions are mapped to URLs
- This config is used by the Navigation Sidebar to determine visibility
- It can also accept a regex for permissions allowing flexible conditions

```json
{
  customerManagement: {
    to: "/branch/app/customer",
    label: "Customer Management",
    faIcon: "address-card",
    role: "CUSTOMER_MGMT"
  },
  remittance: {
    to: "/branch/app/customer/remit",
    label: "Repeat Remittance",
    faIcon: "credit-card",
    role: "CUSTOMER_MGMT.REMITTANCE"
  },
  providerPlaceOrderList: { //GSM user
    to: "/branch/app/customer/placeorder_p",
    label: "Place Order",
    faIcon: "circle",
    role: "CUSTOMER_MGMT.PLACE_ORDER.RATE_PROVIDER"
  },
  exchRateMgmt: {
    to: "/branch/app/rate/exch",
    label: "Exchange Rate Update",
    faIcon: "percent",
    role: /(RATE_MGMT.MAKER|RATE_MGMT.CHECKER|RATE_MGMT.INQUIRY)/
  }
}
```


------------------------------------------------------


## Access to modules via URL

- The Router is also using the same configuration as shown previously
- If the user doesn't have a permission he'll be shown a 403 Page.


------------------------------------------------------


## Useful Links

- [Spring Authorization](https://docs.spring.io/spring-security/site/docs/current/reference/html/authorization.html),  [Spring Authentication](https://docs.spring.io/spring-security/site/docs/current/reference/html/overall-architecture.html#secure-objects)
- [PreAuthorize and PostAuthorize usage example](https://www.programcreek.com/java-api-examples/index.php?api=org.springframework.security.access.prepost.PostAuthorize)


