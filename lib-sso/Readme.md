# RBAAC integration with microservices and UI

<br/>
<br/>
<br/>

## Components consuming RBAAC data

- SERVER\-BRANCH​
- BRANCHWEBAPP​
- OFFSITEWEBAPP (still in development)

------------------------------------------------------

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>


## Flowchart representing the Login Flow and consumption of RBAAC roles and permissions

![alt text](https://drive.google.com/uc?export=view&id=1XscwWOsST9sN7GDseezlGq3KWe-mEX_t)

------------------------------------------------------

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>

## Spring Security Concepts

- Authentication: Who is the user
- Authorization: What the user can do
- Principal: Authenticated User
- Role: Representation of set of permissions
- Permissions: Fine Grained authorities defined for particular modules or actions

------------------------------------------------------

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>

[Video reference](https://www.youtube.com/watch?v=I0poT4UxFxE)

## SERVER-BRANCH

- The Role and Permissions stored in session after successful login, can now be used for authorising requests.

- Spring Security allows incorporation of Roles and Permissions

- After successfully logging in fully populated `Authentication` instance is created.

- Authentication instance consists of the following
    - Principal (user details which can be just username or more details)
    - Authorities / Permissions (this is where we store RBAAC permissions)
    - additional details

> See [SSOAppController.java](https://gitlab.com/almullagroup/amx/amx-jax/blob/staging/lib-sso/src/main/java/com/amx/jax/sso/client/SSOAppController.java) in `lib-sso` where the `Authentication` instance is populated 
    

------------------------------------------------------

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>

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



#### Example utilizing Request Params:

```java
@PreAuthorize("#placeOrderRequestModel.getBoolGsm() == true ? hasPermission('CUSTOMER_MGMT.PLACE_ORDER.RATE_PROVIDER', 'VIEW') : true")
@RequestMapping(value = "/api/placeorder/create", method = { RequestMethod.POST })
public AmxApiResponse<RatePlaceOrderResponseModel, Object> createPlaceOrder(
		@RequestBody PlaceOrderRequestModel placeOrderRequestModel) {
	return branchRemittanceClient.savePlaceOrderApplication(placeOrderRequestModel);
}
```

------------------------------------------------------

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>

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

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>


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
    permission: "CUSTOMER_MGMT"
  },
  remittance: {
    to: "/branch/app/customer/remit",
    label: "Repeat Remittance",
    faIcon: "credit-card",
    permission: "CUSTOMER_MGMT.REMITTANCE"
  },
  providerPlaceOrderList: { //GSM user
    to: "/branch/app/customer/placeorder_p",
    label: "Place Order",
    faIcon: "circle",
    permission: "CUSTOMER_MGMT.PLACE_ORDER.RATE_PROVIDER"
  },
  exchRateMgmt: {
    to: "/branch/app/rate/exch",
    label: "Exchange Rate Update",
    faIcon: "percent",
    permission: /(RATE_MGMT.MAKER|RATE_MGMT.CHECKER|RATE_MGMT.INQUIRY)/
  }
}
```


------------------------------------------------------

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>


## Access to modules via URL

- The Router is also using the same configuration as shown previously
- If the user doesn't have a permission he'll be shown a 403 Page.


------------------------------------------------------

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>


## Useful Links

- [Spring Authorization](https://docs.spring.io/spring-security/site/docs/current/reference/html/authorization.html),  [Spring Authentication](https://docs.spring.io/spring-security/site/docs/current/reference/html/overall-architecture.html#secure-objects)
- [PreAuthorize and PostAuthorize usage example](https://www.programcreek.com/java-api-examples/index.php?api=org.springframework.security.access.prepost.PostAuthorize)


